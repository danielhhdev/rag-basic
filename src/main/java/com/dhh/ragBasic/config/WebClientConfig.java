package com.dhh.ragBasic.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final QdrantConfig qdrantConfig;

    @Bean
    public WebClient qdrantWebClient() {
        // Si necesitas pasar API Key o headers especiales, añádelos aquí
        WebClient.Builder builder = WebClient.builder()
                .baseUrl(qdrantConfig.getBaseUrl());
        if (qdrantConfig.getApiKey() != null && !qdrantConfig.getApiKey().isEmpty()) {
            builder.defaultHeader("api-key", qdrantConfig.getApiKey());
        }
        return builder.build();
    }
}