package ru.skillfactory.monitoring.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringDemoController {

    @GetMapping("/hello")
    public String incrementSessions() {
        return "Hello World!!!";
    }

    @PostMapping("/checkAlert")
    public int checkAlert(@RequestBody Map<String, Object> requestBody) {
        int count = (Integer) requestBody.get("count");
        String text = (String) requestBody.get("text");
        int size = 0;

        if (count <= 0 || text == null || text.isEmpty()) {
            return size;
        }

        HashMap<Integer, String> map = new HashMap<>();
        String value = text;

        while (count > 0) {
            value += value;
            map.put(count, value);
            count--;
            size = map.size();
        }
        return size;
    }
}