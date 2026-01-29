package com.example.ingestion;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @PostMapping("/run-batch")
    public String runBatch() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            // We suppress deprecation here because JobLauncher is standard
            // even if the snapshot version marks it for future removal.
            jobLauncher.run(job, params);

            return "Batch Job Started!";
        } catch (Exception e) {
            e.printStackTrace(); // Good for debugging console logs
            return "Job Failed: " + e.getMessage();
        }
    }
}