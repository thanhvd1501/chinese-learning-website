package com.chineselearning.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for Radical
 * 
 * @author Senior Backend Architect
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RadicalResponse {

    private Long id;
    private String radical;
    private Integer strokeCount;
    private String meaning;
    private String examples;
    private String pinyin;
    private LocalDateTime createdAt;
    // Note: Radical entity doesn't have updatedAt field
}

