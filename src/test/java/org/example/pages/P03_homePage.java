package org.example.pages;

import org.example.stepDefs.Hooks;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class P03_homePage {
    WebDriver driver;
    public  P03_homePage(WebDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public WebElement search() {
        return Hooks.driver.findElement(By.id("small-searchterms"));
    }
    public  WebElement title()
    {
        return Hooks.driver.findElement(By.cssSelector("h2[class=\"product-title\"] > a"));
    }
    public WebElement sku(){
    return Hooks.driver.findElement(By.cssSelector("div[class=\"sku\"]> span[class=\"value\"]"));

    }
    public WebElement currency_list()
    {
       return Hooks.driver.findElement(By.id("customerCurrency"));
    }
    public List<WebElement> priceCurrency()
    {
        return Hooks.driver.findElements(By.cssSelector("span[class=\"price actual-price\"]"));
    }
    public List<WebElement> categories()
    {
        return Hooks.driver.findElements(By.cssSelector("ul[class=\"top-menu notmobile\"]  > li > a[href]"));
    }
    public List<WebElement> subCategories(int categoryNum)
    {
        return Hooks.driver.findElements(By.xpath("//ul[@class=\"top-menu notmobile\"]/li["+categoryNum+"]/ul[@class=\"sublist first-level\"]/li"));
    }

    public WebElement pageTitle()
    {
        return Hooks.driver.findElement(By.cssSelector("div[class=\"page-title\"]"));
    }

    // Sliders


    public WebElement firstSliderHref()
    {
        return Hooks.driver.findElement(By.cssSelector("div[id=\"nivo-slider\"] a[class=\"nivo-imageLink\"]:nth-child(2)"));
    }
    public WebElement secondSliderHref()
    {
        return Hooks.driver.findElement(By.cssSelector("div[id=\"nivo-slider\"] a[class=\"nivo-imageLink\"]:nth-child(2)"));
    }
    public void slidewaiter(WebElement slider)
    {
        WebDriverWait wait = new WebDriverWait(Hooks.driver, Duration.ofSeconds(60));
        wait.until(ExpectedConditions.attributeContains(slider,"style","display: block;"));
    }

    // FollowUS

    public WebElement facebookIcon()
    {
        return Hooks.driver.findElement(By.cssSelector("a[href=\"http://www.facebook.com/nopCommerce\"]"));
    }

    public WebElement twitterIcon()
    {
        return Hooks.driver.findElement(By.cssSelector("a[href=\"https://twitter.com/nopCommerce\"]"));
    }
    public WebElement rssIcon()
    {
        return Hooks.driver.findElement(By.cssSelector("a[href=\"/news/rss/1\"]"));
    }
    public WebElement youtubeIcon()
    {
        return Hooks.driver.findElement(By.cssSelector("a[href=\"http://www.youtube.com/user/nopCommerce\"]"));
    }


    public void switchTab1() throws InterruptedException {
        Thread.sleep(1000);
        ArrayList<String> tabs = new ArrayList<String>(Hooks.driver.getWindowHandles());
        Hooks.driver.switchTo().window(tabs.get(1));
        Thread.sleep(1000);
    }

    // wishlist

    public WebElement wishlist()
    {
        return Hooks.driver.findElement(By.cssSelector("div[data-productid=\"18\"]>div[class=\"details\"]>div[class=\"add-info\"]>div[class=\"buttons\"]> button[class=\"button-2 add-to-wishlist-button\"]"));

    }

    public WebElement wishlistMSG()
    {
        return Hooks.driver.findElement(By.cssSelector("p[class=\"content\"]"));
    }
    public WebElement wishlistPage()
    {
            return Hooks.driver.findElement(By.cssSelector("div[class=\"header-links\"]>ul>li>a[href=\"/wishlist\"]"));
    }
    public WebElement expectedQty()
    {
        return Hooks.driver.findElement(By.cssSelector("td[class=\"quantity\"]>input[value]"));
    }
}
