package com.polak.githubrepofinder.services.implementations;

import com.polak.githubrepofinder.dtos.RepositoriesResponse;
import com.polak.githubrepofinder.services.interfaces.GitHubApiService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GitHubApiServiceImpl implements GitHubApiService {
    @Override
    public RepositoriesResponse getUserRepositories(String username) {
        return null;
    }
}
