package ru.skillfactory.monitoring.metrics;

import io.micrometer.core.instrument.*;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class CustomMetrics {

    private final MeterRegistry meterRegistry;
    private final AtomicInteger activeSessions = new AtomicInteger(0);
    private final AtomicLong memoryUsage = new AtomicLong(30L);
    private final AtomicInteger queueSize = new AtomicInteger(0);
    private final Counter businessTransactionCounter;
    private final Timer customProcessingTimer;
    private final Random random = new Random();

    // Gauge references
    private Gauge sessionsGauge;
    private Gauge memoryGauge;
    private Gauge queueGauge;

    public CustomMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.businessTransactionCounter = Counter.builder("app.business.transactions")
                .description("Business transactions counter")
                .register(meterRegistry);
        this.customProcessingTimer = Timer.builder("app.processing.time")
                .description("Custom processing time")
                .register(meterRegistry);
    }

    @PostConstruct
    public void initialize() {
        // Создаем gauge'ы с использованием более простого синтаксиса
        this.sessionsGauge = Gauge
                .builder("app.sessions.active", activeSessions, AtomicInteger::get)
                .description("Number of active sessions")
                .register(meterRegistry);

        this.memoryGauge = Gauge
                .builder("app.memory.usage.percent", memoryUsage, AtomicLong::get)
                .description("Simulated memory usage percentage")
                .register(meterRegistry);

        this.queueGauge = Gauge
                .builder("app.custom.queue.size", queueSize, AtomicInteger::get)
                .description("Simulated queue size")
                .register(meterRegistry);

        // Запускаем симуляцию метрик
        simulateMetrics();
    }

    public void incrementSessions() {
        activeSessions.incrementAndGet();
    }

    public void decrementSessions() {
        if (activeSessions.get() > 0) {
            activeSessions.decrementAndGet();
        }
    }

    public void incrementQueue() {
        queueSize.incrementAndGet();
    }

    public void decrementQueue() {
        if (queueSize.get() > 0) {
            queueSize.decrementAndGet();
        }
    }

    public void recordBusinessTransaction() {
        businessTransactionCounter.increment();
    }

    public void recordProcessingTime(long timeMillis) {
        customProcessingTimer.record(java.time.Duration.ofMillis(timeMillis));
    }

    public void setMemoryUsage(long usagePercent) {
        if (usagePercent >= 0 && usagePercent <= 100) {
            memoryUsage.set(usagePercent);
        }
    }

    public int getActiveSessions() {
        return activeSessions.get();
    }

    public int getQueueSize() {
        return queueSize.get();
    }

    public long getMemoryUsage() {
        return memoryUsage.get();
    }

    private void simulateMetrics() {
        Thread simulatorThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Обновляем метрики каждые 5 секунд
                    updateSimulatedMetrics();
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        simulatorThread.setDaemon(true);
        simulatorThread.start();
    }

    private void updateSimulatedMetrics() {
        // Симуляция использования памяти
        long newMemoryUsage = 30 + random.nextInt(50);
        memoryUsage.set(newMemoryUsage);

        // Симуляция активных сессий
        int sessionChange = random.nextInt(3) - 1;
        if (sessionChange > 0) {
            activeSessions.incrementAndGet();
        } else if (sessionChange < 0 && activeSessions.get() > 0) {
            activeSessions.decrementAndGet();
        }

        // Симуляция размера очереди
        int queueChange = random.nextInt(3) - 1;
        if (queueChange > 0) {
            queueSize.incrementAndGet();
        } else if (queueChange < 0 && queueSize.get() > 0) {
            queueSize.decrementAndGet();
        }

        // Случайные бизнес-транзакции
        if (random.nextDouble() < 0.3) {
            businessTransactionCounter.increment();
        }
    }
}