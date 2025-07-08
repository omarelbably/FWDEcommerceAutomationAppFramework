package org.example.stepDefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.core.TestBase;
import org.example.pages.P03_homePage;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class D05_hoverCategoriesStepDef extends TestBase {
    
    private static final Logger logger = LoggerFactory.getLogger(D05_hoverCategoriesStepDef.class);
    private P03_homePage home;
    private int randomCategoryIndex;
    private String subCategoryName;
    private String mainCategoryName;

    @When("user hover category and select subcategory then check if the name equals to page title")
    public void userHoverCategoryAndSelectSubcategoryThenCheckIfTheNameEqualsToPageTitle() {
        logger.info("Starting category hover and subcategory selection");
        home = new P03_homePage(Hooks.getStaticDriver());
        
        // Get all categories
        List<String> categoryNames = home.getCategoryNames();
        logger.info("Available categories: {}", categoryNames);
        
        // Select random category
        int randomNumber = new Random().nextInt(Math.min(3, categoryNames.size()));
        mainCategoryName = categoryNames.get(randomNumber);
        randomCategoryIndex = randomNumber + 1;
        
        logger.info("Selected category: {} (index: {})", mainCategoryName, randomNumber);
        
        // Hover over the category
        home.hoverOverCategory(mainCategoryName);
        logger.info("Hovered over category: {}", mainCategoryName);
    }

    @And("user go to subcategory page")
    public void subPage() {
        logger.info("Navigating to subcategory page");
        
        // Get subcategories for the selected category
        List<WebElement> subCategories = home.getSubCategories(randomCategoryIndex);
        logger.info("Found {} subcategories for category {}", subCategories.size(), mainCategoryName);
        
        // Select random subcategory
        int randomSubNumber = new Random().nextInt(Math.min(3, subCategories.size()));
        subCategoryName = home.getElementText(subCategories.get(randomSubNumber));
        
        logger.info("Selected subcategory: {} (index: {})", subCategoryName, randomSubNumber);
        
        // Click on the subcategory
        home.clickElement(subCategories.get(randomSubNumber));
        logger.info("Clicked on subcategory: {}", subCategoryName);
    }

    @Then("user check if page title equals subcategory name")
    public void checkTitle() {
        logger.info("Verifying page title matches subcategory name");
        
        String actualPageTitle = home.getPageTitle();
        logger.info("Expected subcategory: {}", subCategoryName);
        logger.info("Actual page title: {}", actualPageTitle);
        
        Assert.assertTrue(actualPageTitle.contains(subCategoryName), 
                         "Page title should contain subcategory name: " + subCategoryName);
        
        logger.info("Page title verification completed successfully");
    }

    @When("user hovers over {string} category")
    public void user_hovers_over_category(String categoryName) {
        logger.info("Hovering over category: {}", categoryName);
        home = new P03_homePage(Hooks.getStaticDriver());
        home.hoverOverCategory(categoryName);
        mainCategoryName = categoryName;
        logger.info("Hovered over category: {}", categoryName);
    }

    @And("user clicks on {string} subcategory")
    public void user_clicks_on_subcategory(String subcategoryName) {
        logger.info("Clicking on subcategory: {}", subcategoryName);
        
        // Find and click the specific subcategory
        home.clickSubcategory(mainCategoryName, subcategoryName);
        subCategoryName = subcategoryName;
        
        logger.info("Clicked on subcategory: {}", subcategoryName);
    }

    @Then("subcategory dropdown should be displayed")
    public void subcategory_dropdown_should_be_displayed() {
        logger.info("Verifying subcategory dropdown is displayed");
        
        boolean isDropdownDisplayed = home.isSubcategoryDropdownDisplayed(mainCategoryName);
        Assert.assertTrue(isDropdownDisplayed, 
                         "Subcategory dropdown should be displayed for category: " + mainCategoryName);
        
        logger.info("Subcategory dropdown verification completed");
    }

    @Then("subcategory page should be loaded")
    public void subcategory_page_should_be_loaded() {
        logger.info("Verifying subcategory page is loaded");
        
        String currentUrl = driver.getCurrentUrl();
        String pageTitle = home.getPageTitle();
        
        logger.info("Current URL: {}", currentUrl);
        logger.info("Page title: {}", pageTitle);
        
        Assert.assertNotNull(pageTitle, "Page title should not be null");
        Assert.assertFalse(pageTitle.trim().isEmpty(), "Page title should not be empty");
        
        logger.info("Subcategory page loaded successfully");
    }

    @Then("all subcategories should be clickable")
    public void all_subcategories_should_be_clickable() {
        logger.info("Verifying all subcategories are clickable");
        
        List<WebElement> subCategories = home.getSubCategories(randomCategoryIndex);
        
        for (int i = 0; i < subCategories.size(); i++) {
            WebElement subCategory = subCategories.get(i);
            String subCategoryText = home.getElementText(subCategory);
            
            logger.info("Checking subcategory {}: {}", i + 1, subCategoryText);
            
            boolean isClickable = home.isElementClickable(subCategory);
            Assert.assertTrue(isClickable, 
                             "Subcategory should be clickable: " + subCategoryText);
        }
        
        logger.info("All subcategories clickability verification completed");
    }

    @Then("subcategory count should be greater than zero")
    public void subcategory_count_should_be_greater_than_zero() {
        logger.info("Verifying subcategory count");
        
        List<WebElement> subCategories = home.getSubCategories(randomCategoryIndex);
        int subCategoryCount = subCategories.size();
        
        logger.info("Subcategory count: {}", subCategoryCount);
        
        Assert.assertTrue(subCategoryCount > 0, 
                         "Should have at least one subcategory for category: " + mainCategoryName);
        
        logger.info("Subcategory count verification completed");
    }
}

