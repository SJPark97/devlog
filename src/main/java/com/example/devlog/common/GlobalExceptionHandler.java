package com.example.devlog.common;

import com.example.devlog.common.dto.ApiResponse;
import com.example.devlog.common.exception.BusinessException;
import com.example.devlog.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

        if (message != null && message.contains("ProjectStatus")) {
            errorCode = ErrorCode.INVALID_PROJECT_STATUS;
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
}
