package com.polak.githubrepofinder.controllers;

import com.polak.githubrepofinder.dtos.RepositoriesRequest;
import com.polak.githubrepofinder.dtos.RepositoriesResponse;
import com.polak.githubrepofinder.services.interfaces.GitHubApiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/v1/github")
@RequiredArgsConstructor
public class GitHubController {
    private final GitHubApiService gitHubApiService;

    @PostMapping(value = "/repos", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public RepositoriesResponse getUsersRepositories(@RequestBody @Valid RepositoriesRequest request) {
        return gitHubApiService.getUserRepositories(request.getUsername());
    }
}
