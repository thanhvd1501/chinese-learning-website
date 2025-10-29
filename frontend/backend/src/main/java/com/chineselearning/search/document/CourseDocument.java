package com.chineselearning.search.document;

import com.chineselearning.domain.Course.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * Elasticsearch Document for Courses
 * Optimized for course discovery
 * 
 * @author Senior Backend Architect
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "courses")
public class CourseDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Keyword)
    private String level;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer")
    private String description;

    @Field(type = FieldType.Keyword)
    private Difficulty difficulty;

    @Field(type = FieldType.Long)
    private Long textbookId;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String textbookName;

    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String searchText;

    public void buildSearchText() {
        StringBuilder sb = new StringBuilder();
        if (title != null) sb.append(title).append(" ");
        if (description != null) sb.append(description).append(" ");
        if (textbookName != null) sb.append(textbookName).append(" ");
        sb.append("Level ").append(level);
        this.searchText = sb.toString().trim();
    }
}

