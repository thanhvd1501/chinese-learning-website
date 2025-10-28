package com.chineselearning.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_learning_progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLearningProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocab_id", nullable = false)
    private Vocabulary vocabulary;

    @Column(name = "mastery_level", columnDefinition = "INTEGER DEFAULT 0")
    private Integer masteryLevel;

    @Column(name = "last_reviewed")
    private LocalDateTime lastReviewed;

    @Column(name = "review_count", columnDefinition = "INTEGER DEFAULT 0")
    private Integer reviewCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        masteryLevel = masteryLevel != null ? masteryLevel : 0;
        reviewCount = reviewCount != null ? reviewCount : 0;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

