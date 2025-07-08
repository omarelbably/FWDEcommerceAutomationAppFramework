# Test Tagging Strategy

## Overview
This document defines the tagging strategy for organizing and executing tests efficiently across different environments and scenarios.

## Tag Categories

### 1. **Test Type Tags**
- `@smoke` - Critical path tests that must pass for basic functionality
- `@regression` - Comprehensive tests covering all features
- `@api` - API testing scenarios
- `@ui` - User interface testing
- `@visual` - Visual regression testing
- `@performance` - Performance and load testing
- `@security` - Security testing scenarios
- `@accessibility` - Accessibility testing

### 2. **Feature Tags**
- `@registration` - User registration functionality
- `@login` - User authentication
- `@search` - Search functionality
- `@shopping` - Shopping cart and checkout
- `@payment` - Payment processing
- `@inventory` - Product inventory management
- `@user-management` - User profile and settings

### 3. **Environment Tags**
- `@dev` - Development environment tests
- `@staging` - Staging environment tests
- `@prod` - Production environment tests
- `@local` - Local development tests

### 4. **Priority Tags**
- `@critical` - Highest priority tests
- `@high` - High priority tests
- `@medium` - Medium priority tests
- `@low` - Low priority tests

### 5. **Browser Tags**
- `@chrome` - Chrome browser specific tests
- `@firefox` - Firefox browser specific tests
- `@safari` - Safari browser specific tests
- `@edge` - Edge browser specific tests
- `@mobile` - Mobile browser tests

### 6. **Data Tags**
- `@valid-data` - Tests with valid test data
- `@invalid-data` - Tests with invalid test data
- `@boundary` - Boundary value testing
- `@negative` - Negative testing scenarios

## Usage Examples

### Feature File Example
```gherkin
@smoke @registration @critical
Feature: User Registration
  As a new user
  I want to register an account
  So that I can access the application

  @valid-data @chrome
  Scenario: Successful registration with valid data
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

### Test Execution Examples

```bash
# Run smoke tests only
mvn test -Dcucumber.filter.tags="@smoke"

# Run critical tests in Chrome
mvn test -Dcucumber.filter.tags="@critical and @chrome"

# Run API tests excluding performance
mvn test -Dcucumber.filter.tags="@api and not @performance"

# Run registration tests with valid data
mvn test -Dcucumber.filter.tags="@registration and @valid-data"

# Run tests for specific environment
mvn test -Dcucumber.filter.tags="@staging and @regression"
```

## CI/CD Integration

### GitHub Actions Matrix Strategy
```yaml
strategy:
  matrix:
    browser: [chrome, firefox]
    tags: [@smoke, @regression]
    environment: [@staging, @prod]
```

### Jenkins Pipeline
```groovy
pipeline {
    agent any
    stages {
        stage('Smoke Tests') {
            steps {
                sh 'mvn test -Dcucumber.filter.tags="@smoke"'
            }
        }
        stage('Regression Tests') {
            parallel {
                stage('Chrome Tests') {
                    steps {
                        sh 'mvn test -Dcucumber.filter.tags="@regression and @chrome"'
                    }
                }
                stage('Firefox Tests') {
                    steps {
                        sh 'mvn test -Dcucumber.filter.tags="@regression and @firefox"'
                    }
                }
            }
        }
    }
}
```

## Tag Management Best Practices

### 1. **Consistent Naming**
- Use lowercase with hyphens for multi-word tags
- Keep tags short and descriptive
- Use consistent prefixes for related tags

### 2. **Tag Combinations**
- Avoid conflicting tag combinations
- Use logical operators (and, or, not) effectively
- Document complex tag combinations

### 3. **Tag Maintenance**
- Regular review and cleanup of unused tags
- Update tags when features change
- Maintain tag documentation

### 4. **Performance Considerations**
- Limit the number of tags per scenario
- Use specific tags for targeted execution
- Avoid overly complex tag combinations

## Tag Reporting

### Allure Report Integration
```java
@CucumberOptions(
    plugin = {
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
        "pretty",
        "html:target/cucumber-reports"
    },
    tags = "@smoke"
)
```

### Custom Tag Reports
```java
// Generate tag-specific reports
@AfterSuite
public void generateTagReport() {
    // Custom reporting logic for tags
}
```

## Environment-Specific Tags

### Development
- `@dev-only` - Tests that should only run in development
- `@debug` - Debug-specific tests
- `@local-setup` - Local environment setup tests

### Staging
- `@staging-only` - Staging-specific tests
- `@integration` - Integration tests
- `@data-migration` - Data migration tests

### Production
- `@prod-only` - Production-specific tests
- `@monitoring` - Monitoring and alerting tests
- `@backup` - Backup and recovery tests

## Tag Validation

### Automated Tag Validation
```java
@Before
public void validateTags() {
    // Validate tag combinations
    // Check for required tags
    // Verify tag consistency
}
```

### Tag Documentation
- Maintain tag inventory
- Document tag purposes and usage
- Provide examples for each tag

This tagging strategy ensures efficient test organization, execution, and reporting across all testing phases and environments. 