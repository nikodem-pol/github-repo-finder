package com.polak.githubrepofinder.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RepositoriesRequest(
        @NotNull(message = "username is null")
        @NotBlank(message = "username is blank or empty")
        String username)
{}
