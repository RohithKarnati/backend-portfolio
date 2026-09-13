package com.dev.rohith.portfolio.developer.command;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Looks up {@link TerminalCommand} beans by name. Spring injects every registered command bean
 * here automatically, so new commands are picked up without modifying this class.
 */
@Component
public class CommandRegistry {

    private final Map<String, TerminalCommand> commandsByName;

    public CommandRegistry(List<TerminalCommand> commands) {
        Map<String, TerminalCommand> byName = new LinkedHashMap<>();
        for (TerminalCommand command : commands) {
            byName.put(command.name().toLowerCase(), command);
        }
        this.commandsByName = byName;
    }

    public Optional<TerminalCommand> find(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(commandsByName.get(name.toLowerCase()));
    }

    public Collection<TerminalCommand> all() {
        return commandsByName.values();
    }
}
