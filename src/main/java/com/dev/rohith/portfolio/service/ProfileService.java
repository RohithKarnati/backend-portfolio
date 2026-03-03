package com.dev.rohith.portfolio.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProfileService {

    public Map<String, Object> getProfile() {

        Map<String, Object> profile = new HashMap<>();
        profile.put("name", "Rohith Kumar");
        profile.put("role", "Backend Engineer");
        profile.put("stack", "Java, Spring Boot, MSSQL");
        profile.put("focus", "Performance & Scalability");

        return profile;
    }

    public String handleCommand(String command) {

        return switch (command.toLowerCase()) {
            case "help" ->
                    "Available commands: help, whoami, skills, exit";
            case "whoami" ->
                    "Rohith Kumar - Backend Engineer";
            case "skills" ->
                    "Java | Spring Boot | MSSQL | MongoDB | Concurrency";
            case "exit" ->
                    "Exiting developer mode...";
            default ->
                    "Unknown command. Type 'help'";
        };
    }

    public String getGHApiDetaila(String username) {

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.github.com/users/"+username;

        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);


        return forEntity.getBody();
    }
}