package com.chineselearning.dto.request;

import com.chineselearning.domain.Textbook.VersionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for Textbook creation/update
 * 
 * @author Senior Backend Architect
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextbookRequest {

    @NotBlank(message = "Textbook name is required")
    private String name;

    private String description;

    @NotNull(message = "Version is required")
    private VersionType version;  // Vietnamese: phienBan

    @NotNull(message = "Publication year is required")
    @Min(value = 2000, message = "Publication year must be from 2000 onwards")
    private Integer publicationYear;  // Vietnamese: namXuatBan

    private String pdfUrl;

    private String coverImageUrl;
}

