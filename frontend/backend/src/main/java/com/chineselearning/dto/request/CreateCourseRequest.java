package com.chineselearning.dto.request;

import com.chineselearning.domain.Course.Difficulty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest {

    @NotNull(message = "Textbook ID is not null")
    private Long textbookId;

    @NotBlank(message = "Level is not empty")
    private String level;

    @NotBlank(message = "Title is not empty")
    private String title;

    private String description;

    @Min(value = 1, message = "Number of lessons must be greater than 0")
    private Integer lessons;

    private String duration;

    @NotNull(message = "Difficulty is not null")
    private Difficulty difficulty;

    private String coverImageUrl;
}

