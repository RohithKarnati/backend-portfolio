package com.dev.rohith.portfolio.developer.command.impl;

import com.dev.rohith.portfolio.developer.command.CommandContext;
import com.dev.rohith.portfolio.developer.command.CommandOutput;
import com.dev.rohith.portfolio.developer.command.TerminalCommand;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.stereotype.Component;

/**
 * Phase 1: application identity + Actuator's existing health status only.
 * No uptime, request counts, cache ratios, CPU or memory — those are a later phase.
 */
@Component
public class MetricsCommand implements TerminalCommand {

    private final HealthEndpoint healthEndpoint;

    public MetricsCommand(HealthEndpoint healthEndpoint) {
        this.healthEndpoint = healthEndpoint;
    }

    @Override
    public String name() {
        return "metrics";
    }

    @Override
    public String description() {
        return "Portfolio application metrics";
    }

    @Override
    public CommandOutput execute(CommandContext context) {
        String status;
        try {
            status = healthEndpoint.health().getStatus().getCode();
        } catch (Exception e) {
            status = "UNKNOWN";
        }

        String output = """
                PORTFOLIO METRICS
                =================

                Application: Personal Backend Portfolio
                Status: %s
                Runtime: Spring Boot""".formatted(status);

        return CommandOutput.ok(output, context.currentPath());
    }
}
