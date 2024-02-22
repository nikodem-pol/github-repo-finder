package com.polak.githubrepofinder.configuration;

import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GitHub;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Configuration
@Slf4j
public class GitHubConfig {
    @Bean
    WebClient github() {
        return WebClient.create("https://");
    }
}
