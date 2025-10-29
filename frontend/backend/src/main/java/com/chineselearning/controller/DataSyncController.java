package com.chineselearning.controller;

import com.chineselearning.search.service.DataSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Data Synchronization Controller
 * Manual trigger for Elasticsearch sync (Admin only)
 * 
 * @author Senior Backend Architect
 */
@RestController
@RequestMapping("/api/admin/sync")
@RequiredArgsConstructor
@Tag(name = "Data Sync", description = "Đồng bộ dữ liệu PostgreSQL -> Elasticsearch")
public class DataSyncController {

    private final DataSyncService dataSyncService;

    @Operation(
            summary = "Đồng bộ toàn bộ dữ liệu",
            description = "Sync tất cả entities từ PostgreSQL sang Elasticsearch (Chỉ ADMIN)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @PostMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> syncAll() {
        dataSyncService.syncAllData();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Data sync started successfully"
        ));
    }

    @Operation(
            summary = "Đồng bộ từ vựng",
            description = "Sync vocabulary data (Chỉ ADMIN)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @PostMapping("/vocabulary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> syncVocabulary() {
        dataSyncService.syncVocabulary();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Vocabulary sync completed"
        ));
    }

    @Operation(
            summary = "Đồng bộ ngữ pháp",
            description = "Sync grammar topics (Chỉ ADMIN)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @PostMapping("/grammar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> syncGrammar() {
        dataSyncService.syncGrammarTopics();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Grammar sync completed"
        ));
    }

    @Operation(
            summary = "Đồng bộ khóa học",
            description = "Sync courses (Chỉ ADMIN)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @PostMapping("/courses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> syncCourses() {
        dataSyncService.syncCourses();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Courses sync completed"
        ));
    }
}

