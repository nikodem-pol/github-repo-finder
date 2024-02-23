package com.polak.githubrepofinder.services.interfaces;

import com.polak.githubrepofinder.dtos.RepositoryResponse;
import reactor.core.publisher.Flux;

public interface GitHubRepositoriesService {
    Flux<RepositoryResponse> getUserRepositories(String username);
}
