# E-commerce Test Automation Framework

A robust, scalable Selenium Cucumber test automation framework for e-commerce applications with modern best practices and comprehensive reporting.

## 🚀 Features

- **Modern Selenium 4.16+** with WebDriverManager for automatic driver management
- **Cucumber 7.15+** for BDD testing with enhanced reporting
- **TestNG 7.8+** for test execution with parallel support
- **Java 21** with modern language features (switch expressions, pattern matching)
- **Allure Reporting** for beautiful, interactive test reports
- **Multi-browser support** (Chrome, Firefox, Edge)
- **Parallel execution** for faster test runs
- **Comprehensive logging** with Logback
- **Automatic screenshots** on test failures
- **Test data management** with Faker library
- **Configuration management** with properties files
- **Page Object Model** with enhanced base classes
- **Retry mechanism** for flaky tests
- **CI/CD ready** with Maven

## 📁 Project Structure

```
src/
├── main/
│   ├── resources/
│   │   ├── config.properties          # Configuration settings
│   │   ├── logback.xml               # Logging configuration
│   │   └── features/                 # Cucumber feature files
│   └── java/
│       └── org/example/
│           ├── core/                 # Core framework classes
│           │   ├── TestBase.java     # Base test configuration
│           │   └── BasePage.java     # Base page object
│           ├── pages/                # Page Object classes
│           ├── stepDefs/             # Cucumber step definitions
│           ├── testRunner/           # Test runners
│           ├── listeners/            # TestNG listeners
│           └── utils/                # Utility classes
└── test/
    └── java/
        └── org/example/
            └── [test classes]
```

## 🛠️ Setup & Installation

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- Git

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd FWDEcommerceAutomationApp
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Run tests**
   ```bash
   # Run all tests
   mvn test
   
   # Run with specific browser
   mvn test -Dbrowser=firefox
   
   # Run with headless mode
   mvn test -Dbrowser=chrome -Dheadless=true
   
   # Run specific tags
   mvn test -Dcucumber.filter.tags="@smoke"
   ```

## 🎯 Configuration

### Browser Configuration
Edit `src/main/resources/config.properties`:

```properties
# Browser settings
browser=chrome
headless=false
window.size=1920x1080

# Timeouts
implicit.wait=10
explicit.wait=20
page.load.timeout=30

# URLs
base.url=https://demo.nopcommerce.com/
```

### Parallel Execution
Configure in `testng.xml`:

```xml
<suite name="Test Suite" parallel="methods" thread-count="4">
```

## 📊 Reporting

### Allure Reports
```bash
# Generate Allure report
mvn allure:report

# Serve Allure report locally
mvn allure:serve
```

### Cucumber Reports
Reports are automatically generated in:
- `target/cucumber-reports/cucumber.html`
- `target/cucumber-reports/cucumber.json`

### Screenshots
Failed test screenshots are saved in:
- `target/screenshots/`

## 🔧 Framework Components

### 1. TestBase Class
Provides common test setup and teardown functionality:
- WebDriver initialization
- Browser configuration
- Timeout management
- Configuration loading

### 2. BasePage Class
Enhanced page object base with:
- Common wait methods
- Element interaction utilities
- JavaScript executor methods
- Screenshot capabilities
- Error handling

### 3. TestDataManager
Dynamic test data generation:
- User registration data
- Product information
- Address details
- Search terms
- Validation data

### 4. Hooks
Enhanced test lifecycle management:
- Automatic screenshots on failure
- Logging integration
- Error handling
- Driver management

## 🧪 Writing Tests

### Feature File Example
```gherkin
@smoke
Feature: User Registration
  Scenario: Successful user registration
    Given user is on registration page
    When user fills registration form with valid data
    And user submits the form
    Then registration should be successful
```

### Step Definition Example
```java
@Given("user is on registration page")
public void userIsOnRegistrationPage() {
    registrationPage = new RegistrationPage(driver);
    registrationPage.navigateToRegistrationPage();
}
```

### Page Object Example
```java
public class RegistrationPage extends BasePage {
    
    @FindBy(id = "register-button")
    private WebElement registerButton;
    
    public RegistrationPage(WebDriver driver) {
        super(driver);
    }
    
    public void fillRegistrationForm(Map<String, String> userData) {
        sendKeysToElement(firstNameField, userData.get("firstName"));
        sendKeysToElement(lastNameField, userData.get("lastName"));
        sendKeysToElement(emailField, userData.get("email"));
        sendKeysToElement(passwordField, userData.get("password"));
    }
}
```

## 🚀 Best Practices

### 1. Page Object Model
- Keep page objects focused on single responsibility
- Use meaningful element names
- Implement proper wait strategies
- Handle dynamic elements appropriately

### 2. Test Data Management
- Use TestDataManager for dynamic data
- Avoid hardcoded test data
- Implement data-driven testing where appropriate
- Use Faker for realistic test data

### 3. Error Handling
- Implement proper exception handling
- Use soft assertions for multiple validations
- Add meaningful error messages
- Implement retry mechanisms for flaky tests

### 4. Logging
- Use appropriate log levels
- Log important test steps
- Include relevant context in log messages
- Configure log rotation

## 🔄 CI/CD Integration

### GitHub Actions Example
```yaml
name: Test Automation
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 11
        uses: actions/setup-java@v2
        with:
          java-version: '11'
      - name: Run tests
        run: mvn test
      - name: Generate Allure report
        run: mvn allure:report
      - name: Upload Allure report
        uses: actions/upload-artifact@v2
        with:
          name: allure-report
          path: target/site/allure-maven-plugin/
```

## 📈 Performance Optimization

### 1. Parallel Execution
- Configure appropriate thread count
- Use thread-safe page objects
- Avoid shared state between tests

### 2. Browser Optimization
- Use headless mode for CI/CD
- Implement proper driver cleanup
- Configure browser options for performance

### 3. Test Data Optimization
- Use efficient data generation
- Implement data caching where appropriate
- Clean up test data after tests

## 🐛 Troubleshooting

### Common Issues

1. **Driver Issues**
   - Ensure WebDriverManager is properly configured
   - Check browser compatibility
   - Verify driver path configuration

2. **Element Not Found**
   - Implement proper wait strategies
   - Check element locators
   - Verify page load completion

3. **Test Failures**
   - Check logs for detailed error messages
   - Review screenshots for visual verification
   - Verify test data validity

## 📚 Additional Resources

- [Selenium Documentation](https://selenium.dev/documentation/)
- [Cucumber Documentation](https://cucumber.io/docs/)
- [TestNG Documentation](https://testng.org/doc/)
- [Allure Documentation](https://docs.qameta.io/allure/)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

