package com.example.devlog.task.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TaskCreateRequest {
    public final String projectId;
    public final String title;
    public final String content;
}
