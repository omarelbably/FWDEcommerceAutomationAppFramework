package org.example.stepDefs;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.pages.P03_homePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class D03_currenciesStepDef
{
    WebDriver driver = null;
    P03_homePage home = new P03_homePage(driver);
    @When("user select euro option")
    public void select_euro()
    {

        Select select = new Select(home.currency_list());
        select.selectByVisibleText("Euro");
    }


    @Then("euro symbol is displayed on all products")
    public void euroSymbolIsDisplayedOnAllProducts()
    {
        for(int x=0 ; x < 4 ; x++)
        {

      String value = home.priceCurrency().get(x).getText();
            System.out.println(value);
         String[] expectedResult = {"€1032.00","€1548.00","€210.70","€21.50"};
            Assert.assertTrue(value.contains(expectedResult[x]));
        }


    }
}
