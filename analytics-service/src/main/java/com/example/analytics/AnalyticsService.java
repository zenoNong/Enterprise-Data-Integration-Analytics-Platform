package com.example.analytics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

@Service
@EnableCaching
public class AnalyticsService {

    @Autowired
    private SalesRepository repository;

    @Cacheable(value = "totalSales")
    public Double calculateTotalRevenue() {
        System.out.println("⚠️ Fetching from Database (Not Cached)...");
        // Simulate slow query
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        return repository.getTotalSales();
    }
}