package com.example.loyalty.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

/**
 * MongoDB configuration class.
 * Uses Spring-managed beans instead of static access.
 */
@Configuration
public class DBConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database:vegetable_loyalty_saas}")
    private String dbName;

    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder -> builder
                        .maxConnectionLifeTime(10, TimeUnit.MINUTES)
                        .maxWaitTime(10, TimeUnit.SECONDS)
                        .maxSize(50)
                        .minSize(5))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public String mongoDatabaseName() {
        return dbName;
    }
}
