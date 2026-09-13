package com.dev.rohith.portfolio.developer.command.impl;

import com.dev.rohith.portfolio.developer.command.CommandContext;
import com.dev.rohith.portfolio.developer.command.CommandOutput;
import com.dev.rohith.portfolio.developer.command.TerminalCommand;
import com.dev.rohith.portfolio.developer.service.TerminalContentService;
import org.springframework.stereotype.Component;

@Component
public class ProjectsCommand implements TerminalCommand {

    private final TerminalContentService content;

    public ProjectsCommand(TerminalContentService content) {
        this.content = content;
    }

    @Override
    public String name() {
        return "projects";
    }

    @Override
    public String description() {
        return "Portfolio projects";
    }

    @Override
    public CommandOutput execute(CommandContext context) {
        return CommandOutput.ok(content.renderAllProjects(), context.currentPath());
    }
}
