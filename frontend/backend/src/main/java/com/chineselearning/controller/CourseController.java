package com.chineselearning.controller;

import com.chineselearning.domain.Course;
import com.chineselearning.repository.CourseRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Khóa học API")
public class CourseController {

    private final CourseRepository courseRepository;

    @Operation(summary = "Lấy danh sách khóa học", description = "Lấy danh sách tất cả khóa học")
    @GetMapping
    public ResponseEntity<List<Course>> getCourses(
            @RequestParam(required = false) Long textbookId,
            @RequestParam(required = false) String difficulty
    ) {
        if (textbookId != null) {
            return ResponseEntity.ok(courseRepository.findByTextbookId(textbookId));
        }
        List<Course> courses = courseRepository.findAll();
        return ResponseEntity.ok(courses);
    }

    @Operation(summary = "Lấy khóa học theo ID", description = "Lấy chi tiết một khóa học cụ thể")
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseRepository.findById(id);
        return course.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

