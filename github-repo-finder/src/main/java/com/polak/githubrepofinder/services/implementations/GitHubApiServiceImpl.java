package com.polak.githubrepofinder.services.implementations;

import com.polak.githubrepofinder.dtos.GitHubBranch;
import com.polak.githubrepofinder.dtos.GitHubRepository;
import com.polak.githubrepofinder.exceptions.UserNotFoundException;
import com.polak.githubrepofinder.services.interfaces.GitHubApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GitHubApiServiceImpl implements GitHubApiService {
    private final WebClient githubClient;
    @Override
    public Flux<GitHubRepository> getNonForkRepositories(String username) {
        return
                githubClient.get()
                            .uri("/users/{username}/repos", username)
                            .retrieve()
                            .onStatus(HttpStatus.NOT_FOUND::equals, response -> Mono.just(new UserNotFoundException(String.format("User with username: {%s} not found.", username))))
                            .bodyToFlux(GitHubRepository.class)
                            .filter(repo -> !repo.fork());
    }

    @Override
    public Flux<GitHubBranch> getRepositoryBranches(String username, String repoName) {
        return
                githubClient.get()
                            .uri("/repos/{username}/{repoName}/branches", username, repoName)
                            .retrieve()
                            .bodyToFlux(GitHubBranch.class);
    }
}
