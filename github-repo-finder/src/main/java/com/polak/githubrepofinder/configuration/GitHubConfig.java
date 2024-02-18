package com.polak.githubrepofinder.configuration;

import com.polak.githubrepofinder.exceptions.ConnectionFailedException;
import org.kohsuke.github.GitHub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GitHubConfig {
    @Bean
    GitHub github() throws ConnectionFailedException {
        try {
            return GitHub.connectAnonymously();
        } catch (IOException e) {
            throw new ConnectionFailedException("Failed while connecting to GitHub.");
        }
    }
}
