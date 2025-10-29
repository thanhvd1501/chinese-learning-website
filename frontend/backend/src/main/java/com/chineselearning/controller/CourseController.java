package com.chineselearning.controller;

import com.chineselearning.dto.request.CreateCourseRequest;
import com.chineselearning.dto.request.UpdateCourseRequest;
import com.chineselearning.dto.response.CourseResponse;
import com.chineselearning.service.interfaces.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Khóa học API")
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Lấy danh sách khóa học", description = "Lấy danh sách tất cả khóa học (Public)")
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getCourses(
            @Parameter(description = "Filter theo textbook ID") @RequestParam(required = false) Long textbookId
    ) {
        List<CourseResponse> courses = textbookId != null 
                ? courseService.getCoursesByTextbookId(textbookId)
                : courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @Operation(summary = "Lấy khóa học theo ID", description = "Lấy chi tiết một khóa học cụ thể (Public)")
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(
            @Parameter(description = "ID của khóa học") @PathVariable Long id
    ) {
        CourseResponse course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    @Operation(
            summary = "Tạo khóa học mới", 
            description = "Tạo khóa học mới (Chỉ ADMIN hoặc TEACHER)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<CourseResponse> createCourse(
            @Valid @RequestBody CreateCourseRequest request
    ) {
        CourseResponse course = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }

    @Operation(
            summary = "Cập nhật khóa học", 
            description = "Cập nhật thông tin khóa học (Chỉ ADMIN hoặc TEACHER)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<CourseResponse> updateCourse(
            @Parameter(description = "ID của khóa học") @PathVariable Long id,
            @Valid @RequestBody UpdateCourseRequest request
    ) {
        CourseResponse course = courseService.updateCourse(id, request);
        return ResponseEntity.ok(course);
    }

    @Operation(
            summary = "Xóa khóa học", 
            description = "Xóa khóa học (Chỉ ADMIN)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(
            @Parameter(description = "ID của khóa học") @PathVariable Long id
    ) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}

