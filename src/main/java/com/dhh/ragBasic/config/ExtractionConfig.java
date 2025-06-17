package com.dhh.ragBasic.config;

import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExtractionConfig {

    @Bean
    public Tika tika() {
        return new Tika();
    }
}