package com.example.devlog.common.deletion;

import com.example.devlog.common.exception.ErrorCode;

public record DeletionCheck(
        boolean deleted,
        DeletionCheck parent,
        ErrorCode errorCode
) {
}
