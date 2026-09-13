package com.dev.rohith.portfolio.developer.command.impl;

import com.dev.rohith.portfolio.developer.command.CommandContext;
import com.dev.rohith.portfolio.developer.command.CommandOutput;
import com.dev.rohith.portfolio.developer.command.TerminalCommand;
import com.dev.rohith.portfolio.developer.service.TerminalContentService;
import org.springframework.stereotype.Component;

@Component
public class SkillsCommand implements TerminalCommand {

    private final TerminalContentService content;

    public SkillsCommand(TerminalContentService content) {
        this.content = content;
    }

    @Override
    public String name() {
        return "skills";
    }

    @Override
    public String description() {
        return "Technical skills";
    }

    @Override
    public CommandOutput execute(CommandContext context) {
        return CommandOutput.ok(content.renderAllSkills(), context.currentPath());
    }
}
