package com.dev.rohith.portfolio.developer.command.impl;

import com.dev.rohith.portfolio.developer.command.CommandContext;
import com.dev.rohith.portfolio.developer.command.CommandOutput;
import com.dev.rohith.portfolio.developer.command.TerminalCommand;
import com.dev.rohith.portfolio.developer.service.TerminalContentService;
import org.springframework.stereotype.Component;

@Component
public class AboutCommand implements TerminalCommand {

    private final TerminalContentService content;

    public AboutCommand(TerminalContentService content) {
        this.content = content;
    }

    @Override
    public String name() {
        return "about";
    }

    @Override
    public String description() {
        return "About me";
    }

    @Override
    public CommandOutput execute(CommandContext context) {
        return CommandOutput.ok(content.renderAbout(), context.currentPath());
    }
}
