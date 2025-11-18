package ru.skillfactory.monitoring.controller;

import org.springframework.web.bind.annotation.*;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/data")
public class DataController {

    private final Counter dataRequestCounter;
    private final Random random = new Random();

    public DataController(MeterRegistry registry) {
        this.dataRequestCounter = Counter.builder("data.requests")
                .description("Number of data requests")
                .register(registry);
    }

    @GetMapping("/process")
    public String processData() {
        dataRequestCounter.increment();
        simulateHeavyProcessing();
        return "Data processed successfully";
    }

    @GetMapping("/memory-intensive")
    public String memoryIntensiveOperation() {
        // Имитация операции, потребляющей память
        List<byte[]> memoryList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            memoryList.add(new byte[1024 * 1024]); // 1MB каждый
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return "Memory intensive operation completed";
    }

    private void simulateHeavyProcessing() {
        try {
            Thread.sleep(50 + random.nextInt(150));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}