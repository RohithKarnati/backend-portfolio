package com.dev.rohith.portfolio.developer.command.impl;

import com.dev.rohith.portfolio.developer.command.CommandContext;
import com.dev.rohith.portfolio.developer.command.CommandOutput;
import com.dev.rohith.portfolio.developer.command.TerminalCommand;
import com.dev.rohith.portfolio.developer.filesystem.VirtualFile;
import com.dev.rohith.portfolio.developer.filesystem.VirtualFileSystem;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CatCommand implements TerminalCommand {

    private final VirtualFileSystem fileSystem;

    public CatCommand(VirtualFileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public String name() {
        return "cat";
    }

    @Override
    public String description() {
        return "Read a file";
    }

    @Override
    public CommandOutput execute(CommandContext context) {
        String argument = context.firstArgument();
        if (argument == null) {
            return CommandOutput.error("cat: missing operand", context.currentPath());
        }

        int lastSlash = argument.lastIndexOf('/');
        String fileName = lastSlash == -1 ? argument : argument.substring(lastSlash + 1);
        String dirArgument = lastSlash == -1 ? "" : argument.substring(0, lastSlash);

        String parentPath = dirArgument.isEmpty()
                ? context.currentPath()
                : fileSystem.normalize(context.currentPath(), dirArgument);

        Optional<VirtualFile> file = fileSystem.resolveFile(parentPath, fileName);
        if (file.isEmpty()) {
            return CommandOutput.error("cat: " + argument + ": No such file or directory", context.currentPath());
        }

        return CommandOutput.ok(file.get().getContent(), context.currentPath());
    }
}
