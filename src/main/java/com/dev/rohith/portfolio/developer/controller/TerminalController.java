package com.dev.rohith.portfolio.developer.controller;

import com.dev.rohith.portfolio.developer.dto.TerminalRequest;
import com.dev.rohith.portfolio.developer.dto.TerminalResponse;
import com.dev.rohith.portfolio.developer.service.TerminalService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/developer")
public class TerminalController {

    private final TerminalService terminalService;

    public TerminalController(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    @PostMapping("/terminal")
    public TerminalResponse execute(@RequestBody TerminalRequest request) {
        return terminalService.run(request);
    }
}
