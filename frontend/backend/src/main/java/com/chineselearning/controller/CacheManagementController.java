package com.chineselearning.controller;

import com.chineselearning.config.RedisCacheStatistics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Cache Management Controller
 * 
 * Endpoints cho admin để monitor và quản lý Redis cache
 * 
 * @author Senior Backend Architect
 */
@RestController
@RequestMapping("/api/admin/cache")
@RequiredArgsConstructor
@Tag(name = "Cache Management", description = "Admin endpoints for cache monitoring and management")
public class CacheManagementController {

    private final RedisCacheStatistics cacheStatistics;

    @Operation(
            summary = "Get cache statistics",
            description = "Lấy thống kê cache (Chỉ ADMIN)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        Map<String, Object> stats = cacheStatistics.getCacheStatistics();
        return ResponseEntity.ok(stats);
    }

    @Operation(
            summary = "Clear specific cache",
            description = "Xóa cache theo tên (Chỉ ADMIN)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @DeleteMapping("/{cacheName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> clearCache(
            @Parameter(description = "Tên cache cần xóa (vocabularies, courses, etc.)")
            @PathVariable String cacheName
    ) {
        cacheStatistics.clearCache(cacheName);
        return ResponseEntity.ok(Map.of(
                "message", "Cache cleared successfully",
                "cacheName", cacheName
        ));
    }

    @Operation(
            summary = "Clear all caches",
            description = "Xóa tất cả cache (Chỉ ADMIN)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @DeleteMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> clearAllCaches() {
        cacheStatistics.clearAllCaches();
        return ResponseEntity.ok(Map.of(
                "message", "All caches cleared successfully"
        ));
    }

    @Operation(
            summary = "Warm up cache",
            description = "Pre-load frequently accessed data vào cache (Chỉ ADMIN)",
            security = @SecurityRequirement(name = "bearer-jwt")
    )
    @PostMapping("/warmup")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> warmUpCache() {
        cacheStatistics.warmUpCache();
        return ResponseEntity.ok(Map.of(
                "message", "Cache warm-up initiated"
        ));
    }
}

