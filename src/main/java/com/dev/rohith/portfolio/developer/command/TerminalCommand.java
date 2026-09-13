package com.dev.rohith.portfolio.developer.command;

/**
 * A single Developer Mode terminal command. Implementations are Spring beans, auto-discovered
 * by {@link CommandRegistry} — adding a new command never requires touching an existing one.
 */
public interface TerminalCommand {

    String name();

    String description();

    CommandOutput execute(CommandContext context);
}
