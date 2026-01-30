package com.zeno.ingestion;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingest")
public class IngestionController {

    private final JobLauncher jobLauncher;
    private final Job exportJob;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public IngestionController(JobLauncher jobLauncher, Job exportJob, KafkaTemplate<String, String> kafkaTemplate) {
        this.jobLauncher = jobLauncher;
        this.exportJob = exportJob;
        this.kafkaTemplate = kafkaTemplate;
    }

    // 1. Trigger Batch (CSV simulation)
    @PostMapping("/batch")
    public String runBatch() throws Exception {
        jobLauncher.run(exportJob, new JobParametersBuilder().addLong("startAt", System.currentTimeMillis()).toJobParameters());
        return "Batch Job Started!";
    }

    // 2. REST Ingestion (Satisfies "CSV + REST" resume point)
    @PostMapping("/single")
    public String ingestSingle(@RequestBody RecordDto dto) {
        dto.setSource("REST");
        kafkaTemplate.send("enterprise-data", dto.getTransactionId(), dto.toString());
        return "Single record pushed to Kafka";
    }
}