package com.chineselearning.search.dto;

import com.chineselearning.domain.Course.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Universal Search Request DTO
 * Supports advanced search with multiple filters
 * 
 * @author Senior Backend Architect
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    // Common fields
    private String query;
    private Integer page = 0;
    private Integer size = 20;
    private String sortBy;
    private String sortDirection = "ASC";

    // Vocabulary specific
    private String variant;  // SIMPLIFIED, TRADITIONAL, BOTH
    private String[] tags;
    private Integer hskLevel;

    // Grammar specific
    private String grammarLevel;  // BASIC, MEDIUM, ADVANCED

    // Course specific
    private Difficulty difficulty;
    private String courseLevel;
    private Long textbookId;

    // Search options
    private Boolean fuzzySearch = true;
    private Float minScore;
}

