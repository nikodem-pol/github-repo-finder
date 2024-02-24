package com.polak.githubrepofinder.dtos;

import lombok.Builder;

@Builder
public record BranchDto(String name, String lastCommitSha) {
}