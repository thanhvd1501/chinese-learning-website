package com.chineselearning.search.service;

import com.chineselearning.search.document.VocabularyDocument;
import com.chineselearning.search.dto.SearchRequest;
import com.chineselearning.search.dto.SearchResponse;
import com.chineselearning.search.repository.VocabularySearchRepository;
import com.chineselearning.search.service.interfaces.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vocabulary Search Service Implementation
 * Implements advanced vocabulary search with Elasticsearch
 * 
 * @author Senior Backend Architect
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VocabularySearchService implements SearchService<VocabularyDocument> {

    private final VocabularySearchRepository vocabularySearchRepository;

    @Override
    public SearchResponse<VocabularyDocument> search(SearchRequest request) {
        long startTime = System.currentTimeMillis();
        log.info("Vocabulary search started: query={}, page={}, size={}", 
                request.getQuery(), request.getPage(), request.getSize());

        Pageable pageable = createPageable(request);
        Page<VocabularyDocument> searchResults;

        if (request.getVariant() != null && request.getQuery() != null) {
            // Advanced search with filters
            searchResults = vocabularySearchRepository.advancedSearch(
                    request.getQuery(),
                    request.getVariant(),
                    pageable
            );
        } else if (request.getQuery() != null) {
            // Full-text search
            searchResults = vocabularySearchRepository.searchByAllFields(
                    request.getQuery(),
                    pageable
            );
        } else if (request.getVariant() != null) {
            // Filter by variant type only
            searchResults = vocabularySearchRepository.findByVariant(
                    request.getVariant(),
                    pageable
            );
        } else if (request.getHskLevel() != null) {
            // Filter by HSK level
            searchResults = vocabularySearchRepository.findByHskLevel(
                    request.getHskLevel(),
                    pageable
            );
        } else {
            // Get all
            searchResults = vocabularySearchRepository.findAll(pageable);
        }

        long searchTime = System.currentTimeMillis() - startTime;

        log.info("Vocabulary search completed: {} results in {} ms", 
                searchResults.getTotalElements(), searchTime);

        return SearchResponse.of(
                searchResults.getContent(),
                searchResults.getTotalElements(),
                request.getPage(),
                request.getSize(),
                searchTime,
                0.0f // Elasticsearch score not directly available in Spring Data
        );
    }

    @Override
    public SearchResponse<VocabularyDocument> simpleSearch(String keyword, int page, int size) {
        SearchRequest request = SearchRequest.builder()
                .query(keyword)
                .page(page)
                .size(size)
                .build();
        return search(request);
    }

    @Override
    public SearchResponse<VocabularyDocument> fuzzySearch(String keyword, int page, int size) {
        // Fuzzy search already enabled in searchByAllFields
        return simpleSearch(keyword, page, size);
    }

    @Override
    public List<String> getSuggestions(String prefix, int limit) {
        log.debug("Getting vocabulary suggestions for prefix: {}", prefix);

        Pageable pageable = PageRequest.of(0, limit);

        // Search hanzi that starts with prefix
        Page<VocabularyDocument> results = vocabularySearchRepository
                .findByHanziContaining(prefix, pageable);

        return results.stream()
                .map(VocabularyDocument::getHanzi)
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Search by Hanzi specifically
     */
    public SearchResponse<VocabularyDocument> searchByHanzi(String hanzi, int page, int size) {
        long startTime = System.currentTimeMillis();

        Pageable pageable = PageRequest.of(page, size);
        Page<VocabularyDocument> results = vocabularySearchRepository
                .findByHanziContaining(hanzi, pageable);

        long searchTime = System.currentTimeMillis() - startTime;

        return SearchResponse.of(
                results.getContent(),
                results.getTotalElements(),
                page,
                size,
                searchTime,
                0.0f
        );
    }

    /**
     * Search by Pinyin
     */
    public SearchResponse<VocabularyDocument> searchByPinyin(String pinyin, int page, int size) {
        long startTime = System.currentTimeMillis();

        Pageable pageable = PageRequest.of(page, size);
        Page<VocabularyDocument> results = vocabularySearchRepository
                .findByPinyinContainingIgnoreCase(pinyin, pageable);

        long searchTime = System.currentTimeMillis() - startTime;

        return SearchResponse.of(
                results.getContent(),
                results.getTotalElements(),
                page,
                size,
                searchTime,
                0.0f
        );
    }

    /**
     * Search by Vietnamese meaning
     */
    public SearchResponse<VocabularyDocument> searchByMeaning(String meaning, int page, int size) {
        long startTime = System.currentTimeMillis();

        Pageable pageable = PageRequest.of(page, size);
        Page<VocabularyDocument> results = vocabularySearchRepository
                .findByMeaningContainingIgnoreCase(meaning, pageable);

        long searchTime = System.currentTimeMillis() - startTime;

        return SearchResponse.of(
                results.getContent(),
                results.getTotalElements(),
                page,
                size,
                searchTime,
                0.0f
        );
    }

    private Pageable createPageable(SearchRequest request) {
        Sort sort = Sort.unsorted();

        if (request.getSortBy() != null) {
            Sort.Direction direction = "DESC".equalsIgnoreCase(request.getSortDirection())
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            sort = Sort.by(direction, request.getSortBy());
        }

        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }
}

