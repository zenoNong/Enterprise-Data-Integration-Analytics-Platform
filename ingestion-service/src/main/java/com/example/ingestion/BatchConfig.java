package com.example.ingestion;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    // 1. READER
    @Bean
    public FlatFileItemReader<TransactionRecord> reader() {
        return new FlatFileItemReaderBuilder<TransactionRecord>()
                .name("transactionItemReader")
                .resource(new ClassPathResource("data.csv"))
                .delimited()
                .names("transactionId", "amount", "category")
                .targetType(TransactionRecord.class)
                .build();
    }

    // 2. PROCESSOR
    @Bean
    public ItemProcessor<TransactionRecord, TransactionRecord> processor() {
        return item -> {
            item.setCategory(item.getCategory().toUpperCase());
            return item;
        };
    }

    // 3. WRITER
    @Bean
    public ItemWriter<TransactionRecord> writer(KafkaTemplate<String, String> kafkaTemplate) {
        return items -> {
            for (TransactionRecord item : items) {
                String message = item.getTransactionId() + "," + item.getAmount() + "," + item.getCategory();
                kafkaTemplate.send("enterprise_data_topic", message);
                System.out.println("🚀 Sent to Kafka: " + message);
            }
        };
    }

    // 4. STEP
    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      FlatFileItemReader<TransactionRecord> reader,
                      ItemProcessor<TransactionRecord, TransactionRecord> processor,
                      ItemWriter<TransactionRecord> writer) {
        return new StepBuilder("step1", jobRepository)
                .<TransactionRecord, TransactionRecord>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    // 5. JOB
    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .start(step1)
                .build();
    }
}