package com.dev.rohith.portfolio.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubUser {

    private String login;
    private int public_repos;
    private int followers;
    private int following;
    private String html_url;
    private String created_at;
}