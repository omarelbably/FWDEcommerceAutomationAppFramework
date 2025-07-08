package org.example.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.Map;

public class TestBase {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Properties config;
    
    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(String browser) {
        loadConfig();
        driver = createDriver(browser != null ? browser : config.getProperty("browser", "chrome"));
        wait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(config.getProperty("explicit.wait", "20"))));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(config.getProperty("implicit.wait", "10"))));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Integer.parseInt(config.getProperty("page.load.timeout", "30"))));
        driver.get(config.getProperty("base.url"));
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    private void loadConfig() {
        config = new Properties();
        try {
            config.load(new FileInputStream("src/main/resources/config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }
    
    private WebDriver createDriver(String browserType) {
        return switch (browserType.toLowerCase()) {
            case "chrome" -> createChromeDriver();
            case "firefox" -> createFirefoxDriver();
            case "edge" -> createEdgeDriver();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserType);
        };
    }
    
    private WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        if (Boolean.parseBoolean(config.getProperty("headless", "false"))) {
            options.addArguments("--headless");
        }
        
        options.addArguments("--window-size=" + config.getProperty("window.size", "1920x1080"));
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        
        return new ChromeDriver(options);
    }
    
    private WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        if (Boolean.parseBoolean(config.getProperty("headless", "false"))) {
            options.addArguments("--headless");
        }
        
        return new FirefoxDriver(options);
    }
    
    private WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        if (Boolean.parseBoolean(config.getProperty("headless", "false"))) {
            options.addArguments("--headless");
        }
        
        return new EdgeDriver(options);
    }
    
    public WebDriver getDriver() {
        return driver;
    }
    
    public WebDriverWait getWait() {
        return wait;
    }
    
    public Properties getConfig() {
        return config;
    }
} 