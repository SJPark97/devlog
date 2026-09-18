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
- **실시간 알림(SSE)**: 프로젝트 단위로 구독하고 태스크 변경 이벤트 수신

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

- 이벤트는 트랜잭션이 **커밋된 후에만** 발행됩니다.
- 연결은 60초 후 만료되므로 클라이언트에서 재연결해야 합니다.

## 패키지 구조

```
com.example.devlog
├── common
│   ├── dto          # 공통 응답(ApiResponse, PageResponse)
│   ├── exception    # BusinessException, ErrorCode
│   ├── event        # SSE 구독·발행
│   └── GlobalExceptionHandler
├── project
├── task
├── taskcomment
└── dashboard
```
