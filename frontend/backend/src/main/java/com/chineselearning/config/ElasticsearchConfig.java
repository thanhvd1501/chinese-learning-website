package com.chineselearning.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Elasticsearch Configuration
 * Configures Elasticsearch client and repositories
 * 
 * @author Senior Backend Architect
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.chineselearning.search.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris}")
    private String elasticsearchUris;

    @Value("${spring.elasticsearch.username:}")
    private String username;

    @Value("${spring.elasticsearch.password:}")
    private String password;

    @Value("${spring.elasticsearch.socket-timeout:10s}")
    private String socketTimeout;

    @Value("${spring.elasticsearch.connection-timeout:5s}")
    private String connectionTimeout;

    @Override
    public ClientConfiguration clientConfiguration() {
        ClientConfiguration.MaybeSecureClientConfigurationBuilder builder = ClientConfiguration.builder()
                .connectedTo(elasticsearchUris.replace("http://", "").replace("https://", ""));

        // Add basic auth if credentials provided
        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            builder.withBasicAuth(username, password);
        }

        return builder
                .withSocketTimeout(parseDuration(socketTimeout))
                .withConnectTimeout(parseDuration(connectionTimeout))
                .build();
    }

    private java.time.Duration parseDuration(String duration) {
        if (duration.endsWith("s")) {
            return java.time.Duration.ofSeconds(Long.parseLong(duration.replace("s", "")));
        } else if (duration.endsWith("ms")) {
            return java.time.Duration.ofMillis(Long.parseLong(duration.replace("ms", "")));
        }
        return java.time.Duration.ofSeconds(10);
    }
}

