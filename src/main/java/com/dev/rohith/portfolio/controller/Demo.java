package com.dev.rohith.portfolio.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Demo {

    @GetMapping(value = "/fuckNithish")
    public String getDemo() {
        System.out.println("This is a bloody logger");
        return "Fuck You Nithish";
    }
}
