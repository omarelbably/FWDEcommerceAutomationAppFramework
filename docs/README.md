# Enhanced Test Automation Framework

## 🚀 **Overview**

The Enhanced Test Automation Framework is a comprehensive, enterprise-grade solution built with modern Java technologies and best practices. It provides a robust foundation for UI testing, API testing, visual testing, and performance monitoring.

### **Key Features**

- ✅ **Multi-Browser Support**: Chrome, Firefox, Safari, Edge
- ✅ **BDD with Cucumber**: Human-readable test scenarios
- ✅ **API Testing**: REST Assured integration
- ✅ **Visual Testing**: Applitools integration
- ✅ **Performance Testing**: Metrics collection and analysis
- ✅ **Smart Caching**: In-memory and file-based caching
- ✅ **Retry Mechanism**: Intelligent flaky test handling
- ✅ **Parallel Execution**: Multi-threaded test execution
- ✅ **Comprehensive Reporting**: Allure and custom reports
- ✅ **Docker Support**: Containerized execution
- ✅ **CI/CD Ready**: GitHub Actions integration

## 📚 **Documentation**

### **Getting Started**
- [Framework Overview](docs/FRAMEWORK_OVERVIEW.md) - Architecture and design principles
- [User Guide](docs/USER_GUIDE.md) - How to use the framework
- [API Reference](docs/API_REFERENCE.md) - Complete API documentation
- [Examples and Samples](docs/EXAMPLES_AND_SAMPLES.md) - Practical code examples

### **Advanced Topics**
- [Best Practices](docs/BEST_PRACTICES.md) - Coding standards and patterns
- [Troubleshooting Guide](docs/TROUBLESHOOTING_GUIDE.md) - Common issues and solutions
- [Team Training Guide](docs/TEAM_TRAINING_GUIDE.md) - Training materials and exercises

### **Configuration and Setup**
- [Tagging Strategy](src/test/resources/features/tagging-strategy.md) - Test organization
- [Test Data Management](src/test/resources/testdata/README.md) - Data organization
- [Monitoring Setup](monitoring/README.md) - Grafana dashboards and metrics

## 🛠️ **Quick Start**

### **Prerequisites**
- Java 21 or higher
- Maven 3.6 or higher
- Git
- Chrome/Firefox browser
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

### **Installation**
```bash
# Clone the repository
git clone <repository-url>
cd FWDEcommerceAutomationApp

# Build the project
mvn clean install

# Run smoke tests
mvn test -Dcucumber.filter.tags="@smoke"
```

### **Configuration**
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

### **1. Create Feature File**
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

### **2. Create Page Object**
```java
public class LoginPage extends BasePage {
    
    @FindBy(id = "email")
    private WebElement emailField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(id = "login-button")
    private WebElement loginButton;
    
    public LoginPage(WebDriver driver) {
        super(driver);
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
}
```

### **3. Create Step Definitions**
```java
public class LoginStepDef extends TestBase {
    
    private LoginPage loginPage;
    
    @Given("user is on login page")
    public void userIsOnLoginPage() {
        loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage();
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
        // Add assertions here
    }
}
```

### **4. Run the Test**
```bash
# Run specific feature
mvn test -Dcucumber.features="src/test/resources/features/Login.feature"

# Run with specific tags
mvn test -Dcucumber.filter.tags="@smoke"

# Run in parallel
mvn test -Dparallel=true -Dthreads=4
```

## 🔧 **Advanced Features**

### **API Testing**
```java
public class UserApiTest extends ApiTestBase {
    
    @Test
    public void testCreateUser() {
        Map<String, String> userData = TestDataManager.generateUserData();
        
        Response response = post("/users", userData);
        
        validateStatusCode(response, 201);
        validateResponseContainsField(response, "id");
    }
}
```

### **Visual Testing**
```java
public class HomePageVisualTest extends TestBase {
    
    @Test
    public void testHomePageVisual() {
        VisualTestBase visualTest = new VisualTestBase();
        visualTest.initializeEyes(driver);
        visualTest.openEyes("Home Page Test", "1920x1080");
        
        driver.get("https://demo.nopcommerce.com");
        visualTest.takeFullPageScreenshot("Home Page");
        
        boolean result = visualTest.closeEyes();
        Assert.assertTrue(result, "Visual test failed");
    }
}
```

### **Performance Testing**
```java
public class HomePagePerformanceTest extends TestBase {
    
    @Test
    public void testHomePageLoadPerformance() {
        PerformanceTestBase performanceTest = new PerformanceTestBase();
        
        long loadTime = performanceTest.measurePageLoadTime(driver, "Home Page");
        
        boolean meetsSLA = performanceTest.meetsSLA("page_load_Home Page", 3000);
        Assert.assertTrue(meetsSLA, "Page load time exceeds SLA");
    }
}
```

### **Caching**
```java
// Cache test data
Map<String, String> userData = TestDataManager.generateUserData();
TestDataCache.cacheData("user_001", userData, 24);

// Retrieve cached data
Map<String, String> cachedData = TestDataCache.getCachedData("user_001", Map.class);
```

## 🏷️ **Test Organization**

### **Tagging Strategy**
```gherkin
@smoke @ui @chrome
Feature: User Registration
  As a new user
  I want to register an account
  So that I can access the application

  @valid-data @positive
  Scenario: Successful registration
    Given user is on registration page
    When user fills registration form with valid data
    And user submits registration form
    Then user should be registered successfully

  @invalid-data @negative
  Scenario: Registration with invalid email
    Given user is on registration page
    When user enters invalid email format
    And user submits registration form
    Then error message should be displayed
```

### **Test Execution by Tags**
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

### **Allure Reports**
```bash
# Generate Allure report
mvn allure:report

# Open Allure report
mvn allure:serve
```

### **Custom Reporting**
```java
@AfterMethod
public void afterMethod(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
        captureScreenshot(result.getName());
        logger.error("Test failed: " + result.getName());
    }
}
```

### **Performance Reporting**
```java
@Test
public void testWithPerformanceReporting() {
    PerformanceTestBase performanceTest = new PerformanceTestBase();
    
    performanceTest.timeOperation("user_registration", () -> {
        // Registration steps
    });
    
    String report = performanceTest.generatePerformanceReport();
    System.out.println(report);
}
```

## 🐳 **Docker Integration**

### **Local Docker Execution**
```bash
# Build Docker image
docker build -t test-automation .

# Run tests in Docker
docker run test-automation

# Run with specific tags
docker run test-automation mvn test -Dcucumber.filter.tags="@smoke"
```

### **Docker Compose**
```bash
# Start all services
docker-compose up -d

# Run tests
docker-compose run test-automation mvn test

# Access reports
open http://localhost:5050  # Allure Reports
open http://localhost:3000  # Grafana Dashboard
```

## 🔄 **CI/CD Integration**

### **GitHub Actions**
```yaml
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

### **Jenkins Pipeline**
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

### **Common Issues**

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

### **Debug Mode**
```java
// Enable debug logging
@BeforeMethod
public void setUp() {
    System.setProperty("logback.level", "DEBUG");
    // ... rest of setup
}
```

## 📈 **Performance Optimization**

### **Element Locator Optimization**
```java
// ✅ Good - Use ID when available (fastest)
@FindBy(id = "login-button")
private WebElement loginButton;

// ✅ Good - Use CSS selectors (fast and readable)
@FindBy(css = ".login-form input[type='email']")
private WebElement emailField;

// ⚠️ Acceptable - Use XPath for complex scenarios
@FindBy(xpath = "//button[contains(@class, 'login') and text()='Sign In']")
private WebElement signInButton;
```

### **Wait Strategies**
```java
// ✅ Good - Use explicit waits for dynamic elements
public void clickLoginButton() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement button = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
    button.click();
}

// ❌ Avoid - Thread.sleep for waits
public void clickLoginButton() {
    try {
        Thread.sleep(3000); // Don't use fixed delays
        loginButton.click();
    } catch (InterruptedException e) {
        // Handle exception
    }
}
```

## 🔒 **Security Best Practices**

### **Configuration Management**
```java
// ✅ Good - Use environment variables for sensitive data
public class ConfigManager {
    public static String getApiKey() {
        String apiKey = System.getenv("API_KEY");
        if (apiKey == null) {
            throw new RuntimeException("API_KEY environment variable not set");
        }
        return apiKey;
    }
}

// ❌ Bad - Hardcode sensitive data
public class ConfigManager {
    public static String getApiKey() {
        return "sk-1234567890abcdef"; // Never hardcode secrets
    }
}
```

### **Test Data Security**
```java
// ✅ Good - Use test-specific credentials
public class TestDataManager {
    public static Map<String, String> generateTestUserData() {
        Faker faker = new Faker();
        Map<String, String> userData = new HashMap<>();
        userData.put("email", "test_" + System.currentTimeMillis() + "@example.com");
        userData.put("password", faker.internet().password(8, 12));
        return userData;
    }
}
```

## 📚 **Learning Resources**

### **Framework Documentation**
- [Framework Overview](docs/FRAMEWORK_OVERVIEW.md) - Start here to understand the architecture
- [User Guide](docs/USER_GUIDE.md) - Step-by-step usage instructions
- [API Reference](docs/API_REFERENCE.md) - Complete API documentation
- [Examples and Samples](docs/EXAMPLES_AND_SAMPLES.md) - Practical code examples

### **Best Practices**
- [Best Practices Guide](docs/BEST_PRACTICES.md) - Coding standards and patterns
- [Troubleshooting Guide](docs/TROUBLESHOOTING_GUIDE.md) - Common issues and solutions
- [Team Training Guide](docs/TEAM_TRAINING_GUIDE.md) - Training materials and exercises

### **Configuration and Setup**
- [Tagging Strategy](src/test/resources/features/tagging-strategy.md) - Test organization
- [Test Data Management](src/test/resources/testdata/README.md) - Data organization
- [Monitoring Setup](monitoring/README.md) - Grafana dashboards and metrics

## 🤝 **Contributing**

### **Development Setup**
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

### **Code Standards**
- Follow the [Best Practices Guide](docs/BEST_PRACTICES.md)
- Use meaningful commit messages
- Add appropriate documentation
- Include tests for new features

### **Testing**
- Run the full test suite before submitting
- Ensure new tests follow the established patterns
- Verify performance impact of changes

## 📞 **Support**

### **Getting Help**
1. Check the [Troubleshooting Guide](docs/TROUBLESHOOTING_GUIDE.md)
2. Review the [Examples and Samples](docs/EXAMPLES_AND_SAMPLES.md)
3. Search existing issues
4. Create a new issue with detailed information

### **Reporting Issues**
When reporting issues, include:
- Framework version
- Java version
- Browser version
- Error logs
- Steps to reproduce
- Environment details

### **Feature Requests**
- Use the issue tracker
- Provide detailed use cases
- Consider implementation complexity
- Suggest alternatives if applicable

## 📄 **License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 **Acknowledgments**

- [Selenium](https://selenium.dev/) - Web automation
- [Cucumber](https://cucumber.io/) - BDD framework
- [TestNG](https://testng.org/) - Testing framework
- [REST Assured](https://rest-assured.io/) - API testing
- [Applitools](https://applitools.com/) - Visual testing
- [Allure](https://allure.qameta.io/) - Test reporting
- [WebDriverManager](https://github.com/bonigarcia/webdrivermanager) - Driver management

---

**Happy Testing! 🚀**

For more information, visit the [documentation](docs/) or [create an issue](https://github.com/your-repo/issues) for support. 