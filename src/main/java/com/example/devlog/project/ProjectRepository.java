package com.example.devlog.project;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, String> {
    List<Project> findByDeletedFalseOrderByCreatedAtDesc();

    Optional<Project> findByIdAndDeletedFalse(String id);
}