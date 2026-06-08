package com.example.devlog.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findByProjectIdAndDeletedFalseOrderByCreatedAtDesc(String projectId);
    Optional<Task> findByIdAndDeletedFalse(String id);
    @Query("""
            select t.status, count(t)
            from Task t
            where t.deleted = false
            group by t.status
            """)
    List<Object[]> countByStatusGroupByStatus();
}
