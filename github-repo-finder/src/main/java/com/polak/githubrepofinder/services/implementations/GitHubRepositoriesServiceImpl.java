package com.polak.githubrepofinder.services.implementations;

import com.polak.githubrepofinder.dtos.BranchDto;
import com.polak.githubrepofinder.dtos.RepositoryResponse;
import com.polak.githubrepofinder.services.interfaces.GitHubApiService;
import com.polak.githubrepofinder.services.interfaces.GitHubRepositoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class GitHubRepositoriesServiceImpl implements GitHubRepositoriesService {
    private final GitHubApiService gitHubApi;

    @Override
    public Flux<RepositoryResponse> getUserRepositories(String username) {
        return gitHubApi.getNonForkRepositories(username)
                        .publishOn(Schedulers.boundedElastic())
                        .flatMap(repo -> gitHubApi.getRepositoryBranches(repo.owner().login(), repo.name())
                                                  .map(branch -> BranchDto.builder()
                                                                          .name(branch.name())
                                                                          .lastCommitSha(branch.commit().sha())
                                                                          .build())
                                                  .collectList()
                                                  .map(branches -> RepositoryResponse.builder()
                                                                                     .name(repo.name())
                                                                                     .ownerLogin(repo.owner().login())
                                                                                     .branches(branches)
                                                                                     .build()));
    }
}
