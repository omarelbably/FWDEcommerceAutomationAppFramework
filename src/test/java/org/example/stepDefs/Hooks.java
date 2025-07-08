package org.example.stepDefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.example.core.TestBase;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Hooks extends TestBase {
    
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);
    private static WebDriver staticDriver;
    
    @Before
    public void setUp(Scenario scenario) {
        logger.info("Starting scenario: {}", scenario.getName());
        super.setUp(null); // Use default browser from config
        staticDriver = driver;
    }
    
    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.error("Scenario failed: {}", scenario.getName());
            takeScreenshot(scenario);
        } else {
            logger.info("Scenario passed: {}", scenario.getName());
        }
        
        super.tearDown();
        staticDriver = null;
    }
    
    private void takeScreenshot(Scenario scenario) {
        try {
            if (driver instanceof TakesScreenshot) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot");
                
                // Save screenshot to file system
                saveScreenshotToFile(screenshot, scenario.getName());
            }
        } catch (Exception e) {
            logger.error("Failed to take screenshot", e);
        }
    }
    
    private void saveScreenshotToFile(byte[] screenshot, String scenarioName) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = scenarioName.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + ".png";
            
            Path screenshotDir = Paths.get(config.getProperty("screenshot.path", "target/screenshots/"));
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
    
    // Static accessor for backward compatibility with existing page objects
    public static WebDriver getStaticDriver() {
        return staticDriver;
    }
}
