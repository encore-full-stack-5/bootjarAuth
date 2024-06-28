package com.example.bootjarAuth.global.config.gcs;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class GcsConfig {

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String credentialsFileName;

    @PostConstruct
    public void init() {
        log.info("Project ID: " + projectId);
        log.info("Credentials File Name: " + credentialsFileName);
    }

    @Bean
    public Storage storage() throws IOException {
//        ClassPathResource resource = new ClassPathResource(credentialsFileName);
        InputStream resource = new FileInputStream(credentialsFileName);
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource);
        return StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build()
                .getService();
    }
}