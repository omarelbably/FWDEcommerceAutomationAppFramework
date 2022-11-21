package org.example.pages;

import org.example.stepDefs.Hooks;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class P02_login {

    WebDriver driver;
    public  P02_login(WebDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    public WebElement loginPage()
    {
        return Hooks.driver.findElement(By.cssSelector("a[class=\"ico-login\"]"));
    }
    public WebElement email(){
        return Hooks.driver.findElement(By.id("Email"));
    }
    public WebElement password()
    {
        return Hooks.driver.findElement(By.id("Password"));
    }
    public WebElement loginB()
    {
    return Hooks.driver.findElement(By.xpath("//button[@class=\"button-1 login-button\"]"));
    }
    public WebElement unsuccessMSG(){
        return Hooks.driver.findElement(By.xpath("//div[@class=\"message-error validation-summary-errors\"]"));
}

    public void loginSteps(String username, String password)
    {
        email().sendKeys(username);
        password().sendKeys(password);
    }
}
