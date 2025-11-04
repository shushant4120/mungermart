package com.example.loyalty.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@TestConfiguration
@EnableMongoRepositories(basePackages = "com.example.loyalty.repository")
public class MongoTestConfig {

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "vegetable_loyalty_test");
    }

    @Bean(destroyMethod = "close")
    public MongoClient mongoClient() {
        return MongoClients.create();
    }
}