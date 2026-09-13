package com.dev.rohith.portfolio.developer.command.impl;

import com.dev.rohith.portfolio.developer.command.CommandContext;
import com.dev.rohith.portfolio.developer.command.CommandOutput;
import com.dev.rohith.portfolio.developer.command.TerminalCommand;
import com.dev.rohith.portfolio.developer.filesystem.VirtualDirectory;
import com.dev.rohith.portfolio.developer.filesystem.VirtualFileSystem;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ListCommand implements TerminalCommand {

    private final VirtualFileSystem fileSystem;

    public ListCommand(VirtualFileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public String name() {
        return "ls";
    }

    @Override
    public String description() {
        return "List directory contents";
    }

    @Override
    public CommandOutput execute(CommandContext context) {
        String argument = context.firstArgument();
        String targetPath = argument == null
                ? context.currentPath()
                : fileSystem.normalize(context.currentPath(), argument);

        Optional<VirtualDirectory> directory = fileSystem.resolveDirectory(targetPath);
        if (directory.isEmpty()) {
            return CommandOutput.error("ls: " + argument + ": No such directory", context.currentPath());
        }

        return CommandOutput.ok(String.join("\n", directory.get().listEntries()), context.currentPath());
    }
}
