package com.zeno.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import java.util.TimeZone;

@SpringBootApplication
@EnableCaching
public class AnalyticsApplication {

    public static void main(String[] args) {
        // FIX: Set TimeZone BEFORE Spring Boot starts
        // This ensures the DB driver never sends "Asia/Calcutta"
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));

        SpringApplication.run(AnalyticsApplication.class, args);
    }
}