package com.dhh.ragBasic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "qdrant")
public class QdrantConfig {
    private String baseUrl;
    private String collection;
    private String apiKey;
}
