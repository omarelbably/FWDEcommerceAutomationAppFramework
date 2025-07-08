package org.example.stepDefs;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.core.TestBase;
import org.example.pages.P03_homePage;
import org.testng.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class D06_homeSlidersStepDef extends TestBase {
    
    private static final Logger logger = LoggerFactory.getLogger(D06_homeSlidersStepDef.class);
    private P03_homePage home;
    
    @When("clicking on the first slider")
    public void firstSlider() {
        logger.info("Clicking on the first slider");
        home = new P03_homePage(driver);
        home.clickFirstSlider();
        logger.info("First slider clicked successfully");
    }

    @Then("check if u are rotated to nokia page")
    public void checkIfUAreRotatedToNokiaPage() {
        logger.info("Verifying navigation to Nokia page");
        
        String expectedUrl = "https://demo.nopcommerce.com/nokia-lumia-1020";
        String actualUrl = driver.getCurrentUrl();
        
        logger.info("Expected URL: {}", expectedUrl);
        logger.info("Actual URL: {}", actualUrl);
        
        Assert.assertEquals(actualUrl, expectedUrl, 
                           "Should be redirected to Nokia Lumia 1020 page");
        
        logger.info("Nokia page navigation verification completed");
    }

    @When("clicking on the second slider")
    public void clickingOnTheSecondSlider() {
        logger.info("Clicking on the second slider");
        home = new P03_homePage(driver);
        home.clickSecondSlider();
        logger.info("Second slider clicked successfully");
    }

    @Then("check if u are rotated to iphone page")
    public void checkIfUAreRotatedToIphonePage() {
        logger.info("Verifying navigation to iPhone page");
        
        String expectedUrl = "https://demo.nopcommerce.com/iphone-6";
        String actualUrl = driver.getCurrentUrl();
        
        logger.info("Expected URL: {}", expectedUrl);
        logger.info("Actual URL: {}", actualUrl);
        
        Assert.assertEquals(actualUrl, expectedUrl, 
                           "Should be redirected to iPhone 6 page");
        
        logger.info("iPhone page navigation verification completed");
    }

    @When("user waits for slider to be visible")
    public void user_waits_for_slider_to_be_visible() {
        logger.info("Waiting for slider to be visible");
        home = new P03_homePage(driver);
        home.waitForSliderToBeVisible();
        logger.info("Slider is now visible");
    }

    @Then("slider should be displayed")
    public void slider_should_be_displayed() {
        logger.info("Verifying slider is displayed");
        
        boolean isSliderDisplayed = home.isSliderDisplayed();
        Assert.assertTrue(isSliderDisplayed, "Slider should be displayed");
        
        logger.info("Slider display verification completed");
    }

    @Then("slider navigation should work correctly")
    public void slider_navigation_should_work_correctly() {
        logger.info("Verifying slider navigation functionality");
        
        // Test first slider
        home.clickFirstSlider();
        String firstSliderUrl = driver.getCurrentUrl();
        logger.info("First slider URL: {}", firstSliderUrl);
        
        // Navigate back
        driver.navigate().back();
        
        // Test second slider
        home.clickSecondSlider();
        String secondSliderUrl = driver.getCurrentUrl();
        logger.info("Second slider URL: {}", secondSliderUrl);
        
        Assert.assertNotEquals(firstSliderUrl, secondSliderUrl, 
                              "Slider URLs should be different");
        
        logger.info("Slider navigation verification completed");
    }

    @Then("slider images should be loaded")
    public void slider_images_should_be_loaded() {
        logger.info("Verifying slider images are loaded");
        
        boolean areImagesLoaded = home.areSliderImagesLoaded();
        Assert.assertTrue(areImagesLoaded, "Slider images should be loaded");
        
        logger.info("Slider images verification completed");
    }

    @Then("slider should be responsive")
    public void slider_should_be_responsive() {
        logger.info("Verifying slider responsiveness");
        
        // Test different viewport sizes
        home.testSliderResponsiveness();
        
        logger.info("Slider responsiveness verification completed");
    }

    @Then("slider should have proper accessibility attributes")
    public void slider_should_have_proper_accessibility_attributes() {
        logger.info("Verifying slider accessibility attributes");
        
        boolean hasAccessibilityAttributes = home.hasSliderAccessibilityAttributes();
        Assert.assertTrue(hasAccessibilityAttributes, 
                         "Slider should have proper accessibility attributes");
        
        logger.info("Slider accessibility verification completed");
    }
}
