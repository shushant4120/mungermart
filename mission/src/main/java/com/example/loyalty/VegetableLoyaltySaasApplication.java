package com.example.loyalty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VegetableLoyaltySaasApplication {
    public static void main(String[] args) {
        SpringApplication.run(VegetableLoyaltySaasApplication.class, args);
    }
}