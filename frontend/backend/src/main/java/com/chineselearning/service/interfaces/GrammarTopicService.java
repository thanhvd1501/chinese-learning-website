package com.chineselearning.service.interfaces;

import com.chineselearning.dto.request.GrammarTopicRequest;
import com.chineselearning.dto.response.GrammarTopicResponse;

import java.util.List;

/**
 * Grammar Topic Service Interface
 * Defines operations for grammar topic management
 * 
 * @author Senior Backend Architect
 */
public interface GrammarTopicService extends CrudService<GrammarTopicRequest, GrammarTopicResponse, Long> {

    /**
     * Search grammar topics by title or structure
     */
    List<GrammarTopicResponse> search(String keyword);

    /**
     * Find grammar topics by tags
     */
    List<GrammarTopicResponse> findByTag(String tag);
}

