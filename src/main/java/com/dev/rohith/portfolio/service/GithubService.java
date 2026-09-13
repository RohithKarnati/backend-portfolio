package com.dev.rohith.portfolio.service;

import com.dev.rohith.portfolio.dto.GithubUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GithubService {

    private static final Logger log = LoggerFactory.getLogger(GithubService.class);

    private final WebClient webClient;

    public GithubService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://api.github.com")
                .build();
    }

    /**
     * Returns the GitHub profile for the given username, or {@code null} if the
     * GitHub API is unreachable or rate-limited. Callers must handle a null result
     * so the dashboard can degrade gracefully instead of failing to render.
     */
    @Cacheable("githubProfile")
    public GithubUser fetchGithubProfile(String username) {

        try {
            return webClient.get()
                    .uri("/users/{username}", username)
                    .retrieve()
                    .bodyToMono(GithubUser.class)
                    .block();
        } catch (Exception e) {
            log.warn("Failed to fetch GitHub profile for '{}': {}", username, e.getMessage());
            return null;
        }
    }
}
