package com.polak.githubrepofinder.services.implementations;

import com.polak.githubrepofinder.dtos.BranchDto;
import com.polak.githubrepofinder.dtos.RepositoriesResponse;
import com.polak.githubrepofinder.dtos.RepositoryDto;
import com.polak.githubrepofinder.exceptions.CannotGetRepoBranchesException;
import com.polak.githubrepofinder.exceptions.ConnectionFailedException;
import com.polak.githubrepofinder.exceptions.UserNotFoundException;
import com.polak.githubrepofinder.services.interfaces.GitHubApiService;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class GitHubApiServiceImpl implements GitHubApiService {
    private final GitHub github;

    public GitHubApiServiceImpl() throws ConnectionFailedException {
        try {
            this.github = GitHub.connectAnonymously();
        } catch (IOException e) {
            throw new ConnectionFailedException("Failed while connecting to GitHub.");
        }
    }

    @Override
    public RepositoriesResponse getUserRepositories(String username) throws UserNotFoundException {
        try {
            GHUser user = github.getUser(username);
            return RepositoriesResponse.builder().repositories(getNotForkRepositories(user)).build();
        } catch (IOException e) {
            throw new UserNotFoundException(String.format("User with name: {%s}, not found", username));
        }
    }

    private List<RepositoryDto> getNotForkRepositories(GHUser user) throws IOException {
        List<GHRepository> notForkRepos = user.getRepositories()
                                              .values()
                                              .stream()
                                              .filter(repo -> !repo.isFork())
                                              .toList();

        return notForkRepos.stream().map(this::convertGHRepositoryToRepositoryDto).toList();
    }


    private RepositoryDto convertGHRepositoryToRepositoryDto(GHRepository repository) {
        List<BranchDto> branches;
        branches = getRepositoryBranchesDtos(repository);
        return RepositoryDto.builder()
                            .name(repository.getName())
                            .ownerLogin(repository.getOwnerName())
                            .branches(branches)
                            .build();
    }

    private List<BranchDto> getRepositoryBranchesDtos(GHRepository repository) {
        try {
            return repository.getBranches()
                             .values()
                             .stream()
                             .map(branch -> BranchDto.builder()
                                                     .name(branch.getName())
                                                     .lastCommitSha(branch.getSHA1())
                                                     .build())
                             .toList();
        } catch (IOException e) {
            throw new CannotGetRepoBranchesException(e.getMessage());
        }
    }
}
