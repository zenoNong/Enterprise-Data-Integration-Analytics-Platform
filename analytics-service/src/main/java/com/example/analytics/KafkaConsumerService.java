package com.example.analytics;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class KafkaConsumerService {

    @Autowired
    private SalesRepository repository;

    @KafkaListener(topics = "enterprise_data_topic", groupId = "analytics_group")
    public void consume(String message) {
        System.out.println("📥 Received: " + message);

        // Parse message "TXN1001,500.00,ELECTRONICS"
        String[] parts = message.split(",");
        if (parts.length == 3) {
            SalesData data = new SalesData(parts[0], Double.parseDouble(parts[1]), parts[2]);
            repository.save(data);
        }
    }
}