package com.chineselearning.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for Radical
 * 
 * @author Senior Backend Architect
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RadicalRequest {

    @NotBlank(message = "Bộ thủ không được trống")
    private String radical;

    @NotNull(message = "Số nét không được null")
    @Min(value = 1, message = "Số nét phải lớn hơn 0")
    private Integer strokeCount;

    @NotBlank(message = "Nghĩa không được trống")
    private String meaning;

    private String examples;

    private String pinyin;
}

