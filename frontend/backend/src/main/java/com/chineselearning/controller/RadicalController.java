package com.chineselearning.controller;

import com.chineselearning.dto.PageResponse;
import com.chineselearning.dto.request.RadicalRequest;
import com.chineselearning.dto.response.RadicalResponse;
import com.chineselearning.service.interfaces.RadicalService;
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
 * Radical Controller
 * Full CRUD operations cho radicals
 * 
 * @author Senior Backend Architect
 */
@RestController
@RequestMapping("/api/radicals")
@RequiredArgsConstructor
@Tag(name = "Radicals", description = "Quản lý bộ thủ")
public class RadicalController {

    private final RadicalService radicalService;

    @Operation(
            summary = "Lấy danh sách bộ thủ",
            description = "Lấy tất cả bộ thủ (Public)"
    )
    @GetMapping
    public ResponseEntity<List<RadicalResponse>> getAllRadicals(
            @Parameter(description = "Lọc theo số nét") 
            @RequestParam(required = false) Integer strokeCount,
            @Parameter(description = "Tìm kiếm theo nghĩa") 
            @RequestParam(required = false) String meaning
    ) {
        if (strokeCount != null) {
            return ResponseEntity.ok(radicalService.findByStrokeCount(strokeCount));
        }
        if (meaning != null) {
            return ResponseEntity.ok(radicalService.searchByMeaning(meaning));
        }
        return ResponseEntity.ok(radicalService.getAll());
    }

    @Operation(
            summary = "Lấy bộ thủ theo ID",
            description = "Lấy chi tiết một bộ thủ (Public)"
    )
    @GetMapping("/{id}")
    public ResponseEntity<RadicalResponse> getRadicalById(
            @Parameter(description = "ID của bộ thủ") 
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(radicalService.getById(id));
    }

    @Operation(
            summary = "Tìm bộ thủ theo ký tự",
            description = "Tìm bộ thủ bằng ký tự cụ thể (Public)"
    )
    @GetMapping("/search")
    public ResponseEntity<RadicalResponse> findByRadical(
            @Parameter(description = "Ký tự bộ thủ") 
            @RequestParam String radical
    ) {
        return ResponseEntity.ok(radicalService.findByRadical(radical));
    }

    @Operation(
            summary = "Lấy bộ thủ với phân trang",
            description = "Lấy bộ thủ có pagination (Public)"
    )
    @GetMapping("/page")
    public ResponseEntity<PageResponse<RadicalResponse>> getRadicalsPage(
            @Parameter(description = "Số trang") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Kích thước trang") @RequestParam(defaultValue = "50") int size
    ) {
        return ResponseEntity.ok(radicalService.getPage(page, size));
    }

    @Operation(
            summary = "Tạo bộ thủ mới",
            description = "Tạo bộ thủ mới (Chỉ ADMIN, TEACHER)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<RadicalResponse> createRadical(
            @Valid @RequestBody RadicalRequest request
    ) {
        RadicalResponse response = radicalService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Cập nhật bộ thủ",
            description = "Cập nhật thông tin bộ thủ (Chỉ ADMIN, TEACHER)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<RadicalResponse> updateRadical(
            @Parameter(description = "ID của bộ thủ") @PathVariable Long id,
            @Valid @RequestBody RadicalRequest request
    ) {
        RadicalResponse response = radicalService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Xóa bộ thủ",
            description = "Xóa bộ thủ (Chỉ ADMIN)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRadical(
            @Parameter(description = "ID của bộ thủ") @PathVariable Long id
    ) {
        radicalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

