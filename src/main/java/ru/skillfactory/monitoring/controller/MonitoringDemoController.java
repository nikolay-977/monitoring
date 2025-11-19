package ru.skillfactory.monitoring.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringDemoController {

    @GetMapping("/hello")
    public String incrementSessions() {
        return "Hello World!!!";
    }
}