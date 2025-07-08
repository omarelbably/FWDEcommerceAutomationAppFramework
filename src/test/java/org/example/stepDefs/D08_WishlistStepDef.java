package org.example.stepDefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.core.TestBase;
import org.example.pages.P03_homePage;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class D08_WishlistStepDef extends TestBase {
    
    private static final Logger logger = LoggerFactory.getLogger(D08_WishlistStepDef.class);
    private P03_homePage home;

    @When("put a product to wishlist")
    public void wishlistAnItem() {
        logger.info("Adding product to wishlist");
        home = new P03_homePage(driver);
        home.clickWishlistButton();
        logger.info("Product added to wishlist successfully");
    }

    @And("check the success message")
    public void checkTheSuccessMessage() {
        logger.info("Verifying wishlist success message");
        
        SoftAssert soft = new SoftAssert();
        String expectedResult = "The product has been added to your ";
        String expectedColor = "rgba(119, 119, 119, 1)";

        String actualResult = home.getWishlistMessage();
        String actualColor = home.getWishlistButtonColor();
        
        logger.info("Expected message: {}", expectedResult);
        logger.info("Actual message: {}", actualResult);
        logger.info("Expected color: {}", expectedColor);
        logger.info("Actual color: {}", actualColor);
        
        soft.assertTrue(actualColor.contains(expectedColor), 
                       "Wishlist button color should match expected color");
        soft.assertTrue(actualResult.contains(expectedResult), 
                       "Wishlist message should contain expected text");
        
        soft.assertAll();
        logger.info("Wishlist success message verification completed");
    }

    @And("user go to wishlist page")
    public void userGoToWishlistPage() {
        logger.info("Navigating to wishlist page");
        home.clickWishlistPage();
        logger.info("Successfully navigated to wishlist page");
    }

    @Then("check the product quantity")
    public void checkTheProductQuantity() {
        logger.info("Verifying product quantity in wishlist");
        
        SoftAssert soft = new SoftAssert();
        String expectedQty = "1";
        String actualQty = home.getExpectedQuantity();
        
        logger.info("Expected quantity: {}", expectedQty);
        logger.info("Actual quantity: {}", actualQty);
        
        soft.assertTrue(actualQty.contains(expectedQty), 
                       "Product quantity should be " + expectedQty);
        
        soft.assertAll();
        logger.info("Product quantity verification completed");
    }

    @When("user adds multiple products to wishlist")
    public void user_adds_multiple_products_to_wishlist() {
        logger.info("Adding multiple products to wishlist");
        home = new P03_homePage(driver);
        
        // Add first product
        home.clickWishlistButton();
        logger.info("First product added to wishlist");
        
        // Navigate to another product and add it
        home.navigateToProductPage("second_product");
        home.clickWishlistButton();
        logger.info("Second product added to wishlist");
    }

    @Then("wishlist should contain the added products")
    public void wishlist_should_contain_the_added_products() {
        logger.info("Verifying wishlist contains added products");
        
        home.clickWishlistPage();
        int productCount = home.getWishlistProductCount();
        
        logger.info("Wishlist contains {} products", productCount);
        Assert.assertTrue(productCount > 0, "Wishlist should contain at least one product");
        
        logger.info("Wishlist product verification completed");
    }

    @When("user removes product from wishlist")
    public void user_removes_product_from_wishlist() {
        logger.info("Removing product from wishlist");
        home = new P03_homePage(driver);
        home.clickWishlistPage();
        home.removeProductFromWishlist();
        logger.info("Product removed from wishlist");
    }

    @Then("wishlist should be empty")
    public void wishlist_should_be_empty() {
        logger.info("Verifying wishlist is empty");
        
        boolean isWishlistEmpty = home.isWishlistEmpty();
        Assert.assertTrue(isWishlistEmpty, "Wishlist should be empty after removing product");
        
        logger.info("Wishlist empty verification completed");
    }

    @Then("wishlist button should be disabled for added product")
    public void wishlist_button_should_be_disabled_for_added_product() {
        logger.info("Verifying wishlist button is disabled for added product");
        
        boolean isButtonDisabled = home.isWishlistButtonDisabled();
        Assert.assertTrue(isButtonDisabled, "Wishlist button should be disabled for added product");
        
        logger.info("Wishlist button disabled verification completed");
    }

    @Then("wishlist page should be accessible")
    public void wishlist_page_should_be_accessible() {
        logger.info("Verifying wishlist page accessibility");
        
        home.clickWishlistPage();
        String currentUrl = driver.getCurrentUrl();
        
        logger.info("Current URL: {}", currentUrl);
        Assert.assertTrue(currentUrl.contains("wishlist"), 
                         "Should be redirected to wishlist page");
        
        boolean isWishlistPageLoaded = home.isWishlistPageLoaded();
        Assert.assertTrue(isWishlistPageLoaded, "Wishlist page should be loaded");
        
        logger.info("Wishlist page accessibility verification completed");
    }

    @Then("wishlist functionality should work correctly")
    public void wishlist_functionality_should_work_correctly() {
        logger.info("Verifying complete wishlist functionality");
        
        // Test adding product
        home.clickWishlistButton();
        boolean isSuccessMessageDisplayed = home.isWishlistMessageDisplayed();
        Assert.assertTrue(isSuccessMessageDisplayed, "Success message should be displayed");
        
        // Test navigating to wishlist page
        home.clickWishlistPage();
        boolean isWishlistPageLoaded = home.isWishlistPageLoaded();
        Assert.assertTrue(isWishlistPageLoaded, "Wishlist page should be loaded");
        
        // Test product quantity
        String quantity = home.getExpectedQuantity();
        Assert.assertNotNull(quantity, "Product quantity should not be null");
        
        logger.info("Complete wishlist functionality verification completed");
    }
}
