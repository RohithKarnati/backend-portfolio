package com.dev.rohith.portfolio.developer.command.impl;

import com.dev.rohith.portfolio.developer.command.CommandContext;
import com.dev.rohith.portfolio.developer.command.CommandOutput;
import com.dev.rohith.portfolio.developer.command.TerminalCommand;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements TerminalCommand {

    @Override
    public String name() {
        return "help";
    }

    @Override
    public String description() {
        return "List available commands";
    }

    @Override
    public CommandOutput execute(CommandContext context) {
        String output = """
                Available commands:

                Navigation
                  ls          List directory contents
                  cd          Change directory
                  pwd         Show current directory
                  cat         Read a file

                Profile
                  whoami      Display professional identity
                  about       About me
                  experience  Professional experience
                  skills      Technical skills
                  projects    Projects
                  github      GitHub profile
                  contact     Contact information
                  resume      Resume

                System
                  metrics     Portfolio application metrics

                Terminal
                  history     Command history
                  clear       Clear terminal""";
        return CommandOutput.ok(output, context.currentPath());
    }
}
