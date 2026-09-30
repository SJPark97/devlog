package com.example.devlog.common.deletion;

import com.example.devlog.common.exception.BusinessException;

public final class DeletionValidator {
    private DeletionValidator() {}

    public static void validateNotDeleted(DeletionCheck check) {
        if (check.parent() != null) {
            validateNotDeleted(check.parent());
        }
        if (check.deleted()) {
            throw new BusinessException(check.errorCode());
        }
    }
}
