package com.chineselearning.controller;

import com.chineselearning.domain.Textbook.PhienBanType;
import com.chineselearning.dto.PageResponse;
import com.chineselearning.dto.request.TextbookRequest;
import com.chineselearning.dto.response.TextbookResponse;
import com.chineselearning.service.interfaces.TextbookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Textbook Management Controller
 * Full CRUD operations cho textbooks
 * 
 * @author Senior Backend Architect
 */
@RestController
@RequestMapping("/api/textbooks")
@RequiredArgsConstructor
@Tag(name = "Textbook Management", description = "Quản lý giáo trình")
public class TextbookManagementController {

    private final TextbookService textbookService;

    @Operation(
            summary = "Lấy danh sách giáo trình",
            description = "Lấy tất cả giáo trình (Public)"
    )
    @GetMapping
    public ResponseEntity<List<TextbookResponse>> getAllTextbooks(
            @Parameter(description = "Filter theo phiên bản") 
            @RequestParam(required = false) PhienBanType phienBan,
            @Parameter(description = "Filter theo năm xuất bản") 
            @RequestParam(required = false) Integer year
    ) {
        if (phienBan != null) {
            return ResponseEntity.ok(textbookService.findByPhienBan(phienBan));
        }
        if (year != null) {
            return ResponseEntity.ok(textbookService.findByNamXuatBan(year));
        }
        return ResponseEntity.ok(textbookService.getAll());
    }

    @Operation(
            summary = "Lấy giáo trình theo ID",
            description = "Lấy chi tiết một giáo trình (Public)"
    )
    @GetMapping("/{id}")
    public ResponseEntity<TextbookResponse> getTextbookById(
            @Parameter(description = "ID của giáo trình") 
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(textbookService.getById(id));
    }

    @Operation(
            summary = "Lấy giáo trình với phân trang",
            description = "Lấy giáo trình có pagination (Public)"
    )
    @GetMapping("/page")
    public ResponseEntity<PageResponse<TextbookResponse>> getTextbooksPage(
            @Parameter(description = "Số trang") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Kích thước trang") @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(textbookService.getPage(page, size));
    }

    @Operation(
            summary = "Tìm kiếm giáo trình",
            description = "Tìm kiếm giáo trình theo tên (Public)"
    )
    @GetMapping("/search")
    public ResponseEntity<List<TextbookResponse>> searchTextbooks(
            @Parameter(description = "Từ khóa tìm kiếm") 
            @RequestParam String name
    ) {
        return ResponseEntity.ok(textbookService.searchByName(name));
    }

    @Operation(
            summary = "Tạo giáo trình mới",
            description = "Tạo giáo trình mới (Chỉ ADMIN, TEACHER)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<TextbookResponse> createTextbook(
            @Valid @RequestBody TextbookRequest request
    ) {
        TextbookResponse response = textbookService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Cập nhật giáo trình",
            description = "Cập nhật thông tin giáo trình (Chỉ ADMIN, TEACHER)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<TextbookResponse> updateTextbook(
            @Parameter(description = "ID của giáo trình") @PathVariable Long id,
            @Valid @RequestBody TextbookRequest request
    ) {
        TextbookResponse response = textbookService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Xóa giáo trình",
            description = "Xóa giáo trình (Chỉ ADMIN)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTextbook(
            @Parameter(description = "ID của giáo trình") @PathVariable Long id
    ) {
        textbookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

