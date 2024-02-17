package com.polak.githubrepofinder.services.interfaces;

import com.polak.githubrepofinder.dtos.RepositoriesResponse;

import java.util.List;

public interface GitHubApiService {
    RepositoriesResponse getUserRepositories(String username);
}
