package com.zeno.ingestion;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
public class BatchConfig {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public BatchConfig(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // READER: Simulating reading 10k rows from CSV
    @Bean
    public ItemReader<RecordDto> reader() {
        List<RecordDto> items = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            items.add(new RecordDto("CSV", UUID.randomUUID().toString(), Math.random() * 100, "SALES"));
        }
        return new ListItemReader<>(items);
    }

    // WRITER: Publish to Kafka (Decoupling)
    @Bean
    public ItemWriter<RecordDto> writer() {
        return items -> {
            for (RecordDto item : items) {
                // Sending JSON string to Kafka topic 'enterprise-data'
                kafkaTemplate.send("enterprise-data", item.getTransactionId(), item.toString());
            }
            System.out.println("Pushed " + items.size() + " records to Kafka.");
        };
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("csv-to-kafka-step", jobRepository)
                .<RecordDto, RecordDto>chunk(1000, transactionManager) // Batch size 1000
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public Job exportJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("exportJob", jobRepository)
                .start(step1)
                .build();
    }
}