package com.chineselearning.dto.request;

import com.chineselearning.domain.Textbook.PhienBanType;
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

    @NotBlank(message = "Tên giáo trình không được trống")
    private String name;

    private String description;

    @NotNull(message = "Phiên bản không được null")
    private PhienBanType phienBan;

    @NotNull(message = "Năm xuất bản không được null")
    @Min(value = 2000, message = "Năm xuất bản phải từ 2000 trở lên")
    private Integer namXuatBan;

    private String pdfUrl;

    private String coverImageUrl;
}

