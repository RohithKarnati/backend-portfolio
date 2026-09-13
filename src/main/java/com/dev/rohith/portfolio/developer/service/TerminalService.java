package com.dev.rohith.portfolio.developer.service;

import com.dev.rohith.portfolio.developer.command.CommandContext;
import com.dev.rohith.portfolio.developer.command.CommandOutput;
import com.dev.rohith.portfolio.developer.command.CommandRegistry;
import com.dev.rohith.portfolio.developer.command.TerminalCommand;
import com.dev.rohith.portfolio.developer.dto.TerminalRequest;
import com.dev.rohith.portfolio.developer.dto.TerminalResponse;
import com.dev.rohith.portfolio.developer.filesystem.VirtualFileSystem;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Parses a raw terminal line, dispatches it to the matching {@link TerminalCommand} via the
 * {@link CommandRegistry}, and assembles the response. Stateless: the caller's virtual path is
 * taken from the request and the (possibly new) path is handed back — nothing is held here
 * between requests, so this scales horizontally with no session affinity required.
 */
@Service
public class TerminalService {

    private final CommandRegistry registry;

    public TerminalService(CommandRegistry registry) {
        this.registry = registry;
    }

    public TerminalResponse run(TerminalRequest request) {
        String path = isBlank(request.path()) ? VirtualFileSystem.HOME : request.path();
        String line = request.command() == null ? "" : request.command().trim();

        if (line.isEmpty()) {
            return TerminalResponse.text("", "", path);
        }

        List<String> tokens = Arrays.stream(line.split("\\s+")).toList();
        String commandName = tokens.get(0);
        List<String> arguments = tokens.subList(1, tokens.size());

        Optional<TerminalCommand> command = registry.find(commandName);
        if (command.isEmpty()) {
            return TerminalResponse.error(line, commandName + ": command not found", path);
        }

        CommandOutput result = command.get().execute(new CommandContext(path, arguments));
        return result.error()
                ? TerminalResponse.error(line, result.output(), result.currentPath())
                : TerminalResponse.text(line, result.output(), result.currentPath());
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
