package com.chineselearning.search.service;

import com.chineselearning.search.document.GrammarTopicDocument;
import com.chineselearning.search.dto.SearchRequest;
import com.chineselearning.search.dto.SearchResponse;
import com.chineselearning.search.repository.GrammarSearchRepository;
import com.chineselearning.search.service.interfaces.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Grammar Topic Search Service Implementation
 * Advanced search for grammar patterns and explanations
 * 
 * @author Senior Backend Architect
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GrammarSearchService implements SearchService<GrammarTopicDocument> {

    private final GrammarSearchRepository grammarSearchRepository;

    @Override
    public SearchResponse<GrammarTopicDocument> search(SearchRequest request) {
        long startTime = System.currentTimeMillis();
        log.info("Grammar search started: query={}", request.getQuery());

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<GrammarTopicDocument> searchResults;

        if (request.getQuery() != null && !request.getQuery().isEmpty()) {
            searchResults = grammarSearchRepository.searchAll(request.getQuery(), pageable);
        } else {
            searchResults = grammarSearchRepository.findAll(pageable);
        }

        long searchTime = System.currentTimeMillis() - startTime;

        return SearchResponse.of(
                searchResults.getContent(),
                searchResults.getTotalElements(),
                request.getPage(),
                request.getSize(),
                searchTime,
                0.0f
        );
    }

    @Override
    public SearchResponse<GrammarTopicDocument> simpleSearch(String keyword, int page, int size) {
        SearchRequest request = SearchRequest.builder()
                .query(keyword)
                .page(page)
                .size(size)
                .build();
        return search(request);
    }

    @Override
    public SearchResponse<GrammarTopicDocument> fuzzySearch(String keyword, int page, int size) {
        return simpleSearch(keyword, page, size);
    }

    @Override
    public List<String> getSuggestions(String prefix, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<GrammarTopicDocument> results = grammarSearchRepository
                .findByTitleContainingIgnoreCase(prefix, pageable);

        return results.stream()
                .map(GrammarTopicDocument::getTitle)
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Search by content
     */
    public SearchResponse<GrammarTopicDocument> searchByContent(String content, int page, int size) {
        long startTime = System.currentTimeMillis();

        Pageable pageable = PageRequest.of(page, size);
        Page<GrammarTopicDocument> results = grammarSearchRepository
                .findByContentContaining(content, pageable);

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
     * Find by level
     */
    public SearchResponse<GrammarTopicDocument> findByLevel(String level, int page, int size) {
        long startTime = System.currentTimeMillis();

        Pageable pageable = PageRequest.of(page, size);
        Page<GrammarTopicDocument> results = grammarSearchRepository
                .findByLevel(level, pageable);

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
}

