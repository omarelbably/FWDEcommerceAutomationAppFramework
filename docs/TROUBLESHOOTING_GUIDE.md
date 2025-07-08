# Troubleshooting Guide - Test Automation Framework

## 🔍 **Common Issues and Solutions**

### 1. **WebDriver Issues**

#### Issue: WebDriver Not Found
**Symptoms:**
```
Exception in thread "main" java.lang.IllegalStateException: 
The path to the driver executable must be set by the webdriver.chrome.driver system property
```

**Solutions:**
```bash
# Solution 1: Use WebDriverManager (Recommended)
# Already configured in the framework

# Solution 2: Manual setup
export PATH=$PATH:/path/to/chromedriver
mvn test -Dwebdriver.chrome.driver=/path/to/chromedriver

# Solution 3: Update WebDriverManager
mvn clean install -U
```

#### Issue: Chrome Version Mismatch
**Symptoms:**
```
SessionNotCreatedException: Message: session not created: 
This version of ChromeDriver only supports Chrome version XX
```

**Solutions:**
```bash
# Solution 1: Update Chrome browser
# Download latest Chrome from https://www.google.com/chrome/

# Solution 2: Force WebDriverManager to download correct version
mvn test -Dwebdriver.manager.force-download=true

# Solution 3: Clear WebDriverManager cache
rm -rf ~/.cache/selenium
```

#### Issue: Browser Crashes
**Symptoms:**
```
WebDriverException: chrome not reachable
```

**Solutions:**
```properties
# Add to config.properties
chrome.options=--no-sandbox,--disable-dev-shm-usage,--disable-gpu,--disable-extensions
```

```java
// In TestBase.java
ChromeOptions options = new ChromeOptions();
options.addArguments("--no-sandbox");
options.addArguments("--disable-dev-shm-usage");
options.addArguments("--disable-gpu");
options.addArguments("--disable-extensions");
```

### 2. **Element Interaction Issues**

#### Issue: Element Not Found
**Symptoms:**
```
NoSuchElementException: no such element: Unable to locate element
```

**Solutions:**
```java
// Solution 1: Use explicit wait
public boolean waitForElementVisible(By locator) {
    try {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return true;
    } catch (TimeoutException e) {
        logger.error("Element not visible: " + locator);
        return false;
    }
}

// Solution 2: Check if element exists before interaction
public void clickElement(WebElement element) {
    try {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    } catch (Exception e) {
        logger.error("Failed to click element: " + e.getMessage());
        throw e;
    }
}

// Solution 3: Use JavaScript click
public void clickElementWithJS(WebElement element) {
    try {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    } catch (Exception e) {
        logger.error("Failed to click element with JS: " + e.getMessage());
        throw e;
    }
}
```

#### Issue: Element Not Clickable
**Symptoms:**
```
ElementClickInterceptedException: element click intercepted
```

**Solutions:**
```java
// Solution 1: Scroll to element first
public void clickElement(WebElement element) {
    try {
        scrollToElement(element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    } catch (Exception e) {
        logger.error("Failed to click element: " + e.getMessage());
        throw e;
    }
}

// Solution 2: Wait for overlay to disappear
public void waitForOverlayToDisappear() {
    try {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".overlay")));
    } catch (TimeoutException e) {
        logger.warn("Overlay did not disappear within timeout");
    }
}

// Solution 3: Use JavaScript click
public void clickElementWithJS(WebElement element) {
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
}
```

#### Issue: Stale Element Reference
**Symptoms:**
```
StaleElementReferenceException: stale element reference
```

**Solutions:**
```java
// Solution 1: Re-find element
public void clickElementWithRetry(By locator) {
    int maxAttempts = 3;
    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
        try {
            WebElement element = driver.findElement(locator);
            element.click();
            return;
        } catch (StaleElementReferenceException e) {
            if (attempt == maxAttempts) {
                throw e;
            }
            logger.warn("Stale element, retrying... Attempt " + attempt);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

// Solution 2: Use page factory refresh
public void refreshPageElements() {
    PageFactory.initElements(driver, this);
}
```

### 3. **Test Execution Issues**

#### Issue: Tests Running Slowly
**Symptoms:**
- Tests taking much longer than expected
- High resource usage

**Solutions:**
```properties
# Optimize configuration
implicit.wait=5
explicit.wait=10
headless=true
```

```java
// Solution 1: Optimize waits
public void clickElement(WebElement element) {
    try {
        // Use shorter wait for clickable
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        shortWait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    } catch (Exception e) {
        logger.error("Failed to click element: " + e.getMessage());
        throw e;
    }
}

// Solution 2: Use parallel execution
@Test(threadPoolSize = 4)
public void testParallel() {
    // Test implementation
}

// Solution 3: Optimize element locators
// Use ID instead of XPath when possible
@FindBy(id = "login-button")  // Fast
private WebElement loginButton;

@FindBy(xpath = "//button[contains(text(),'Login')]")  // Slower
private WebElement loginButtonXPath;
```

#### Issue: Memory Leaks
**Symptoms:**
- OutOfMemoryError
- Increasing memory usage over time

**Solutions:**
```java
// Solution 1: Proper cleanup
@AfterMethod
public void tearDown() {
    try {
        if (driver != null) {
            driver.quit();
        }
    } catch (Exception e) {
        logger.error("Error during driver cleanup: " + e.getMessage());
    } finally {
        driver = null;
    }
}

// Solution 2: Clear browser cache
public void clearBrowserCache() {
    driver.manage().deleteAllCookies();
    ((JavascriptExecutor) driver).executeScript("window.localStorage.clear();");
    ((JavascriptExecutor) driver).executeScript("window.sessionStorage.clear();");
}

// Solution 3: Increase JVM memory
mvn test -Xmx2g -Xms1g
```

### 4. **API Testing Issues**

#### Issue: API Connection Timeout
**Symptoms:**
```
java.net.SocketTimeoutException: connect timed out
```

**Solutions:**
```properties
# Increase timeout in config.properties
api.timeout=60000
```

```java
// Solution 1: Configure timeout
protected RequestSpecification createRequestSpec() {
    return RestAssured.given()
            .baseUri(BASE_URL)
            .timeout(Duration.ofSeconds(60))
            .config(RestAssured.config()
                    .connectionConfig(ConnectionConfig.connectionConfig()
                            .connectTimeout(60000)
                            .readTimeout(60000)));
}

// Solution 2: Retry mechanism
public Response getWithRetry(String endpoint, int maxRetries) {
    for (int attempt = 1; attempt <= maxRetries; attempt++) {
        try {
            return get(endpoint);
        } catch (Exception e) {
            if (attempt == maxRetries) {
                throw e;
            }
            logger.warn("API call failed, retrying... Attempt " + attempt);
            try {
                Thread.sleep(2000 * attempt); // Exponential backoff
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ie);
            }
        }
    }
    throw new RuntimeException("API call failed after " + maxRetries + " attempts");
}
```

#### Issue: SSL Certificate Issues
**Symptoms:**
```
javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException
```

**Solutions:**
```java
// Solution 1: Disable SSL verification (for testing only)
protected RequestSpecification createRequestSpec() {
    return RestAssured.given()
            .baseUri(BASE_URL)
            .relaxedHTTPSValidation()
            .config(RestAssured.config()
                    .sslConfig(SSLConfig.sslConfig()
                            .relaxedHTTPSValidation()));
}

// Solution 2: Trust all certificates
static {
    TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() { return null; }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
        }
    };
    
    try {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
        // Handle exception
    }
}
```

### 5. **Visual Testing Issues**

#### Issue: Applitools Connection Issues
**Symptoms:**
```
java.net.ConnectException: Connection refused
```

**Solutions:**
```properties
# Check Applitools configuration
applitools.api.key=your-valid-api-key
applitools.server.url=https://eyesapi.applitools.com
```

```java
// Solution 1: Verify API key
public void verifyApplitoolsConnection() {
    try {
        eyes.setApiKey(APPLITOOLS_API_KEY);
        // Test connection
        eyes.open(driver, "Connection Test", "Test", new RectangleSize(1920, 1080));
        eyes.close();
    } catch (Exception e) {
        logger.error("Applitools connection failed: " + e.getMessage());
        throw e;
    }
}

// Solution 2: Handle network issues
public void initializeEyesWithRetry(WebDriver driver) {
    int maxRetries = 3;
    for (int attempt = 1; attempt <= maxRetries; attempt++) {
        try {
            initializeEyes(driver);
            return;
        } catch (Exception e) {
            if (attempt == maxRetries) {
                throw e;
            }
            logger.warn("Applitools initialization failed, retrying... Attempt " + attempt);
            try {
                Thread.sleep(2000 * attempt);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ie);
            }
        }
    }
}
```

#### Issue: Visual Test Failures
**Symptoms:**
- Visual tests failing due to minor differences
- False positives

**Solutions:**
```java
// Solution 1: Use appropriate match level
public void takeScreenshotWithMatchLevel(String tag, MatchLevel matchLevel) {
    eyes.setMatchLevel(matchLevel);
    eyes.check(tag, Target.window().fully());
}

// Solution 2: Ignore dynamic content
public void takeScreenshotIgnoringDynamicContent(String tag) {
    eyes.check(tag, Target.window().fully()
            .ignore(By.cssSelector(".dynamic-content"))
            .ignore(By.cssSelector(".timestamp"))
            .ignore(By.cssSelector(".user-specific")));
}

// Solution 3: Use layout comparison
public void takeLayoutScreenshot(String tag) {
    eyes.check(tag, Target.window().layout());
}
```

### 6. **Performance Testing Issues**

#### Issue: Inconsistent Performance Results
**Symptoms:**
- Performance metrics varying significantly between runs
- Unreliable SLA validation

**Solutions:**
```java
// Solution 1: Warm up before measurement
public long measurePageLoadTimeWithWarmup(WebDriver driver, String pageName) {
    // Warm up
    driver.get(getBaseUrl());
    try {
        Thread.sleep(2000);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
    
    // Actual measurement
    return measurePageLoadTime(driver, pageName);
}

// Solution 2: Multiple measurements
public double getAveragePageLoadTime(WebDriver driver, String pageName, int iterations) {
    long totalTime = 0;
    for (int i = 0; i < iterations; i++) {
        totalTime += measurePageLoadTime(driver, pageName);
        try {
            Thread.sleep(1000); // Wait between measurements
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    return (double) totalTime / iterations;
}

// Solution 3: Account for system load
public boolean meetsSLAWithTolerance(String operationName, long slaThreshold, double tolerance) {
    PerformanceStats stats = getPerformanceStats(operationName);
    if (stats == null) {
        return false;
    }
    
    double maxAllowed = slaThreshold * (1 + tolerance);
    return stats.getMean() <= maxAllowed;
}
```

### 7. **Configuration Issues**

#### Issue: Configuration Not Loading
**Symptoms:**
- Default values being used instead of configured values
- Configuration file not found

**Solutions:**
```java
// Solution 1: Verify configuration file path
public void verifyConfiguration() {
    File configFile = new File("src/main/resources/config.properties");
    if (!configFile.exists()) {
        throw new RuntimeException("Configuration file not found: " + configFile.getAbsolutePath());
    }
    
    Properties props = new Properties();
    try (FileInputStream fis = new FileInputStream(configFile)) {
        props.load(fis);
        logger.info("Configuration loaded successfully");
        logger.info("Browser: " + props.getProperty("browser"));
        logger.info("Base URL: " + props.getProperty("base.url"));
    } catch (IOException e) {
        throw new RuntimeException("Failed to load configuration", e);
    }
}

// Solution 2: Environment-specific configuration
public void loadEnvironmentConfiguration(String environment) {
    String configFile = "src/main/resources/config-" + environment + ".properties";
    Properties props = new Properties();
    
    try (FileInputStream fis = new FileInputStream(configFile)) {
        props.load(fis);
        // Apply configuration
    } catch (IOException e) {
        logger.warn("Environment-specific config not found, using default");
        loadDefaultConfiguration();
    }
}
```

#### Issue: Environment Variables Not Set
**Symptoms:**
- API keys not working
- Environment-specific settings not applied

**Solutions:**
```bash
# Solution 1: Set environment variables
export API_KEY="your-api-key"
export APPLITOOLS_API_KEY="your-applitools-key"
export BASE_URL="https://your-app-url.com"

# Solution 2: Use .env file
echo "API_KEY=your-api-key" > .env
echo "APPLITOOLS_API_KEY=your-applitools-key" >> .env
echo "BASE_URL=https://your-app-url.com" >> .env
```

```java
// Solution 3: Load from environment variables
public void loadFromEnvironment() {
    String apiKey = System.getenv("API_KEY");
    if (apiKey == null) {
        logger.warn("API_KEY environment variable not set");
    } else {
        System.setProperty("api.key", apiKey);
    }
    
    String applitoolsKey = System.getenv("APPLITOOLS_API_KEY");
    if (applitoolsKey == null) {
        logger.warn("APPLITOOLS_API_KEY environment variable not set");
    } else {
        System.setProperty("applitools.api.key", applitoolsKey);
    }
}
```

### 8. **Reporting Issues**

#### Issue: Allure Reports Not Generated
**Symptoms:**
- No reports generated after test execution
- Missing test results

**Solutions:**
```xml
<!-- Solution 1: Verify Allure plugin configuration in pom.xml -->
<plugin>
    <groupId>io.qameta.allure</groupId>
    <artifactId>allure-maven</artifactId>
    <version>2.12.0</version>
    <configuration>
        <reportVersion>2.20.1</reportVersion>
        <resultsDirectory>${project.build.directory}/allure-results</resultsDirectory>
        <reportDirectory>${project.build.directory}/allure-reports</reportDirectory>
    </configuration>
</plugin>
```

```bash
# Solution 2: Generate reports manually
mvn allure:report

# Solution 3: Serve reports
mvn allure:serve
```

```java
// Solution 4: Verify Allure listener
@CucumberOptions(
    plugin = {
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
        "pretty",
        "html:target/cucumber-reports"
    }
)
```

#### Issue: Screenshots Not Captured
**Symptoms:**
- No screenshots in reports
- Screenshot directory not created

**Solutions:**
```java
// Solution 1: Verify screenshot directory
public void captureScreenshot(String fileName) {
    String screenshotDir = "target/screenshots";
    File dir = new File(screenshotDir);
    if (!dir.exists()) {
        dir.mkdirs();
    }
    
    String filePath = screenshotDir + "/" + fileName + ".png";
    try {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Files.copy(screenshot.toPath(), new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        logger.info("Screenshot saved: " + filePath);
    } catch (IOException e) {
        logger.error("Failed to save screenshot: " + e.getMessage());
    }
}

// Solution 2: Add to Allure report
@AfterMethod
public void afterMethod(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
        String screenshotPath = captureScreenshot(result.getName());
        Allure.addAttachment("Screenshot", "image/png", 
            new ByteArrayInputStream(Files.readAllBytes(Paths.get(screenshotPath))), "png");
    }
}
```

## 🛠️ **Debug Mode**

### Enable Debug Logging
```properties
# In config.properties
log.level=DEBUG
```

```bash
# Command line
mvn test -Dlogback.level=DEBUG
```

### Verbose Test Execution
```bash
# Maven verbose
mvn test -X

# Cucumber verbose
mvn test -Dcucumber.options="--plugin pretty --plugin html:target/cucumber-reports"
```

### Browser Debug Mode
```java
// Enable browser logging
ChromeOptions options = new ChromeOptions();
options.setCapability("goog:loggingPrefs", new HashMap<String, String>() {{
    put("browser", "ALL");
    put("driver", "ALL");
}});

// Capture browser logs
public void captureBrowserLogs() {
    LogEntries logs = driver.manage().logs().get(LogType.BROWSER);
    for (LogEntry entry : logs) {
        logger.debug("Browser Log: " + entry.getMessage());
    }
}
```

## 📞 **Getting Help**

### 1. **Check Logs**
- Review test execution logs
- Check browser console logs
- Verify framework logs

### 2. **Common Debugging Steps**
1. Verify environment setup
2. Check configuration files
3. Validate test data
4. Review element locators
5. Check network connectivity
6. Verify API endpoints

### 3. **Support Resources**
- Framework documentation
- GitHub issues
- Stack Overflow
- Community forums

### 4. **Reporting Issues**
When reporting issues, include:
- Framework version
- Java version
- Browser version
- Error logs
- Steps to reproduce
- Environment details

This troubleshooting guide covers the most common issues and their solutions. For additional help, refer to the framework documentation or community resources. 