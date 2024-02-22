package com.polak.githubrepofinder.services.implementations;

import com.polak.githubrepofinder.dtos.BranchDto;
import com.polak.githubrepofinder.dtos.GitHubBranch;
import com.polak.githubrepofinder.dtos.GitHubRepository;
import com.polak.githubrepofinder.dtos.RepositoryDto;
import com.polak.githubrepofinder.services.interfaces.GitHubApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitHubApiServiceImpl implements GitHubApiService {
    private final WebClient githubClient;

    @Override
    public Flux<RepositoryDto> getUserRepositories(String username) {
        return getNonForkRepositories(username).publishOn(Schedulers.boundedElastic()).map(repo -> {
            List<BranchDto> branches = getRepositoryBranches(repo.owner().login(), repo.name())
                                                            .map(branch -> BranchDto.builder()
                                                                                    .name(branch.name())
                                                                                    .lastCommitSha(branch.commit().sha())
                                                                                    .build())
                                                            .collectList()
                                                            .block();
            return
                    RepositoryDto.builder()
                                .name(repo.name())
                                .ownerLogin(repo.owner().login())
                                .branches(branches)
                                .build();
        });
    }

    private Flux<GitHubRepository> getNonForkRepositories(String username) {
        return
                githubClient.get()
                           .uri("/users/{username}/repos", username)
                           .retrieve()
                           .bodyToFlux(GitHubRepository.class)
                           .filter(repo -> !repo.fork());
    }

    private Flux<GitHubBranch> getRepositoryBranches(String username, String repoName) {
        return
                githubClient.get()
                           .uri("/repos/{username}/{repoName}/branches", username, repoName)
                           .retrieve()
                           .bodyToFlux(GitHubBranch.class);
    }

}
