package com.example.devlog.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;

@Getter
public class ProjectCreateRequest {

    @NotBlank(message = "프로젝트 이름은 필수입니다.")
    @Size(max = 100, message = "프로젝트 이름은 100자 이하로 입력해주세요.")
    private String name;

    @Size(max = 1000, message = "프로젝트 설명은 1000자 이하로 입력해주세요.")
    private String description;
}