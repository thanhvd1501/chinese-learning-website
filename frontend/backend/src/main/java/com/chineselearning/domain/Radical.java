package com.chineselearning.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "radicals")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Radical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 10)
    private String hanzi;

    @Column(nullable = false)
    private Integer strokes;

    private String meaning;

    private String pronunciation;

    @Column(name = "frequency_rank")
    private Integer frequencyRank;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

