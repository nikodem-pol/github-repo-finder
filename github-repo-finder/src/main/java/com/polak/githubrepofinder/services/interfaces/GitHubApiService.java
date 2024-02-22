package com.polak.githubrepofinder.services.interfaces;

import com.polak.githubrepofinder.dtos.RepositoryDto;
import com.polak.githubrepofinder.exceptions.UserNotFoundException;
import reactor.core.publisher.Flux;

public interface GitHubApiService {
    Flux<RepositoryDto> getUserRepositories(String username) throws UserNotFoundException;
}
