package org.example.stepDefs;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.core.TestBase;
import org.example.pages.P03_homePage;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class D03_currenciesStepDef extends TestBase {
    
    private static final Logger logger = LoggerFactory.getLogger(D03_currenciesStepDef.class);
    private P03_homePage home;

    @When("user select euro option")
    public void select_euro() {
        logger.info("Selecting Euro currency");
        home = new P03_homePage(Hooks.getStaticDriver());
        home.selectCurrency("Euro");
        logger.info("Euro currency selected successfully");
    }

    @When("user select dollar option")
    public void select_dollar() {
        logger.info("Selecting Dollar currency");
        home = new P03_homePage(Hooks.getStaticDriver());
        home.selectCurrency("US Dollar");
        logger.info("Dollar currency selected successfully");
    }

    @When("user select pound option")
    public void select_pound() {
        logger.info("Selecting Pound currency");
        home = new P03_homePage(Hooks.getStaticDriver());
        home.selectCurrency("British pound");
        logger.info("Pound currency selected successfully");
    }

    @Then("euro symbol is displayed on all products")
    public void euroSymbolIsDisplayedOnAllProducts() {
        logger.info("Verifying Euro symbol on all products");
        
        List<String> expectedPrices = Arrays.asList("€1032.00", "€1548.00", "€210.70", "€21.50");
        List<String> actualPrices = home.getProductPrices();
        
        logger.info("Expected prices: {}", expectedPrices);
        logger.info("Actual prices: {}", actualPrices);
        
        for (int i = 0; i < Math.min(expectedPrices.size(), actualPrices.size()); i++) {
            String expectedPrice = expectedPrices.get(i);
            String actualPrice = actualPrices.get(i);
            
            logger.info("Product {} - Expected: {}, Actual: {}", i + 1, expectedPrice, actualPrice);
            Assert.assertTrue(actualPrice.contains("€"), 
                             "Product " + (i + 1) + " should display Euro symbol");
            Assert.assertTrue(actualPrice.contains(expectedPrice.replace("€", "")), 
                             "Product " + (i + 1) + " should display correct price");
        }
        
        logger.info("Euro symbol verification completed successfully");
    }

    @Then("dollar symbol is displayed on all products")
    public void dollarSymbolIsDisplayedOnAllProducts() {
        logger.info("Verifying Dollar symbol on all products");
        
        List<String> actualPrices = home.getProductPrices();
        
        for (int i = 0; i < actualPrices.size(); i++) {
            String actualPrice = actualPrices.get(i);
            
            logger.info("Product {} - Price: {}", i + 1, actualPrice);
            Assert.assertTrue(actualPrice.contains("$"), 
                             "Product " + (i + 1) + " should display Dollar symbol");
        }
        
        logger.info("Dollar symbol verification completed successfully");
    }

    @Then("pound symbol is displayed on all products")
    public void poundSymbolIsDisplayedOnAllProducts() {
        logger.info("Verifying Pound symbol on all products");
        
        List<String> actualPrices = home.getProductPrices();
        
        for (int i = 0; i < actualPrices.size(); i++) {
            String actualPrice = actualPrices.get(i);
            
            logger.info("Product {} - Price: {}", i + 1, actualPrice);
            Assert.assertTrue(actualPrice.contains("£"), 
                             "Product " + (i + 1) + " should display Pound symbol");
        }
        
        logger.info("Pound symbol verification completed successfully");
    }

    @Then("currency should be changed successfully")
    public void currency_should_be_changed_successfully() {
        logger.info("Verifying currency change");
        
        String selectedCurrency = home.getSelectedCurrency();
        Assert.assertNotNull(selectedCurrency, "Selected currency should not be null");
        Assert.assertFalse(selectedCurrency.trim().isEmpty(), "Selected currency should not be empty");
        
        logger.info("Currency changed successfully to: {}", selectedCurrency);
    }

    @Then("all product prices should be updated")
    public void all_product_prices_should_be_updated() {
        logger.info("Verifying all product prices are updated");
        
        List<String> prices = home.getProductPrices();
        Assert.assertFalse(prices.isEmpty(), "Product prices should be available");
        
        for (int i = 0; i < prices.size(); i++) {
            String price = prices.get(i);
            Assert.assertNotNull(price, "Product " + (i + 1) + " price should not be null");
            Assert.assertFalse(price.trim().isEmpty(), "Product " + (i + 1) + " price should not be empty");
            Assert.assertTrue(price.matches(".*[€$£]\\d+.*"), 
                             "Product " + (i + 1) + " should have valid currency format");
            
            logger.info("Product {} - Price: {}", i + 1, price);
        }
        
        logger.info("All product prices updated successfully");
    }
}
