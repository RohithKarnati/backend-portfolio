package com.dev.rohith.portfolio.developer.dto;

/** Response body for POST /api/developer/terminal. */
public record TerminalResponse(String type, String command, String output, String currentPath) {

    public static TerminalResponse text(String command, String output, String currentPath) {
        return new TerminalResponse("TEXT", command, output, currentPath);
    }

    public static TerminalResponse error(String command, String output, String currentPath) {
        return new TerminalResponse("ERROR", command, output, currentPath);
    }
}
