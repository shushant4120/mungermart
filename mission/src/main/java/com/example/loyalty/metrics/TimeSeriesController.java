package com.example.loyalty.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TimeSeriesController {

    private final Counter requestCounter;
    private final MeterRegistry meterRegistry;

    public TimeSeriesController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.requestCounter = Counter.builder("api_requests_total")
                .description("Total number of API requests")
                .register(meterRegistry);
    }

    @GetMapping(value = "/metric-sample", produces = "text/plain")
    public String getSampleMetric() {
        requestCounter.increment(); // count every call

        double cpuUsage = Math.random() * 100;
        double memoryUsage = Math.random() * 100;
        double requestsTotal = requestCounter.count();

        // Return in Prometheus plaintext format
        StringBuilder sb = new StringBuilder();
        sb.append("# HELP cpu_usage CPU usage percentage\n");
        sb.append("# TYPE cpu_usage gauge\n");
        sb.append("cpu_usage ").append(cpuUsage).append("\n\n");

        sb.append("# HELP memory_usage Memory usage percentage\n");
        sb.append("# TYPE memory_usage gauge\n");
        sb.append("memory_usage ").append(memoryUsage).append("\n\n");

        sb.append("# HELP api_requests_total Total number of API requests\n");
        sb.append("# TYPE api_requests_total counter\n");
        sb.append("api_requests_total ").append(requestsTotal).append("\n");

        return sb.toString();
    }
}
