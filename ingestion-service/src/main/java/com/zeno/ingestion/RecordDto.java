package com.zeno.ingestion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordDto {
    private String source; // "CSV" or "REST"
    private String transactionId;
    private Double amount;
    private String category;
}