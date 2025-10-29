package com.chineselearning.search.repository;

import com.chineselearning.search.document.VocabularyDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Elasticsearch Repository for Vocabulary Search
 * Provides advanced search capabilities
 * 
 * @author Senior Backend Architect
 */
@Repository
public interface VocabularySearchRepository extends ElasticsearchRepository<VocabularyDocument, Long> {

    /**
     * Full-text search across all fields
     */
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"hanzi^3\", \"pinyin^2\", \"nghia^2\", \"viDu\"], \"fuzziness\": \"AUTO\"}}")
    Page<VocabularyDocument> searchByAllFields(String query, Pageable pageable);

    /**
     * Search by Hanzi with fuzzy matching
     */
    Page<VocabularyDocument> findByHanziContaining(String hanzi, Pageable pageable);

    /**
     * Search by Pinyin
     */
    Page<VocabularyDocument> findByPinyinContainingIgnoreCase(String pinyin, Pageable pageable);

    /**
     * Search by Vietnamese meaning
     */
    Page<VocabularyDocument> findByNghiaContainingIgnoreCase(String nghia, Pageable pageable);

    /**
     * Filter by variant type
     */
    Page<VocabularyDocument> findByVariant(String variant, Pageable pageable);

    /**
     * Find by tags
     */
    Page<VocabularyDocument> findByTagsContaining(String tag, Pageable pageable);

    /**
     * Advanced multi-criteria search
     */
    @Query("{\"bool\": {\"must\": [{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"hanzi^3\", \"pinyin^2\", \"nghia^2\"], \"fuzziness\": \"AUTO\"}}], \"filter\": [{\"term\": {\"variant\": \"?1\"}}]}}")
    Page<VocabularyDocument> advancedSearch(String query, String variant, Pageable pageable);
    
    /**
     * Find by HSK level
     */
    Page<VocabularyDocument> findByHskLevel(Integer hskLevel, Pageable pageable);
}

