package com.chineselearning.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vocabulary")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String hanzi;

    @Column(nullable = false)
    private String pinyin;

    @Column(name = "nghia", nullable = false)
    private String meaning;

    @Column(name = "example")
    private String example;

    @Column(name = "variant", nullable = false)
    @Enumerated(EnumType.STRING)
    private VariantType variant;

    @Column(name = "hsk_level")
    private Integer hskLevel;

    @Column(name = "frequency_rank")
    private Integer frequencyRank;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "vocab_tags", joinColumns = @JoinColumn(name = "vocab_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum VariantType {
        SIMPLIFIED, TRADITIONAL, BOTH
    }
}

