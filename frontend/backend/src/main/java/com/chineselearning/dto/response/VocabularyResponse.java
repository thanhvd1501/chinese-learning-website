package com.chineselearning.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Response DTO for Vocabulary
 * 
 * @author Senior Backend Architect
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyResponse {

    private Long id;
    private String hanzi;
    private String pinyin;
    private String meaning;  // Vietnamese: nghia
    private String example;  // Vietnamese: viDu
    private String variant;  // Changed from bienThe (SIMPLIFIED, TRADITIONAL, BOTH)
    private Integer hskLevel;
    private Integer frequencyRank;
    private Set<String> tags;
}

