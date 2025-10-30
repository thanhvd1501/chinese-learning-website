package com.chineselearning.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Redis Cache Statistics & Health Monitoring
 * 
 * Provides:
 * - Health check endpoint
 * - Cache statistics
 * - Memory usage monitoring
 * 
 * @author Senior Backend Architect
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RedisCacheStatistics implements HealthIndicator {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisConnectionFactory connectionFactory;

    @Override
    public Health health() {
        try {
            // Ping Redis để check connection
            String pong = redisTemplate.getConnectionFactory()
                    .getConnection()
                    .ping();
            
            if ("PONG".equals(pong)) {
                Map<String, Object> details = getCacheStatistics();
                return Health.up()
                        .withDetails(details)
                        .build();
            } else {
                return Health.down()
                        .withDetail("error", "Redis ping failed")
                        .build();
            }
        } catch (Exception e) {
            log.error("Redis health check failed", e);
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }

    /**
     * Get cache statistics
     */
    public Map<String, Object> getCacheStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        try {
            // Get Redis info
            var connection = redisTemplate.getConnectionFactory().getConnection();
            Properties info = connection.info();
            
            stats.put("connected", true);
            stats.put("uptime_in_seconds", extractValue(info, "uptime_in_seconds"));
            stats.put("used_memory_human", extractValue(info, "used_memory_human"));
            stats.put("connected_clients", extractValue(info, "connected_clients"));
            stats.put("total_commands_processed", extractValue(info, "total_commands_processed"));
            
            // Get cache keys count
            Set<String> keys = redisTemplate.keys("chinese-learning::*");
            stats.put("total_cached_keys", keys != null ? keys.size() : 0);
            
            // Get cache by prefix
            Map<String, Integer> cacheBreakdown = new HashMap<>();
            if (keys != null) {
                for (String key : keys) {
                    String cacheType = extractCacheType(key);
                    cacheBreakdown.merge(cacheType, 1, Integer::sum);
                }
            }
            stats.put("cache_breakdown", cacheBreakdown);
            
        } catch (Exception e) {
            log.error("Error collecting cache statistics", e);
            stats.put("error", e.getMessage());
        }
        
        return stats;
    }

    /**
     * Clear specific cache
     */
    public void clearCache(String cacheName) {
        try {
            String pattern = "chinese-learning::" + cacheName + "::*";
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("Cleared {} keys from cache: {}", keys.size(), cacheName);
            }
        } catch (Exception e) {
            log.error("Error clearing cache: {}", cacheName, e);
        }
    }

    /**
     * Clear all caches
     */
    public void clearAllCaches() {
        try {
            Set<String> keys = redisTemplate.keys("chinese-learning::*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.info("Cleared all {} cache keys", keys.size());
            }
        } catch (Exception e) {
            log.error("Error clearing all caches", e);
        }
    }

    /**
     * Warm up cache với commonly accessed data
     */
    public void warmUpCache() {
        log.info("Starting cache warm-up...");
        // This can be triggered on application startup
        // Load frequently accessed data into cache
        // Implementation depends on business requirements
    }

    /**
     * Extract value from Redis info Properties
     */
    private String extractValue(Properties info, String key) {
        if (info == null) {
            return "N/A";
        }
        Object value = info.get(key);
        return value != null ? value.toString() : "N/A";
    }

    private String extractCacheType(String key) {
        String[] parts = key.split("::");
        return parts.length > 1 ? parts[1] : "unknown";
    }
}

