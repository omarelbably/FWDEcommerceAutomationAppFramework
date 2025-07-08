# API Reference - Test Automation Framework

## 📚 **Core Classes Reference**

### TestBase Class
**Package:** `org.example.core.TestBase`
**Purpose:** Foundation class for all test classes

#### Constructor
```java
public TestBase()
```

#### Properties
```java
protected WebDriver driver;           // WebDriver instance
protected WebDriverWait wait;         // Explicit wait instance
protected Actions actions;            // Actions instance
protected JavascriptExecutor js;      // JavaScript executor
protected Logger logger;              // Logger instance
```

#### Methods

##### setUp()
```java
@BeforeMethod
public void setUp()
```
**Purpose:** Initialize test environment
**Usage:** Automatically called before each test method

##### tearDown()
```java
@AfterMethod
public void tearDown()
```
**Purpose:** Clean up test environment
**Usage:** Automatically called after each test method

##### captureScreenshot(String fileName)
```java
protected void captureScreenshot(String fileName)
```
**Purpose:** Capture screenshot and save to target/screenshots/
**Parameters:**
- `fileName` (String): Name of the screenshot file
**Returns:** void

##### getDriver()
```java
public WebDriver getDriver()
```
**Purpose:** Get WebDriver instance
**Returns:** WebDriver

##### getWait()
```java
public WebDriverWait getWait()
```
**Purpose:** Get WebDriverWait instance
**Returns:** WebDriverWait

### BasePage Class
**Package:** `org.example.core.BasePage`
**Purpose:** Base class for all page objects

#### Constructor
```java
public BasePage(WebDriver driver)
```
**Parameters:**
- `driver` (WebDriver): WebDriver instance

#### Methods

##### clickElement(WebElement element)
```java
public void clickElement(WebElement element)
```
**Purpose:** Click element with retry and logging
**Parameters:**
- `element` (WebElement): Element to click
**Returns:** void

##### sendKeysToElement(WebElement element, String text)
```java
public void sendKeysToElement(WebElement element, String text)
```
**Purpose:** Send keys to element with validation
**Parameters:**
- `element` (WebElement): Target element
- `text` (String): Text to enter
**Returns:** void

##### waitForElementVisible(By locator)
```java
public boolean waitForElementVisible(By locator)
```
**Purpose:** Wait for element to be visible
**Parameters:**
- `locator` (By): Element locator
**Returns:** boolean - true if element is visible

##### waitForElementClickable(By locator)
```java
public boolean waitForElementClickable(By locator)
```
**Purpose:** Wait for element to be clickable
**Parameters:**
- `locator` (By): Element locator
**Returns:** boolean - true if element is clickable

##### getElementText(WebElement element)
```java
public String getElementText(WebElement element)
```
**Purpose:** Get text from element
**Parameters:**
- `element` (WebElement): Target element
**Returns:** String - element text

##### isElementDisplayed(WebElement element)
```java
public boolean isElementDisplayed(WebElement element)
```
**Purpose:** Check if element is displayed
**Parameters:**
- `element` (WebElement): Target element
**Returns:** boolean - true if element is displayed

##### scrollToElement(WebElement element)
```java
public void scrollToElement(WebElement element)
```
**Purpose:** Scroll to element
**Parameters:**
- `element` (WebElement): Target element
**Returns:** void

##### highlightElement(WebElement element)
```java
public void highlightElement(WebElement element)
```
**Purpose:** Highlight element for debugging
**Parameters:**
- `element` (WebElement): Target element
**Returns:** void

## 🔧 **Utility Classes Reference**

### TestDataManager Class
**Package:** `org.example.utils.TestDataManager`
**Purpose:** Generate dynamic test data

#### Static Methods

##### generateUserData()
```java
public static Map<String, String> generateUserData()
```
**Purpose:** Generate comprehensive user data
**Returns:** Map<String, String> - User data with keys: firstName, lastName, email, password, etc.

##### generateProductData()
```java
public static Map<String, String> generateProductData()
```
**Purpose:** Generate product information
**Returns:** Map<String, String> - Product data with keys: name, description, price, category, etc.

##### generateSearchTerm()
```java
public static String generateSearchTerm()
```
**Purpose:** Generate random search term
**Returns:** String - Random search term

##### generateEmail()
```java
public static String generateEmail()
```
**Purpose:** Generate unique email address
**Returns:** String - Unique email address

##### generatePassword()
```java
public static String generatePassword()
```
**Purpose:** Generate secure password
**Returns:** String - Secure password

##### generateAddress()
```java
public static Map<String, String> generateAddress()
```
**Purpose:** Generate address information
**Returns:** Map<String, String> - Address data with keys: street, city, state, zipCode, country

### TestDataCache Class
**Package:** `org.example.utils.TestDataCache`
**Purpose:** Cache test data for performance

#### Static Methods

##### cacheData(String key, Object data)
```java
public static void cacheData(String key, Object data)
```
**Purpose:** Cache data with default TTL (24 hours)
**Parameters:**
- `key` (String): Cache key
- `data` (Object): Data to cache
**Returns:** void

##### cacheData(String key, Object data, long ttlHours)
```java
public static void cacheData(String key, Object data, long ttlHours)
```
**Purpose:** Cache data with custom TTL
**Parameters:**
- `key` (String): Cache key
- `data` (Object): Data to cache
- `ttlHours` (long): Time to live in hours
**Returns:** void

##### getCachedData(String key, Class<T> clazz)
```java
public static <T> T getCachedData(String key, Class<T> clazz)
```
**Purpose:** Retrieve cached data
**Parameters:**
- `key` (String): Cache key
- `clazz` (Class<T>): Expected data type
**Returns:** T - Cached data or null if not found/expired

##### hasCachedData(String key)
```java
public static boolean hasCachedData(String key)
```
**Purpose:** Check if data exists in cache and is not expired
**Parameters:**
- `key` (String): Cache key
**Returns:** boolean - true if valid cached data exists

##### removeCachedData(String key)
```java
public static void removeCachedData(String key)
```
**Purpose:** Remove data from cache
**Parameters:**
- `key` (String): Cache key
**Returns:** void

##### clearCache()
```java
public static void clearCache()
```
**Purpose:** Clear all cached data
**Returns:** void

##### getCacheStats()
```java
public static CacheStats getCacheStats()
```
**Purpose:** Get cache statistics
**Returns:** CacheStats - Cache statistics object

##### getOrCreateUserData(String key)
```java
public static Map<String, String> getOrCreateUserData(String key)
```
**Purpose:** Get cached user data or create new if not exists
**Parameters:**
- `key` (String): Cache key
**Returns:** Map<String, String> - User data

##### getOrCreateProductData(String key)
```java
public static Map<String, String> getOrCreateProductData(String key)
```
**Purpose:** Get cached product data or create new if not exists
**Parameters:**
- `key` (String): Cache key
**Returns:** Map<String, String> - Product data

#### Inner Classes

##### CacheStats Class
```java
public static class CacheStats {
    private final long totalEntries;
    private final long validEntries;
    private final long expiredEntries;
    private final long diskEntries;
    
    // Getters
    public long getTotalEntries();
    public long getValidEntries();
    public long getExpiredEntries();
    public long getDiskEntries();
}
```

## 🔄 **Retry Mechanism Reference**

### RetryAnalyzer Class
**Package:** `org.example.core.RetryAnalyzer`
**Purpose:** Handle flaky tests with retry mechanism

#### Constructor
```java
public RetryAnalyzer()
```

#### Methods

##### retry(ITestResult result)
```java
@Override
public boolean retry(ITestResult result)
```
**Purpose:** Determine if test should be retried
**Parameters:**
- `result` (ITestResult): Test result
**Returns:** boolean - true if test should be retried

##### resetRetryCount()
```java
public void resetRetryCount()
```
**Purpose:** Reset retry count for new test
**Returns:** void

##### getRetryCount()
```java
public int getRetryCount()
```
**Purpose:** Get current retry count
**Returns:** int - Current retry count

##### shouldRetry(Throwable throwable)
```java
public boolean shouldRetry(Throwable throwable)
```
**Purpose:** Check if exception should trigger retry
**Parameters:**
- `throwable` (Throwable): Exception to check
**Returns:** boolean - true if exception should trigger retry

## 🌐 **API Testing Reference**

### ApiTestBase Class
**Package:** `org.example.api.ApiTestBase`
**Purpose:** Base class for API testing

#### Static Properties
```java
protected static final String BASE_URL;    // API base URL
protected static final String API_KEY;     // API key
protected static final int TIMEOUT;        // Request timeout
```

#### Methods

##### createRequestSpec()
```java
protected RequestSpecification createRequestSpec()
```
**Purpose:** Create base request specification
**Returns:** RequestSpecification - Configured request specification

##### get(String endpoint)
```java
protected Response get(String endpoint)
```
**Purpose:** Perform GET request
**Parameters:**
- `endpoint` (String): API endpoint
**Returns:** Response - API response

##### get(String endpoint, Map<String, Object> pathParams)
```java
protected Response get(String endpoint, Map<String, Object> pathParams)
```
**Purpose:** Perform GET request with path parameters
**Parameters:**
- `endpoint` (String): API endpoint
- `pathParams` (Map<String, Object>): Path parameters
**Returns:** Response - API response

##### get(String endpoint, Map<String, Object> pathParams, Map<String, Object> queryParams)
```java
protected Response get(String endpoint, Map<String, Object> pathParams, Map<String, Object> queryParams)
```
**Purpose:** Perform GET request with path and query parameters
**Parameters:**
- `endpoint` (String): API endpoint
- `pathParams` (Map<String, Object>): Path parameters
- `queryParams` (Map<String, Object>): Query parameters
**Returns:** Response - API response

##### post(String endpoint, Object body)
```java
protected Response post(String endpoint, Object body)
```
**Purpose:** Perform POST request
**Parameters:**
- `endpoint` (String): API endpoint
- `body` (Object): Request body
**Returns:** Response - API response

##### put(String endpoint, Object body)
```java
protected Response put(String endpoint, Object body)
```
**Purpose:** Perform PUT request
**Parameters:**
- `endpoint` (String): API endpoint
- `body` (Object): Request body
**Returns:** Response - API response

##### delete(String endpoint)
```java
protected Response delete(String endpoint)
```
**Purpose:** Perform DELETE request
**Parameters:**
- `endpoint` (String): API endpoint
**Returns:** Response - API response

##### patch(String endpoint, Object body)
```java
protected Response patch(String endpoint, Object body)
```
**Purpose:** Perform PATCH request
**Parameters:**
- `endpoint` (String): API endpoint
- `body` (Object): Request body
**Returns:** Response - API response

##### validateStatusCode(Response response, int expectedStatusCode)
```java
protected void validateStatusCode(Response response, int expectedStatusCode)
```
**Purpose:** Validate response status code
**Parameters:**
- `response` (Response): API response
- `expectedStatusCode` (int): Expected status code
**Returns:** void

##### validateResponseContainsField(Response response, String fieldName)
```java
protected void validateResponseContainsField(Response response, String fieldName)
```
**Purpose:** Validate response contains field
**Parameters:**
- `response` (Response): API response
- `fieldName` (String): Field name to check
**Returns:** void

##### validateResponseFieldValue(Response response, String fieldName, Object expectedValue)
```java
protected void validateResponseFieldValue(Response response, String fieldName, Object expectedValue)
```
**Purpose:** Validate response field value
**Parameters:**
- `response` (Response): API response
- `fieldName` (String): Field name
- `expectedValue` (Object): Expected value
**Returns:** void

##### toJson(Object object)
```java
protected String toJson(Object object)
```
**Purpose:** Convert object to JSON string
**Parameters:**
- `object` (Object): Object to convert
**Returns:** String - JSON string

##### fromJson(String json, Class<T> clazz)
```java
protected <T> T fromJson(String json, Class<T> clazz)
```
**Purpose:** Convert JSON string to object
**Parameters:**
- `json` (String): JSON string
- `clazz` (Class<T>): Target class
**Returns:** T - Deserialized object

## 👁️ **Visual Testing Reference**

### VisualTestBase Class
**Package:** `org.example.visual.VisualTestBase`
**Purpose:** Base class for visual testing with Applitools

#### Properties
```java
private Eyes eyes;           // Applitools Eyes instance
private WebDriver driver;    // WebDriver instance
```

#### Methods

##### initializeEyes(WebDriver driver)
```java
protected void initializeEyes(WebDriver driver)
```
**Purpose:** Initialize Applitools Eyes
**Parameters:**
- `driver` (WebDriver): WebDriver instance
**Returns:** void

##### openEyes(String testName, String viewportSize)
```java
protected void openEyes(String testName, String viewportSize)
```
**Purpose:** Open Eyes for visual testing
**Parameters:**
- `testName` (String): Test name
- `viewportSize` (String): Viewport size (e.g., "1920x1080")
**Returns:** void

##### takeFullPageScreenshot(String tag)
```java
protected void takeFullPageScreenshot(String tag)
```
**Purpose:** Take full page screenshot
**Parameters:**
- `tag` (String): Screenshot tag
**Returns:** void

##### takeElementScreenshot(String tag, WebElement element)
```java
protected void takeElementScreenshot(String tag, WebElement element)
```
**Purpose:** Take screenshot of specific element
**Parameters:**
- `tag` (String): Screenshot tag
- `element` (WebElement): Target element
**Returns:** void

##### takeRegionScreenshot(String tag, int x, int y, int width, int height)
```java
protected void takeRegionScreenshot(String tag, int x, int y, int width, int height)
```
**Purpose:** Take screenshot of specific region
**Parameters:**
- `tag` (String): Screenshot tag
- `x` (int): X coordinate
- `y` (int): Y coordinate
- `width` (int): Region width
- `height` (int): Region height
**Returns:** void

##### takeScreenshotIgnoringRegions(String tag, WebElement... elementsToIgnore)
```java
protected void takeScreenshotIgnoringRegions(String tag, WebElement... elementsToIgnore)
```
**Purpose:** Take screenshot ignoring specific regions
**Parameters:**
- `tag` (String): Screenshot tag
- `elementsToIgnore` (WebElement...): Elements to ignore
**Returns:** void

##### takeLayoutScreenshot(String tag)
```java
protected void takeLayoutScreenshot(String tag)
```
**Purpose:** Take screenshot with layout comparison
**Parameters:**
- `tag` (String): Screenshot tag
**Returns:** void

##### takeContentScreenshot(String tag)
```java
protected void takeContentScreenshot(String tag)
```
**Purpose:** Take screenshot with content comparison
**Parameters:**
- `tag` (String): Screenshot tag
**Returns:** void

##### takeStrictScreenshot(String tag)
```java
protected void takeStrictScreenshot(String tag)
```
**Purpose:** Take screenshot with strict comparison
**Parameters:**
- `tag` (String): Screenshot tag
**Returns:** void

##### closeEyes()
```java
protected boolean closeEyes()
```
**Purpose:** Close Eyes and get test results
**Returns:** boolean - true if visual test passed

##### abortEyes()
```java
protected void abortEyes()
```
**Purpose:** Abort Eyes if not closed
**Returns:** void

##### setMatchLevel(MatchLevel matchLevel)
```java
protected void setMatchLevel(MatchLevel matchLevel)
```
**Purpose:** Set match level for visual comparison
**Parameters:**
- `matchLevel` (MatchLevel): Match level
**Returns:** void

##### setBaselineBranch(String branchName)
```java
protected void setBaselineBranch(String branchName)
```
**Purpose:** Set baseline branch
**Parameters:**
- `branchName` (String): Branch name
**Returns:** void

##### setParentBranch(String branchName)
```java
protected void setParentBranch(String branchName)
```
**Purpose:** Set parent branch
**Parameters:**
- `branchName` (String): Branch name
**Returns:** void

##### enableVisualGrid()
```java
protected void enableVisualGrid()
```
**Purpose:** Enable visual grid for cross-browser testing
**Returns:** void

## ⚡ **Performance Testing Reference**

### PerformanceTestBase Class
**Package:** `org.example.performance.PerformanceTestBase`
**Purpose:** Base class for performance testing

#### Static Properties
```java
protected static final MetricRegistry metrics;           // Metrics registry
protected static final Map<String, Timer> timers;        // Timer collection
protected static final Map<String, Counter> counters;    // Counter collection
protected static final Map<String, Histogram> histograms; // Histogram collection
```

#### Methods

##### startTimer(String operationName)
```java
protected Timer.Context startTimer(String operationName)
```
**Purpose:** Start timing an operation
**Parameters:**
- `operationName` (String): Operation name
**Returns:** Timer.Context - Timer context

##### stopTimer(Timer.Context context, String operationName)
```java
protected long stopTimer(Timer.Context context, String operationName)
```
**Purpose:** Stop timing an operation
**Parameters:**
- `context` (Timer.Context): Timer context
- `operationName` (String): Operation name
**Returns:** long - Duration in milliseconds

##### timeOperation(String operationName, Runnable operation)
```java
protected long timeOperation(String operationName, Runnable operation)
```
**Purpose:** Time an operation with automatic start/stop
**Parameters:**
- `operationName` (String): Operation name
- `operation` (Runnable): Operation to time
**Returns:** long - Duration in milliseconds

##### incrementCounter(String counterName)
```java
protected void incrementCounter(String counterName)
```
**Purpose:** Increment a counter
**Parameters:**
- `counterName` (String): Counter name
**Returns:** void

##### incrementCounter(String counterName, long amount)
```java
protected void incrementCounter(String counterName, long amount)
```
**Purpose:** Increment a counter by specific amount
**Parameters:**
- `counterName` (String): Counter name
- `amount` (long): Amount to increment
**Returns:** void

##### recordHistogram(String histogramName, long value)
```java
protected void recordHistogram(String histogramName, long value)
```
**Purpose:** Record a value in a histogram
**Parameters:**
- `histogramName` (String): Histogram name
- `value` (long): Value to record
**Returns:** void

##### measurePageLoadTime(WebDriver driver, String pageName)
```java
protected long measurePageLoadTime(WebDriver driver, String pageName)
```
**Purpose:** Measure page load time
**Parameters:**
- `driver` (WebDriver): WebDriver instance
- `pageName` (String): Page name
**Returns:** long - Load time in milliseconds

##### measureElementInteraction(String elementName, Runnable interaction)
```java
protected long measureElementInteraction(String elementName, Runnable interaction)
```
**Purpose:** Measure element interaction time
**Parameters:**
- `elementName` (String): Element name
- `interaction` (Runnable): Interaction to measure
**Returns:** long - Interaction time in milliseconds

##### measureApiResponseTime(String endpoint, Runnable apiCall)
```java
protected long measureApiResponseTime(String endpoint, Runnable apiCall)
```
**Purpose:** Measure API response time
**Parameters:**
- `endpoint` (String): API endpoint
- `apiCall` (Runnable): API call to measure
**Returns:** long - Response time in milliseconds

##### getPerformanceStats(String operationName)
```java
protected PerformanceStats getPerformanceStats(String operationName)
```
**Purpose:** Get performance statistics for an operation
**Parameters:**
- `operationName` (String): Operation name
**Returns:** PerformanceStats - Performance statistics

##### analyzePerformance(String operationName, int sampleSize)
```java
protected PerformanceAnalysis analyzePerformance(String operationName, int sampleSize)
```
**Purpose:** Analyze performance trends
**Parameters:**
- `operationName` (String): Operation name
- `sampleSize` (int): Sample size for analysis
**Returns:** PerformanceAnalysis - Performance analysis

##### meetsSLA(String operationName, long slaThreshold)
```java
protected boolean meetsSLA(String operationName, long slaThreshold)
```
**Purpose:** Check if performance meets SLA requirements
**Parameters:**
- `operationName` (String): Operation name
- `slaThreshold` (long): SLA threshold in milliseconds
**Returns:** boolean - true if SLA is met

##### generatePerformanceReport()
```java
protected String generatePerformanceReport()
```
**Purpose:** Generate performance report
**Returns:** String - Performance report

#### Inner Classes

##### PerformanceStats Class
```java
public static class PerformanceStats {
    private final long count;
    private final double mean;
    private final double median;
    private final double percentile95;
    private final double percentile99;
    private final double min;
    private final double max;
    
    // Getters
    public long getCount();
    public double getMean();
    public double getMedian();
    public double getPercentile95();
    public double getPercentile99();
    public double getMin();
    public double getMax();
}
```

##### PerformanceAnalysis Class
```java
public static class PerformanceAnalysis {
    private final String operationName;
    private final double mean;
    private final double standardDeviation;
    private final double percentile95;
    private final double percentile99;
    private final double min;
    private final double max;
    private final long sampleSize;
    
    // Getters
    public String getOperationName();
    public double getMean();
    public double getStandardDeviation();
    public double getPercentile95();
    public double getPercentile99();
    public double getMin();
    public double getMax();
    public long getSampleSize();
}
```

## 🔧 **Configuration Reference**

### Configuration Properties
**File:** `src/main/resources/config.properties`

#### Browser Configuration
```properties
browser=chrome                    # Browser type (chrome, firefox, safari, edge)
headless=false                    # Headless mode
implicit.wait=10                  # Implicit wait in seconds
explicit.wait=20                  # Explicit wait in seconds
```

#### Application Configuration
```properties
base.url=https://demo.nopcommerce.com  # Application base URL
timeout=30                             # Default timeout in seconds
```

#### API Configuration
```properties
api.base.url=https://api.example.com   # API base URL
api.key=your-api-key                   # API key
api.timeout=30000                      # API timeout in milliseconds
```

#### Performance Configuration
```properties
performance.threshold.ms=5000           # Performance threshold
performance.sla.page.load=3000          # Page load SLA
performance.sla.element.interaction=1000 # Element interaction SLA
```

#### Visual Testing Configuration
```properties
applitools.api.key=your-applitools-key  # Applitools API key
applitools.server.url=https://eyesapi.applitools.com  # Applitools server URL
applitools.app.name=E-commerce App      # Application name
applitools.batch.name=E-commerce Tests  # Batch name
```

#### Cache Configuration
```properties
cache.ttl.hours=24                      # Cache TTL in hours
cache.max.size=1000                     # Maximum cache size
cache.cleanup.interval.hours=1          # Cleanup interval
```

#### Retry Configuration
```properties
retry.count=2                           # Retry count
retry.interval=1000                     # Retry interval in milliseconds
```

## 📊 **Annotations Reference**

### TestNG Annotations
```java
@Test(retryAnalyzer = RetryAnalyzer.class)  // Test method with retry
@BeforeMethod                              // Setup method
@AfterMethod                               // Cleanup method
@BeforeClass                               // Class setup
@AfterClass                                // Class cleanup
@DataProvider(name = "dataProvider")       // Data provider
```

### Cucumber Annotations
```java
@Given("step description")                 // Given step
@When("step description")                  // When step
@Then("step description")                  // Then step
@And("step description")                   // And step
@But("step description")                   // But step
```

### Selenium Annotations
```java
@FindBy(id = "element-id")                // Element locator
@FindBy(css = "css-selector")             // CSS selector
@FindBy(xpath = "xpath-expression")       // XPath expression
@FindBy(className = "class-name")         // Class name
@FindBy(name = "name-attribute")          // Name attribute
```

This API reference provides comprehensive documentation for all classes, methods, and configurations in the enhanced test automation framework. Use this reference to understand the available functionality and implement effective test automation solutions. 