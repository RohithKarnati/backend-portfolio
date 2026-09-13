package com.dev.rohith.portfolio.developer.command.impl;

import com.dev.rohith.portfolio.developer.command.CommandContext;
import com.dev.rohith.portfolio.developer.command.CommandOutput;
import com.dev.rohith.portfolio.developer.command.TerminalCommand;
import com.dev.rohith.portfolio.developer.service.TerminalContentService;
import org.springframework.stereotype.Component;

@Component
public class ContactCommand implements TerminalCommand {

    private final TerminalContentService content;

    public ContactCommand(TerminalContentService content) {
        this.content = content;
    }

    @Override
    public String name() {
        return "contact";
    }

    @Override
    public String description() {
        return "Contact information";
    }

    @Override
    public CommandOutput execute(CommandContext context) {
        return CommandOutput.ok(content.renderContact(), context.currentPath());
    }
}
