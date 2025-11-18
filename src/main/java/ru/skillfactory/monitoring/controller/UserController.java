package ru.skillfactory.monitoring.controller;

import org.springframework.web.bind.annotation.*;
import io.micrometer.core.instrument.MeterRegistry;
import ru.skillfactory.monitoring.metrics.CustomMetrics;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final CustomMetrics customMetrics;
    private long idCounter = 1;

    public UserController(MeterRegistry registry, CustomMetrics customMetrics) {
        this.customMetrics = customMetrics;
    }

    public static class User {
        public Long id;
        public String name;
        public String email;

        public User() {}

        public User(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        customMetrics.incrementSessions();
        customMetrics.recordBusinessTransaction();

        // Замер времени обработки
        long startTime = System.currentTimeMillis();
        try {
            simulateProcessing();
            return new ArrayList<>(users.values());
        } finally {
            long processingTime = System.currentTimeMillis() - startTime;
            customMetrics.recordProcessingTime(processingTime);
            customMetrics.decrementSessions();
        }
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        customMetrics.incrementSessions();
        customMetrics.recordBusinessTransaction();

        long startTime = System.currentTimeMillis();
        try {
            simulateProcessing();
            return users.get(id);
        } finally {
            long processingTime = System.currentTimeMillis() - startTime;
            customMetrics.recordProcessingTime(processingTime);
            customMetrics.decrementSessions();
        }
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        customMetrics.incrementSessions();
        customMetrics.recordBusinessTransaction();

        long startTime = System.currentTimeMillis();
        try {
            user.id = idCounter++;
            users.put(user.id, user);
            simulateProcessing();
            return user;
        } finally {
            long processingTime = System.currentTimeMillis() - startTime;
            customMetrics.recordProcessingTime(processingTime);
            customMetrics.decrementSessions();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        customMetrics.incrementSessions();
        customMetrics.recordBusinessTransaction();

        long startTime = System.currentTimeMillis();
        try {
            users.remove(id);
            simulateProcessing();
        } finally {
            long processingTime = System.currentTimeMillis() - startTime;
            customMetrics.recordProcessingTime(processingTime);
            customMetrics.decrementSessions();
        }
    }

    private void simulateProcessing() {
        // Имитация обработки для генерации метрик
        try {
            Thread.sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}