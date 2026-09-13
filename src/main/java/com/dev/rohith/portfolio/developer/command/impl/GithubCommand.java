package com.dev.rohith.portfolio.developer.command.impl;

import com.dev.rohith.portfolio.developer.command.CommandContext;
import com.dev.rohith.portfolio.developer.command.CommandOutput;
import com.dev.rohith.portfolio.developer.command.TerminalCommand;
import com.dev.rohith.portfolio.developer.service.TerminalContentService;
import org.springframework.stereotype.Component;

/** Delegates to the existing GithubService (via ProfileService / TerminalContentService) — no second GitHub client. */
@Component
public class GithubCommand implements TerminalCommand {

    private final TerminalContentService content;

    public GithubCommand(TerminalContentService content) {
        this.content = content;
    }

    @Override
    public String name() {
        return "github";
    }

    @Override
    public String description() {
        return "GitHub profile";
    }

    @Override
    public CommandOutput execute(CommandContext context) {
        String rendered = content.renderGithubProfile();
        if (rendered == null) {
            return CommandOutput.error("github: unable to retrieve GitHub information", context.currentPath());
        }
        return CommandOutput.ok(rendered, context.currentPath());
    }
}
