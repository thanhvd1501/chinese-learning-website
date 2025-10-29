package com.chineselearning.search.service.interfaces;

import com.chineselearning.search.dto.SearchRequest;
import com.chineselearning.search.dto.SearchResponse;

/**
 * Search Service Interface
 * Defines universal search operations across all entities
 * 
 * @author Senior Backend Architect
 */
public interface SearchService<T> {

    /**
     * Universal search with filters
     */
    SearchResponse<T> search(SearchRequest request);

    /**
     * Simple keyword search
     */
    SearchResponse<T> simpleSearch(String keyword, int page, int size);

    /**
     * Fuzzy search for typo tolerance
     */
    SearchResponse<T> fuzzySearch(String keyword, int page, int size);

    /**
     * Get suggestions for autocomplete
     */
    java.util.List<String> getSuggestions(String prefix, int limit);
}

