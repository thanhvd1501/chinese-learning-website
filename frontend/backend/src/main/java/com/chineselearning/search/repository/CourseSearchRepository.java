package com.chineselearning.search.repository;

import com.chineselearning.domain.Course.Difficulty;
import com.chineselearning.search.document.CourseDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Elasticsearch Repository for Course Search
 * 
 * @author Senior Backend Architect
 */
@Repository
public interface CourseSearchRepository extends ElasticsearchRepository<CourseDocument, Long> {

    /**
     * Full-text search courses
     */
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title^3\", \"description^2\", \"textbookName\"], \"fuzziness\": \"AUTO\"}}")
    Page<CourseDocument> searchAll(String query, Pageable pageable);

    /**
     * Find by difficulty
     */
    List<CourseDocument> findByDifficulty(Difficulty difficulty);

    /**
     * Find by level
     */
    List<CourseDocument> findByLevel(String level);

    /**
     * Find by textbook
     */
    List<CourseDocument> findByTextbookId(Long textbookId);

    /**
     * Advanced search with filters
     */
    @Query("{\"bool\": {\"must\": [{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title\", \"description\"]}}], \"filter\": [{\"term\": {\"difficulty\": \"?1\"}}]}}")
    Page<CourseDocument> advancedSearch(String query, Difficulty difficulty, Pageable pageable);
}

