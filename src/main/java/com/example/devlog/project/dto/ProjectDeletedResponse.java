package com.example.devlog.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProjectDeletedResponse(
    @Schema(description = "삭제된 프로젝트 ID", example = "31e7e404-7937-4bd1-a669-f9383e3b61b6")
    String id
) {
}
