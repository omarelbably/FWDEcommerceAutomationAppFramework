package org.example.pages;

import org.example.core.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class P02_login extends BasePage {
    
    private static final Logger logger = LoggerFactory.getLogger(P02_login.class);
    
    // Page elements
    @FindBy(css = "a[class=\"ico-login\"]")
    private WebElement loginPageLink;
    
    @FindBy(id = "Email")
    private WebElement emailField;
    
    @FindBy(id = "Password")
    private WebElement passwordField;
    
    @FindBy(xpath = "//button[@class=\"button-1 login-button\"]")
    private WebElement loginButton;
    
    @FindBy(xpath = "//div[@class=\"message-error validation-summary-errors\"]")
    private WebElement unsuccessMessage;
    
    @FindBy(css = "a[href=\"/customer/info\"]")
    private WebElement accountLink;
    
    @FindBy(css = ".welcome-message")
    private WebElement welcomeMessage;
    
    @FindBy(css = ".login-form")
    private WebElement loginForm;
    
    public P02_login(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
    
    // Navigation methods
    public void navigateToLoginPage() {
        logger.info("Navigating to login page");
        driver.get(getBaseUrl() + "/login");
        waitForElementVisible(loginForm);
        logger.info("Login page loaded successfully");
    }
    
    public void clickLoginPageLink() {
        logger.info("Clicking login page link");
        clickElement(loginPageLink);
        waitForElementVisible(loginForm);
        logger.info("Login page link clicked successfully");
    }
    
    // Form interaction methods
    public void enterEmail(String email) {
        logger.info("Entering email: {}", email);
        sendKeysToElement(emailField, email);
        logger.info("Email entered successfully");
    }
    
    public void enterPassword(String password) {
        logger.info("Entering password");
        sendKeysToElement(passwordField, password);
        logger.info("Password entered successfully");
    }
    
    public void clickLoginButton() {
        logger.info("Clicking login button");
        clickElement(loginButton);
        logger.info("Login button clicked successfully");
    }
    
    // Business logic methods
    public void loginSteps(String username, String password) {
        logger.info("Performing login steps with username: {}", username);
        enterEmail(username);
        enterPassword(password);
        logger.info("Login steps completed");
    }
    
    public void login(String username, String password) {
        logger.info("Logging in with username: {}", username);
        loginSteps(username, password);
        clickLoginButton();
        logger.info("Login completed");
    }
    
    public void loginWithRememberMe(String username, String password) {
        logger.info("Logging in with remember me option");
        loginSteps(username, password);
        // Add remember me checkbox logic if available
        clickLoginButton();
        logger.info("Login with remember me completed");
    }
    
    // Validation methods
    public boolean isLoginFormDisplayed() {
        return isElementDisplayed(loginForm);
    }
    
    public boolean isEmailFieldDisplayed() {
        return isElementDisplayed(emailField);
    }
    
    public boolean isPasswordFieldDisplayed() {
        return isElementDisplayed(passwordField);
    }
    
    public boolean isLoginButtonDisplayed() {
        return isElementDisplayed(loginButton);
    }
    
    public boolean isAccountLinkDisplayed() {
        return isElementDisplayed(accountLink);
    }
    
    public boolean isWelcomeMessageDisplayed() {
        return isElementDisplayed(welcomeMessage);
    }
    
    public boolean isUnsuccessMessageDisplayed() {
        return isElementDisplayed(unsuccessMessage);
    }
    
    // Getter methods
    public String getUnsuccessMessage() {
        return getElementText(unsuccessMessage);
    }
    
    public String getUnsuccessMessageColor() {
        return unsuccessMessage.getCssValue("color");
    }
    
    public String getWelcomeMessage() {
        return getElementText(welcomeMessage);
    }
    
    public String getEmailFieldValue() {
        return emailField.getAttribute("value");
    }
    
    public String getPasswordFieldValue() {
        return passwordField.getAttribute("value");
    }
    
    // Form validation methods
    public boolean isEmailFieldEnabled() {
        return emailField.isEnabled();
    }
    
    public boolean isPasswordFieldEnabled() {
        return passwordField.isEnabled();
    }
    
    public boolean isLoginButtonEnabled() {
        return loginButton.isEnabled();
    }
    
    // Form clearing methods
    public void clearLoginForm() {
        logger.info("Clearing login form");
        emailField.clear();
        passwordField.clear();
        logger.info("Login form cleared successfully");
    }
    
    public void clearEmailField() {
        logger.info("Clearing email field");
        emailField.clear();
        logger.info("Email field cleared successfully");
    }
    
    public void clearPasswordField() {
        logger.info("Clearing password field");
        passwordField.clear();
        logger.info("Password field cleared successfully");
    }
    
    // Wait methods
    public void waitForLoginForm() {
        logger.info("Waiting for login form to be visible");
        waitForElementVisible(loginForm);
        logger.info("Login form is visible");
    }
    
    public void waitForLoginSuccess() {
        logger.info("Waiting for login success");
        waitForElementVisible(accountLink);
        logger.info("Login successful - account link visible");
    }
    
    public void waitForLoginError() {
        logger.info("Waiting for login error message");
        waitForElementVisible(unsuccessMessage);
        logger.info("Login error message visible");
    }
    
    // Error handling methods
    public boolean hasValidationErrors() {
        return isElementDisplayed(unsuccessMessage);
    }
    
    public String getValidationErrorMessage() {
        if (isElementDisplayed(unsuccessMessage)) {
            return getElementText(unsuccessMessage);
        }
        return "";
    }
    
    // Accessibility methods
    public String getEmailFieldPlaceholder() {
        return emailField.getAttribute("placeholder");
    }
    
    public String getPasswordFieldPlaceholder() {
        return passwordField.getAttribute("placeholder");
    }
    
    public String getLoginButtonText() {
        return getElementText(loginButton);
    }
    
    // Security methods
    public boolean isPasswordFieldMasked() {
        String type = passwordField.getAttribute("type");
        return "password".equals(type);
    }
    
    // Performance methods
    public long getLoginFormLoadTime() {
        long startTime = System.currentTimeMillis();
        waitForElementVisible(loginForm);
        return System.currentTimeMillis() - startTime;
    }
}
