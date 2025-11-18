package ru.skillfactory.monitoring.controller;

import org.springframework.web.bind.annotation.*;
import ru.skillfactory.monitoring.metrics.CustomMetrics;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/metrics")
public class MetricsDemoController {

    private final CustomMetrics customMetrics;

    public MetricsDemoController(CustomMetrics customMetrics) {
        this.customMetrics = customMetrics;
    }

    @PostMapping("/sessions/increment")
    public Map<String, Object> incrementSessions() {
        customMetrics.incrementSessions();
        return createResponse("Session incremented",
                Map.of("activeSessions", customMetrics.getActiveSessions()));
    }

    @PostMapping("/sessions/decrement")
    public Map<String, Object> decrementSessions() {
        customMetrics.decrementSessions();
        return createResponse("Session decremented",
                Map.of("activeSessions", customMetrics.getActiveSessions()));
    }

    @PostMapping("/queue/increment")
    public Map<String, Object> incrementQueue() {
        customMetrics.incrementQueue();
        return createResponse("Queue item added",
                Map.of("queueSize", customMetrics.getQueueSize()));
    }

    @PostMapping("/queue/decrement")
    public Map<String, Object> decrementQueue() {
        customMetrics.decrementQueue();
        return createResponse("Queue item processed",
                Map.of("queueSize", customMetrics.getQueueSize()));
    }

    @PostMapping("/transactions")
    public Map<String, Object> recordTransaction() {
        customMetrics.recordBusinessTransaction();
        return createResponse("Business transaction recorded", Map.of());
    }

    @PostMapping("/memory/{percent}")
    public Map<String, Object> setMemoryUsage(@PathVariable int percent) {
        if (percent < 0 || percent > 100) {
            return createErrorResponse("Memory usage must be between 0 and 100 percent");
        }
        customMetrics.setMemoryUsage(percent);
        return createResponse("Memory usage set",
                Map.of("memoryUsage", customMetrics.getMemoryUsage()));
    }

    @PostMapping("/processing/{millis}")
    public Map<String, Object> simulateProcessing(@PathVariable long millis) {
        customMetrics.recordProcessingTime(millis);
        return createResponse("Processing time recorded",
                Map.of("processingTimeMs", millis));
    }

    @GetMapping("/status")
    public Map<String, Object> getMetricsStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("activeSessions", customMetrics.getActiveSessions());
        status.put("memoryUsage", customMetrics.getMemoryUsage());
        status.put("queueSize", customMetrics.getQueueSize());
        status.put("timestamp", System.currentTimeMillis());
        return status;
    }

    private Map<String, Object> createResponse(String message, Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    private Map<String, Object> createErrorResponse(String error) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", error);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}