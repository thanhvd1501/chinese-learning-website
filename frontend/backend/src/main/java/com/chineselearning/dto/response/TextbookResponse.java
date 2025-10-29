package com.chineselearning.dto.response;

import com.chineselearning.domain.Textbook.PhienBanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for Textbook
 * 
 * @author Senior Backend Architect
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextbookResponse {

    private Long id;
    private String name;
    private String description;
    private PhienBanType phienBan;
    private Integer namXuatBan;
    private String pdfUrl;
    private String coverImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Include count of courses in this textbook
    private Integer courseCount;
}

