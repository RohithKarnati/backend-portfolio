package com.dev.rohith.portfolio.developer.command.impl;

import com.dev.rohith.portfolio.developer.command.CommandContext;
import com.dev.rohith.portfolio.developer.command.CommandOutput;
import com.dev.rohith.portfolio.developer.command.TerminalCommand;
import org.springframework.stereotype.Component;

@Component
public class PwdCommand implements TerminalCommand {

    @Override
    public String name() {
        return "pwd";
    }

    @Override
    public String description() {
        return "Show the current virtual directory";
    }

    @Override
    public CommandOutput execute(CommandContext context) {
        return CommandOutput.ok(context.currentPath(), context.currentPath());
    }
}
