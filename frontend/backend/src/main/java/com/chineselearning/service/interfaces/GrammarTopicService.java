package com.chineselearning.service.interfaces;

import com.chineselearning.domain.GrammarTopic;
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
     * Search grammar topics by keyword (title, description, content)
     */
    List<GrammarTopicResponse> search(String keyword);

    /**
     * Find grammar topics by level
     */
    List<GrammarTopicResponse> findByLevel(GrammarTopic.Level level);
}

