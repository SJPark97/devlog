package com.example.devlog.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TaskDeletedResponse(
        @Schema(description = "삭제된 작업 ID", example = "31e7e404-7937-4bd1-a669-f9383e3b61b6")
        String id
) {

}
