package com.example.devlog.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, String> {
    List<Project> findByDeletedFalseOrderByCreatedAtDesc();
    Page<Project> findByDeletedFalse(Pageable pageable);
    Page<Project> findByDeletedFalseAndNameContainingIgnoreCase(String keyword, Pageable pageable);
    Optional<Project> findByIdAndDeletedFalse(String id);
    long countByDeletedFalseAndStatus(ProjectStatus status);
}