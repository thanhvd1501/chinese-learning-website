package com.chineselearning.service.interfaces;

import com.chineselearning.domain.Textbook.PhienBanType;
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
     * Find textbooks by phien ban type
     */
    List<TextbookResponse> findByPhienBan(PhienBanType phienBan);

    /**
     * Find textbooks by publication year
     */
    List<TextbookResponse> findByNamXuatBan(Integer year);

    /**
     * Search textbooks by name
     */
    List<TextbookResponse> searchByName(String name);
}

