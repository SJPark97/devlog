package com.example.devlog.project;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class ProjectTest {
    String projectName = "프로젝트";
    String description = "테스트 프로젝트입니다.";
    Project project;

    @BeforeEach
    void setUP() {
        project = Project.create(projectName, description);
    }

    /**
     * 입력한 이름·설명이 그대로 담기고, 초기 상태가 ACTIVE·deleted=false 인지 한 번에 검증한다.
     * 조회 쿼리가 전부 deleted=false 로 거르고 상태별 개수를 status 로 세므로, 초기값이 틀리면 방금 만든 프로젝트가 목록에서 빠진다.
     * id·createdAt 은 실행마다 달라지는 값이라 비교에서 뺐다.
     */
    @Test
    @DisplayName("생성하면 입력값을 담고 ACTIVE 상태, 삭제되지 않은 채로 시작한다")
    void create_setsInitialValues() {
        Assertions.assertThat(project)
                .extracting(Project::getName, Project::getDescription, Project::getStatus, Project::isDeleted)
                .containsExactly(projectName, description, ProjectStatus.ACTIVE, false);
    }

    /**
     * 삭제는 행을 지우지 않고 deleted 플래그와 삭제 시각만 남기는 소프트 삭제임을 검증한다.
     * 조회 쿼리가 deleted=false 로 거르므로 이 값이 켜져야 목록·단건 조회에서 빠진다. deletedAt 은 now() 라 채워졌는지만 본다.
     */
    @Test
    @DisplayName("삭제하면 deleted 가 true 가 되고 삭제 시각이 기록된다")
    void delete_marksDeleted() {
        project.delete();
        Assertions.assertThat(project)
                .returns(true, Project::isDeleted)
                .doesNotReturn(null, Project::getDeletedAt);
    }
}
