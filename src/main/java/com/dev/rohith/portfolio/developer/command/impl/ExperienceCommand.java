package com.dev.rohith.portfolio.developer.command.impl;

import com.dev.rohith.portfolio.developer.command.CommandContext;
import com.dev.rohith.portfolio.developer.command.CommandOutput;
import com.dev.rohith.portfolio.developer.command.TerminalCommand;
import com.dev.rohith.portfolio.developer.service.TerminalContentService;
import org.springframework.stereotype.Component;

@Component
public class ExperienceCommand implements TerminalCommand {

    private final TerminalContentService content;

    public ExperienceCommand(TerminalContentService content) {
        this.content = content;
    }

    @Override
    public String name() {
        return "experience";
    }

    @Override
    public String description() {
        return "Professional experience";
    }

    @Override
    public CommandOutput execute(CommandContext context) {
        return CommandOutput.ok(content.renderAllExperience(), context.currentPath());
    }
}
