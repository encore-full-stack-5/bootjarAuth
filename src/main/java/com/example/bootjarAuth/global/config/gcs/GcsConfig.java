package com.example.bootjarAuth.global.config.gcs;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import javax.annotation.PostConstruct;

@Configuration
public class GcsConfig {

    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String credentialsFileName;

    @PostConstruct
    public void init() {
        System.out.println("Project ID: " + projectId);
        System.out.println("Credentials File Name: " + credentialsFileName);
    }

    @Bean
    public Storage storage() throws IOException {
        ClassPathResource resource = new ClassPathResource(credentialsFileName);
        System.out.println(resource.getFilename());
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
        return StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build()
                .getService();
    }
}