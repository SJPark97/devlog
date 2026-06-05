package com.example.devlog.taskcomment;

import com.example.devlog.task.Task;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Table(name = "task_comments")
public class TaskComment {

    @Id
    @Column(columnDefinition = "CHAR(36)")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false, columnDefinition = "CHAR(36)")
    private Task task;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private Boolean deleted;
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    protected TaskComment() {
    }

    private TaskComment(Task task, String content) {
        this.id = UUID.randomUUID().toString();
        this.task = task;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.deleted = false;
        this.updatedAt = null;
    }

    public static TaskComment create(Task task, String content) {
        return new TaskComment(task, content);
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}