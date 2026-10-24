package com.pulse.integration.github;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
class GitHubSmokeRunner implements CommandLineRunner {

    private final GitHubClient client;

    GitHubSmokeRunner(GitHubClient client) {
        this.client = client;
    }

    @Override
    public void run(String... args) {
        List<GitHubRepoDto> repos = client.fetchRepos("spring-projects", 1);
        System.out.println(">>> Fetched " + repos.size() + " repos. First: "
                + (repos.isEmpty() ? "none" : repos.get(0)));
    }
}
