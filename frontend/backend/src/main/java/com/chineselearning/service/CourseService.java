package com.chineselearning.service;

import com.chineselearning.domain.Course;
import com.chineselearning.domain.Textbook;
import com.chineselearning.dto.request.CreateCourseRequest;
import com.chineselearning.dto.request.UpdateCourseRequest;
import com.chineselearning.dto.response.CourseResponse;
import com.chineselearning.exception.custom.DuplicateResourceException;
import com.chineselearning.exception.custom.ResourceNotFoundException;
import com.chineselearning.mapper.CourseMapper;
import com.chineselearning.repository.CourseRepository;
import com.chineselearning.repository.TextbookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Course Service Implementation
 * Implements business logic for course management
 * 
 * @author Senior Backend Architect
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CourseServiceImpl implements com.chineselearning.service.interfaces.CourseService {

    private final CourseRepository courseRepository;
    private final TextbookRepository textbookRepository;
    private final CourseMapper courseMapper;

    @Override
    @Cacheable(value = "courses", key = "'all'")
    public List<CourseResponse> getAllCourses() {
        log.debug("Fetching all courses");
        return courseRepository.findAll().stream()
                .map(courseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "courses", key = "'textbook-' + #textbookId")
    public List<CourseResponse> getCoursesByTextbookId(Long textbookId) {
        log.debug("Fetching courses for textbook: {}", textbookId);
        return courseRepository.findByTextbookId(textbookId).stream()
                .map(courseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "courses", key = "#id")
    public CourseResponse getCourseById(Long id) {
        log.debug("Fetching course by id: {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        return courseMapper.toResponse(course);
    }

    @Override
    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public CourseResponse createCourse(CreateCourseRequest request) {
        log.info("Creating new course: {}", request.getTitle());

        // Validate textbook exists
        Textbook textbook = textbookRepository.findById(request.getTextbookId())
                .orElseThrow(() -> new ResourceNotFoundException("Textbook", "id", request.getTextbookId()));

        // Check if course with same level already exists for this textbook
        courseRepository.findByLevelAndTextbookId(request.getLevel(), request.getTextbookId())
                .ifPresent(existingCourse -> {
                    throw new DuplicateResourceException(
                            "Course",
                            "level for textbook",
                            request.getLevel() + " in " + textbook.getName()
                    );
                });

        Course course = courseMapper.toEntity(request);
        course.setTextbook(textbook);

        Course savedCourse = courseRepository.save(course);
        log.info("Course created successfully with id: {}", savedCourse.getId());

        return courseMapper.toResponse(savedCourse);
    }

    @Override
    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public CourseResponse updateCourse(Long id, UpdateCourseRequest request) {
        log.info("Updating course with id: {}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));

        if (request.getLevel() != null) {
            course.setLevel(request.getLevel());
        }
        if (request.getTitle() != null) {
            course.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            course.setDescription(request.getDescription());
        }
        if (request.getLessons() != null) {
            course.setLessons(request.getLessons());
        }
        if (request.getDuration() != null) {
            course.setDuration(request.getDuration());
        }
        if (request.getDifficulty() != null) {
            course.setDifficulty(request.getDifficulty());
        }
        if (request.getCoverImageUrl() != null) {
            course.setCoverImageUrl(request.getCoverImageUrl());
        }

        Course updatedCourse = courseRepository.save(course);
        log.info("Course updated successfully: {}", updatedCourse.getId());

        return courseMapper.toResponse(updatedCourse);
    }

    @Override
    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public void deleteCourse(Long id) {
        log.info("Deleting course with id: {}", id);

        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course", "id", id);
        }

        courseRepository.deleteById(id);
        log.info("Course deleted successfully: {}", id);
    }
}

