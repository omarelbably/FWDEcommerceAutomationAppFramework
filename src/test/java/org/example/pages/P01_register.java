package org.example.pages;

import org.example.stepDefs.Hooks;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class P01_register {
    WebDriver driver;

    public P01_register(WebDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    public WebElement registerlink()
    {
        return Hooks.driver.findElement(By.cssSelector("a[class=\"ico-register\"]"));
    }

    public WebElement maleGender(){
        return Hooks.driver.findElement(By.id("gender-male"));
    }
    public WebElement firstName(){
        return Hooks.driver.findElement(By.id("FirstName"));

    }
    public WebElement lastName(){
        return Hooks.driver.findElement(By.id("LastName"));
    }
    public WebElement password(){
        return Hooks.driver.findElement(By.id("Password"));

    }
    public WebElement passwordCon(){
        return Hooks.driver.findElement(By.id("ConfirmPassword"));
    }

    public WebElement email(){
        return Hooks.driver.findElement(By.id("Email"));
    }
    public WebElement regButton(){
        return Hooks.driver.findElement(By.id("register-button"));
    }
    public WebElement successMSG(){
        return Hooks.driver.findElement(By.className("result"));
    }
    public void birthDate()
    {
        Select day = new Select(Hooks.driver.findElement(By.name("DateOfBirthDay")));
        day.selectByValue("4");
        Select month = new Select(Hooks.driver.findElement(By.name("DateOfBirthMonth")));
        month.selectByValue("5");
        Select year = new Select(Hooks.driver.findElement(By.name("DateOfBirthYear")));
        year.selectByValue("1996");
    }



    public void fillNames(String firstName, String lastName){

        // Enter Firstname
        firstName().sendKeys(firstName);
        // enter last name
        lastName().sendKeys(lastName);
    }
    public void fillPassword(String firstPw, String secondPw){

        // Enter Firstname
        password().sendKeys(firstPw);
        // enter last name
        passwordCon().sendKeys(secondPw);
    }

}
