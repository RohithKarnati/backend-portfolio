package com.dev.rohith.portfolio.service;

import com.dev.rohith.portfolio.dto.GithubUser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GithubService {

    private final WebClient webClient;

    public GithubService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://api.github.com")
                .build();
    }

    @Cacheable("githubProfile")
    public GithubUser fetchGithubProfile(String username) {

        return webClient.get()
                .uri("/users/{username}", username)
                .retrieve()
                .bodyToMono(GithubUser.class)
                .block();
    }
}