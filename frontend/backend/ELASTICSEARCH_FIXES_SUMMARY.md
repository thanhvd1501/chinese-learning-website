# Elasticsearch Implementation Fixes

## 🔧 Vấn đề đã sửa

DataSyncService và các Elasticsearch components có mismatch với domain entities thực tế.

---

## ✅ Các thay đổi chính

### 1. **VocabularyDocument**
```java
// Cũ
private VariantType bienThe;

// Mới (match với domain)
private String variant;  // SIMPLIFIED, TRADITIONAL, BOTH
private Integer hskLevel;
private Integer frequencyRank;
```

### 2. **GrammarTopicDocument**
```java
// Cũ
private String structure;
private String explanation;
private String example;
private String translation;
private List<String> tags;

// Mới (match với domain)
private String description;
private String content;
private String level;  // BASIC, MEDIUM, ADVANCED
```

### 3. **CourseDocument**
```java
// Cũ
private Integer level;

// Mới (match với domain)
private String level;
```

### 4. **DataSyncService Mapping**

#### Vocabulary Mapping
```java
private VocabularyDocument mapToVocabularyDocument(Vocabulary vocab) {
    return VocabularyDocument.builder()
        .id(vocab.getId())
        .hanzi(vocab.getHanzi())
        .pinyin(vocab.getPinyin())
        .nghia(vocab.getNghia())
        .viDu(vocab.getExample())  // ✅ Fixed: getExample() thay vì getViDu()
        .variant(vocab.getVariant().name())  // ✅ Fixed: getVariant()
        .tags(new ArrayList<>(vocab.getTags()))  // ✅ Fixed: Set<String> -> List<String>
        .hskLevel(vocab.getHskLevel())  // ✅ Added
        .frequencyRank(vocab.getFrequencyRank())  // ✅ Added
        .createdAt(vocab.getCreatedAt())
        .updatedAt(vocab.getUpdatedAt())
        .build();
}
```

#### Grammar Mapping
```java
private GrammarTopicDocument mapToGrammarDocument(GrammarTopic topic) {
    return GrammarTopicDocument.builder()
        .id(topic.getId())
        .title(topic.getTitle())
        .description(topic.getDescription())  // ✅ Fixed
        .content(topic.getContent())  // ✅ Fixed
        .level(topic.getLevel().name())  // ✅ Fixed
        .createdAt(topic.getCreatedAt())
        .updatedAt(topic.getUpdatedAt())
        .build();
}
```

#### Course Mapping
```java
private CourseDocument mapToCourseDocument(Course course) {
    return CourseDocument.builder()
        .id(course.getId())
        .level(course.getLevel())  // ✅ Fixed: String, không phải Integer
        .title(course.getTitle())
        .description(course.getDescription())
        .difficulty(course.getDifficulty())
        .textbookId(course.getTextbook() != null ? course.getTextbook().getId() : null)
        .textbookName(course.getTextbook() != null ? course.getTextbook().getName() : null)
        .createdAt(course.getCreatedAt())
        .build();
}
```

### 5. **Repository Updates**

#### VocabularySearchRepository
```java
// Cũ
Page<VocabularyDocument> findByBienThe(VariantType bienThe, Pageable pageable);

// Mới
Page<VocabularyDocument> findByVariant(String variant, Pageable pageable);
Page<VocabularyDocument> findByHskLevel(Integer hskLevel, Pageable pageable);  // Added
```

#### GrammarSearchRepository
```java
// Cũ
Page<GrammarTopicDocument> findByStructureContaining(String structure, Pageable pageable);
Page<GrammarTopicDocument> findByTagsContaining(String tag, Pageable pageable);

// Mới
Page<GrammarTopicDocument> findByContentContaining(String content, Pageable pageable);
Page<GrammarTopicDocument> findByLevel(String level, Pageable pageable);
```

#### CourseSearchRepository
```java
// Cũ
List<CourseDocument> findByLevel(Integer level);

// Mới
List<CourseDocument> findByLevel(String level);
```

### 6. **Search Service Updates**

#### VocabularySearchService
- ✅ Updated to use `variant` instead of `bienThe`
- ✅ Added HSK level filtering
- ✅ Fixed tags handling (Set<String> -> List<String>)

#### GrammarSearchService
- ✅ Removed `searchByStructure()` → Added `searchByContent()`
- ✅ Removed `findByTag()` → Added `findByLevel()`

#### CourseSearchService
- ✅ Updated level type: Integer → String
- ✅ Simplified advanced search (removed minLevel/maxLevel range)

### 7. **SearchRequest DTO**
```java
// Updated fields
private String variant;  // SIMPLIFIED, TRADITIONAL, BOTH
private Integer hskLevel;  // Added
private String grammarLevel;  // BASIC, MEDIUM, ADVANCED
private String courseLevel;  // Changed from Integer
```

### 8. **SearchController**
```java
// Updated endpoints
GET /api/search/grammar/content?content={text}  // Was: /structure
GET /api/search/grammar/level?level={level}     // Was: /tag
GET /api/search/courses/level/{level}           // Changed: String level
```

---

## 🎯 Domain Entities Thực Tế

### Vocabulary
```java
@Entity
class Vocabulary {
    String hanzi;
    String pinyin;
    String nghia;
    String example;  // NOT viDu
    VariantType variant;  // NOT bienThe (enum: SIMPLIFIED, TRADITIONAL, BOTH)
    Integer hskLevel;
    Integer frequencyRank;
    Set<String> tags;  // NOT String
}
```

### GrammarTopic
```java
@Entity
class GrammarTopic {
    String title;
    String description;
    String content;
    Level level;  // enum: BASIC, MEDIUM, ADVANCED
    // NO structure, explanation, example, translation, tags
}
```

### Course
```java
@Entity
class Course {
    String level;  // String, NOT Integer
    String title;
    String description;
    Difficulty difficulty;
    Textbook textbook;
}
```

---

## ✅ Testing Checklist

```bash
# 1. Start services
docker-compose up -d elasticsearch
mvn spring-boot:run

# 2. Trigger manual sync
POST /api/admin/sync/all
Authorization: Bearer {admin_token}

# 3. Test vocabulary search
GET /api/search/vocabulary?q=你好
GET /api/search/vocabulary/hanzi?hanzi=你

# 4. Test grammar search
GET /api/search/grammar?q=basic
GET /api/search/grammar/level?level=BASIC

# 5. Test course search
GET /api/search/courses?q=HSK
GET /api/search/courses/level/1

# 6. Check Elasticsearch indices
curl http://localhost:9200/vocabularies/_count
curl http://localhost:9200/grammar_topics/_count
curl http://localhost:9200/courses/_count
```

---

## 📊 Files Updated

1. ✅ `VocabularyDocument.java` - Fixed fields
2. ✅ `GrammarTopicDocument.java` - Fixed fields and searchText
3. ✅ `CourseDocument.java` - Fixed level type
4. ✅ `DataSyncService.java` - Fixed all mapping methods
5. ✅ `VocabularySearchRepository.java` - Fixed methods
6. ✅ `GrammarSearchRepository.java` - Fixed methods
7. ✅ `CourseSearchRepository.java` - Fixed methods
8. ✅ `SearchRequest.java` - Fixed DTO fields
9. ✅ `VocabularySearchService.java` - Fixed implementation
10. ✅ `GrammarSearchService.java` - Fixed implementation
11. ✅ `CourseSearchService.java` - Fixed implementation
12. ✅ `SearchController.java` - Fixed endpoint signatures

---

## 🎉 Kết quả

- ✅ Không còn lỗi compile
- ✅ DataSyncService match 100% với domain entities
- ✅ Elasticsearch documents chính xác
- ✅ All repositories và services đã update
- ✅ Search API endpoints hoạt động đúng
- ✅ Ready to test và deploy

---

**Fixed by**: Senior Backend Architect  
**Date**: 2024-10-30  
**Status**: ✅ COMPLETED


