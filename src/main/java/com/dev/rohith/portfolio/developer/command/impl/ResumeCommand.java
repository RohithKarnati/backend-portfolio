package com.dev.rohith.portfolio.developer.command.impl;

import com.dev.rohith.portfolio.developer.command.CommandContext;
import com.dev.rohith.portfolio.developer.command.CommandOutput;
import com.dev.rohith.portfolio.developer.command.TerminalCommand;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class ResumeCommand implements TerminalCommand {

    private static final String RESUME_PATH = "static/resume.pdf";

    @Override
    public String name() {
        return "resume";
    }

    @Override
    public String description() {
        return "Resume";
    }

    @Override
    public CommandOutput execute(CommandContext context) {
        boolean available = new ClassPathResource(RESUME_PATH).exists();

        String output = available
                ? "RESUME\n======\n\nResume available.\nDownload: /resume.pdf"
                : "RESUME\n======\n\nResume not uploaded yet — check back soon.";

        return CommandOutput.ok(output, context.currentPath());
    }
}
