package com.dev.rohith.portfolio.service;

import com.dev.rohith.portfolio.dto.GithubUser;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProfileService {

    private final GithubService githubService;

    public ProfileService(GithubService githubService) {
        this.githubService = githubService;
    }

    public Map<String, Object> getProfile() {
        GithubUser github = githubService.fetchGithubProfile("RohithKarnati");

        Map<String, Object> response = new HashMap<>();

        response.put("name", "Rohith Kumar");
        response.put("role", "Backend Engineer");
        response.put("focus", "Performance & Scalability");

        response.put("github", github);
        response.put("generatedAt", Instant.now());

        return response;
    }

    public String handleCommand(String command) {

        switch (command.toLowerCase()) {

            case "help":
                return "Available: help, whoami, skills, github, metrics, exit";
            case "whoami":
                return "Rohith Kumar - Backend Engineer";
            case "skills":
                return "Java | Spring Boot | MSSQL | MongoDB | Concurrency";
            case "github":
                GithubUser github = githubService.fetchGithubProfile("RohithKarnati");
                return """
                        GitHub Profile:
                        Repos: %d
                        Followers: %d
                        Profile: %s
                        """.formatted(
                        github.getPublic_repos(),
                        github.getFollowers(),
                        github.getHtml_url()
                );
            case "metrics":
                long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
                return "JVM Uptime (ms): " + uptime;
            case "exit":
                return "Exiting developer mode...";
            case "":
                return "";
            default:
                return "Unknown command. Type 'help'";
        }
    }
}