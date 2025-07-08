package org.example.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Retry analyzer for handling flaky tests
 * Implements configurable retry logic with exponential backoff
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    
    private static final Logger logger = LoggerFactory.getLogger(RetryAnalyzer.class);
    private static final Properties config = loadConfig();
    
    private static final int MAX_RETRY_COUNT = Integer.parseInt(
        config.getProperty("retry.count", "2")
    );
    
    private static final long RETRY_INTERVAL = Long.parseLong(
        config.getProperty("retry.interval", "1000")
    );
    
    private int retryCount = 0;
    
    private static Properties loadConfig() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("src/main/resources/config.properties"));
        } catch (IOException e) {
            logger.warn("Could not load config.properties, using default values", e);
        }
        return props;
    }
    
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            logger.warn("Retrying test: {} (Attempt {}/{})", 
                       result.getName(), retryCount, MAX_RETRY_COUNT);
            
            // Exponential backoff
            long waitTime = RETRY_INTERVAL * (long) Math.pow(2, retryCount - 1);
            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Retry wait interrupted", e);
            }
            
            return true;
        }
        
        logger.error("Test failed after {} retries: {}", MAX_RETRY_COUNT, result.getName());
        return false;
    }
    
    /**
     * Reset retry count for a new test
     */
    public void resetRetryCount() {
        retryCount = 0;
    }
    
    /**
     * Get current retry count
     */
    public int getRetryCount() {
        return retryCount;
    }
    
    /**
     * Check if test should be retried based on exception type
     */
    public boolean shouldRetry(Throwable throwable) {
        if (throwable == null) {
            return false;
        }
        
        String exceptionName = throwable.getClass().getSimpleName();
        
        // Retry for common flaky test exceptions
        return switch (exceptionName) {
            case "TimeoutException", 
                 "NoSuchElementException", 
                 "StaleElementReferenceException",
                 "ElementClickInterceptedException",
                 "ElementNotInteractableException" -> true;
            default -> false;
        };
    }
} 