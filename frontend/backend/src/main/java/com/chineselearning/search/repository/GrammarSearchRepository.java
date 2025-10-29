package com.chineselearning.search.repository;

import com.chineselearning.search.document.GrammarTopicDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Elasticsearch Repository for Grammar Topic Search
 * 
 * @author Senior Backend Architect
 */
@Repository
public interface GrammarSearchRepository extends ElasticsearchRepository<GrammarTopicDocument, Long> {

    /**
     * Full-text search across grammar topics
     */
    @Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"title^3\", \"description^2\", \"content\"], \"fuzziness\": \"AUTO\"}}")
    Page<GrammarTopicDocument> searchAll(String query, Pageable pageable);

    /**
     * Search by content
     */
    Page<GrammarTopicDocument> findByContentContaining(String content, Pageable pageable);

    /**
     * Find by level
     */
    Page<GrammarTopicDocument> findByLevel(String level, Pageable pageable);

    /**
     * Search by title
     */
    Page<GrammarTopicDocument> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}

