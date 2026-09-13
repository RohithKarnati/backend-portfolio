package com.dev.rohith.portfolio.controller;

import com.dev.rohith.portfolio.service.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final ProfileService profileService;

    public ViewController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAllAttributes(profileService.getProfile());
        return "dashboard";
    }

    @GetMapping("/developer")
    public String developer() {
        return "developer";
    }
}
