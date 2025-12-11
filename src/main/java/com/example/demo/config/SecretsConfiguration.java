package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class SecretsConfiguration {

    @Value("${SPRING_DATASOURCE_PASSWORD_FILE:}")
    private String dbPasswordFile;

    @Value("${API_KEY_FILE:}")
    private String apiKeyFile;

    public String getDatabasePassword() {
        if (dbPasswordFile != null && !dbPasswordFile.isEmpty()) {
            try {
                return Files.readString(Paths.get(dbPasswordFile)).trim();
            } catch (IOException e) {
                System.err.println("Failed to read database password from file: " + e.getMessage());
            }
        }
        return System.getenv("SPRING_DATASOURCE_PASSWORD");
    }

    public String getApiKey() {
        if (apiKeyFile != null && !apiKeyFile.isEmpty()) {
            try {
                return Files.readString(Paths.get(apiKeyFile)).trim();
            } catch (IOException e) {
                System.err.println("Failed to read API key from file: " + e.getMessage());
            }
        }
        return System.getenv("API_KEY");
    }
}