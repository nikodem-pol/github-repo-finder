package com.polak.githubrepofinder.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryDto {
    String name;
    String ownerLogin;
    List<BranchDto> branches;
}
