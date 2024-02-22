package com.polak.githubrepofinder.services.interfaces;

import com.polak.githubrepofinder.dtos.GitHubRepository;
import com.polak.githubrepofinder.dtos.RepositoriesResponse;
import com.polak.githubrepofinder.dtos.RepositoryDto;
import com.polak.githubrepofinder.exceptions.UserNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface GitHubApiService {
    Flux<RepositoryDto> getUserRepositories(String username) throws UserNotFoundException;
}
