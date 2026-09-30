package com.example.devlog.common.deletion;

import com.example.devlog.common.exception.BusinessException;
import com.example.devlog.common.exception.ErrorCode;
import com.example.devlog.project.Project;
import com.example.devlog.task.Task;
import com.example.devlog.taskcomment.TaskComment;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DeletionValidatorTest {
    Project project;
    Task task;
    TaskComment taskComment;

    @BeforeEach
    void setUP() {
        project = Project.create("프로젝트명", "프로젝트 설명");
        task = Task.create(project, "태스크명", "태스크 내용");
        taskComment = TaskComment.create(task, "댓글 내용");
    }

    /**
     * 댓글 → 태스크 → 프로젝트가 모두 살아 있으면 끝까지 올라가도 예외 없이 통과하는지 검증한다.
     * 가장 아래인 댓글에서 확인해야 세 단계가 전부 거쳐진다.
     */
    @Test
    @DisplayName("댓글·태스크·프로젝트가 모두 살아 있으면 통과한다")
    void validate_passesWhenAlive() {
        Assertions.assertThatCode(() -> DeletionValidator.validateNotDeleted(taskComment.deletionCheck()))
                .doesNotThrowAnyException();
    }

    /**
     * 프로젝트는 살아 있고 태스크만 삭제됐을 때, 태스크에서 확인하면 태스크 자신의 코드가 나오는지 검증한다.
     * 예외 종류만 보면 DELETED_PROJECT 가 잘못 나와도 통과하므로 errorCode 까지 비교한다.
     */
    @Test
    @DisplayName("태스크만 삭제됐으면 DELETED_TASK 로 막는다")
    void validate_throwsDeletedTask() {
        task.delete();
        Assertions.assertThatThrownBy(() -> DeletionValidator.validateNotDeleted(task.deletionCheck()))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DELETED_TASK);
    }
}
