package com.chineselearning.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for Grammar Topic
 * 
 * @author Senior Backend Architect
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrammarTopicRequest {

    @NotBlank(message = "Tiêu đề không được trống")
    private String title;

    @NotBlank(message = "Cấu trúc không được trống")
    private String structure;

    @NotNull(message = "Giải thích không được null")
    private String explanation;

    private String example;

    private String translation;

    private String tags;
}

