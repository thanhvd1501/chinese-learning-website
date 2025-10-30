package com.chineselearning.service.interfaces;

import com.chineselearning.dto.PageResponse;
import com.chineselearning.dto.response.VocabularyResponse;

import java.util.List;

/**
 * Vocabulary Service Interface
 * Defines operations for vocabulary management
 * 
 * @author Senior Backend Architect
 */
public interface VocabularyService {

    /**
     * Get paginated vocabulary list
     */
    PageResponse<VocabularyResponse> getVocabulary(int page, int size, String search, String variant);

    /**
     * Get vocabulary by ID
     */
    VocabularyResponse getById(Long id);

    /**
     * Get all vocabulary
     */
    List<VocabularyResponse> getAll();

    /**
     * Search vocabulary by hanzi, pinyin or meaning
     */
    List<VocabularyResponse> search(String keyword);
}

