package com.dev.rohith.portfolio.developer.command;

/** What a {@link TerminalCommand} produces: text (or error text) plus the resulting virtual path. */
public record CommandOutput(String output, String currentPath, boolean error) {

    public static CommandOutput ok(String output, String currentPath) {
        return new CommandOutput(output, currentPath, false);
    }

    public static CommandOutput error(String output, String currentPath) {
        return new CommandOutput(output, currentPath, true);
    }
}
