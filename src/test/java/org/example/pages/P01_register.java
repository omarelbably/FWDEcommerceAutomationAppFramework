package org.example.pages;

import org.example.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class P01_register extends BasePage {
    
    private static final Logger logger = LoggerFactory.getLogger(P01_register.class);
    
    // Page elements
    @FindBy(css = "a[class=\"ico-register\"]")
    private WebElement registerLink;
    
    @FindBy(id = "gender-male")
    private WebElement maleGender;
    
    @FindBy(id = "gender-female")
    private WebElement femaleGender;
    
    @FindBy(id = "FirstName")
    private WebElement firstName;
    
    @FindBy(id = "LastName")
    private WebElement lastName;
    
    @FindBy(id = "Password")
    private WebElement password;
    
    @FindBy(id = "ConfirmPassword")
    private WebElement passwordCon;
    
    @FindBy(id = "Email")
    private WebElement email;
    
    @FindBy(id = "register-button")
    private WebElement regButton;
    
    @FindBy(className = "result")
    private WebElement successMSG;
    
    @FindBy(css = ".message-error")
    private WebElement errorMessage;
    
    @FindBy(name = "DateOfBirthDay")
    private WebElement dayDropdown;
    
    @FindBy(name = "DateOfBirthMonth")
    private WebElement monthDropdown;
    
    @FindBy(name = "DateOfBirthYear")
    private WebElement yearDropdown;
    
    public P01_register(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
    
    // Navigation methods
    public void navigateToRegistrationPage() {
        logger.info("Navigating to registration page");
        driver.get(getBaseUrl() + "/register");
        waitForElementVisible(registerLink);
        logger.info("Registration page loaded successfully");
    }
    
    public void clickRegisterLink() {
        logger.info("Clicking register link");
        clickElement(registerLink);
        waitForElementVisible(firstName);
        logger.info("Register link clicked successfully");
    }
    
    // Gender selection methods
    public void selectMaleGender() {
        logger.info("Selecting male gender");
        clickElement(maleGender);
        logger.info("Male gender selected successfully");
    }
    
    public void selectFemaleGender() {
        logger.info("Selecting female gender");
        clickElement(femaleGender);
        logger.info("Female gender selected successfully");
    }
    
    // Form filling methods
    public void fillNames(String firstName, String lastName) {
        logger.info("Filling names: firstName={}, lastName={}", firstName, lastName);
        sendKeysToElement(this.firstName, firstName);
        sendKeysToElement(this.lastName, lastName);
        logger.info("Names filled successfully");
    }
    
    public void fillPassword(String firstPw, String secondPw) {
        logger.info("Filling password fields");
        sendKeysToElement(password, firstPw);
        sendKeysToElement(passwordCon, secondPw);
        logger.info("Password fields filled successfully");
    }
    
    public void enterEmail(String emailAddress) {
        logger.info("Entering email: {}", emailAddress);
        sendKeysToElement(email, emailAddress);
        logger.info("Email entered successfully");
    }
    
    public void birthDate() {
        logger.info("Setting birth date");
        Select day = new Select(dayDropdown);
        day.selectByValue("4");
        Select month = new Select(monthDropdown);
        month.selectByValue("5");
        Select year = new Select(yearDropdown);
        year.selectByValue("1996");
        logger.info("Birth date set successfully");
    }
    
    public void setBirthDate(String day, String month, String year) {
        logger.info("Setting custom birth date: day={}, month={}, year={}", day, month, year);
        Select daySelect = new Select(dayDropdown);
        daySelect.selectByValue(day);
        Select monthSelect = new Select(monthDropdown);
        monthSelect.selectByValue(month);
        Select yearSelect = new Select(yearDropdown);
        yearSelect.selectByValue(year);
        logger.info("Custom birth date set successfully");
    }
    
    // Action methods
    public void clickRegisterButton() {
        logger.info("Clicking register button");
        clickElement(regButton);
        logger.info("Register button clicked successfully");
    }
    
    // Business logic methods
    public void registerUser(String firstName, String lastName, String email, String password) {
        logger.info("Registering user with email: {}", email);
        fillNames(firstName, lastName);
        enterEmail(email);
        fillPassword(password, password);
        clickRegisterButton();
        logger.info("User registration completed");
    }
    
    public void registerUserWithGender(String firstName, String lastName, String email, 
                                     String password, String gender) {
        logger.info("Registering user with gender: {}", gender);
        if ("male".equalsIgnoreCase(gender)) {
            selectMaleGender();
        } else if ("female".equalsIgnoreCase(gender)) {
            selectFemaleGender();
        }
        registerUser(firstName, lastName, email, password);
    }
    
    // Validation methods
    public boolean isRegistrationPageDisplayed() {
        return isElementDisplayed(firstName) && isElementDisplayed(lastName) && 
               isElementDisplayed(email) && isElementDisplayed(password);
    }
    
    public boolean isSuccessMessageDisplayed() {
        return isElementDisplayed(successMSG);
    }
    
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }
    
    // Getter methods
    public String getSuccessMessage() {
        return getElementText(successMSG);
    }
    
    public String getErrorMessage() {
        return getElementText(errorMessage);
    }
    
    public WebElement getSuccessMSG() {
        return successMSG;
    }
    
    public WebElement getErrorMessageElement() {
        return errorMessage;
    }
    
    // Form validation methods
    public boolean isFirstNameFieldDisplayed() {
        return isElementDisplayed(firstName);
    }
    
    public boolean isLastNameFieldDisplayed() {
        return isElementDisplayed(lastName);
    }
    
    public boolean isEmailFieldDisplayed() {
        return isElementDisplayed(email);
    }
    
    public boolean isPasswordFieldDisplayed() {
        return isElementDisplayed(password);
    }
    
    public boolean isConfirmPasswordFieldDisplayed() {
        return isElementDisplayed(passwordCon);
    }
    
    public boolean isRegisterButtonEnabled() {
        return regButton.isEnabled();
    }
    
    // Form clearing methods
    public void clearRegistrationForm() {
        logger.info("Clearing registration form");
        firstName.clear();
        lastName.clear();
        email.clear();
        password.clear();
        passwordCon.clear();
        logger.info("Registration form cleared successfully");
    }
    
    // Wait methods
    public void waitForRegistrationComplete() {
        logger.info("Waiting for registration to complete");
        waitForElementVisible(successMSG);
        logger.info("Registration completed");
    }
    
    public void waitForErrorMessage() {
        logger.info("Waiting for error message");
        waitForElementVisible(errorMessage);
        logger.info("Error message displayed");
    }
}
