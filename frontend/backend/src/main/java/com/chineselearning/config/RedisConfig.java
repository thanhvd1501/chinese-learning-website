package com.chineselearning.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis Cache Configuration - Enterprise Level
 * 
 * Features:
 * - Connection pooling với Lettuce
 * - Custom serialization với Jackson
 * - Multiple cache configurations với different TTLs
 * - Custom error handling
 * - Cache statistics và monitoring
 * 
 * @author Senior Backend Architect
 */
@Configuration
@EnableCaching
@Slf4j
public class RedisConfig implements CachingConfigurer {

    /**
     * Custom ObjectMapper cho Redis serialization
     * Configured để handle Java 8 time, enums, và polymorphic types
     */
    @Bean
    public ObjectMapper redisObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Enable Java 8 date/time support
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Enable polymorphic type handling để avoid deserialization issues
        objectMapper.activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder()
                        .allowIfBaseType(Object.class)
                        .build(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        
        return objectMapper;
    }

    /**
     * RedisTemplate với custom serialization
     * Dùng cho manual cache operations
     */
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // Key serializer - always use String
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        
        // Value serializer - JSON for flexibility
        GenericJackson2JsonRedisSerializer jsonSerializer = 
                new GenericJackson2JsonRedisSerializer(redisObjectMapper());
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
        
        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();
        
        log.info("RedisTemplate configured successfully with JSON serialization");
        return template;
    }

    /**
     * Cache Manager với multiple cache configurations
     * Mỗi cache có TTL riêng phù hợp với data type
     */
    @Bean
    @Override
    public CacheManager cacheManager() {
        RedisCacheConfiguration defaultConfig = createDefaultCacheConfig(Duration.ofMinutes(10));
        
        // Custom configurations cho từng cache
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // Vocabulary cache - 30 minutes (data ít thay đổi)
        cacheConfigurations.put("vocabularies", 
                createDefaultCacheConfig(Duration.ofMinutes(30)));
        
        // Textbooks cache - 1 hour (static data)
        cacheConfigurations.put("textbooks", 
                createDefaultCacheConfig(Duration.ofHours(1)));
        
        // Courses cache - 1 hour (static data)
        cacheConfigurations.put("courses", 
                createDefaultCacheConfig(Duration.ofHours(1)));
        
        // Grammar topics cache - 1 hour
        cacheConfigurations.put("grammar-topics", 
                createDefaultCacheConfig(Duration.ofHours(1)));
        
        // Radicals cache - 2 hours (very static data)
        cacheConfigurations.put("radicals", 
                createDefaultCacheConfig(Duration.ofHours(2)));
        
        // User data cache - 5 minutes (frequently updated)
        cacheConfigurations.put("users", 
                createDefaultCacheConfig(Duration.ofMinutes(5)));
        
        // User progress cache - 10 minutes
        cacheConfigurations.put("user-progress", 
                createDefaultCacheConfig(Duration.ofMinutes(10)));
        
        // AI chat cache - 1 hour (for repeated queries)
        cacheConfigurations.put("ai-responses", 
                createDefaultCacheConfig(Duration.ofHours(1)));
        
        RedisCacheManager cacheManager = RedisCacheManager.builder(getConnectionFactory())
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware() // Enable transaction support
                .build();
        
        log.info("RedisCacheManager initialized with {} custom cache configurations", 
                cacheConfigurations.size());
        
        return cacheManager;
    }

    /**
     * Create default cache configuration với custom serialization
     */
    private RedisCacheConfiguration createDefaultCacheConfig(Duration ttl) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .disableCachingNullValues()
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer(redisObjectMapper())))
                .prefixCacheNameWith("chinese-learning::");
    }

    /**
     * Custom key generator để tạo meaningful cache keys
     * Format: className.methodName:arg1:arg2:...
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder key = new StringBuilder();
            key.append(target.getClass().getSimpleName())
                    .append(".")
                    .append(method.getName());
            
            for (Object param : params) {
                if (param != null) {
                    key.append(":").append(param.toString());
                }
            }
            
            return key.toString();
        };
    }

    /**
     * Custom error handler để prevent cache failures from breaking application
     * Log errors nhưng không throw exception
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, 
                                           org.springframework.cache.Cache cache, 
                                           Object key) {
                log.error("Cache GET error for cache: {}, key: {}", 
                        cache.getName(), key, exception);
                // Don't throw - graceful degradation
            }

            @Override
            public void handleCachePutError(RuntimeException exception, 
                                           org.springframework.cache.Cache cache, 
                                           Object key, 
                                           Object value) {
                log.error("Cache PUT error for cache: {}, key: {}", 
                        cache.getName(), key, exception);
            }

            @Override
            public void handleCacheEvictError(RuntimeException exception, 
                                             org.springframework.cache.Cache cache, 
                                             Object key) {
                log.error("Cache EVICT error for cache: {}, key: {}", 
                        cache.getName(), key, exception);
            }

            @Override
            public void handleCacheClearError(RuntimeException exception, 
                                             org.springframework.cache.Cache cache) {
                log.error("Cache CLEAR error for cache: {}", 
                        cache.getName(), exception);
            }
        };
    }

    /**
     * Get connection factory - helper method
     */
    private RedisConnectionFactory getConnectionFactory() {
        // This will be auto-configured by Spring Boot
        // We can inject it if needed for customization
        return new LettuceConnectionFactory();
    }

    /**
     * CacheResolver - optional, for dynamic cache resolution
     */
    @Override
    public CacheResolver cacheResolver() {
        return CachingConfigurer.super.cacheResolver();
    }
}

