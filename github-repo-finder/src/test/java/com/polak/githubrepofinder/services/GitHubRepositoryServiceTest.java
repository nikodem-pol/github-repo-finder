package com.polak.githubrepofinder.services;

import com.polak.githubrepofinder.dtos.*;
import com.polak.githubrepofinder.services.implementations.GitHubApiServiceImpl;
import com.polak.githubrepofinder.services.implementations.GitHubRepositoriesServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GitHubRepositoryServiceTest {
    @Mock
    private GitHubApiServiceImpl mockGitHubApiService;
    @InjectMocks
    private GitHubRepositoriesServiceImpl mockGitHubRepositoryService;

    @Test
    public void GitHubRepositoryService_GetUserRepositories() {
        when(mockGitHubApiService.getNonForkRepositories(Mockito.anyString())).thenReturn(gitHubRepositoryTestData());
        when(mockGitHubApiService.getRepositoryBranches("user", "Repo 1")).thenReturn(gitHubBranchTestData());

        List<RepositoryResponse> expectedRepos = expectedRepositoryResponses();
        Flux<RepositoryResponse> repositories = mockGitHubRepositoryService.getUserRepositories("user");

        StepVerifier.create(repositories)
                    .expectNext(expectedRepos.toArray(RepositoryResponse[]::new))
                    .expectComplete()
                    .verify();
    }

    private Flux<GitHubRepository> gitHubRepositoryTestData() {
        return Flux.just(
            new GitHubRepository("Repo 1", false, new GitHubRepositoryOwner("user"))
        );
    }

    private Flux<GitHubBranch> gitHubBranchTestData() {
        return Flux.just(
                new GitHubBranch("Branch 1", new GitHubCommit("18ac3e7343f016890c510e93f935261169d9e3f565436429830faf0934f4f8e4")),
                new GitHubBranch("Branch 2", new GitHubCommit("3e23e8160039594a33894f6564e1b1348bbd7a0088d42c4acb73eeaed59c009d"))
        );
    }

    private List<RepositoryResponse> expectedRepositoryResponses() {
        return List.of(
          new RepositoryResponse("Repo 1", "user",
                  List.of(
                          new BranchDto("Branch 1", "18ac3e7343f016890c510e93f935261169d9e3f565436429830faf0934f4f8e4"),
                          new BranchDto("Branch 2", "3e23e8160039594a33894f6564e1b1348bbd7a0088d42c4acb73eeaed59c009d")
                  )
          )
        );
    }
}
