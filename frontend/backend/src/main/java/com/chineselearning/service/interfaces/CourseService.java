package com.chineselearning.service.interfaces;

import com.chineselearning.dto.request.CreateCourseRequest;
import com.chineselearning.dto.request.UpdateCourseRequest;
import com.chineselearning.dto.response.CourseResponse;

import java.util.List;

/**
 * Course Service Interface
 * Defines operations for course management
 * 
 * @author Senior Backend Architect
 */
public interface CourseService {

    /**
     * Get all courses
     */
    List<CourseResponse> getAllCourses();

    /**
     * Get courses by textbook ID
     */
    List<CourseResponse> getCoursesByTextbookId(Long textbookId);

    /**
     * Get course by ID
     */
    CourseResponse getCourseById(Long id);

    /**
     * Create new course
     */
    CourseResponse createCourse(CreateCourseRequest request);

    /**
     * Update existing course
     */
    CourseResponse updateCourse(Long id, UpdateCourseRequest request);

    /**
     * Delete course
     */
    void deleteCourse(Long id);
}

