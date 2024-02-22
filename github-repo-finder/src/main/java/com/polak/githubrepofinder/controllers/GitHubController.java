package com.polak.githubrepofinder.controllers;

import com.polak.githubrepofinder.dtos.GitHubRepository;
import com.polak.githubrepofinder.dtos.RepositoriesRequest;
import com.polak.githubrepofinder.dtos.RepositoriesResponse;
import com.polak.githubrepofinder.dtos.RepositoryDto;
import com.polak.githubrepofinder.exceptions.UserNotFoundException;
import com.polak.githubrepofinder.services.interfaces.GitHubApiService;
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
    private final GitHubApiService gitHubApiService;

    @PostMapping(value = "/repos", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Flux<RepositoryDto> getUsersRepositories(@RequestBody @Valid RepositoriesRequest request) throws UserNotFoundException {
        return gitHubApiService.getUserRepositories(request.getUsername());
    }
}
