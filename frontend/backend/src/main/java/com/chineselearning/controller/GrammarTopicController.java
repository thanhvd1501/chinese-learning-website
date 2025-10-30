package com.chineselearning.controller;

import com.chineselearning.domain.GrammarTopic;
import com.chineselearning.dto.PageResponse;
import com.chineselearning.dto.request.GrammarTopicRequest;
import com.chineselearning.dto.response.GrammarTopicResponse;
import com.chineselearning.service.interfaces.GrammarTopicService;
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
 * Grammar Topic Controller
 * Full CRUD operations cho grammar topics
 * 
 * @author Senior Backend Architect
 */
@RestController
@RequestMapping("/api/grammar")
@RequiredArgsConstructor
@Tag(name = "Grammar Topics", description = "Quản lý chủ đề ngữ pháp")
public class GrammarTopicController {

    private final GrammarTopicService grammarTopicService;

    @Operation(
            summary = "Get all grammar topics",
            description = "Get all grammar topics with optional filtering (Public)"
    )
    @GetMapping
    public ResponseEntity<List<GrammarTopicResponse>> getAllGrammarTopics(
            @Parameter(description = "Search by keyword") 
            @RequestParam(required = false) String keyword,
            @Parameter(description = "Filter by level: BASIC, MEDIUM, ADVANCED") 
            @RequestParam(required = false) String level
    ) {
        if (keyword != null) {
            return ResponseEntity.ok(grammarTopicService.search(keyword));
        }
        if (level != null) {
            try {
                GrammarTopic.Level grammarLevel = GrammarTopic.Level.valueOf(level.toUpperCase());
                return ResponseEntity.ok(grammarTopicService.findByLevel(grammarLevel));
            } catch (IllegalArgumentException e) {
                // Invalid level, return all
                return ResponseEntity.ok(grammarTopicService.getAll());
            }
        }
        return ResponseEntity.ok(grammarTopicService.getAll());
    }

    @Operation(
            summary = "Lấy ngữ pháp theo ID",
            description = "Lấy chi tiết một chủ đề ngữ pháp (Public)"
    )
    @GetMapping("/{id}")
    public ResponseEntity<GrammarTopicResponse> getGrammarTopicById(
            @Parameter(description = "ID của chủ đề ngữ pháp") 
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(grammarTopicService.getById(id));
    }

    @Operation(
            summary = "Lấy ngữ pháp với phân trang",
            description = "Lấy ngữ pháp có pagination (Public)"
    )
    @GetMapping("/page")
    public ResponseEntity<PageResponse<GrammarTopicResponse>> getGrammarTopicsPage(
            @Parameter(description = "Số trang") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Kích thước trang") @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(grammarTopicService.getPage(page, size));
    }

    @Operation(
            summary = "Tạo chủ đề ngữ pháp mới",
            description = "Tạo chủ đề ngữ pháp mới (Chỉ ADMIN, TEACHER)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<GrammarTopicResponse> createGrammarTopic(
            @Valid @RequestBody GrammarTopicRequest request
    ) {
        GrammarTopicResponse response = grammarTopicService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Cập nhật chủ đề ngữ pháp",
            description = "Cập nhật thông tin chủ đề ngữ pháp (Chỉ ADMIN, TEACHER)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<GrammarTopicResponse> updateGrammarTopic(
            @Parameter(description = "ID của chủ đề ngữ pháp") @PathVariable Long id,
            @Valid @RequestBody GrammarTopicRequest request
    ) {
        GrammarTopicResponse response = grammarTopicService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Xóa chủ đề ngữ pháp",
            description = "Xóa chủ đề ngữ pháp (Chỉ ADMIN)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteGrammarTopic(
            @Parameter(description = "ID của chủ đề ngữ pháp") @PathVariable Long id
    ) {
        grammarTopicService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

