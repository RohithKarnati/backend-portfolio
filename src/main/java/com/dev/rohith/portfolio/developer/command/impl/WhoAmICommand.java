package com.dev.rohith.portfolio.developer.command.impl;

import com.dev.rohith.portfolio.developer.command.CommandContext;
import com.dev.rohith.portfolio.developer.command.CommandOutput;
import com.dev.rohith.portfolio.developer.command.TerminalCommand;
import com.dev.rohith.portfolio.service.ProfileService;
import org.springframework.stereotype.Component;

@Component
public class WhoAmICommand implements TerminalCommand {

    private final ProfileService profileService;

    public WhoAmICommand(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public String name() {
        return "whoami";
    }

    @Override
    public String description() {
        return "Display professional identity";
    }

    @Override
    public CommandOutput execute(CommandContext context) {
        String output = """
                %s

                %s
                Java | Spring Boot | Microservices | DevOps

                Problem-solving oriented backend engineer interested in
                building reliable, scalable systems.""".formatted(profileService.getName(), profileService.getRole());
        return CommandOutput.ok(output, context.currentPath());
    }
}
