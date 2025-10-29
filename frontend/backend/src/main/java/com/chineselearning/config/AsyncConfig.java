package com.chineselearning.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Async and Scheduling Configuration
 * Enables async processing and scheduled tasks
 * 
 * @author Senior Backend Architect
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfig {
    // Configuration for async and scheduling support
}

