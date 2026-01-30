package com.zeno.analytics;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final TransactionRepository repository;

    public KafkaConsumerService(TransactionRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "enterprise-data", groupId = "analytics-group")
    public void consume(String message) {
        // In real life, parse JSON here. MVP: Just save.
        TransactionEntity entity = new TransactionEntity();
        entity.setOriginalData(message);
        repository.save(entity);
        System.out.println("Persisted: " + message.substring(0, 20) + "...");
    }
}