package com.chineselearning.dto.response;

import com.chineselearning.domain.Course.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {

    private Long id;
    private Long textbookId;
    private String textbookName;
    private String level;
    private String title;
    private String description;
    private Integer lessons;
    private String duration;
    private Difficulty difficulty;
    private String coverImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

