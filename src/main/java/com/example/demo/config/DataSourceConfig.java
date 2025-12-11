package com.example.demo.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${SPRING_DATASOURCE_PASSWORD:}")
    private String passwordEnv;

    @Value("${SPRING_DATASOURCE_PASSWORD_FILE:}")
    private String passwordFile;

    @Bean
    public DataSource dataSource() {
        String password = getPassword();

        return DataSourceBuilder
                .create()
                .url(url)
                .username(username)
                .password(password)
                .build();
    }

    private String getPassword() {
        if (passwordFile != null && !passwordFile.isEmpty()) {
            try {
                return Files.readString(Paths.get(passwordFile)).trim();
            } catch (IOException e) {
                System.err.println("Failed to read password from file: " + passwordFile);
                System.err.println("Error: " + e.getMessage());
            }
        }

        if (passwordEnv != null && !passwordEnv.isEmpty()) {
            return passwordEnv;
        }
        throw new RuntimeException("Database password not configured!");
    }
}