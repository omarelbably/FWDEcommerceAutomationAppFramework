# User Guide - Test Automation Framework

## 🚀 **Quick Start Guide**

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher
- Git
- Chrome/Firefox browser
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Installation
```bash
# Clone the repository
git clone <repository-url>
cd FWDEcommerceAutomationApp

# Build the project
mvn clean install

# Run smoke tests
mvn test -Dcucumber.filter.tags="@smoke"
```

## 📚 **Getting Started**

### 1. **Project Structure**
```
FWDEcommerceAutomationApp/
├── src/
│   ├── main/
│   │   ├── java/org/example/
│   │   │   ├── core/           # Core framework classes
│   │   │   ├── utils/          # Utility classes
│   │   │   └── config/         # Configuration management
│   │   └── resources/
│   │       ├── config.properties
│   │       └── logback.xml
│   └── test/
│       ├── java/org/example/
│       │   ├── pages/          # Page Object Models
│       │   ├── stepDefs/       # Step Definitions
│       │   ├── api/            # API Testing
│       │   ├── visual/         # Visual Testing
│       │   └── performance/    # Performance Testing
│       └── resources/
│           └── features/       # Cucumber Feature Files
├── pom.xml
├── Dockerfile
├── docker-compose.yml
└── README.md
```

### 2. **Configuration Setup**
Edit `src/main/resources/config.properties`:
```properties
# Browser Configuration
browser=chrome
headless=false
implicit.wait=10
explicit.wait=20

# Application Configuration
base.url=https://demo.nopcommerce.com
timeout=30

# API Configuration (if needed)
api.base.url=https://api.example.com
api.key=your-api-key

# Visual Testing Configuration (if needed)
applitools.api.key=your-applitools-key
```

## 🧪 **Writing Your First Test**

### 1. **Create a Feature File**
Create `src/test/resources/features/F10_MyFirstTest.feature`:
```gherkin
@smoke @ui
Feature: My First Test
  As a test automation engineer
  I want to write my first test
  So that I can validate the framework

  @chrome @valid-data
  Scenario: Navigate to home page
    Given user opens the browser
    When user navigates to home page
    Then home page should be displayed
    And page title should be correct
```

### 2. **Create Page Object**
Create `src/test/java/org/example/pages/HomePage.java`:
```java
package org.example.pages;

import org.example.core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {
    
    @FindBy(css = "h1")
    private WebElement pageTitle;
    
    @FindBy(css = ".header-logo")
    private WebElement logo;
    
    public HomePage(WebDriver driver) {
        super(driver);
    }
    
    public void navigateToHomePage() {
        driver.get(getBaseUrl());
        waitForElementVisible(pageTitle);
    }
    
    public String getPageTitle() {
        return getElementText(pageTitle);
    }
    
    public boolean isLogoDisplayed() {
        return isElementDisplayed(logo);
    }
    
    public String getExpectedTitle() {
        return "nopCommerce demo store";
    }
}
```

### 3. **Create Step Definitions**
Create `src/test/java/org/example/stepDefs/MyFirstTestStepDef.java`:
```java
package org.example.stepDefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.pages.HomePage;
import org.example.core.TestBase;
import org.testng.Assert;

public class MyFirstTestStepDef extends TestBase {
    
    private HomePage homePage;
    
    @Given("user opens the browser")
    public void userOpensTheBrowser() {
        // Browser is already opened in TestBase.setUp()
        homePage = new HomePage(driver);
    }
    
    @When("user navigates to home page")
    public void userNavigatesToHomePage() {
        homePage.navigateToHomePage();
    }
    
    @Then("home page should be displayed")
    public void homePageShouldBeDisplayed() {
        Assert.assertTrue(homePage.isLogoDisplayed(), 
                         "Home page logo should be displayed");
    }
    
    @Then("page title should be correct")
    public void pageTitleShouldBeCorrect() {
        String actualTitle = homePage.getPageTitle();
        String expectedTitle = homePage.getExpectedTitle();
        Assert.assertEquals(actualTitle, expectedTitle, 
                           "Page title should match expected title");
    }
}
```

### 4. **Run the Test**
```bash
# Run specific feature
mvn test -Dcucumber.features="src/test/resources/features/F10_MyFirstTest.feature"

# Run with specific tags
mvn test -Dcucumber.filter.tags="@smoke"

# Run in parallel
mvn test -Dparallel=true -Dthreads=4
```

## 🔧 **Advanced Usage**

### 1. **Using Test Data Management**

#### Generate Dynamic Test Data
```java
import org.example.utils.TestDataManager;

public class RegistrationStepDef extends TestBase {
    
    @When("user fills registration form with dynamic data")
    public void userFillsRegistrationFormWithDynamicData() {
        Map<String, String> userData = TestDataManager.generateUserData();
        
        registrationPage.enterFirstName(userData.get("firstName"));
        registrationPage.enterLastName(userData.get("lastName"));
        registrationPage.enterEmail(userData.get("email"));
        registrationPage.enterPassword(userData.get("password"));
    }
}
```

#### Using Cached Test Data
```java
import org.example.utils.TestDataCache;

public class LoginStepDef extends TestBase {
    
    @When("user logs in with cached credentials")
    public void userLogsInWithCachedCredentials() {
        Map<String, String> userData = TestDataCache.getOrCreateUserData("test_user_001");
        
        loginPage.enterEmail(userData.get("email"));
        loginPage.enterPassword(userData.get("password"));
        loginPage.clickLoginButton();
    }
}
```

### 2. **API Testing**

#### Create API Test Class
```java
package org.example.api;

import org.example.api.ApiTestBase;
import org.example.utils.TestDataManager;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.Map;

public class UserApiTest extends ApiTestBase {
    
    @Test
    public void testCreateUser() {
        Map<String, String> userData = TestDataManager.generateUserData();
        
        Response response = post("/users", userData);
        
        validateStatusCode(response, 201);
        validateResponseContainsField(response, "id");
    }
    
    @Test
    public void testGetUser() {
        Response response = get("/users/123");
        
        validateStatusCode(response, 200);
        validateResponseFieldValue(response, "id", "123");
    }
    
    @Test
    public void testUpdateUser() {
        Map<String, String> updateData = Map.of("name", "Updated Name");
        
        Response response = put("/users/123", updateData);
        
        validateStatusCode(response, 200);
        validateResponseFieldValue(response, "name", "Updated Name");
    }
    
    @Test
    public void testDeleteUser() {
        Response response = delete("/users/123");
        
        validateStatusCode(response, 204);
    }
}
```

### 3. **Visual Testing**

#### Create Visual Test Class
```java
package org.example.visual;

import org.example.visual.VisualTestBase;
import org.example.core.TestBase;
import org.testng.annotations.Test;
import org.testng.Assert;

public class HomePageVisualTest extends TestBase {
    
    private VisualTestBase visualTest;
    
    @Test
    public void testHomePageVisual() {
        visualTest = new VisualTestBase();
        visualTest.initializeEyes(driver);
        visualTest.openEyes("Home Page Visual Test", "1920x1080");
        
        // Navigate to home page
        driver.get("https://demo.nopcommerce.com");
        
        // Take full page screenshot
        visualTest.takeFullPageScreenshot("Home Page");
        
        // Close eyes and validate
        boolean result = visualTest.closeEyes();
        Assert.assertTrue(result, "Visual test failed");
    }
    
    @Test
    public void testHeaderElementVisual() {
        visualTest = new VisualTestBase();
        visualTest.initializeEyes(driver);
        visualTest.openEyes("Header Element Test", "1920x1080");
        
        driver.get("https://demo.nopcommerce.com");
        
        // Take screenshot of specific element
        WebElement header = driver.findElement(By.cssSelector(".header"));
        visualTest.takeElementScreenshot("Header Element", header);
        
        boolean result = visualTest.closeEyes();
        Assert.assertTrue(result, "Header visual test failed");
    }
}
```

### 4. **Performance Testing**

#### Create Performance Test Class
```java
package org.example.performance;

import org.example.performance.PerformanceTestBase;
import org.example.core.TestBase;
import org.testng.annotations.Test;
import org.testng.Assert;

public class HomePagePerformanceTest extends TestBase {
    
    private PerformanceTestBase performanceTest;
    
    @Test
    public void testHomePageLoadPerformance() {
        performanceTest = new PerformanceTestBase();
        
        long loadTime = performanceTest.measurePageLoadTime(driver, "Home Page");
        
        // Validate against SLA
        boolean meetsSLA = performanceTest.meetsSLA("page_load_Home Page", 3000);
        Assert.assertTrue(meetsSLA, "Page load time exceeds SLA");
        
        // Get performance statistics
        PerformanceTestBase.PerformanceStats stats = 
            performanceTest.getPerformanceStats("page_load_Home Page");
        
        System.out.println("Average load time: " + stats.getMean() + " ms");
        System.out.println("95th percentile: " + stats.getPercentile95() + " ms");
    }
    
    @Test
    public void testElementInteractionPerformance() {
        performanceTest = new PerformanceTestBase();
        
        long interactionTime = performanceTest.measureElementInteraction("Login Button", () -> {
            WebElement loginButton = driver.findElement(By.cssSelector(".ico-login"));
            loginButton.click();
        });
        
        Assert.assertTrue(interactionTime < 1000, "Element interaction too slow");
    }
}
```

### 5. **Retry Mechanism for Flaky Tests**

#### Apply Retry to Test Methods
```java
@Test(retryAnalyzer = RetryAnalyzer.class)
public void testFlakyOperation() {
    // Test implementation that might be flaky
    WebElement element = driver.findElement(By.id("dynamic-element"));
    element.click();
}
```

#### Custom Retry Configuration
```properties
# In config.properties
retry.count=3
retry.interval=2000
```

## 🏷️ **Test Tagging Strategy**

### Tag Categories
```gherkin
@smoke @critical @registration @chrome
Feature: User Registration
  As a new user
  I want to register an account
  So that I can access the application

  @valid-data @positive
  Scenario: Successful registration
    Given user is on registration page
    When user enters valid registration details
    And user submits the registration form
    Then user should be registered successfully

  @invalid-data @negative
  Scenario: Registration with invalid email
    Given user is on registration page
    When user enters invalid email format
    And user submits the registration form
    Then error message should be displayed
```

### Test Execution by Tags
```bash
# Run smoke tests only
mvn test -Dcucumber.filter.tags="@smoke"

# Run critical tests in Chrome
mvn test -Dcucumber.filter.tags="@critical and @chrome"

# Run API tests excluding performance
mvn test -Dcucumber.filter.tags="@api and not @performance"

# Run tests for specific feature
mvn test -Dcucumber.filter.tags="@registration"
```

## 📊 **Reporting and Monitoring**

### 1. **Allure Reports**
```bash
# Generate Allure report
mvn allure:report

# Open Allure report
mvn allure:serve
```

### 2. **Custom Reporting**
```java
@AfterMethod
public void afterMethod(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
        // Capture screenshot
        captureScreenshot(result.getName());
        
        // Log additional information
        logger.error("Test failed: " + result.getName());
        logger.error("Exception: " + result.getThrowable().getMessage());
    }
}
```

### 3. **Performance Reporting**
```java
@Test
public void testWithPerformanceReporting() {
    PerformanceTestBase performanceTest = new PerformanceTestBase();
    
    // Run test operations
    performanceTest.timeOperation("user_registration", () -> {
        // Registration steps
    });
    
    // Generate performance report
    String report = performanceTest.generatePerformanceReport();
    System.out.println(report);
}
```

## 🐳 **Docker Integration**

### 1. **Local Docker Execution**
```bash
# Build Docker image
docker build -t test-automation .

# Run tests in Docker
docker run test-automation

# Run with specific tags
docker run test-automation mvn test -Dcucumber.filter.tags="@smoke"
```

### 2. **Docker Compose**
```bash
# Start all services
docker-compose up -d

# Run tests
docker-compose run test-automation mvn test

# Access reports
open http://localhost:5050  # Allure Reports
open http://localhost:3000  # Grafana Dashboard
```

### 3. **Selenium Grid**
```bash
# Start Selenium Grid
docker-compose up -d selenium-hub chrome-node firefox-node

# Run tests against grid
mvn test -Dselenium.grid.url=http://localhost:4444
```

## 🔧 **Configuration Management**

### 1. **Environment-Specific Configuration**
```properties
# Development
browser=chrome
headless=false
base.url=https://dev.example.com
log.level=DEBUG

# Staging
browser=firefox
headless=true
base.url=https://staging.example.com
log.level=INFO

# Production
browser=chrome
headless=true
base.url=https://prod.example.com
log.level=WARN
```

### 2. **Dynamic Configuration**
```java
// Override configuration at runtime
System.setProperty("browser", "firefox");
System.setProperty("headless", "true");
System.setProperty("base.url", "https://custom.example.com");
```

## 🚀 **CI/CD Integration**

### 1. **GitHub Actions**
```yaml
# .github/workflows/test.yml
name: Test Automation
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
      - name: Run Tests
        run: mvn test -Dcucumber.filter.tags="@smoke"
      - name: Generate Report
        run: mvn allure:report
```

### 2. **Jenkins Pipeline**
```groovy
pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test -Dcucumber.filter.tags="@smoke"'
            }
        }
        stage('Report') {
            steps {
                sh 'mvn allure:report'
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/allure-reports',
                    reportFiles: 'index.html',
                    reportName: 'Allure Report'
                ])
            }
        }
    }
}
```

## 🛠️ **Troubleshooting**

### 1. **Common Issues**

#### WebDriver Issues
```bash
# Update WebDriverManager
mvn clean install -U

# Check browser compatibility
mvn test -Dbrowser=chrome -Dwebdriver.chrome.driver=/path/to/chromedriver
```

#### Test Failures
```bash
# Run with debug logging
mvn test -Dlogback.level=DEBUG

# Run single test
mvn test -Dtest=MyTestClass#testMethod

# Run with retry
mvn test -Dretry.count=3
```

#### Performance Issues
```bash
# Increase memory
mvn test -Xmx2g -Xms1g

# Run in parallel
mvn test -Dparallel=true -Dthreads=2
```

### 2. **Debug Mode**
```java
// Enable debug logging
@BeforeMethod
public void setUp() {
    System.setProperty("logback.level", "DEBUG");
    // ... rest of setup
}
```

### 3. **Screenshot Capture**
```java
// Manual screenshot capture
@AfterMethod
public void captureScreenshot(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
        String screenshotPath = "target/screenshots/" + 
                               result.getName() + "_" + 
                               System.currentTimeMillis() + ".png";
        captureScreenshot(screenshotPath);
    }
}
```

## 📚 **Best Practices**

### 1. **Page Object Model**
- Keep page objects focused on single page
- Use meaningful method names
- Implement proper encapsulation
- Extend BasePage for common functionality

### 2. **Test Data Management**
- Use dynamic data generation
- Implement proper caching strategies
- Clean up test data after tests
- Use data providers for multiple scenarios

### 3. **Test Organization**
- Use descriptive feature names
- Implement proper tagging strategy
- Group related scenarios
- Maintain test independence

### 4. **Error Handling**
- Implement proper exception handling
- Use meaningful error messages
- Capture screenshots on failures
- Log relevant information

### 5. **Performance**
- Monitor test execution time
- Use parallel execution
- Implement smart waits
- Optimize element locators

This user guide provides comprehensive information on how to use the enhanced test automation framework effectively. Follow these guidelines to create robust, maintainable, and scalable test automation solutions. 