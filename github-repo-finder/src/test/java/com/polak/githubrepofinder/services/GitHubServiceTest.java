package com.polak.githubrepofinder.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.polak.githubrepofinder.dtos.GitHubRepository;
import com.polak.githubrepofinder.dtos.GitHubRepositoryOwner;
import com.polak.githubrepofinder.services.implementations.GitHubApiServiceImpl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

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
    void GitHubApiService_GetRepositoryBranches() {

    }
}
