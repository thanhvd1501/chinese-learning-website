package com.chineselearning.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Universal Search Response DTO
 * Contains search results with metadata
 * 
 * @author Senior Backend Architect
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse<T> {

    private List<T> results;
    private Long totalHits;
    private Integer page;
    private Integer size;
    private Integer totalPages;
    private Long searchTimeMs;
    private Float maxScore;

    public static <T> SearchResponse<T> of(
            List<T> results,
            Long totalHits,
            Integer page,
            Integer size,
            Long searchTimeMs,
            Float maxScore
    ) {
        int totalPages = size > 0 ? (int) Math.ceil((double) totalHits / size) : 0;

        return SearchResponse.<T>builder()
                .results(results)
                .totalHits(totalHits)
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .searchTimeMs(searchTimeMs)
                .maxScore(maxScore)
                .build();
    }
}

