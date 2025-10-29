package com.chineselearning.controller;

import com.chineselearning.search.document.CourseDocument;
import com.chineselearning.search.document.GrammarTopicDocument;
import com.chineselearning.search.document.VocabularyDocument;
import com.chineselearning.search.dto.SearchRequest;
import com.chineselearning.search.dto.SearchResponse;
import com.chineselearning.search.service.CourseSearchService;
import com.chineselearning.search.service.GrammarSearchService;
import com.chineselearning.search.service.VocabularySearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Advanced Search Controller
 * Provides full-text search across all entities using Elasticsearch
 * 
 * @author Senior Backend Architect
 */
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Advanced Search", description = "Tìm kiếm nâng cao với Elasticsearch")
public class SearchController {

    private final VocabularySearchService vocabularySearchService;
    private final GrammarSearchService grammarSearchService;
    private final CourseSearchService courseSearchService;

    // ==================== Vocabulary Search ====================

    @Operation(
            summary = "Tìm kiếm từ vựng nâng cao",
            description = "Full-text search từ vựng với fuzzy matching và filters"
    )
    @PostMapping("/vocabulary")
    public ResponseEntity<SearchResponse<VocabularyDocument>> searchVocabulary(
            @Valid @RequestBody SearchRequest request
    ) {
        return ResponseEntity.ok(vocabularySearchService.search(request));
    }

    @Operation(summary = "Tìm kiếm từ vựng đơn giản")
    @GetMapping("/vocabulary")
    public ResponseEntity<SearchResponse<VocabularyDocument>> simpleSearchVocabulary(
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam String q,
            @Parameter(description = "Số trang") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Kích thước trang") @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(vocabularySearchService.simpleSearch(q, page, size));
    }

    @Operation(summary = "Tìm kiếm theo Hanzi")
    @GetMapping("/vocabulary/hanzi")
    public ResponseEntity<SearchResponse<VocabularyDocument>> searchByHanzi(
            @Parameter(description = "Chữ Hán") @RequestParam String hanzi,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(vocabularySearchService.searchByHanzi(hanzi, page, size));
    }

    @Operation(summary = "Tìm kiếm theo Pinyin")
    @GetMapping("/vocabulary/pinyin")
    public ResponseEntity<SearchResponse<VocabularyDocument>> searchByPinyin(
            @Parameter(description = "Phiên âm") @RequestParam String pinyin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(vocabularySearchService.searchByPinyin(pinyin, page, size));
    }

    @Operation(summary = "Tìm kiếm theo nghĩa tiếng Việt")
    @GetMapping("/vocabulary/meaning")
    public ResponseEntity<SearchResponse<VocabularyDocument>> searchByMeaning(
            @Parameter(description = "Nghĩa tiếng Việt") @RequestParam String meaning,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(vocabularySearchService.searchByMeaning(meaning, page, size));
    }

    @Operation(summary = "Gợi ý autocomplete từ vựng")
    @GetMapping("/vocabulary/suggest")
    public ResponseEntity<List<String>> suggestVocabulary(
            @Parameter(description = "Tiền tố") @RequestParam String prefix,
            @Parameter(description = "Số lượng gợi ý") @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(vocabularySearchService.getSuggestions(prefix, limit));
    }

    // ==================== Grammar Search ====================

    @Operation(
            summary = "Tìm kiếm ngữ pháp nâng cao",
            description = "Full-text search ngữ pháp và cấu trúc"
    )
    @PostMapping("/grammar")
    public ResponseEntity<SearchResponse<GrammarTopicDocument>> searchGrammar(
            @Valid @RequestBody SearchRequest request
    ) {
        return ResponseEntity.ok(grammarSearchService.search(request));
    }

    @Operation(summary = "Tìm kiếm ngữ pháp đơn giản")
    @GetMapping("/grammar")
    public ResponseEntity<SearchResponse<GrammarTopicDocument>> simpleSearchGrammar(
            @Parameter(description = "Từ khóa") @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(grammarSearchService.simpleSearch(q, page, size));
    }

    @Operation(summary = "Tìm kiếm theo nội dung ngữ pháp")
    @GetMapping("/grammar/content")
    public ResponseEntity<SearchResponse<GrammarTopicDocument>> searchByContent(
            @Parameter(description = "Nội dung") @RequestParam String content,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(grammarSearchService.searchByContent(content, page, size));
    }

    @Operation(summary = "Tìm ngữ pháp theo level")
    @GetMapping("/grammar/level")
    public ResponseEntity<SearchResponse<GrammarTopicDocument>> findGrammarByLevel(
            @Parameter(description = "Level: BASIC, MEDIUM, ADVANCED") @RequestParam String level,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(grammarSearchService.findByLevel(level, page, size));
    }

    // ==================== Course Search ====================

    @Operation(
            summary = "Tìm kiếm khóa học nâng cao",
            description = "Full-text search với filters (difficulty, level range)"
    )
    @PostMapping("/courses")
    public ResponseEntity<SearchResponse<CourseDocument>> searchCourses(
            @Valid @RequestBody SearchRequest request
    ) {
        return ResponseEntity.ok(courseSearchService.search(request));
    }

    @Operation(summary = "Tìm kiếm khóa học đơn giản")
    @GetMapping("/courses")
    public ResponseEntity<SearchResponse<CourseDocument>> simpleSearchCourses(
            @Parameter(description = "Từ khóa") @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(courseSearchService.simpleSearch(q, page, size));
    }

    @Operation(summary = "Tìm khóa học theo level")
    @GetMapping("/courses/level/{level}")
    public ResponseEntity<List<CourseDocument>> findCoursesByLevel(
            @Parameter(description = "Level") @PathVariable String level
    ) {
        return ResponseEntity.ok(courseSearchService.findByLevel(level));
    }

    @Operation(summary = "Tìm khóa học theo giáo trình")
    @GetMapping("/courses/textbook/{textbookId}")
    public ResponseEntity<List<CourseDocument>> findCoursesByTextbook(
            @Parameter(description = "Textbook ID") @PathVariable Long textbookId
    ) {
        return ResponseEntity.ok(courseSearchService.findByTextbook(textbookId));
    }
}

