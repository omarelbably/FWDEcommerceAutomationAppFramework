package org.example.stepDefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.core.TestBase;
import org.example.pages.P03_homePage;
import org.example.utils.TestDataManager;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class D04_searchStepDef extends TestBase {
    
    private static final Logger logger = LoggerFactory.getLogger(D04_searchStepDef.class);
    private P03_homePage home;

    @When("user clicks on search field")
    public void click_search() {
        logger.info("Clicking on search field");
        home = new P03_homePage(driver);
        home.clickSearchField();
        logger.info("Search field clicked successfully");
    }

    @And("user search with {string}")
    public void userSearchWith(String searchTerm) {
        logger.info("Searching for: {}", searchTerm);
        home.enterSearchTerm(searchTerm);
        home.clickSearchButton();
        logger.info("Search completed for: {}", searchTerm);
    }

    @And("user search with dynamic product")
    public void userSearchWithDynamicProduct() {
        String searchTerm = TestDataManager.generateSearchTerm();
        logger.info("Searching with dynamic product: {}", searchTerm);
        home.enterSearchTerm(searchTerm);
        home.clickSearchButton();
        logger.info("Dynamic search completed for: {}", searchTerm);
    }

    @Then("user could find {string} relative results")
    public void userCouldFindRelativeResults(String searchTerm) {
        logger.info("Verifying search results for: {}", searchTerm);
        
        SoftAssert soft = new SoftAssert();
        String expectedUrl = "https://demo.nopcommerce.com/search?q=" + searchTerm;
        String actualUrl = driver.getCurrentUrl();
        
        logger.info("Expected URL: {}", expectedUrl);
        logger.info("Actual URL: {}", actualUrl);
        
        soft.assertEquals(actualUrl, expectedUrl, 
                         "Search URL should match expected URL");
        
        // Verify search results are displayed
        boolean hasSearchResults = home.hasSearchResults();
        soft.assertTrue(hasSearchResults, "Search results should be displayed");
        
        soft.assertAll();
        logger.info("Search results verification completed");
    }

    @Then("user could find {string} inside product detail page")
    public void userCouldFindInsideProductDetailPage(String searchTerm) {
        logger.info("Verifying product detail page contains: {}", searchTerm);
        
        home.clickProductTitle();
        String skuText = home.getSkuText();
        
        logger.info("Product SKU: {}", skuText);
        logger.info("Search term: {}", searchTerm);
        
        Assert.assertTrue(skuText.contains(searchTerm), 
                         "Product SKU should contain search term: " + searchTerm);
        
        logger.info("Product detail page verification completed");
    }

    @Then("search results should be displayed")
    public void search_results_should_be_displayed() {
        logger.info("Verifying search results are displayed");
        
        boolean hasResults = home.hasSearchResults();
        Assert.assertTrue(hasResults, "Search results should be displayed");
        
        int resultCount = home.getSearchResultCount();
        logger.info("Found {} search results", resultCount);
        
        Assert.assertTrue(resultCount > 0, "Should have at least one search result");
        logger.info("Search results verification completed");
    }

    @Then("no search results message should be displayed")
    public void no_search_results_message_should_be_displayed() {
        logger.info("Verifying no search results message");
        
        String noResultsMessage = home.getNoSearchResultsMessage();
        Assert.assertNotNull(noResultsMessage, "No results message should be displayed");
        Assert.assertFalse(noResultsMessage.trim().isEmpty(), 
                          "No results message should not be empty");
        
        logger.info("No search results message: {}", noResultsMessage);
    }

    @Then("search suggestions should be displayed")
    public void search_suggestions_should_be_displayed() {
        logger.info("Verifying search suggestions");
        
        boolean hasSuggestions = home.hasSearchSuggestions();
        Assert.assertTrue(hasSuggestions, "Search suggestions should be displayed");
        
        int suggestionCount = home.getSearchSuggestionCount();
        logger.info("Found {} search suggestions", suggestionCount);
        
        Assert.assertTrue(suggestionCount > 0, "Should have at least one search suggestion");
        logger.info("Search suggestions verification completed");
    }

    @Then("search field should be cleared")
    public void search_field_should_be_cleared() {
        logger.info("Verifying search field is cleared");
        
        String searchFieldValue = home.getSearchFieldValue();
        Assert.assertTrue(searchFieldValue.isEmpty(), 
                         "Search field should be empty after search");
        
        logger.info("Search field cleared verification completed");
    }
}
