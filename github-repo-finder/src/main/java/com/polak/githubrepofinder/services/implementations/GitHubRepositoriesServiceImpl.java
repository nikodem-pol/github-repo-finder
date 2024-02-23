package com.polak.githubrepofinder.services.implementations;

import com.polak.githubrepofinder.dtos.BranchDto;
import com.polak.githubrepofinder.dtos.RepositoryResponse;
import com.polak.githubrepofinder.services.interfaces.GitHubApiService;
import com.polak.githubrepofinder.services.interfaces.GitHubRepositoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitHubRepositoriesServiceImpl implements GitHubRepositoriesService {
    private final GitHubApiService gitHubApi;

    @Override
    public Flux<RepositoryResponse> getUserRepositories(String username) {
        return gitHubApi.getNonForkRepositories(username).publishOn(Schedulers.boundedElastic()).map(repo -> {
            List<BranchDto> branches = gitHubApi.getRepositoryBranches(repo.owner().login(), repo.name())
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

}
