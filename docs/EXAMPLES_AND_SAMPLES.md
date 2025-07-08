# Examples and Samples - Test Automation Framework

## 🚀 **Quick Start Examples**

### 1. **Basic Test Example**

#### Feature File
```gherkin
@smoke @ui
Feature: User Login
  As a registered user
  I want to log into the application
  So that I can access my account

  @chrome @valid-credentials
  Scenario: Successful login with valid credentials
    Given user is on login page
    When user enters valid email "test@example.com"
    And user enters valid password "password123"
    And user clicks login button
    Then user should be logged in successfully
    And welcome message should be displayed
```

#### Step Definitions
```java
package org.example.stepDefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.core.TestBase;
import org.example.pages.LoginPage;
import org.example.pages.HomePage;
import org.example.utils.TestDataManager;
import org.testng.Assert;

public class LoginStepDef extends TestBase {
    
    private LoginPage loginPage;
    private HomePage homePage;
    
    @Given("user is on login page")
    public void userIsOnLoginPage() {
        loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage();
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), 
                         "Login page should be displayed");
    }
    
    @When("user enters valid email {string}")
    public void userEntersValidEmail(String email) {
        loginPage.enterEmail(email);
    }
    
    @When("user enters valid password {string}")
    public void userEntersValidPassword(String password) {
        loginPage.enterPassword(password);
    }
    
    @When("user clicks login button")
    public void userClicksLoginButton() {
        loginPage.clickLoginButton();
    }
    
    @Then("user should be logged in successfully")
    public void userShouldBeLoggedInSuccessfully() {
        homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isUserLoggedIn(), 
                         "User should be logged in");
    }
    
    @Then("welcome message should be displayed")
    public void welcomeMessageShouldBeDisplayed() {
        String welcomeMessage = homePage.getWelcomeMessage();
        Assert.assertNotNull(welcomeMessage, "Welcome message should not be null");
        Assert.assertTrue(welcomeMessage.contains("Welcome"), 
                         "Welcome message should contain 'Welcome'");
    }
}
```

#### Page Object
```java
package org.example.pages;

import org.example.core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BasePage {
    
    @FindBy(id = "email")
    private WebElement emailField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(id = "login-button")
    private WebElement loginButton;
    
    @FindBy(css = ".error-message")
    private WebElement errorMessage;
    
    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
    
    public void navigateToLoginPage() {
        driver.get(getBaseUrl() + "/login");
        waitForElementVisible(emailField);
    }
    
    public void enterEmail(String email) {
        sendKeysToElement(emailField, email);
    }
    
    public void enterPassword(String password) {
        sendKeysToElement(passwordField, password);
    }
    
    public void clickLoginButton() {
        clickElement(loginButton);
    }
    
    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }
    
    public boolean isLoginPageDisplayed() {
        return isElementDisplayed(emailField) && 
               isElementDisplayed(passwordField) && 
               isElementDisplayed(loginButton);
    }
    
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }
    
    public String getErrorMessage() {
        return getElementText(errorMessage);
    }
}
```

### 2. **Dynamic Test Data Example**

#### Feature File with Dynamic Data
```gherkin
@regression @ui
Feature: User Registration
  As a new user
  I want to register an account
  So that I can access the application

  @chrome @dynamic-data
  Scenario: Register new user with dynamic data
    Given user is on registration page
    When user fills registration form with dynamic data
    And user submits registration form
    Then user should be registered successfully
    And confirmation email should be sent
```

#### Step Definitions with Dynamic Data
```java
package org.example.stepDefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.core.TestBase;
import org.example.pages.RegistrationPage;
import org.example.utils.TestDataManager;
import org.example.utils.TestDataCache;
import org.testng.Assert;
import java.util.Map;

public class RegistrationStepDef extends TestBase {
    
    private RegistrationPage registrationPage;
    private Map<String, String> userData;
    
    @Given("user is on registration page")
    public void userIsOnRegistrationPage() {
        registrationPage = new RegistrationPage(driver);
        registrationPage.navigateToRegistrationPage();
        Assert.assertTrue(registrationPage.isRegistrationPageDisplayed());
    }
    
    @When("user fills registration form with dynamic data")
    public void userFillsRegistrationFormWithDynamicData() {
        // Generate dynamic test data
        userData = TestDataManager.generateUserData();
        
        // Fill form with generated data
        registrationPage.enterFirstName(userData.get("firstName"));
        registrationPage.enterLastName(userData.get("lastName"));
        registrationPage.enterEmail(userData.get("email"));
        registrationPage.enterPassword(userData.get("password"));
        registrationPage.enterConfirmPassword(userData.get("password"));
        
        // Cache the data for later use
        TestDataCache.cacheData("user_" + userData.get("email"), userData, 24);
    }
    
    @When("user submits registration form")
    public void userSubmitsRegistrationForm() {
        registrationPage.clickRegisterButton();
    }
    
    @Then("user should be registered successfully")
    public void userShouldBeRegisteredSuccessfully() {
        // Wait for registration to complete
        registrationPage.waitForRegistrationComplete();
        
        // Verify success message
        String successMessage = registrationPage.getSuccessMessage();
        Assert.assertTrue(successMessage.contains("Registration successful"), 
                         "Registration should be successful");
        
        // Verify user data in success message
        Assert.assertTrue(successMessage.contains(userData.get("firstName")), 
                         "Success message should contain user's first name");
    }
    
    @Then("confirmation email should be sent")
    public void confirmationEmailShouldBeSent() {
        // Verify email confirmation message
        String emailMessage = registrationPage.getEmailConfirmationMessage();
        Assert.assertTrue(emailMessage.contains("confirmation email"), 
                         "Email confirmation message should be displayed");
    }
}
```

## 🔧 **Advanced Examples**

### 1. **API Testing Examples**

#### API Test Class
```java
package org.example.api;

import org.example.api.ApiTestBase;
import org.example.utils.TestDataManager;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import java.util.Map;

public class UserApiTest extends ApiTestBase {
    
    private Map<String, String> testUserData;
    
    @BeforeClass
    public void setUp() {
        testUserData = TestDataManager.generateUserData();
    }
    
    @Test(description = "Create new user via API")
    public void testCreateUser() {
        // Prepare user data
        Map<String, Object> userPayload = Map.of(
            "firstName", testUserData.get("firstName"),
            "lastName", testUserData.get("lastName"),
            "email", testUserData.get("email"),
            "password", testUserData.get("password")
        );
        
        // Create user
        Response response = post("/users", userPayload);
        
        // Validate response
        validateStatusCode(response, 201);
        validateResponseContainsField(response, "id");
        validateResponseContainsField(response, "email");
        
        // Verify user data
        validateResponseFieldValue(response, "email", testUserData.get("email"));
        validateResponseFieldValue(response, "firstName", testUserData.get("firstName"));
        
        // Store user ID for cleanup
        String userId = response.jsonPath().getString("id");
        TestDataCache.cacheData("created_user_id", userId, 1);
    }
    
    @Test(description = "Get user by ID", dependsOnMethods = "testCreateUser")
    public void testGetUser() {
        String userId = TestDataCache.getCachedData("created_user_id", String.class);
        
        Response response = get("/users/" + userId);
        
        validateStatusCode(response, 200);
        validateResponseFieldValue(response, "id", userId);
        validateResponseFieldValue(response, "email", testUserData.get("email"));
    }
    
    @Test(description = "Update user information", dependsOnMethods = "testGetUser")
    public void testUpdateUser() {
        String userId = TestDataCache.getCachedData("created_user_id", String.class);
        
        Map<String, Object> updatePayload = Map.of(
            "firstName", "Updated " + testUserData.get("firstName"),
            "lastName", testUserData.get("lastName")
        );
        
        Response response = put("/users/" + userId, updatePayload);
        
        validateStatusCode(response, 200);
        validateResponseFieldValue(response, "firstName", "Updated " + testUserData.get("firstName"));
    }
    
    @Test(description = "Delete user", dependsOnMethods = "testUpdateUser")
    public void testDeleteUser() {
        String userId = TestDataCache.getCachedData("created_user_id", String.class);
        
        Response response = delete("/users/" + userId);
        
        validateStatusCode(response, 204);
        
        // Verify user is deleted
        Response getResponse = get("/users/" + userId);
        validateStatusCode(getResponse, 404);
    }
    
    @Test(description = "Get all users")
    public void testGetAllUsers() {
        Response response = get("/users");
        
        validateStatusCode(response, 200);
        validateResponseContainsField(response, "users");
        
        // Verify response structure
        int userCount = response.jsonPath().getInt("users.size()");
        Assert.assertTrue(userCount >= 0, "User count should be non-negative");
    }
    
    @Test(description = "Search users by email")
    public void testSearchUsers() {
        Map<String, Object> queryParams = Map.of(
            "email", testUserData.get("email")
        );
        
        Response response = get("/users/search", null, queryParams);
        
        validateStatusCode(response, 200);
        validateResponseContainsField(response, "users");
    }
}
```

#### API Data Models
```java
package org.example.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public User() {}
    
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @Override
    public String toString() {
        return String.format("User{id='%s', firstName='%s', lastName='%s', email='%s'}", 
                           id, firstName, lastName, email);
    }
}
```

### 2. **Visual Testing Examples**

#### Visual Test Class
```java
package org.example.visual;

import org.example.visual.VisualTestBase;
import org.example.core.TestBase;
import org.example.pages.HomePage;
import org.example.pages.LoginPage;
import org.example.pages.ProductPage;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;

public class EcommerceVisualTest extends TestBase {
    
    private VisualTestBase visualTest;
    private HomePage homePage;
    private LoginPage loginPage;
    private ProductPage productPage;
    
    @BeforeMethod
    public void setUp() {
        visualTest = new VisualTestBase();
        visualTest.initializeEyes(driver);
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        productPage = new ProductPage(driver);
    }
    
    @Test(description = "Visual test for home page")
    public void testHomePageVisual() {
        visualTest.openEyes("Home Page Visual Test", "1920x1080");
        
        // Navigate to home page
        driver.get("https://demo.nopcommerce.com");
        
        // Take full page screenshot
        visualTest.takeFullPageScreenshot("Home Page");
        
        // Close eyes and validate
        boolean result = visualTest.closeEyes();
        Assert.assertTrue(result, "Home page visual test failed");
    }
    
    @Test(description = "Visual test for login page")
    public void testLoginPageVisual() {
        visualTest.openEyes("Login Page Visual Test", "1920x1080");
        
        // Navigate to login page
        loginPage.navigateToLoginPage();
        
        // Take full page screenshot
        visualTest.takeFullPageScreenshot("Login Page");
        
        // Take screenshot of login form specifically
        visualTest.takeElementScreenshot("Login Form", loginPage.getLoginForm());
        
        boolean result = visualTest.closeEyes();
        Assert.assertTrue(result, "Login page visual test failed");
    }
    
    @Test(description = "Visual test for product page")
    public void testProductPageVisual() {
        visualTest.openEyes("Product Page Visual Test", "1920x1080");
        
        // Navigate to product page
        productPage.navigateToProduct("laptop");
        
        // Take full page screenshot
        visualTest.takeFullPageScreenshot("Product Page");
        
        // Take screenshot of product image
        visualTest.takeElementScreenshot("Product Image", productPage.getProductImage());
        
        // Take screenshot of product details
        visualTest.takeElementScreenshot("Product Details", productPage.getProductDetails());
        
        boolean result = visualTest.closeEyes();
        Assert.assertTrue(result, "Product page visual test failed");
    }
    
    @Test(description = "Visual test with layout comparison")
    public void testLayoutComparison() {
        visualTest.openEyes("Layout Comparison Test", "1920x1080");
        
        driver.get("https://demo.nopcommerce.com");
        
        // Use layout comparison (ignores content changes)
        visualTest.takeLayoutScreenshot("Home Page Layout");
        
        boolean result = visualTest.closeEyes();
        Assert.assertTrue(result, "Layout comparison test failed");
    }
    
    @Test(description = "Visual test ignoring dynamic content")
    public void testIgnoringDynamicContent() {
        visualTest.openEyes("Dynamic Content Test", "1920x1080");
        
        driver.get("https://demo.nopcommerce.com");
        
        // Ignore dynamic elements like timestamps, user-specific content
        visualTest.takeScreenshotIgnoringRegions("Home Page No Dynamic Content",
            homePage.getTimestampElement(),
            homePage.getUserSpecificElement(),
            homePage.getAdvertisementElement()
        );
        
        boolean result = visualTest.closeEyes();
        Assert.assertTrue(result, "Dynamic content test failed");
    }
    
    @Test(description = "Cross-browser visual test")
    public void testCrossBrowserVisual() {
        visualTest.enableVisualGrid();
        visualTest.openEyes("Cross Browser Test", "1920x1080");
        
        driver.get("https://demo.nopcommerce.com");
        
        visualTest.takeFullPageScreenshot("Cross Browser Home Page");
        
        boolean result = visualTest.closeEyes();
        Assert.assertTrue(result, "Cross-browser visual test failed");
    }
}
```

### 3. **Performance Testing Examples**

#### Performance Test Class
```java
package org.example.performance;

import org.example.performance.PerformanceTestBase;
import org.example.core.TestBase;
import org.example.pages.HomePage;
import org.example.pages.LoginPage;
import org.example.pages.ProductPage;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.Assert;

public class EcommercePerformanceTest extends TestBase {
    
    private PerformanceTestBase performanceTest;
    private HomePage homePage;
    private LoginPage loginPage;
    private ProductPage productPage;
    
    @BeforeMethod
    public void setUp() {
        performanceTest = new PerformanceTestBase();
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
        productPage = new ProductPage(driver);
    }
    
    @Test(description = "Measure home page load performance")
    public void testHomePageLoadPerformance() {
        long loadTime = performanceTest.measurePageLoadTime(driver, "Home Page");
        
        // Validate against SLA
        boolean meetsSLA = performanceTest.meetsSLA("page_load_Home Page", 3000);
        Assert.assertTrue(meetsSLA, "Home page load time exceeds SLA of 3 seconds");
        
        // Get performance statistics
        PerformanceTestBase.PerformanceStats stats = 
            performanceTest.getPerformanceStats("page_load_Home Page");
        
        System.out.println("Home Page Load Statistics:");
        System.out.println("Average: " + stats.getMean() + " ms");
        System.out.println("95th percentile: " + stats.getPercentile95() + " ms");
        System.out.println("Maximum: " + stats.getMax() + " ms");
    }
    
    @Test(description = "Measure login form interaction performance")
    public void testLoginFormPerformance() {
        loginPage.navigateToLoginPage();
        
        // Measure form filling performance
        long formFillTime = performanceTest.measureElementInteraction("Login Form Fill", () -> {
            loginPage.enterEmail("test@example.com");
            loginPage.enterPassword("password123");
        });
        
        Assert.assertTrue(formFillTime < 1000, "Form filling should complete within 1 second");
        
        // Measure login button click performance
        long clickTime = performanceTest.measureElementInteraction("Login Button Click", () -> {
            loginPage.clickLoginButton();
        });
        
        Assert.assertTrue(clickTime < 500, "Button click should complete within 500ms");
    }
    
    @Test(description = "Measure product page performance")
    public void testProductPagePerformance() {
        // Measure product page load time
        long pageLoadTime = performanceTest.measurePageLoadTime(driver, "Product Page");
        
        // Measure product image load time
        long imageLoadTime = performanceTest.measureElementInteraction("Product Image Load", () -> {
            productPage.waitForProductImageToLoad();
        });
        
        // Validate performance requirements
        Assert.assertTrue(pageLoadTime < 4000, "Product page should load within 4 seconds");
        Assert.assertTrue(imageLoadTime < 2000, "Product image should load within 2 seconds");
    }
    
    @Test(description = "Measure search functionality performance")
    public void testSearchPerformance() {
        homePage.navigateToHomePage();
        
        // Measure search input performance
        long searchInputTime = performanceTest.measureElementInteraction("Search Input", () -> {
            homePage.enterSearchTerm("laptop");
        });
        
        // Measure search results load time
        long searchResultsTime = performanceTest.measureElementInteraction("Search Results", () -> {
            homePage.clickSearchButton();
            homePage.waitForSearchResults();
        });
        
        Assert.assertTrue(searchInputTime < 500, "Search input should respond within 500ms");
        Assert.assertTrue(searchResultsTime < 3000, "Search results should load within 3 seconds");
    }
    
    @Test(description = "Performance analysis with multiple iterations")
    public void testPerformanceAnalysis() {
        int iterations = 10;
        
        // Perform multiple measurements
        for (int i = 0; i < iterations; i++) {
            performanceTest.measurePageLoadTime(driver, "Home Page");
            try {
                Thread.sleep(1000); // Wait between measurements
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Analyze performance trends
        PerformanceTestBase.PerformanceAnalysis analysis = 
            performanceTest.analyzePerformance("page_load_Home Page", iterations);
        
        System.out.println("Performance Analysis:");
        System.out.println("Mean: " + analysis.getMean() + " ms");
        System.out.println("Standard Deviation: " + analysis.getStandardDeviation() + " ms");
        System.out.println("95th percentile: " + analysis.getPercentile95() + " ms");
        System.out.println("Sample Size: " + analysis.getSampleSize());
        
        // Validate consistency
        Assert.assertTrue(analysis.getStandardDeviation() < 500, 
                         "Performance should be consistent (low standard deviation)");
    }
    
    @Test(description = "Generate performance report")
    public void testGeneratePerformanceReport() {
        // Perform various operations to collect metrics
        performanceTest.measurePageLoadTime(driver, "Home Page");
        performanceTest.measurePageLoadTime(driver, "Login Page");
        performanceTest.measurePageLoadTime(driver, "Product Page");
        
        // Generate comprehensive report
        String report = performanceTest.generatePerformanceReport();
        System.out.println("Performance Report:");
        System.out.println(report);
        
        Assert.assertNotNull(report, "Performance report should be generated");
        Assert.assertTrue(report.contains("Home Page"), "Report should contain Home Page metrics");
    }
}
```

### 4. **Caching Examples**

#### Caching Usage Examples
```java
package org.example.examples;

import org.example.utils.TestDataCache;
import org.example.utils.TestDataManager;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import java.util.Map;

public class CachingExamples {
    
    @BeforeMethod
    public void setUp() {
        // Clear cache before each test
        TestDataCache.clearCache();
    }
    
    @Test(description = "Basic caching example")
    public void testBasicCaching() {
        // Cache user data
        Map<String, String> userData = TestDataManager.generateUserData();
        TestDataCache.cacheData("test_user_001", userData, 24);
        
        // Retrieve cached data
        Map<String, String> cachedData = TestDataCache.getCachedData("test_user_001", Map.class);
        
        // Verify data is cached correctly
        Assert.assertEquals(cachedData.get("email"), userData.get("email"));
        Assert.assertEquals(cachedData.get("firstName"), userData.get("firstName"));
    }
    
    @Test(description = "Cache with custom TTL")
    public void testCustomTTL() {
        // Cache data with 1 hour TTL
        Map<String, String> userData = TestDataManager.generateUserData();
        TestDataCache.cacheData("short_lived_user", userData, 1);
        
        // Verify data exists
        Assert.assertTrue(TestDataCache.hasCachedData("short_lived_user"));
        
        // Retrieve data
        Map<String, String> cachedData = TestDataCache.getCachedData("short_lived_user", Map.class);
        Assert.assertNotNull(cachedData);
    }
    
    @Test(description = "Get or create cached data")
    public void testGetOrCreateData() {
        // First call - should create new data
        Map<String, String> userData1 = TestDataCache.getOrCreateUserData("user_001");
        Assert.assertNotNull(userData1);
        
        // Second call - should return cached data
        Map<String, String> userData2 = TestDataCache.getOrCreateUserData("user_001");
        Assert.assertEquals(userData1.get("email"), userData2.get("email"));
    }
    
    @Test(description = "Cache statistics")
    public void testCacheStatistics() {
        // Add some data to cache
        TestDataCache.cacheData("user1", TestDataManager.generateUserData(), 24);
        TestDataCache.cacheData("user2", TestDataManager.generateUserData(), 24);
        TestDataCache.cacheData("product1", TestDataManager.generateProductData(), 12);
        
        // Get cache statistics
        TestDataCache.CacheStats stats = TestDataCache.getCacheStats();
        
        System.out.println("Cache Statistics:");
        System.out.println("Total entries: " + stats.getTotalEntries());
        System.out.println("Valid entries: " + stats.getValidEntries());
        System.out.println("Expired entries: " + stats.getExpiredEntries());
        System.out.println("Disk entries: " + stats.getDiskEntries());
        
        Assert.assertTrue(stats.getTotalEntries() >= 3, "Should have at least 3 cached entries");
    }
    
    @Test(description = "Cache cleanup")
    public void testCacheCleanup() {
        // Add data with short TTL
        TestDataCache.cacheData("temp_user", TestDataManager.generateUserData(), 1);
        
        // Verify data exists
        Assert.assertTrue(TestDataCache.hasCachedData("temp_user"));
        
        // Remove specific data
        TestDataCache.removeCachedData("temp_user");
        Assert.assertFalse(TestDataCache.hasCachedData("temp_user"));
        
        // Clear all cache
        TestDataCache.clearCache();
        TestDataCache.CacheStats stats = TestDataCache.getCacheStats();
        Assert.assertEquals(stats.getTotalEntries(), 0, "Cache should be empty");
    }
    
    @AfterMethod
    public void tearDown() {
        // Clean up cache after each test
        TestDataCache.clearCache();
    }
}
```

### 5. **Retry Mechanism Examples**

#### Retry Usage Examples
```java
package org.example.examples;

import org.example.core.RetryAnalyzer;
import org.example.core.TestBase;
import org.example.pages.LoginPage;
import org.testng.annotations.Test;
import org.testng.Assert;

public class RetryExamples extends TestBase {
    
    private LoginPage loginPage;
    
    @Test(retryAnalyzer = RetryAnalyzer.class, description = "Test with retry mechanism")
    public void testLoginWithRetry() {
        loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage();
        
        // This test might fail due to timing issues, but will be retried
        loginPage.login("test@example.com", "password123");
        
        // Verify login success
        Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful");
    }
    
    @Test(retryAnalyzer = RetryAnalyzer.class, description = "Flaky element interaction test")
    public void testFlakyElementInteraction() {
        loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage();
        
        // This interaction might fail due to element not being ready
        loginPage.enterEmail("test@example.com");
        loginPage.enterPassword("password123");
        loginPage.clickLoginButton();
        
        // Verify the action completed
        Assert.assertTrue(loginPage.isLoginButtonClicked(), "Login button should be clicked");
    }
    
    @Test(retryAnalyzer = RetryAnalyzer.class, description = "Network-dependent test")
    public void testNetworkDependentOperation() {
        // This test might fail due to network issues
        driver.get("https://demo.nopcommerce.com");
        
        // Verify page loaded
        Assert.assertTrue(driver.getTitle().contains("nopCommerce"), 
                         "Page title should contain 'nopCommerce'");
    }
}
```

## 🔧 **Configuration Examples**

### 1. **Environment-Specific Configuration**

#### Development Configuration
```properties
# config-dev.properties
browser=chrome
headless=false
base.url=https://dev.example.com
log.level=DEBUG
implicit.wait=10
explicit.wait=20
timeout=30

# API Configuration
api.base.url=https://dev-api.example.com
api.key=dev-api-key
api.timeout=30000

# Performance Configuration
performance.threshold.ms=10000
performance.sla.page.load=5000
performance.sla.element.interaction=2000

# Visual Testing Configuration
applitools.api.key=dev-applitools-key
applitools.server.url=https://eyesapi.applitools.com
applitools.app.name=E-commerce Dev
applitools.batch.name=Dev Tests

# Cache Configuration
cache.ttl.hours=1
cache.max.size=100
cache.cleanup.interval.hours=1

# Retry Configuration
retry.count=3
retry.interval=2000
```

#### Staging Configuration
```properties
# config-staging.properties
browser=firefox
headless=true
base.url=https://staging.example.com
log.level=INFO
implicit.wait=10
explicit.wait=20
timeout=30

# API Configuration
api.base.url=https://staging-api.example.com
api.key=staging-api-key
api.timeout=30000

# Performance Configuration
performance.threshold.ms=8000
performance.sla.page.load=4000
performance.sla.element.interaction=1500

# Visual Testing Configuration
applitools.api.key=staging-applitools-key
applitools.server.url=https://eyesapi.applitools.com
applitools.app.name=E-commerce Staging
applitools.batch.name=Staging Tests

# Cache Configuration
cache.ttl.hours=12
cache.max.size=500
cache.cleanup.interval.hours=2

# Retry Configuration
retry.count=2
retry.interval=1000
```

#### Production Configuration
```properties
# config-prod.properties
browser=chrome
headless=true
base.url=https://prod.example.com
log.level=WARN
implicit.wait=5
explicit.wait=10
timeout=15

# API Configuration
api.base.url=https://api.example.com
api.key=${API_KEY}
api.timeout=15000

# Performance Configuration
performance.threshold.ms=5000
performance.sla.page.load=3000
performance.sla.element.interaction=1000

# Visual Testing Configuration
applitools.api.key=${APPLITOOLS_API_KEY}
applitools.server.url=https://eyesapi.applitools.com
applitools.app.name=E-commerce Production
applitools.batch.name=Production Tests

# Cache Configuration
cache.ttl.hours=24
cache.max.size=1000
cache.cleanup.interval.hours=4

# Retry Configuration
retry.count=1
retry.interval=500
```

### 2. **Configuration Loading Example**
```java
package org.example.examples;

import org.example.utils.ConfigManager;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

public class ConfigurationExamples {
    
    @BeforeClass
    public void setUp() {
        // Load environment-specific configuration
        String environment = System.getProperty("env", "dev");
        ConfigManager.loadEnvironmentConfiguration(environment);
    }
    
    @Test(description = "Test configuration loading")
    public void testConfigurationLoading() {
        // Verify configuration is loaded
        String browser = ConfigManager.getProperty("browser");
        String baseUrl = ConfigManager.getProperty("base.url");
        String logLevel = ConfigManager.getProperty("log.level");
        
        System.out.println("Configuration loaded:");
        System.out.println("Browser: " + browser);
        System.out.println("Base URL: " + baseUrl);
        System.out.println("Log Level: " + logLevel);
        
        Assert.assertNotNull(browser, "Browser should be configured");
        Assert.assertNotNull(baseUrl, "Base URL should be configured");
        Assert.assertNotNull(logLevel, "Log level should be configured");
    }
    
    @Test(description = "Test environment variable loading")
    public void testEnvironmentVariableLoading() {
        // Test loading from environment variables
        String apiKey = ConfigManager.getProperty("api.key");
        String applitoolsKey = ConfigManager.getProperty("applitools.api.key");
        
        System.out.println("API Key: " + (apiKey != null ? "Set" : "Not set"));
        System.out.println("Applitools Key: " + (applitoolsKey != null ? "Set" : "Not set"));
    }
}
```

## 🚀 **Running Examples**

### 1. **Command Line Examples**

#### Basic Test Execution
```bash
# Run all tests
mvn test

# Run specific feature
mvn test -Dcucumber.features="src/test/resources/features/Login.feature"

# Run with specific tags
mvn test -Dcucumber.filter.tags="@smoke"

# Run in parallel
mvn test -Dparallel=true -Dthreads=4

# Run with specific browser
mvn test -Dbrowser=firefox

# Run with headless mode
mvn test -Dheadless=true
```

#### Advanced Test Execution
```bash
# Run with custom configuration
mvn test -Dconfig.file=config-staging.properties

# Run with environment variables
mvn test -DAPI_KEY=your-api-key -DAPPLITOOLS_API_KEY=your-applitools-key

# Run with debug logging
mvn test -Dlogback.level=DEBUG

# Run with performance monitoring
mvn test -Dperformance.monitoring=true

# Run with visual testing
mvn test -Dvisual.testing=true

# Run with retry mechanism
mvn test -Dretry.count=3
```

### 2. **TestNG XML Examples**

#### Basic TestNG Configuration
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Test Suite" parallel="methods" thread-count="4">
    <test name="UI Tests">
        <classes>
            <class name="org.example.tests.UserLoginTest"/>
            <class name="org.example.tests.UserRegistrationTest"/>
            <class name="org.example.tests.HomePageTest"/>
        </classes>
    </test>
    
    <test name="API Tests">
        <classes>
            <class name="org.example.api.UserApiTest"/>
            <class name="org.example.api.ProductApiTest"/>
        </classes>
    </test>
    
    <test name="Performance Tests">
        <classes>
            <class name="org.example.performance.EcommercePerformanceTest"/>
        </classes>
    </test>
</suite>
```

#### Advanced TestNG Configuration
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Complete Test Suite" parallel="tests" thread-count="3">
    
    <parameter name="browser" value="chrome"/>
    <parameter name="headless" value="true"/>
    <parameter name="base.url" value="https://demo.nopcommerce.com"/>
    
    <test name="Smoke Tests" parallel="methods" thread-count="2">
        <groups>
            <run>
                <include name="smoke"/>
            </run>
        </groups>
        <classes>
            <class name="org.example.tests.SmokeTestSuite"/>
        </classes>
    </test>
    
    <test name="Regression Tests" parallel="methods" thread-count="4">
        <groups>
            <run>
                <include name="regression"/>
                <exclude name="slow"/>
            </run>
        </groups>
        <classes>
            <class name="org.example.tests.RegressionTestSuite"/>
        </classes>
    </test>
    
    <test name="API Tests" parallel="methods" thread-count="2">
        <classes>
            <class name="org.example.api.UserApiTest"/>
            <class name="org.example.api.ProductApiTest"/>
        </classes>
    </test>
    
    <test name="Visual Tests" parallel="methods" thread-count="1">
        <classes>
            <class name="org.example.visual.EcommerceVisualTest"/>
        </classes>
    </test>
    
    <test name="Performance Tests" parallel="methods" thread-count="1">
        <classes>
            <class name="org.example.performance.EcommercePerformanceTest"/>
        </classes>
    </test>
</suite>
```

These examples provide comprehensive coverage of all framework features and demonstrate best practices for implementation. Use these as templates and adapt them to your specific testing needs. 