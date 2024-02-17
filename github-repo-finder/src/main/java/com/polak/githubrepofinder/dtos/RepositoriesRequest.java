package com.polak.githubrepofinder.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoriesRequest {
    @NotNull(message = "username is null")
    @NotBlank(message = "username is blank or empty")
    private String username;
}
