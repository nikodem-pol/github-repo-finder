package com.polak.githubrepofinder.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polak.githubrepofinder.dtos.GitHubBranch;
import com.polak.githubrepofinder.dtos.GitHubCommit;
import com.polak.githubrepofinder.dtos.GitHubRepository;
import com.polak.githubrepofinder.dtos.GitHubRepositoryOwner;
import com.polak.githubrepofinder.services.implementations.GitHubApiServiceImpl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GitHubServiceTest {

    public static MockWebServer mockWebServer;
    private final ObjectMapper mapper = new ObjectMapper();
    private GitHubApiServiceImpl mockGitHubApiService;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void initialize() {
        WebClient client = WebClient.create(String.format("http://localhost:%s", mockWebServer.getPort()));
        mockGitHubApiService = new GitHubApiServiceImpl(client);
    }

    @Test
    void GitHubApiService_GetNonForkRepositories() throws JsonProcessingException, InterruptedException {
        List<GitHubRepository> repositories = gitHubRepositoriesTestData();
        List<GitHubRepository> nonForkRepos = repositories.stream().filter(repo -> !repo.fork()).toList();

        mockWebServer.enqueue(new MockResponse().setBody(mapper.writeValueAsString(repositories))
                                                .addHeader("Content-Type", "application/json"));

        Flux<GitHubRepository> repositoryFlux = mockGitHubApiService.getNonForkRepositories("user");

        StepVerifier.create(repositoryFlux)
                    .expectNext(nonForkRepos.toArray(GitHubRepository[]::new))
                    .expectComplete()
                    .verify();

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/users/user/repos", recordedRequest.getPath());
    }

    private List<GitHubRepository> gitHubRepositoriesTestData() {
        return List.of(
                new GitHubRepository("Repo 1", true, new GitHubRepositoryOwner("user")),
                new GitHubRepository("Repo 2", false, new GitHubRepositoryOwner("user")),
                new GitHubRepository("Repo 3", false, new GitHubRepositoryOwner("user")),
                new GitHubRepository("Repo 4", true, new GitHubRepositoryOwner("user"))
        );
    }

    @Test
    void GitHubApiService_GetRepositoryBranches() throws JsonProcessingException, InterruptedException {
        List<GitHubBranch> branches = gitHubBranchesTestData();

        mockWebServer.enqueue(new MockResponse().setBody(mapper.writeValueAsString(branches))
                                                .addHeader("Content-Type", "application/json"));

        Flux<GitHubBranch> branchFlux = mockGitHubApiService.getRepositoryBranches("user", "Repo1");

        StepVerifier.create(branchFlux)
                    .expectNext(branches.toArray(GitHubBranch[]::new))
                    .expectComplete()
                    .verify();

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/repos/user/Repo1/branches", recordedRequest.getPath());
    }

    private List<GitHubBranch> gitHubBranchesTestData() {
        return List.of(
                new GitHubBranch("Branch 1", new GitHubCommit("ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb")),
                new GitHubBranch("Branch 2", new GitHubCommit("3e23e8160039594a33894f6564e1b1348bbd7a0088d42c4acb73eeaed59c009d")),
                new GitHubBranch("Branch 3", new GitHubCommit("2e7d2c03a9507ae265ecf5b5356885a53393a2029d241394997265a1a25aefc6")),
                new GitHubBranch("Branch 4", new GitHubCommit("18ac3e7343f016890c510e93f935261169d9e3f565436429830faf0934f4f8e4"))
        );
    }
}
