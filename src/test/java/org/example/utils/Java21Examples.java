package org.example.utils;

import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Examples of Java 21 features that can be used in the test automation framework
 */
public class Java21Examples {
    
    private static final Logger logger = LoggerFactory.getLogger(Java21Examples.class);
    
    // Record for immutable test data
    public record TestUser(String firstName, String lastName, String email, String password) {
        public TestUser {
            // Validation in compact constructor
            if (firstName == null || firstName.trim().isEmpty()) {
                throw new IllegalArgumentException("First name cannot be null or empty");
            }
            if (email == null || !email.contains("@")) {
                throw new IllegalArgumentException("Invalid email format");
            }
        }
    }
    
    // Record for test results
    public record TestResult(String testName, String status, long duration, String message) {}
    
    // Sealed interface for test data types
    public sealed interface TestData permits UserTestData, ProductTestData, SearchTestData {}
    // Refactored to records for deconstruction patterns
    public record UserTestData(String firstName, String lastName, String email) implements TestData {
        public String getType() { return "USER"; }
    }
    public record ProductTestData(String name, String category, double price) implements TestData {
        public String getType() { return "PRODUCT"; }
    }
    public record SearchTestData(String term, String category) implements TestData {
        public String getType() { return "SEARCH"; }
    }
    
    /**
     * Switch expression example for browser selection
     */
    public String getBrowserDisplayName(String browserType) {
        return switch (browserType.toLowerCase()) {
            case "chrome" -> "Google Chrome";
            case "firefox" -> "Mozilla Firefox";
            case "edge" -> "Microsoft Edge";
            case "safari" -> "Apple Safari";
            default -> "Unknown Browser";
        };
    }
    
    /**
     * Pattern matching with switch for element type detection
     */
    public String getElementType(WebElement element) {
        return switch (element.getTagName()) {
            case "input" -> switch (element.getAttribute("type")) {
                case "text" -> "text input";
                case "password" -> "password input";
                case "email" -> "email input";
                case "submit" -> "submit button";
                case "button" -> "button input";
                default -> "other input";
            };
            case "button" -> "button";
            case "a" -> "link";
            case "select" -> "dropdown";
            case "textarea" -> "text area";
            default -> "unknown element";
        };
    }
    
    /**
     * Pattern matching with instanceof for data processing
     */
    public void processTestData(TestData data) {
        switch (data) {
            case UserTestData(String firstName, String lastName, String email) -> 
                logger.info("Processing user: {} {} ({})", firstName, lastName, email);
            case ProductTestData(String name, String category, double price) -> 
                logger.info("Processing product: {} in category {} at ${}", name, category, price);
            case SearchTestData(String term, String category) -> 
                logger.info("Processing search: '{}' in category {}", term, category);
        }
    }
    
    /**
     * Text blocks for multi-line strings
     */
    public String getExpectedErrorMessage() {
        return """
            The email address you entered is not valid.
            Please enter a valid email address in the format: user@domain.com
            
            Common issues:
            - Missing @ symbol
            - Invalid domain format
            - Special characters not allowed
            """;
    }
    
    /**
     * Virtual threads for parallel test execution
     */
    public void executeTestsInParallel(List<Runnable> tests) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<?>> futures = tests.stream()
                .map(executor::submit)
                .toList();
            
            // Wait for all tests to complete
            for (Future<?> future : futures) {
                future.get();
            }
        } catch (Exception e) {
            logger.error("Test execution failed", e);
            throw new RuntimeException("Test execution failed", e);
        }
    }
    
    /**
     * Enhanced switch with pattern matching and guards
     */
    public String getElementAction(WebElement element) {
        return switch (element) {
            case WebElement e when e.getTagName().equals("button") -> "click";
            case WebElement e when e.getTagName().equals("input") && 
                                  e.getAttribute("type").equals("text") -> "type";
            case WebElement e when e.getTagName().equals("select") -> "select";
            case WebElement e when e.getTagName().equals("a") -> "navigate";
            default -> "interact";
        };
    }
    
    /**
     * Unnamed patterns example
     */
    public void processUserData(Object data) {
        if (data instanceof TestUser(String firstName, String lastName, _, _)) {
            logger.info("Processing user: {} {}", firstName, lastName);
        }
    }
    
    /**
     * Record patterns with nested matching
     */
    public String getTestSummary(List<TestResult> results) {
        long totalDuration = results.stream()
            .mapToLong(TestResult::duration)
            .sum();
        
        long passedCount = results.stream()
            .filter(r -> "PASS".equals(r.status()))
            .count();
        
        return STR."""
            Test Summary:
            Total Tests: \{results.size()}
            Passed: \{passedCount}
            Failed: \{results.size() - passedCount}
            Total Duration: \{totalDuration}ms
            Average Duration: \{totalDuration / results.size()}ms
            """;
    }
    
    /**
     * Factory method using switch expression
     */
    public TestData createTestData(String type, Map<String, Object> data) {
        return switch (type.toUpperCase()) {
            case "USER" -> new UserTestData(
                (String) data.get("firstName"),
                (String) data.get("lastName"),
                (String) data.get("email")
            );
            case "PRODUCT" -> new ProductTestData(
                (String) data.get("name"),
                (String) data.get("category"),
                (Double) data.get("price")
            );
            case "SEARCH" -> new SearchTestData(
                (String) data.get("term"),
                (String) data.get("category")
            );
            default -> throw new IllegalArgumentException("Unknown test data type: " + type);
        };
    }
    
    /**
     * Enhanced error handling with pattern matching
     */
    public void handleTestException(Exception e) {
        switch (e) {
            case IllegalArgumentException iae -> 
                logger.warn("Invalid argument: {}", iae.getMessage());
            case RuntimeException re when re.getCause() instanceof InterruptedException -> 
                logger.error("Test interrupted: {}", re.getMessage());
            case RuntimeException re -> 
                logger.error("Runtime error: {}", re.getMessage());
            default -> 
                logger.error("Unexpected error: {}", e.getMessage());
        }
    }
} 