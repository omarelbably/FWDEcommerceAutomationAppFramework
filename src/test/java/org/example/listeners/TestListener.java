package org.example.listeners;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestListener implements ITestListener {
    
    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);
    
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Starting test: {} in class: {}", 
                   result.getName(), result.getTestClass().getName());
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test passed: {} in class: {}", 
                   result.getName(), result.getTestClass().getName());
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Test failed: {} in class: {}", 
                    result.getName(), result.getTestClass().getName());
        
        // Get the WebDriver instance from the test class
        WebDriver driver = getDriverFromTest(result);
        if (driver != null) {
            takeScreenshot(driver, result.getName());
        }
        
        // Log the exception
        if (result.getThrowable() != null) {
            logger.error("Test failure reason: ", result.getThrowable());
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("Test skipped: {} in class: {}", 
                   result.getName(), result.getTestClass().getName());
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.warn("Test failed but within success percentage: {} in class: {}", 
                   result.getName(), result.getTestClass().getName());
    }
    
    @Override
    public void onStart(ITestContext context) {
        logger.info("Starting test suite: {}", context.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        logger.info("Finished test suite: {}", context.getName());
        logger.info("Total tests: {}, Passed: {}, Failed: {}, Skipped: {}", 
                   context.getAllTestMethods().length,
                   context.getPassedTests().size(),
                   context.getFailedTests().size(),
                   context.getSkippedTests().size());
    }
    
    private WebDriver getDriverFromTest(ITestResult result) {
        try {
            // Try to get driver from the test instance
            Object testInstance = result.getInstance();
            if (testInstance != null) {
                // Look for common driver field names
                java.lang.reflect.Field[] fields = testInstance.getClass().getDeclaredFields();
                for (java.lang.reflect.Field field : fields) {
                    if (field.getType().equals(org.openqa.selenium.WebDriver.class)) {
                        field.setAccessible(true);
                        return (WebDriver) field.get(testInstance);
                    }
                }
                
                // Try to get driver from parent class
                java.lang.reflect.Field[] parentFields = testInstance.getClass().getSuperclass().getDeclaredFields();
                for (java.lang.reflect.Field field : parentFields) {
                    if (field.getType().equals(org.openqa.selenium.WebDriver.class)) {
                        field.setAccessible(true);
                        return (WebDriver) field.get(testInstance);
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Could not get WebDriver instance from test", e);
        }
        return null;
    }
    
    private void takeScreenshot(WebDriver driver, String testName) {
        try {
            if (driver instanceof TakesScreenshot) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                saveScreenshotToFile(screenshot, testName);
                attachScreenshotToAllure(screenshot, testName);
            }
        } catch (Exception e) {
            logger.error("Failed to take screenshot for test: {}", testName, e);
        }
    }
    
    private void saveScreenshotToFile(byte[] screenshot, String testName) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + ".png";
            
            Path screenshotDir = Paths.get("target/screenshots/");
            if (!Files.exists(screenshotDir)) {
                Files.createDirectories(screenshotDir);
            }
            
            Path screenshotPath = screenshotDir.resolve(fileName);
            Files.write(screenshotPath, screenshot);
            logger.info("Screenshot saved: {}", screenshotPath);
        } catch (IOException e) {
            logger.error("Failed to save screenshot", e);
        }
    }
    
    @Attachment(value = "Screenshot", type = "image/png")
    private byte[] attachScreenshotToAllure(byte[] screenshot, String testName) {
        return screenshot;
    }
} 