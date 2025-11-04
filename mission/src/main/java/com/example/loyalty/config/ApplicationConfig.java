package com.example.loyalty.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public PropertiesExtractor propertiesExtractor() {
        return new PropertiesExtractor();
    }
}