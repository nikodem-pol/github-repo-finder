package com.polak.githubrepofinder.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record RepositoryResponse(String name, String ownerLogin, List<BranchDto> branches) {
}
