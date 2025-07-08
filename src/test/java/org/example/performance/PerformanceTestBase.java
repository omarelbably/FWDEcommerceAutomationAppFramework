package org.example.performance;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class for performance testing
 * Provides metrics collection and performance analysis capabilities
 */
public class PerformanceTestBase {
    
    private static final Logger logger = LoggerFactory.getLogger(PerformanceTestBase.class);
    private static final Properties config = loadConfig();
    
    protected static final MetricRegistry metrics = new MetricRegistry();
    protected static final Map<String, Timer> timers = new ConcurrentHashMap<>();
    protected static final Map<String, Counter> counters = new ConcurrentHashMap<>();
    protected static final Map<String, Histogram> histograms = new ConcurrentHashMap<>();
    
    private static final long PERFORMANCE_THRESHOLD = Long.parseLong(
        config.getProperty("performance.threshold.ms", "5000")
    );
    
    private static Properties loadConfig() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("src/main/resources/config.properties"));
        } catch (IOException e) {
            logger.warn("Could not load config.properties, using default values", e);
        }
        return props;
    }
    
    /**
     * Start timing an operation
     */
    protected Timer.Context startTimer(String operationName) {
        Timer timer = timers.computeIfAbsent(operationName, k -> metrics.timer(operationName));
        logger.info("Starting timer for operation: {}", operationName);
        return timer.time();
    }
    
    /**
     * Stop timing an operation
     */
    protected long stopTimer(Timer.Context context, String operationName) {
        long duration = context.stop();
        logger.info("Operation {} completed in {} ms", operationName, duration);
        
        // Check performance threshold
        if (duration > PERFORMANCE_THRESHOLD) {
            logger.warn("Operation {} exceeded performance threshold: {} ms > {} ms", 
                       operationName, duration, PERFORMANCE_THRESHOLD);
        }
        
        return duration;
    }
    
    /**
     * Time an operation with automatic start/stop
     */
    protected long timeOperation(String operationName, Runnable operation) {
        Timer.Context context = startTimer(operationName);
        try {
            operation.run();
            return stopTimer(context, operationName);
        } catch (Exception e) {
            context.stop();
            logger.error("Operation {} failed", operationName, e);
            throw e;
        }
    }
    
    /**
     * Increment a counter
     */
    protected void incrementCounter(String counterName) {
        Counter counter = counters.computeIfAbsent(counterName, k -> metrics.counter(counterName));
        counter.inc();
        logger.debug("Counter {} incremented", counterName);
    }
    
    /**
     * Increment a counter by a specific amount
     */
    protected void incrementCounter(String counterName, long amount) {
        Counter counter = counters.computeIfAbsent(counterName, k -> metrics.counter(counterName));
        counter.inc(amount);
        logger.debug("Counter {} incremented by {}", counterName, amount);
    }
    
    /**
     * Record a value in a histogram
     */
    protected void recordHistogram(String histogramName, long value) {
        Histogram histogram = histograms.computeIfAbsent(histogramName, k -> metrics.histogram(histogramName));
        histogram.update(value);
        logger.debug("Histogram {} updated with value: {}", histogramName, value);
    }
    
    /**
     * Measure page load time
     */
    protected long measurePageLoadTime(WebDriver driver, String pageName) {
        return timeOperation("page_load_" + pageName, () -> {
            // Navigate to page and wait for load
            driver.getCurrentUrl();
        });
    }
    
    /**
     * Measure element interaction time
     */
    protected long measureElementInteraction(String elementName, Runnable interaction) {
        return timeOperation("element_interaction_" + elementName, interaction);
    }
    
    /**
     * Measure API response time
     */
    protected long measureApiResponseTime(String endpoint, Runnable apiCall) {
        return timeOperation("api_response_" + endpoint, apiCall);
    }
    
    /**
     * Get performance statistics for an operation
     */
    protected PerformanceStats getPerformanceStats(String operationName) {
        Timer timer = timers.get(operationName);
        if (timer == null) {
            return null;
        }
        
        return new PerformanceStats(
            timer.getCount(),
            timer.getSnapshot().getMean(),
            timer.getSnapshot().getMedian(),
            timer.getSnapshot().get95thPercentile(),
            timer.getSnapshot().get99thPercentile(),
            timer.getSnapshot().getMin(),
            timer.getSnapshot().getMax()
        );
    }
    
    /**
     * Analyze performance trends
     */
    protected PerformanceAnalysis analyzePerformance(String operationName, int sampleSize) {
        Timer timer = timers.get(operationName);
        if (timer == null) {
            return null;
        }
        
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (long value : timer.getSnapshot().getValues()) {
            stats.addValue(value);
        }
        
        return new PerformanceAnalysis(
            operationName,
            stats.getMean(),
            stats.getStandardDeviation(),
            stats.getPercentile(95),
            stats.getPercentile(99),
            stats.getMin(),
            stats.getMax(),
            stats.getN()
        );
    }
    
    /**
     * Check if performance meets SLA requirements
     */
    protected boolean meetsSLA(String operationName, long slaThreshold) {
        PerformanceStats stats = getPerformanceStats(operationName);
        if (stats == null) {
            return false;
        }
        
        boolean meetsSLA = stats.getMean() <= slaThreshold;
        if (!meetsSLA) {
            logger.warn("Operation {} does not meet SLA: {} ms > {} ms", 
                       operationName, stats.getMean(), slaThreshold);
        }
        
        return meetsSLA;
    }
    
    /**
     * Generate performance report
     */
    protected String generatePerformanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Performance Report ===\n");
        
        for (Map.Entry<String, Timer> entry : timers.entrySet()) {
            String operationName = entry.getKey();
            Timer timer = entry.getValue();
            PerformanceStats stats = getPerformanceStats(operationName);
            
            if (stats != null) {
                report.append(String.format("Operation: %s\n", operationName));
                report.append(String.format("  Count: %d\n", stats.getCount()));
                report.append(String.format("  Mean: %.2f ms\n", stats.getMean()));
                report.append(String.format("  Median: %.2f ms\n", stats.getMedian()));
                report.append(String.format("  95th Percentile: %.2f ms\n", stats.getPercentile95()));
                report.append(String.format("  99th Percentile: %.2f ms\n", stats.getPercentile99()));
                report.append(String.format("  Min: %.2f ms\n", stats.getMin()));
                report.append(String.format("  Max: %.2f ms\n", stats.getMax()));
                report.append("\n");
            }
        }
        
        return report.toString();
    }
    
    /**
     * Performance statistics data class
     */
    public static class PerformanceStats {
        private final long count;
        private final double mean;
        private final double median;
        private final double percentile95;
        private final double percentile99;
        private final double min;
        private final double max;
        
        public PerformanceStats(long count, double mean, double median, 
                              double percentile95, double percentile99, 
                              double min, double max) {
            this.count = count;
            this.mean = mean;
            this.median = median;
            this.percentile95 = percentile95;
            this.percentile99 = percentile99;
            this.min = min;
            this.max = max;
        }
        
        // Getters
        public long getCount() { return count; }
        public double getMean() { return mean; }
        public double getMedian() { return median; }
        public double getPercentile95() { return percentile95; }
        public double getPercentile99() { return percentile99; }
        public double getMin() { return min; }
        public double getMax() { return max; }
    }
    
    /**
     * Performance analysis data class
     */
    public static class PerformanceAnalysis {
        private final String operationName;
        private final double mean;
        private final double standardDeviation;
        private final double percentile95;
        private final double percentile99;
        private final double min;
        private final double max;
        private final long sampleSize;
        
        public PerformanceAnalysis(String operationName, double mean, double standardDeviation,
                                 double percentile95, double percentile99, double min, double max, long sampleSize) {
            this.operationName = operationName;
            this.mean = mean;
            this.standardDeviation = standardDeviation;
            this.percentile95 = percentile95;
            this.percentile99 = percentile99;
            this.min = min;
            this.max = max;
            this.sampleSize = sampleSize;
        }
        
        // Getters
        public String getOperationName() { return operationName; }
        public double getMean() { return mean; }
        public double getStandardDeviation() { return standardDeviation; }
        public double getPercentile95() { return percentile95; }
        public double getPercentile99() { return percentile99; }
        public double getMin() { return min; }
        public double getMax() { return max; }
        public long getSampleSize() { return sampleSize; }
    }
} 