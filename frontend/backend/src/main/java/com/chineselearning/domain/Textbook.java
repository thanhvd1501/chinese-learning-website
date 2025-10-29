package com.chineselearning.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "textbooks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Textbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "version", nullable = false)
    @Enumerated(EnumType.STRING)
    private VersionType version;

    @Column(name = "publication_year", nullable = false)
    private Integer publicationYear;

    @Column(name = "pdf_url")
    private String pdfUrl;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @OneToMany(mappedBy = "textbook", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Course> courses = new ArrayList<>();

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

    public enum VersionType {
        PB3, NEW, OLD
    }
}

