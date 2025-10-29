package com.chineselearning.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * HTTP Request/Response Logging Interceptor
 * Logs all HTTP requests and measures response time
 * 
 * @author Senior Backend Architect
 */
@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME, startTime);

        log.info("===== HTTP REQUEST START =====");
        log.info("Method: {}", request.getMethod());
        log.info("URI: {}", request.getRequestURI());
        log.info("Query String: {}", request.getQueryString());
        log.info("Remote Addr: {}", request.getRemoteAddr());
        log.info("Headers: User-Agent={}", request.getHeader("User-Agent"));
        
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            log.info("Auth: {}", authHeader.startsWith("Bearer ") ? "Bearer [REDACTED]" : "Other");
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, 
                          ModelAndView modelAndView) {
        // Can be used for additional post-processing
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, 
                               Exception ex) {
        Long startTime = (Long) request.getAttribute(START_TIME);
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;

        log.info("===== HTTP RESPONSE END =====");
        log.info("Status: {}", response.getStatus());
        log.info("Execution Time: {} ms", executeTime);
        
        if (ex != null) {
            log.error("Exception during request: ", ex);
        }
        
        // Performance warning for slow requests
        if (executeTime > 1000) {
            log.warn("SLOW REQUEST DETECTED - URI: {}, Time: {} ms", request.getRequestURI(), executeTime);
        }
        
        log.info("================================");
    }
}

