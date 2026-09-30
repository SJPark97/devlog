package com.example.devlog.project;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class ProjectTest {

    /**
     * 입력한 이름·설명이 그대로 담기고, 초기 상태가 ACTIVE·deleted=false 인지 한 번에 검증한다.
     * 조회 쿼리가 전부 deleted=false 로 거르고 상태별 개수를 status 로 세므로, 초기값이 틀리면 방금 만든 프로젝트가 목록에서 빠진다.
     * id·createdAt 은 실행마다 달라지는 값이라 비교에서 뺐다.
     */
    @Test
    @DisplayName("생성하면 입력값을 담고 ACTIVE 상태, 삭제되지 않은 채로 시작한다")
    void create_setsInitialValues() {
        String projectName = "프로젝트";
        String description = "테스트 프로젝트입니다.";
        Project project = Project.create(projectName, description);
        Assertions.assertThat(project)
                .extracting(Project::getName, Project::getDescription, Project::getStatus, Project::isDeleted)
                .containsExactly(projectName, description, ProjectStatus.ACTIVE, false);
    }
}
