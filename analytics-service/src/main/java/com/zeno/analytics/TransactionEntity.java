package com.zeno.analytics;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TransactionEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String originalData; // Storing raw string for MVP simplicity
}