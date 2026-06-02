package com.example.devlog.taskcomment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskCommentRepository extends JpaRepository<TaskComment, String> {
    List<TaskComment> findByTaskIdAndDeletedFalseOrderByCreatedAtDesc(String taskId);
    Optional<TaskComment> findByIdAndDeletedFalse(String taskId);
}
