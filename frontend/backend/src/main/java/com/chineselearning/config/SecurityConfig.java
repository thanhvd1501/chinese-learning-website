package com.chineselearning.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints
                        .requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // Public READ endpoints
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/vocab/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/textbooks/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/courses/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/grammar/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/radicals/**").permitAll()
                        // Protected WRITE endpoints (POST, PUT, DELETE) - require authentication
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/courses/**").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/courses/**").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/courses/**").authenticated()
                        // Admin endpoints - cache management
                        .requestMatchers("/api/admin/**").authenticated()
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                );

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://localhost:3000",
                "http://localhost:5174"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CorsFilter corsFilter(UrlBasedCorsConfigurationSource corsConfigurationSource) {
        return new CorsFilter(corsConfigurationSource);
    }
}

