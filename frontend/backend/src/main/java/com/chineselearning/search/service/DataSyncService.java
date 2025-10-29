package com.chineselearning.search.service;

import com.chineselearning.domain.Course;
import com.chineselearning.domain.GrammarTopic;
import com.chineselearning.domain.Vocabulary;
import com.chineselearning.repository.CourseRepository;
import com.chineselearning.repository.GrammarTopicRepository;
import com.chineselearning.repository.VocabularyRepository;
import com.chineselearning.search.document.CourseDocument;
import com.chineselearning.search.document.GrammarTopicDocument;
import com.chineselearning.search.document.VocabularyDocument;
import com.chineselearning.search.repository.CourseSearchRepository;
import com.chineselearning.search.repository.GrammarSearchRepository;
import com.chineselearning.search.repository.VocabularySearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Synchronization Service
 * Syncs data from PostgreSQL to Elasticsearch
 * Runs on application startup and scheduled intervals
 * 
 * @author Senior Backend Architect
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DataSyncService {

    private final VocabularyRepository vocabularyRepository;
    private final GrammarTopicRepository grammarTopicRepository;
    private final CourseRepository courseRepository;

    private final VocabularySearchRepository vocabularySearchRepository;
    private final GrammarSearchRepository grammarSearchRepository;
    private final CourseSearchRepository courseSearchRepository;

    /**
     * Initial sync on application startup
     */
    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void syncOnStartup() {
        log.info("=== Starting initial data sync to Elasticsearch ===");
        try {
            syncAllData();
            log.info("=== Initial data sync completed successfully ===");
        } catch (Exception e) {
            log.error("Error during initial data sync", e);
        }
    }

    /**
     * Scheduled sync every 30 minutes (configurable)
     */
    @Scheduled(cron = "${app.elasticsearch.sync.cron:0 */30 * * * *}")
    @Async
    public void scheduledSync() {
        log.info("=== Starting scheduled data sync ===");
        try {
            syncAllData();
            log.info("=== Scheduled sync completed ===");
        } catch (Exception e) {
            log.error("Error during scheduled sync", e);
        }
    }

    /**
     * Sync all entities
     */
    @Transactional(readOnly = true)
    public void syncAllData() {
        syncVocabulary();
        syncGrammarTopics();
        syncCourses();
    }

    /**
     * Sync vocabulary data
     */
    @Transactional(readOnly = true)
    public void syncVocabulary() {
        log.info("Syncing vocabulary data...");
        long startTime = System.currentTimeMillis();

        List<Vocabulary> vocabularies = vocabularyRepository.findAll();
        log.info("Found {} vocabulary items to sync", vocabularies.size());

        List<VocabularyDocument> documents = vocabularies.stream()
                .map(this::mapToVocabularyDocument)
                .collect(Collectors.toList());

        vocabularySearchRepository.saveAll(documents);

        long duration = System.currentTimeMillis() - startTime;
        log.info("Vocabulary sync completed: {} items in {} ms", documents.size(), duration);
    }

    /**
     * Sync grammar topics
     */
    @Transactional(readOnly = true)
    public void syncGrammarTopics() {
        log.info("Syncing grammar topics...");
        long startTime = System.currentTimeMillis();

        List<GrammarTopic> topics = grammarTopicRepository.findAll();
        log.info("Found {} grammar topics to sync", topics.size());

        List<GrammarTopicDocument> documents = topics.stream()
                .map(this::mapToGrammarDocument)
                .collect(Collectors.toList());

        grammarSearchRepository.saveAll(documents);

        long duration = System.currentTimeMillis() - startTime;
        log.info("Grammar sync completed: {} items in {} ms", documents.size(), duration);
    }

    /**
     * Sync courses
     */
    @Transactional(readOnly = true)
    public void syncCourses() {
        log.info("Syncing courses...");
        long startTime = System.currentTimeMillis();

        List<Course> courses = courseRepository.findAll();
        log.info("Found {} courses to sync", courses.size());

        List<CourseDocument> documents = courses.stream()
                .map(this::mapToCourseDocument)
                .collect(Collectors.toList());

        courseSearchRepository.saveAll(documents);

        long duration = System.currentTimeMillis() - startTime;
        log.info("Course sync completed: {} items in {} ms", documents.size(), duration);
    }

    /**
     * Sync single vocabulary item
     */
    public void syncVocabularyItem(Vocabulary vocabulary) {
        VocabularyDocument document = mapToVocabularyDocument(vocabulary);
        vocabularySearchRepository.save(document);
        log.debug("Synced vocabulary item: {}", vocabulary.getId());
    }

    /**
     * Sync single grammar topic
     */
    public void syncGrammarTopic(GrammarTopic topic) {
        GrammarTopicDocument document = mapToGrammarDocument(topic);
        grammarSearchRepository.save(document);
        log.debug("Synced grammar topic: {}", topic.getId());
    }

    /**
     * Sync single course
     */
    public void syncCourse(Course course) {
        CourseDocument document = mapToCourseDocument(course);
        courseSearchRepository.save(document);
        log.debug("Synced course: {}", course.getId());
    }

    /**
     * Delete from Elasticsearch
     */
    public void deleteVocabulary(Long id) {
        vocabularySearchRepository.deleteById(id);
        log.debug("Deleted vocabulary from ES: {}", id);
    }

    public void deleteGrammarTopic(Long id) {
        grammarSearchRepository.deleteById(id);
        log.debug("Deleted grammar topic from ES: {}", id);
    }

    public void deleteCourse(Long id) {
        courseSearchRepository.deleteById(id);
        log.debug("Deleted course from ES: {}", id);
    }

    // ========== Mapping Methods ==========

    private VocabularyDocument mapToVocabularyDocument(Vocabulary vocab) {
        VocabularyDocument doc = VocabularyDocument.builder()
                .id(vocab.getId())
                .hanzi(vocab.getHanzi())
                .pinyin(vocab.getPinyin())
                .nghia(vocab.getNghia())
                .viDu(vocab.getExample())
                .variant(vocab.getVariant() != null ? vocab.getVariant().name() : null)
                .tags(vocab.getTags() != null ? new java.util.ArrayList<>(vocab.getTags()) : List.of())
                .hskLevel(vocab.getHskLevel())
                .frequencyRank(vocab.getFrequencyRank())
                .createdAt(vocab.getCreatedAt())
                .updatedAt(vocab.getUpdatedAt())
                .build();

        doc.buildSearchText();
        return doc;
    }

    private GrammarTopicDocument mapToGrammarDocument(GrammarTopic topic) {
        GrammarTopicDocument doc = GrammarTopicDocument.builder()
                .id(topic.getId())
                .title(topic.getTitle())
                .description(topic.getDescription())
                .content(topic.getContent())
                .level(topic.getLevel() != null ? topic.getLevel().name() : null)
                .createdAt(topic.getCreatedAt())
                .updatedAt(topic.getUpdatedAt())
                .build();

        doc.buildSearchText();
        return doc;
    }

    private CourseDocument mapToCourseDocument(Course course) {
        CourseDocument doc = CourseDocument.builder()
                .id(course.getId())
                .level(course.getLevel())
                .title(course.getTitle())
                .description(course.getDescription())
                .difficulty(course.getDifficulty())
                .textbookId(course.getTextbook() != null ? course.getTextbook().getId() : null)
                .textbookName(course.getTextbook() != null ? course.getTextbook().getName() : null)
                .createdAt(course.getCreatedAt())
                .build();

        doc.buildSearchText();
        return doc;
    }

}


