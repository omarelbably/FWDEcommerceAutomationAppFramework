package org.example.stepDefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.core.TestBase;
import org.example.pages.P01_register;
import org.example.utils.TestDataManager;
import org.example.utils.TestDataCache;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class D01_registerStepDef extends TestBase {
    
    private static final Logger logger = LoggerFactory.getLogger(D01_registerStepDef.class);
    private P01_register register;
    private Map<String, String> userData;

    @Given("user go to register page")
    public void go_to_registerPage() {
        logger.info("Navigating to registration page");
        register = new P01_register(Hooks.getStaticDriver());
        userData = TestDataManager.generateUserData();
        
        // Cache the user data for potential reuse
        TestDataCache.cacheData("registration_user_" + userData.get("email"), userData, 24);
        
        register.clickRegisterLink();
        logger.info("Successfully navigated to registration page");
    }

    @When("user select gender type")
    public void select_gender() {
        logger.info("Selecting male gender");
        register.selectMaleGender();
        logger.info("Male gender selected successfully");
    }

    @And("^user enter \"(.*)\" and \"(.*)\"$")
    public void username(String firstName, String lastName) {
        logger.info("Entering user names: firstName={}, lastName={}", firstName, lastName);
        register.fillNames(firstName, lastName);
        logger.info("User names entered successfully");
    }

    @And("user enter date of birth")
    public void birth_date() {
        logger.info("Entering date of birth");
        register.birthDate();
        logger.info("Date of birth entered successfully");
    }

    @And("user enter email field")
    public void email_field() {
        String email = userData.get("email");
        logger.info("Entering email: {}", email);
        register.enterEmail(email);
        logger.info("Email entered successfully");
    }

    @And("^user fills Password fields \"(.*)\" \"(.*)\"$")
    public void password_field(String first, String con) {
        logger.info("Entering password fields");
        register.fillPassword(first, con);
        logger.info("Password fields filled successfully");
    }

    @And("user clicks on register button")
    public void register_button() {
        logger.info("Clicking register button");
        register.clickRegisterButton();
        logger.info("Register button clicked successfully");
    }

    @Then("success message is displayed")
    public void success_message() {
        logger.info("Verifying registration success message");
        
        SoftAssert soft = new SoftAssert();
        String expectedMsg = "Your registration completed";
        String expectedColor = "rgba(76, 177, 124, 1)";
        
        String actualResult1 = register.getSuccessMessage();
        String actualResult2 = register.getSuccessMSG().getCssValue("color");
        
        logger.info("Expected message: {}", expectedMsg);
        logger.info("Actual message: {}", actualResult1);
        logger.info("Expected color: {}", expectedColor);
        logger.info("Actual color: {}", actualResult2);
        
        Assert.assertTrue(actualResult1.contains(expectedMsg), 
                         "Success message should contain: " + expectedMsg);
        soft.assertTrue(actualResult2.contains(expectedColor), 
                       "Success message color should be: " + expectedColor);
        
        soft.assertAll();
        logger.info("Registration success message verification completed");
    }

    @Then("registration should be completed successfully")
    public void registration_should_be_completed_successfully() {
        logger.info("Verifying complete registration success");
        
        String successMessage = register.getSuccessMessage();
        Assert.assertTrue(successMessage.contains("Your registration completed"), 
                         "Registration should be completed successfully");
        
        // Verify user is redirected to appropriate page
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("registerresult"), 
                         "User should be redirected to registration result page");
        
        logger.info("Registration completed successfully. Current URL: {}", currentUrl);
    }

    @Then("error message should be displayed for invalid registration")
    public void error_message_should_be_displayed_for_invalid_registration() {
        logger.info("Verifying error message for invalid registration");
        
        String errorMessage = register.getErrorMessage();
        Assert.assertNotNull(errorMessage, "Error message should be displayed");
        Assert.assertFalse(errorMessage.trim().isEmpty(), "Error message should not be empty");
        
        logger.info("Error message displayed: {}", errorMessage);
    }
}
