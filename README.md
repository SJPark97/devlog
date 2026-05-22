# Devlog

개발 프로젝트와 작업(Task)을 관리하는 백엔드 API 서버입니다.

## 기술 스택

- **Java 17**
- **Spring Boot 3.5**
- **Spring Data JPA**
- **MySQL 8.4**
- **Lombok**
- **Gradle**

## 주요 기능

- **프로젝트 관리**: 생성, 조회, 수정, 삭제, 상태 변경
- **태스크 관리**: 프로젝트 하위 작업 생성, 조회, 수정, 삭제, 상태 변경
- **댓글 관리**: 태스크에 댓글 작성 및 조회

## 실행 방법

### 1. MySQL 실행

```bash
docker-compose up -d
```

### 2. 애플리케이션 실행

```bash
./gradlew bootRun
```

기본 포트: `http://localhost:8080`

## API 구조

| 도메인 | 엔드포인트 |
|--------|-----------|
| 프로젝트 | `/api/projects` |
| 태스크 | `/api/projects/{projectId}/tasks` |
| 댓글 | `/api/tasks/{taskId}/comments` |
