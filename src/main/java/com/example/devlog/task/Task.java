package com.example.devlog.task;

import com.example.devlog.project.Project;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false, columnDefinition = "CHAR(36)")
    private Project project;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false, length = 30, columnDefinition = "VARCHAR(30) DEFAULT 'TODO'")
    private String status;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean deleted;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    protected Task() {
    }

    private Task(Project project, String title, String content) {
        this.id = UUID.randomUUID().toString();
        this.project = project;
        this.title = title;
        this.content = content;
        this.status = "TODO";
        this.startedAt = null;
        this.completedAt = null;
        this.deleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = null;
    }

    public static Task create(Project project, String title, String content) {
        return new Task(project, title, content);
    }
}
