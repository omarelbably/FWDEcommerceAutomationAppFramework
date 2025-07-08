package org.example.stepDefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.core.TestBase;
import org.example.pages.P02_login;
import org.example.utils.TestDataManager;
import org.example.utils.TestDataCache;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class D02_loginStepDef extends TestBase {
    
    private static final Logger logger = LoggerFactory.getLogger(D02_loginStepDef.class);
    private P02_login login;
    private Map<String, String> userData;

    @Given("user go to login page")
    public void user_open_browser() {
        logger.info("Navigating to login page");
        login = new P02_login(Hooks.getStaticDriver());
        login.navigateToLoginPage();
        logger.info("Successfully navigated to login page");
    }

    @When("^user login with \"(.*)\" and \"(.*)\"$")
    public void valid_data(String username, String password) {
        logger.info("Logging in with username: {}", username);
        login.loginSteps(username, password);
        logger.info("Login credentials entered successfully");
    }

    @When("user login with cached credentials")
    public void login_with_cached_credentials() {
        logger.info("Logging in with cached credentials");
        userData = TestDataCache.getOrCreateUserData("test_user_001");
        login.loginSteps(userData.get("email"), userData.get("password"));
        logger.info("Cached credentials used for login");
    }

    @When("user login with dynamic credentials")
    public void login_with_dynamic_credentials() {
        logger.info("Logging in with dynamic credentials");
        userData = TestDataManager.generateUserData();
        login.loginSteps(userData.get("email"), userData.get("password"));
        logger.info("Dynamic credentials used for login");
    }

    @And("user press on login button")
    public void loginBTN() {
        logger.info("Clicking login button");
        login.clickLoginButton();
        logger.info("Login button clicked successfully");
    }

    @Then("user login to the system successfully")
    public void success() {
        logger.info("Verifying successful login");
        
        SoftAssert soft = new SoftAssert();
        
        // Verify user is logged in by checking for account link
        boolean isAccountLinkDisplayed = login.isAccountLinkDisplayed();
        soft.assertTrue(isAccountLinkDisplayed, "Account link should be displayed after successful login");
        
        // Verify current URL
        String currentUrl = driver.getCurrentUrl();
        String expectedUrl = "https://demo.nopcommerce.com/";
        soft.assertEquals(currentUrl, expectedUrl, "User should be redirected to home page after login");
        
        // Verify welcome message if available
        if (login.isWelcomeMessageDisplayed()) {
            String welcomeMessage = login.getWelcomeMessage();
            soft.assertNotNull(welcomeMessage, "Welcome message should be displayed");
            soft.assertFalse(welcomeMessage.trim().isEmpty(), "Welcome message should not be empty");
        }
        
        soft.assertAll();
        logger.info("Login verification completed successfully");
    }

    @Then("user could not login to the system")
    public void unsuccessful() {
        logger.info("Verifying unsuccessful login");
        
        SoftAssert soft = new SoftAssert();
        String expectedMsg = "Login was unsuccessful";
        String expectedColor = "rgba(228, 67, 75, 1)";
        
        String actualResult1 = login.getUnsuccessMessage();
        String actualResult2 = login.getUnsuccessMessageColor();
        
        logger.info("Expected message: {}", expectedMsg);
        logger.info("Actual message: {}", actualResult1);
        logger.info("Expected color: {}", expectedColor);
        logger.info("Actual color: {}", actualResult2);
        
        soft.assertTrue(actualResult1.contains(expectedMsg), 
                       "Error message should contain: " + expectedMsg);
        soft.assertTrue(actualResult2.contains(expectedColor), 
                       "Error message color should be: " + expectedColor);
        
        soft.assertAll();
        logger.info("Unsuccessful login verification completed");
    }

    @Then("login error message should be displayed")
    public void login_error_message_should_be_displayed() {
        logger.info("Verifying login error message");
        
        String errorMessage = login.getUnsuccessMessage();
        Assert.assertNotNull(errorMessage, "Error message should be displayed");
        Assert.assertFalse(errorMessage.trim().isEmpty(), "Error message should not be empty");
        
        logger.info("Login error message displayed: {}", errorMessage);
    }

    @Then("user should be redirected to login page")
    public void user_should_be_redirected_to_login_page() {
        logger.info("Verifying user is redirected to login page");
        
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("login"), 
                         "User should be redirected to login page");
        
        boolean isLoginFormDisplayed = login.isLoginFormDisplayed();
        Assert.assertTrue(isLoginFormDisplayed, "Login form should be displayed");
        
        logger.info("User successfully redirected to login page. Current URL: {}", currentUrl);
    }

    @Then("login form should be displayed")
    public void login_form_should_be_displayed() {
        logger.info("Verifying login form is displayed");
        
        boolean isFormDisplayed = login.isLoginFormDisplayed();
        Assert.assertTrue(isFormDisplayed, "Login form should be displayed");
        
        boolean isEmailFieldDisplayed = login.isEmailFieldDisplayed();
        Assert.assertTrue(isEmailFieldDisplayed, "Email field should be displayed");
        
        boolean isPasswordFieldDisplayed = login.isPasswordFieldDisplayed();
        Assert.assertTrue(isPasswordFieldDisplayed, "Password field should be displayed");
        
        boolean isLoginButtonDisplayed = login.isLoginButtonDisplayed();
        Assert.assertTrue(isLoginButtonDisplayed, "Login button should be displayed");
        
        logger.info("Login form verification completed successfully");
    }
}
