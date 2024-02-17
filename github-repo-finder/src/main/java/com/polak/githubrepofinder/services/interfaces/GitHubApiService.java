package com.polak.githubrepofinder.services.interfaces;

import com.polak.githubrepofinder.dtos.RepositoriesResponse;
import com.polak.githubrepofinder.exceptions.UserNotFoundException;

import java.io.IOException;
import java.util.List;

public interface GitHubApiService {
    RepositoriesResponse getUserRepositories(String username) throws UserNotFoundException;
}
