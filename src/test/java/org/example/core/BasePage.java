package org.example.core;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Logger logger;
    protected Actions actions;
    protected String baseUrl = "https://demo.nopcommerce.com";
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }
    
    // Common wait methods
    protected void waitForElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    protected void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    protected void waitForElementToBePresent(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    protected void waitForTextToBePresent(WebElement element, String text) {
        wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }
    
    // Common interaction methods
    public void clickElement(WebElement element) {
        try {
            waitForElementToBeClickable(element);
            element.click();
            logger.info("Clicked element: {}", element);
        } catch (Exception e) {
            logger.error("Failed to click element: {}", element, e);
            throw e;
        }
    }
    
    public void sendKeysToElement(WebElement element, String text) {
        try {
            waitForElementToBeVisible(element);
            element.clear();
            element.sendKeys(text);
            logger.info("Entered text '{}' into element: {}", text, element);
        } catch (Exception e) {
            logger.error("Failed to send keys to element: {}", element, e);
            throw e;
        }
    }
    
    public String getElementText(WebElement element) {
        try {
            waitForElementToBeVisible(element);
            String text = element.getText();
            logger.info("Got text '{}' from element: {}", text, element);
            return text;
        } catch (Exception e) {
            logger.error("Failed to get text from element: {}", element, e);
            throw e;
        }
    }
    
    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    protected boolean isElementEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    
    // JavaScript executor methods
    protected void scrollToElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(500); // Small delay to ensure scroll completes
        } catch (Exception e) {
            logger.error("Failed to scroll to element: {}", element, e);
        }
    }
    
    protected void clickElementWithJS(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            logger.info("Clicked element with JavaScript: {}", element);
        } catch (Exception e) {
            logger.error("Failed to click element with JavaScript: {}", element, e);
            throw e;
        }
    }
    
    // Navigation methods
    protected void navigateTo(String url) {
        try {
            driver.navigate().to(url);
            logger.info("Navigated to: {}", url);
        } catch (Exception e) {
            logger.error("Failed to navigate to: {}", url, e);
            throw e;
        }
    }
    
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    protected String getPageTitle() {
        return driver.getTitle();
    }
    
    // Screenshot method
    protected void takeScreenshot(String name) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
            // You can implement file saving logic here if needed
            logger.info("Screenshot taken: {}", name);
        } catch (Exception e) {
            logger.error("Failed to take screenshot: {}", name, e);
        }
    }
    
    // Wait for page load
    protected void waitForPageLoad() {
        try {
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
        } catch (Exception e) {
            logger.error("Failed to wait for page load", e);
        }
    }
    
    // Get elements by locator
    protected WebElement findElement(By locator) {
        return driver.findElement(locator);
    }
    
    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }
    
    // Refresh page
    protected void refreshPage() {
        driver.navigate().refresh();
        waitForPageLoad();
    }

    // Add getBaseUrl method
    public String getBaseUrl() {
        return baseUrl;
    }
    // Add waitForElementVisible method (alias for waitForElementToBeVisible)
    public void waitForElementVisible(WebElement element) {
        waitForElementToBeVisible(element);
    }
} 