package com.chineselearning.dto.request;

import com.chineselearning.domain.Course.Difficulty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseRequest {

    private String level;
    private String title;
    private String description;

    @Min(value = 1, message = "Number of lessons must be greater than 0")
    private Integer lessons;

    private String duration;
    private Difficulty difficulty;
    private String coverImageUrl;
}

