package com.polak.githubrepofinder.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Slf4j
public class GitHubApiClientConfig {
    @Bean
    WebClient github() {
        return WebClient.create("https://api.github.com");
    }
}
