package com.polak.githubrepofinder.services.interfaces;

import com.polak.githubrepofinder.dtos.GitHubBranch;
import com.polak.githubrepofinder.dtos.GitHubRepository;
import reactor.core.publisher.Flux;

public interface GitHubApiService {
    Flux<GitHubRepository> getNonForkRepositories(String username);
    Flux<GitHubBranch> getRepositoryBranches(String username, String repoName);
}
