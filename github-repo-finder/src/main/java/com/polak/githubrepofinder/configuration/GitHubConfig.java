package com.polak.githubrepofinder.configuration;

import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GitHub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@Slf4j
public class GitHubConfig {
    @Bean
    GitHub github() {
        try {
            return GitHub.connectAnonymously();
        } catch (IOException e) {
            log.error("Failed to connect to GitHub API.");
            throw new RuntimeException(e.getMessage());
        }
    }
}
