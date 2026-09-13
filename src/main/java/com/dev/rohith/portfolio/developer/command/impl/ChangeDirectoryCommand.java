package com.dev.rohith.portfolio.developer.command.impl;

import com.dev.rohith.portfolio.developer.command.CommandContext;
import com.dev.rohith.portfolio.developer.command.CommandOutput;
import com.dev.rohith.portfolio.developer.command.TerminalCommand;
import com.dev.rohith.portfolio.developer.filesystem.VirtualFileSystem;
import org.springframework.stereotype.Component;

@Component
public class ChangeDirectoryCommand implements TerminalCommand {

    private final VirtualFileSystem fileSystem;

    public ChangeDirectoryCommand(VirtualFileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public String name() {
        return "cd";
    }

    @Override
    public String description() {
        return "Change the current virtual directory";
    }

    @Override
    public CommandOutput execute(CommandContext context) {
        if (!context.hasArguments()) {
            return CommandOutput.error("cd: missing operand", context.currentPath());
        }

        String target = context.firstArgument();
        String normalized = fileSystem.normalize(context.currentPath(), target);

        if (fileSystem.resolveDirectory(normalized).isEmpty()) {
            return CommandOutput.error("cd: " + target + ": No such directory", context.currentPath());
        }

        return CommandOutput.ok("", normalized);
    }
}
