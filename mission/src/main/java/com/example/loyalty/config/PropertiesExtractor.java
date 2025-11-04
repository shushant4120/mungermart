package com.example.loyalty.config;

import org.springframework.beans.factory.annotation.Value;

public class PropertiesExtractor {
    private static String dbPass;

    @Value("${db.pass}")
    public void setDbPass(String pass) {
        PropertiesExtractor.dbPass = pass;
    }

    public static String getProperty(String key) {
        if ("db.pass".equals(key)) {
            return dbPass;
        }
        return null;
    }
}