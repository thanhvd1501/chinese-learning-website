package com.chineselearning.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for Grammar Topic
 * 
 * @author Senior Backend Architect
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrammarTopicResponse {

    private Long id;
    private String title;
    private String structure;
    private String explanation;
    private String example;
    private String translation;
    private String tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

