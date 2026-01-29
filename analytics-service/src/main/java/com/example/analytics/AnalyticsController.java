package com.example.analytics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/analytics/revenue")
    public String getTotalRevenue() {
        Double revenue = analyticsService.calculateTotalRevenue();
        return "Total Revenue: $" + (revenue != null ? revenue : 0.0);
    }
}