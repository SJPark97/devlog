# Devlog

개발 프로젝트와 작업(Task)을 관리하는 백엔드 API 서버입니다.

## 기술 스택

- **Java 17**
- **Spring Boot 3.5**
- **Spring Data JPA**
- **MySQL 8.4**
- **Bean Validation**
- **springdoc-openapi (Swagger UI)**
- **Lombok**
- **Gradle**

## 주요 기능

- **프로젝트 관리**: 생성, 조회(페이징·검색), 수정, 삭제, 상태 변경
- **태스크 관리**: 프로젝트 하위 작업 생성, 조회, 수정, 삭제, 상태 변경
- **댓글 관리**: 태스크에 댓글 작성, 조회, 수정, 삭제
- **대시보드**: 활성 프로젝트 수, 작업 상태별 개수 요약
- **실시간 알림(SSE)**: 프로젝트 단위로 구독하고 태스크 변경·프로젝트 삭제 이벤트 수신

## 실행 방법

### 1. MySQL 실행

```bash
docker-compose up -d
```

### 2. 애플리케이션 실행

```bash
./gradlew bootRun
```

- 기본 포트: `http://localhost:8080`
- API 문서(Swagger UI): `http://localhost:8080/swagger-ui/index.html`

## API 구조

| 도메인 | 엔드포인트 |
|--------|-----------|
| 프로젝트 | `/api/projects` |
| 태스크 | `/api/tasks` (목록 조회: `?projectId=`) |
| 댓글 | `/api/task-comments` (목록 조회: `?taskId=`) |
| 대시보드 | `/api/dashboard/summary` |
| 실시간 알림 | `/api/events/projects/{projectId}/events` |

상세 요청·응답 형식은 Swagger UI에서 확인할 수 있습니다.

### 삭제 정책

- 프로젝트·태스크·댓글은 실제로 지우지 않고 삭제 표시만 합니다(소프트 삭제).
- 없는 리소스는 `404`, 삭제된 리소스는 `410`으로 응답합니다.
- 상위 리소스가 삭제되면 하위 리소스도 조회·생성·수정·삭제할 수 없습니다. 이때는 가장 위에서 삭제된 리소스의 에러로 응답합니다. (예: 삭제된 프로젝트의 태스크 → `410 삭제된 프로젝트입니다.`)
- 삭제는 하위 리소스로 전파하지 않습니다. 나중에 상위 리소스를 복구할 때 하위 리소스를 따로 되살리지 않아도 되게 하기 위해서입니다.

## 실시간 알림(SSE)

프로젝트 단위로 구독하면 해당 프로젝트의 태스크 변경 사항을 실시간으로 받습니다.

```bash
curl -N http://localhost:8080/api/events/projects/{projectId}/events
```

| 이벤트 | 발생 시점 | data |
|--------|----------|------|
| `connect` | 구독 직후 | `"SSE connected"` |
| `TASK_CREATED` | 태스크 생성 | 태스크 응답 |
| `TASK_UPDATED` | 태스크 수정, 상태 변경 | 태스크 응답 |
| `TASK_DELETED` | 태스크 삭제 | `{"id": "작업 ID"}` |
| `PROJECT_DELETED` | 프로젝트 삭제 (이후 연결 종료) | `{"id": "프로젝트 ID"}` |
| `resync` | 재연결 시 놓친 이벤트를 재생할 수 없을 때 | `"resync required"` |

- 이벤트는 트랜잭션이 **커밋된 후에만** 발행됩니다.
- `connect`와 `resync`를 제외한 이벤트에는 프로젝트별로 1부터 증가하는 `id`가 붙습니다.
- 연결은 60초 후 만료되므로 클라이언트에서 재연결해야 합니다.
- 전송에 실패했거나 이미 끝난 연결은 구독 목록에서 제거하고, 나머지 구독자에게는 계속 보냅니다.
- 없는 프로젝트(`404`)나 삭제된 프로젝트(`410`)는 구독할 수 없습니다.
- 구독 중에 프로젝트가 삭제되면 `PROJECT_DELETED`를 보낸 뒤 연결을 종료합니다. 클라이언트는 이 이벤트를 받으면 `EventSource.close()`로 재연결을 멈추면 됩니다. 닫지 않아도 재연결 요청은 거부됩니다.

### 재연결

마지막으로 받은 이벤트 `id`를 `Last-Event-ID` 헤더로 보내며 다시 구독하면 놓친 이벤트를 받을 수 있습니다. 브라우저 `EventSource`는 자동 재연결할 때 이 헤더를 알아서 붙입니다.

```bash
curl -N -H "Last-Event-ID: 5" http://localhost:8080/api/events/projects/{projectId}/events
```

- 서버는 프로젝트별로 최근 이벤트 **100개**를 보관하고, 놓친 이벤트가 그 안에 모두 있으면 순서대로 다시 보냅니다.
- 놓친 이벤트가 이미 밀려나 재생할 수 없으면 `resync`를 보냅니다. 이때 클라이언트는 태스크 목록을 다시 조회해야 합니다.
- 이벤트 `id`와 보관 버퍼는 서버 메모리에 있어서 서버를 재시작하면 초기화됩니다. (Redis로 옮길 예정)

## 패키지 구조

```
com.example.devlog
├── common
│   ├── dto          # 공통 응답(ApiResponse, PageResponse)
│   ├── exception    # BusinessException, ErrorCode
│   ├── event        # SSE 구독·발행
│   ├── deletion     # 상위 리소스까지 따라가는 삭제 여부 검증(DeletionCheck, DeletionValidator)
│   └── GlobalExceptionHandler
├── project
├── task
├── taskcomment
└── dashboard
```
