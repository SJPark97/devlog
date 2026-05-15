package com.example.devlog.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findByProjectIdAndDeletedFalseOrderByCreatedAtDesc(String projectId);
    Optional<Task> findByIdAndDeletedFalse(String id);
}
