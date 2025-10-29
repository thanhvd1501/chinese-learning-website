package com.chineselearning.service.interfaces;

import com.chineselearning.dto.PageResponse;

import java.util.List;

/**
 * Base CRUD Service Interface
 * Generic interface cho tất cả CRUD operations
 * 
 * @param <REQUEST> - Request DTO type
 * @param <RESPONSE> - Response DTO type
 * @param <ID> - Entity ID type
 * 
 * @author Senior Backend Architect
 */
public interface CrudService<REQUEST, RESPONSE, ID> {

    /**
     * Create new resource
     */
    RESPONSE create(REQUEST request);

    /**
     * Get resource by ID
     */
    RESPONSE getById(ID id);

    /**
     * Get all resources
     */
    List<RESPONSE> getAll();

    /**
     * Get resources with pagination
     */
    PageResponse<RESPONSE> getPage(int page, int size);

    /**
     * Update existing resource
     */
    RESPONSE update(ID id, REQUEST request);

    /**
     * Delete resource by ID
     */
    void delete(ID id);

    /**
     * Check if resource exists
     */
    boolean exists(ID id);
}

