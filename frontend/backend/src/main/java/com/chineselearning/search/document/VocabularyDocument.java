package com.chineselearning.search.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Elasticsearch Document for Vocabulary
 * Optimized for full-text search and fuzzy matching
 * 
 * @author Senior Backend Architect
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "vocabularies")
public class VocabularyDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String hanzi;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String pinyin;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String meaning;  // Vietnamese: nghia

    @Field(type = FieldType.Text, analyzer = "standard")
    private String example;  // Vietnamese: viDu

    @Field(type = FieldType.Keyword)
    private String variant;

    @Field(type = FieldType.Keyword)
    private List<String> tags;
    
    @Field(type = FieldType.Integer)
    private Integer hskLevel;
    
    @Field(type = FieldType.Integer)
    private Integer frequencyRank;

    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date)
    private LocalDateTime updatedAt;

    // Composite field for multi-field search
    @Field(type = FieldType.Text, analyzer = "standard")
    private String searchText;

    /**
     * Build search text from all searchable fields
     */
    public void buildSearchText() {
        StringBuilder sb = new StringBuilder();
        if (hanzi != null) sb.append(hanzi).append(" ");
        if (pinyin != null) sb.append(pinyin).append(" ");
        if (meaning != null) sb.append(meaning).append(" ");
        if (example != null) sb.append(example);
        this.searchText = sb.toString().trim();
    }
}

