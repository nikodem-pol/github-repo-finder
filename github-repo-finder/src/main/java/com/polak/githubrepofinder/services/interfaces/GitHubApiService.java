package com.polak.githubrepofinder.services.interfaces;

import com.polak.githubrepofinder.dtos.RepositoryResponse;
import com.polak.githubrepofinder.exceptions.UserNotFoundException;
import reactor.core.publisher.Flux;

public interface GitHubApiService {
    Flux<RepositoryResponse> getUserRepositories(String username) throws UserNotFoundException;
}
