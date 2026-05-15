package com.example.devlog.project;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(
        name = "projects",
        indexes = {
                @Index(name = "idx_projects_deleted_created_at", columnList = "deleted, created_at"),
                @Index(name = "idx_projects_name", columnList = "name")
        }
)
public class Project {

    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30, columnDefinition = "VARCHAR(30) DEFAULT 'ACTIVE'")
    private ProjectStatus status;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean deleted;
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    protected Project() {}

    private Project(String name, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.status = ProjectStatus.ACTIVE;
        this.deleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
    }

    public static Project create(String name, String description) {
        return new Project(name, description);
    }

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void updateStatus(ProjectStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
}
