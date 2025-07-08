package org.example.visual;

import com.applitools.eyes.Eyes;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Base class for visual testing with Applitools
 * Provides common functionality for visual regression testing
 */
public class VisualTestBase {
    
    private static final Logger logger = LoggerFactory.getLogger(VisualTestBase.class);
    private static final Properties config = loadConfig();
    
    protected static final String APPLITOOLS_API_KEY = config.getProperty("applitools.api.key", "");
    protected static final String APPLITOOLS_SERVER_URL = config.getProperty("applitools.server.url", "https://eyesapi.applitools.com");
    protected static final String APPLITOOLS_APP_NAME = config.getProperty("applitools.app.name", "E-commerce App");
    protected static final String APPLITOOLS_BATCH_NAME = config.getProperty("applitools.batch.name", "E-commerce Tests");
    
    private Eyes eyes;
    private WebDriver driver;
    
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
     * Initialize Applitools Eyes
     */
    protected void initializeEyes(WebDriver driver) {
        this.driver = driver;
        this.eyes = new Eyes();
        
        // Configure Applitools
        eyes.setApiKey(APPLITOOLS_API_KEY);
        eyes.setServerUrl(APPLITOOLS_SERVER_URL);
        eyes.setBatch(APPLITOOLS_BATCH_NAME);
        
        logger.info("Applitools Eyes initialized");
    }
    
    /**
     * Open Eyes for visual testing
     */
    protected void openEyes(String testName, String viewportSize) {
        if (eyes == null) {
            throw new IllegalStateException("Eyes not initialized. Call initializeEyes() first.");
        }
        
        // Parse viewport size (e.g., "1920x1080")
        String[] dimensions = viewportSize.split("x");
        int width = Integer.parseInt(dimensions[0]);
        int height = Integer.parseInt(dimensions[1]);
        
        eyes.open(driver, APPLITOOLS_APP_NAME, testName, new RectangleSize(width, height));
        logger.info("Eyes opened for test: {} with viewport: {}", testName, viewportSize);
    }
    
    /**
     * Take full page screenshot
     */
    protected void takeFullPageScreenshot(String tag) {
        if (eyes == null) {
            throw new IllegalStateException("Eyes not opened. Call openEyes() first.");
        }
        
        eyes.check(tag, Target.window().fully());
        logger.info("Full page screenshot taken with tag: {}", tag);
    }
    
    /**
     * Take screenshot of specific element
     */
    protected void takeElementScreenshot(String tag, org.openqa.selenium.WebElement element) {
        if (eyes == null) {
            throw new IllegalStateException("Eyes not opened. Call openEyes() first.");
        }
        
        eyes.check(tag, Target.region(element));
        logger.info("Element screenshot taken with tag: {}", tag);
    }
    
    /**
     * Take screenshot of specific region
     */
    protected void takeRegionScreenshot(String tag, int x, int y, int width, int height) {
        if (eyes == null) {
            throw new IllegalStateException("Eyes not opened. Call openEyes() first.");
        }
        
        eyes.check(tag, Target.region(x, y, width, height));
        logger.info("Region screenshot taken with tag: {}", tag);
    }
    
    /**
     * Take screenshot ignoring specific regions
     */
    protected void takeScreenshotIgnoringRegions(String tag, org.openqa.selenium.WebElement... elementsToIgnore) {
        if (eyes == null) {
            throw new IllegalStateException("Eyes not opened. Call openEyes() first.");
        }
        
        Target target = Target.window().fully();
        for (org.openqa.selenium.WebElement element : elementsToIgnore) {
            target = target.ignore(element);
        }
        
        eyes.check(tag, target);
        logger.info("Screenshot taken with ignored regions, tag: {}", tag);
    }
    
    /**
     * Take screenshot with layout comparison
     */
    protected void takeLayoutScreenshot(String tag) {
        if (eyes == null) {
            throw new IllegalStateException("Eyes not opened. Call openEyes() first.");
        }
        
        eyes.check(tag, Target.window().layout());
        logger.info("Layout screenshot taken with tag: {}", tag);
    }
    
    /**
     * Take screenshot with content comparison
     */
    protected void takeContentScreenshot(String tag) {
        if (eyes == null) {
            throw new IllegalStateException("Eyes not opened. Call openEyes() first.");
        }
        
        eyes.check(tag, Target.window().content());
        logger.info("Content screenshot taken with tag: {}", tag);
    }
    
    /**
     * Take screenshot with strict comparison
     */
    protected void takeStrictScreenshot(String tag) {
        if (eyes == null) {
            throw new IllegalStateException("Eyes not opened. Call openEyes() first.");
        }
        
        eyes.check(tag, Target.window().strict());
        logger.info("Strict screenshot taken with tag: {}", tag);
    }
    
    /**
     * Close Eyes and get test results
     */
    protected boolean closeEyes() {
        if (eyes == null) {
            throw new IllegalStateException("Eyes not opened. Call openEyes() first.");
        }
        
        try {
            boolean result = eyes.close();
            logger.info("Eyes closed. Test result: {}", result ? "PASSED" : "FAILED");
            return result;
        } catch (Exception e) {
            logger.error("Error closing Eyes", e);
            eyes.abortIfNotClosed();
            throw new RuntimeException("Error closing Eyes", e);
        }
    }
    
    /**
     * Abort Eyes if not closed
     */
    protected void abortEyes() {
        if (eyes != null) {
            eyes.abortIfNotClosed();
            logger.info("Eyes aborted");
        }
    }
    
    /**
     * Set match level for visual comparison
     */
    protected void setMatchLevel(com.applitools.eyes.MatchLevel matchLevel) {
        if (eyes == null) {
            throw new IllegalStateException("Eyes not initialized. Call initializeEyes() first.");
        }
        
        eyes.setMatchLevel(matchLevel);
        logger.info("Match level set to: {}", matchLevel);
    }
    
    /**
     * Set baseline branch
     */
    protected void setBaselineBranch(String branchName) {
        if (eyes == null) {
            throw new IllegalStateException("Eyes not initialized. Call initializeEyes() first.");
        }
        
        eyes.setBaselineBranchName(branchName);
        logger.info("Baseline branch set to: {}", branchName);
    }
    
    /**
     * Set parent branch
     */
    protected void setParentBranch(String branchName) {
        if (eyes == null) {
            throw new IllegalStateException("Eyes not initialized. Call initializeEyes() first.");
        }
        
        eyes.setParentBranchName(branchName);
        logger.info("Parent branch set to: {}", branchName);
    }
    
    /**
     * Enable visual grid for cross-browser testing
     */
    protected void enableVisualGrid() {
        if (eyes == null) {
            throw new IllegalStateException("Eyes not initialized. Call initializeEyes() first.");
        }
        
        eyes.setForceFullPageScreenshot(true);
        logger.info("Visual grid enabled");
    }
    
    /**
     * Get Eyes instance
     */
    protected Eyes getEyes() {
        return eyes;
    }
    
    /**
     * Get WebDriver instance
     */
    protected WebDriver getDriver() {
        return driver;
    }
} 