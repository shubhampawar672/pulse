package com.pulse.integration.github;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GitHubClient {

    private final RestClient restClient;
    private final GitHubProperties props;

    public GitHubClient(RestClient restClient, GitHubProperties props) {
        this.restClient = restClient;
        this.props = props;
    }

    public List<GitHubRepoDto> fetchRepos(String org, int page) {
        GitHubRepoDto[] body = restClient.get()
                .uri("/orgs/{org}/repos?per_page={size}&page={page}&type=all",
                        org, props.pageSize(), page)
                .retrieve()
                .body(GitHubRepoDto[].class);

        return body == null ? List.of() : List.of(body);
    }
}