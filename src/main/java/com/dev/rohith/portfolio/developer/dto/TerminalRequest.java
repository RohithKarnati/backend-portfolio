package com.dev.rohith.portfolio.developer.dto;

/** Request body for POST /api/developer/terminal. The frontend owns and sends the current virtual path. */
public record TerminalRequest(String command, String path) {
}
