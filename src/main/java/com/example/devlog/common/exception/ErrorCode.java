package com.example.devlog.common.exception;

import com.example.devlog.project.ProjectStatus;
import com.example.devlog.task.TaskStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."),
    INVALID_REQUEST_BODY(
            HttpStatus.BAD_REQUEST,
            "요청 본문 형식이 올바르지 않습니다."
    ),
    INVALID_PROJECT_STATUS(
            HttpStatus.BAD_REQUEST,
            "올바르지 않은 프로젝트 상태입니다. 사용 가능한 값: " + ProjectStatus.getAvailableValues()
    ),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "태스크를 찾을 수 없습니다."),
    INVALID_TASK_STATUS(
            HttpStatus.BAD_REQUEST,
            "올바르지 않은 태스크 상태입니다. 사용 가능한 값: " + TaskStatus.getAvailableValues()
    );


    private final HttpStatus status;
    private final String message;
}
