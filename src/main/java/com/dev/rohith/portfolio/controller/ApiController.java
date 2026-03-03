package com.dev.rohith.portfolio.controller;

import com.dev.rohith.portfolio.service.ProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ProfileService profileService;

    public ApiController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public Map<String, Object> getProfile() {
        return profileService.getProfile();
    }

    @GetMapping("/dev")
    public String handleCommand(@RequestParam String command) {
        return profileService.handleCommand(command);
    }

}