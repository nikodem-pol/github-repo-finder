package com.polak.githubrepofinder.controllers;

import com.polak.githubrepofinder.dtos.RepositoriesRequest;
import com.polak.githubrepofinder.dtos.RepositoryResponse;
import com.polak.githubrepofinder.services.interfaces.GitHubRepositoriesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/v1/github")
@RequiredArgsConstructor
public class GitHubController {
    private final GitHubRepositoriesService gitHubApiService;

    @PostMapping(value = "/repos", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Flux<RepositoryResponse> getUsersRepositories(@RequestBody @Valid Mono<RepositoriesRequest> requestMono) {
        return requestMono.flatMapMany(request -> gitHubApiService.getUserRepositories(request.username()));
    }
}
