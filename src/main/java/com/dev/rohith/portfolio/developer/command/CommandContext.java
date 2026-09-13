package com.dev.rohith.portfolio.developer.command;

import java.util.List;

/** Everything a {@link TerminalCommand} needs to execute: the virtual cwd and its arguments (command name excluded). */
public record CommandContext(String currentPath, List<String> arguments) {

    public String firstArgument() {
        return arguments.isEmpty() ? null : arguments.get(0);
    }

    public boolean hasArguments() {
        return !arguments.isEmpty();
    }
}
