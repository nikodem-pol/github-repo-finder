package com.polak.githubrepofinder.dtos;

public record GitHubRepository(String name, boolean fork, GitHubRepositoryOwner owner) {
}
