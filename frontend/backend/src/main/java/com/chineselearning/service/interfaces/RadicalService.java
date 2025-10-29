package com.chineselearning.service.interfaces;

import com.chineselearning.dto.request.RadicalRequest;
import com.chineselearning.dto.response.RadicalResponse;

import java.util.List;

/**
 * Radical Service Interface
 * Defines operations for radical management
 * 
 * @author Senior Backend Architect
 */
public interface RadicalService extends CrudService<RadicalRequest, RadicalResponse, Long> {

    /**
     * Find radicals by stroke count
     */
    List<RadicalResponse> findByStrokeCount(Integer strokeCount);

    /**
     * Find radical by character
     */
    RadicalResponse findByRadical(String radical);

    /**
     * Search radicals by meaning
     */
    List<RadicalResponse> searchByMeaning(String keyword);
}

