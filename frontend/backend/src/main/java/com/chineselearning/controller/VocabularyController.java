package com.chineselearning.controller;

import com.chineselearning.domain.Vocabulary.BienTheType;
import com.chineselearning.dto.PageResponse;
import com.chineselearning.dto.VocabularyResponse;
import com.chineselearning.service.VocabularyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vocab")
@RequiredArgsConstructor
@Tag(name = "Vocabulary", description = "Từ vựng API")
public class VocabularyController {

    private final VocabularyService vocabularyService;

    @Operation(summary = "Lấy danh sách từ vựng", description = "Lấy danh sách từ vựng có phân trang, lọc và tìm kiếm")
    @GetMapping
    public ResponseEntity<PageResponse<VocabularyResponse>> getVocabularies(
            @Parameter(description = "Số trang (bắt đầu từ 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng từ vựng mỗi trang", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Lọc theo biến thể: GIAN, PHON, BOTH") @RequestParam(required = false) BienTheType bienThe,
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam(required = false) String search
    ) {
        PageResponse<VocabularyResponse> response = vocabularyService.getVocabularies(page, size, bienThe, search);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy từ vựng theo ID", description = "Lấy chi tiết một từ vựng cụ thể")
    @GetMapping("/{id}")
    public ResponseEntity<VocabularyResponse> getVocabularyById(
            @Parameter(description = "ID của từ vựng") @PathVariable Long id
    ) {
        VocabularyResponse response = vocabularyService.getVocabularyById(id);
        return ResponseEntity.ok(response);
    }
}

