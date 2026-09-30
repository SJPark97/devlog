package com.example.devlog.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, String> {
    List<Project> findByDeletedFalseOrderByCreatedAtDesc();
    Page<Project> findByDeletedFalse(Pageable pageable);
    Page<Project> findByDeletedFalseAndNameContainingIgnoreCase(String keyword, Pageable pageable);
    long countByDeletedFalseAndStatus(ProjectStatus status);
}