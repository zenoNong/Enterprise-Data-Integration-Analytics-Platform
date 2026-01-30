package com.zeno.analytics;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/analytics")
@EnableCaching // Enable Redis Caching
public class AnalyticsController {

    private final TransactionRepository repository;

    public AnalyticsController(TransactionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/summary")
    @Cacheable(value = "stats") // Hits Redis first, ignores DB if cached
    public String getSummary() {
        // Simulate expensive query
        long count = repository.count();
        try { Thread.sleep(2000); } catch (InterruptedException e) {} // Fake latency
        return "Total Records Processed: " + count;
    }
}