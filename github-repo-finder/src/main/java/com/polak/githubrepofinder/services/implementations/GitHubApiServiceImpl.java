package com.polak.githubrepofinder.services.implementations;

import com.polak.githubrepofinder.dtos.BranchDto;
import com.polak.githubrepofinder.dtos.GitHubBranch;
import com.polak.githubrepofinder.dtos.GitHubRepository;
import com.polak.githubrepofinder.dtos.RepositoryResponse;
import com.polak.githubrepofinder.exceptions.UserNotFoundException;
import com.polak.githubrepofinder.services.interfaces.GitHubApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitHubApiServiceImpl implements GitHubApiService {
    private final WebClient githubClient;

    @Override
    public Flux<RepositoryResponse> getUserRepositories(String username) {
        return getNonForkRepositories(username).publishOn(Schedulers.boundedElastic()).map(repo -> {
            List<BranchDto> branches = getRepositoryBranches(repo.owner().login(), repo.name())
                                                            .map(branch -> BranchDto.builder()
                                                                                    .name(branch.name())
                                                                                    .lastCommitSha(branch.commit().sha())
                                                                                    .build())
                                                            .collectList()
                                                            .block();
            return
                    RepositoryResponse.builder()
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
                           .onStatus(HttpStatus.NOT_FOUND::equals, response -> Mono.just(new UserNotFoundException(String.format("User with username: {%s} not found.", username))))
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
