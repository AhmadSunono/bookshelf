package com.sonouno.bookshelf.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sonouno.bookshelf.enums.EBookStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_statuses")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class BookStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "book_id", unique = true)
    private Book book;

    @Column(name = "read_pages", columnDefinition = "int default 0")
    private Integer readPages;

    @Column(name = "started_at", nullable = true, updatable = true)
    private LocalDateTime startedAt;

    @Column(name = "completed_at", nullable = true, updatable = true)
    private LocalDateTime completedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EBookStatus status;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "visible")
    @Builder.Default()
    private boolean visible = true;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.visible = true;
        this.status = EBookStatus.BACKLOG;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
