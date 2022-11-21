package org.example.stepDefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.pages.P01_register;
import org.example.pages.P02_login;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class D02_loginStepDef {

    WebDriver driver = null;
    P02_login login = new P02_login(driver);

  @Given("user go to login page")
  public void user_open_browser() throws InterruptedException {
        login.loginPage().click();
    }

    @When("^user login with \"(.*)\" and \"(.*)\"$")
    public void valid_data(String username, String password)
    {
        login.loginSteps(username,password);
    }

    @And("user press on login button")
    public void loginBTN()
    {
        login.loginB().click();
    }

    @Then("user login to the system successfully")
    public void success()
    {
        SoftAssert soft = new SoftAssert();
        soft.assertTrue(Hooks.driver.findElement(By.cssSelector("a[href=\"/customer/info\"]")).isDisplayed());
        soft.assertEquals(Hooks.driver.getCurrentUrl(), "https://demo.nopcommerce.com/", "third assertion");
        soft.assertAll();
    }

    @Then("user could not login to the system")
    public void unsuccessful()
    {
        SoftAssert soft = new SoftAssert();
        String expectedmsg = "Login was unsuccessful";
        String expectedcolor = "rgba(228, 67, 75, 1)";
        String actualResult1 = login.unsuccessMSG().getText();
        String actualResult2 = login.unsuccessMSG().getCssValue("color");
        soft.assertTrue(actualResult1.contains(expectedmsg), "Success message");
        soft.assertTrue(actualResult2.contains(expectedcolor),"color Assertion");

        soft.assertAll();
    }


}
