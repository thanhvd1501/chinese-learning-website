package com.chineselearning.service.interfaces;

import com.chineselearning.domain.Textbook.VersionType;
import com.chineselearning.dto.request.TextbookRequest;
import com.chineselearning.dto.response.TextbookResponse;

import java.util.List;

/**
 * Textbook Service Interface
 * Defines operations for textbook management
 * 
 * @author Senior Backend Architect
 */
public interface TextbookService extends CrudService<TextbookRequest, TextbookResponse, Long> {

    /**
     * Find textbooks by version type
     */
    List<TextbookResponse> findByVersion(VersionType version);

    /**
     * Find textbooks by publication year
     */
    List<TextbookResponse> findByPublicationYear(Integer year);

    /**
     * Search textbooks by name
     */
    List<TextbookResponse> searchByName(String name);
}

