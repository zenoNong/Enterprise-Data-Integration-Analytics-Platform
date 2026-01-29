package com.example.ingestion;
import lombok.Data;

@Data
public class TransactionRecord {
    private String transactionId;
    private Double amount;
    private String category;
}