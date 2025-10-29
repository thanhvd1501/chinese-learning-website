package com.chineselearning.search.service;

import com.chineselearning.search.document.CourseDocument;
import com.chineselearning.search.dto.SearchRequest;
import com.chineselearning.search.dto.SearchResponse;
import com.chineselearning.search.repository.CourseSearchRepository;
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
 * Course Search Service Implementation
 * Advanced course discovery and filtering
 * 
 * @author Senior Backend Architect
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CourseSearchService implements SearchService<CourseDocument> {

    private final CourseSearchRepository courseSearchRepository;

    @Override
    public SearchResponse<CourseDocument> search(SearchRequest request) {
        long startTime = System.currentTimeMillis();
        log.info("Course search started: query={}, difficulty={}, level={}",
                request.getQuery(), request.getDifficulty(), request.getCourseLevel());

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<CourseDocument> searchResults;

        if (request.getQuery() != null && request.getDifficulty() != null) {
            // Advanced search with filters
            searchResults = courseSearchRepository.advancedSearch(
                    request.getQuery(),
                    request.getDifficulty(),
                    pageable
            );
        } else if (request.getQuery() != null) {
            // Simple full-text search
            searchResults = courseSearchRepository.searchAll(request.getQuery(), pageable);
        } else {
            searchResults = courseSearchRepository.findAll(pageable);
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
    public SearchResponse<CourseDocument> simpleSearch(String keyword, int page, int size) {
        SearchRequest request = SearchRequest.builder()
                .query(keyword)
                .page(page)
                .size(size)
                .build();
        return search(request);
    }

    @Override
    public SearchResponse<CourseDocument> fuzzySearch(String keyword, int page, int size) {
        return simpleSearch(keyword, page, size);
    }

    @Override
    public List<String> getSuggestions(String prefix, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<CourseDocument> results = courseSearchRepository.searchAll(prefix, pageable);

        return results.stream()
                .map(CourseDocument::getTitle)
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Find courses by textbook
     */
    public List<CourseDocument> findByTextbook(Long textbookId) {
        return courseSearchRepository.findByTextbookId(textbookId);
    }

    /**
     * Find courses by level
     */
    public List<CourseDocument> findByLevel(String level) {
        return courseSearchRepository.findByLevel(level);
    }
}

