package com.example.devlog.common;

import com.example.devlog.common.dto.ApiResponse;
import com.example.devlog.common.exception.BusinessException;
import com.example.devlog.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException exception) {
        HttpStatus status = exception.getErrorCode().getStatus();

        return ResponseEntity
                .status(status)
                .body(ApiResponse.fail(status, exception.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception
    ) {
        String message = exception.getMostSpecificCause().getMessage();
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST_BODY;

        if (message != null) {
            if (message.contains("ProjectStatus")) {
                errorCode = ErrorCode.INVALID_PROJECT_STATUS;
            } else if (message.contains("TaskStatus")) {
                errorCode = ErrorCode.INVALID_TASK_STATUS;
            }
        }
        HttpStatus status = errorCode.getStatus();

        return ResponseEntity
                .status(status)
                .body(ApiResponse.fail(status, errorCode.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : ErrorCode.INVALID_REQUEST_BODY.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(ApiResponse.fail(status, message));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity
                .status(status)
                .body(ApiResponse.fail(status, "데이터 제약 조건을 위반했습니다."));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(
            NoResourceFoundException exception
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        return ResponseEntity
                .status(status)
                .body(ApiResponse.fail(status, "요청한 리소스를 찾을 수 없습니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception exception) {
        log.error("Unexpected server error", exception);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(status)
                .body(ApiResponse.fail(status, "서버 내부 오류가 발생했습니다."));
    }
}
