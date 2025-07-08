package org.example.stepDefs;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.core.TestBase;
import org.example.pages.P03_homePage;
import org.testng.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class D07_followUsStepDef extends TestBase {
    
    private static final Logger logger = LoggerFactory.getLogger(D07_followUsStepDef.class);
    private P03_homePage home;

    @When("clicking on the facebook icon")
    public void facebookIcon() {
        logger.info("Clicking on Facebook icon");
        home = new P03_homePage(driver);
        home.clickFacebookIcon();
        home.switchToNewTab();
        logger.info("Facebook icon clicked and new tab opened");
    }

    @Then("verify the facebook url")
    public void facebookURL() {
        logger.info("Verifying Facebook URL");
        
        String expectedUrl = "https://web.facebook.com/nopCommerce?_rdc=1&_rdr";
        String actualUrl = driver.getCurrentUrl();
        
        logger.info("Expected URL: {}", expectedUrl);
        logger.info("Actual URL: {}", actualUrl);
        
        Assert.assertEquals(actualUrl, expectedUrl, 
                           "Should be redirected to Facebook page");
        
        logger.info("Facebook URL verification completed");
    }

    @When("clicking on the twitter icon")
    public void clickingOnTheTwitterIcon() {
        logger.info("Clicking on Twitter icon");
        home = new P03_homePage(driver);
        home.clickTwitterIcon();
        home.switchToNewTab();
        logger.info("Twitter icon clicked and new tab opened");
    }

    @Then("verify the twitter url")
    public void twitterURL() {
        logger.info("Verifying Twitter URL");
        
        String expectedUrl = "https://twitter.com/nopCommerce";
        String actualUrl = driver.getCurrentUrl();
        
        logger.info("Expected URL: {}", expectedUrl);
        logger.info("Actual URL: {}", actualUrl);
        
        Assert.assertEquals(actualUrl, expectedUrl, 
                           "Should be redirected to Twitter page");
        
        logger.info("Twitter URL verification completed");
    }

    @When("clicking on the Rss icon")
    public void clickingOnTheRssIcon() {
        logger.info("Clicking on RSS icon");
        home = new P03_homePage(driver);
        home.clickRssIcon();
        logger.info("RSS icon clicked");
    }

    @Then("verify the Rss url")
    public void rssURL() {
        logger.info("Verifying RSS URL");
        
        String expectedUrl = "https://demo.nopcommerce.com/new-online-store-is-open";
        String actualUrl = driver.getCurrentUrl();
        
        logger.info("Expected URL: {}", expectedUrl);
        logger.info("Actual URL: {}", actualUrl);
        
        Assert.assertEquals(actualUrl, expectedUrl, 
                           "Should be redirected to RSS page");
        
        logger.info("RSS URL verification completed");
    }

    @When("clicking on the youtube icon")
    public void clickingOnTheYoutubeIcon() {
        logger.info("Clicking on YouTube icon");
        home = new P03_homePage(driver);
        home.clickYoutubeIcon();
        home.switchToNewTab();
        logger.info("YouTube icon clicked and new tab opened");
    }

    @Then("verify the youtube url")
    public void youtubeURL() {
        logger.info("Verifying YouTube URL");
        
        String expectedUrl = "https://www.youtube.com/user/nopCommerce";
        String actualUrl = driver.getCurrentUrl();
        
        logger.info("Expected URL: {}", expectedUrl);
        logger.info("Actual URL: {}", actualUrl);
        
        Assert.assertEquals(actualUrl, expectedUrl, 
                           "Should be redirected to YouTube page");
        
        logger.info("YouTube URL verification completed");
    }

    @Then("social media icons should be displayed")
    public void social_media_icons_should_be_displayed() {
        logger.info("Verifying social media icons are displayed");
        
        boolean isFacebookDisplayed = home.isFacebookIconDisplayed();
        boolean isTwitterDisplayed = home.isTwitterIconDisplayed();
        boolean isRssDisplayed = home.isRssIconDisplayed();
        boolean isYoutubeDisplayed = home.isYoutubeIconDisplayed();
        
        Assert.assertTrue(isFacebookDisplayed, "Facebook icon should be displayed");
        Assert.assertTrue(isTwitterDisplayed, "Twitter icon should be displayed");
        Assert.assertTrue(isRssDisplayed, "RSS icon should be displayed");
        Assert.assertTrue(isYoutubeDisplayed, "YouTube icon should be displayed");
        
        logger.info("All social media icons are displayed");
    }

    @Then("social media icons should be clickable")
    public void social_media_icons_should_be_clickable() {
        logger.info("Verifying social media icons are clickable");
        
        boolean isFacebookClickable = home.isFacebookIconClickable();
        boolean isTwitterClickable = home.isTwitterIconClickable();
        boolean isRssClickable = home.isRssIconClickable();
        boolean isYoutubeClickable = home.isYoutubeIconClickable();
        
        Assert.assertTrue(isFacebookClickable, "Facebook icon should be clickable");
        Assert.assertTrue(isTwitterClickable, "Twitter icon should be clickable");
        Assert.assertTrue(isRssClickable, "RSS icon should be clickable");
        Assert.assertTrue(isYoutubeClickable, "YouTube icon should be clickable");
        
        logger.info("All social media icons are clickable");
    }

    @Then("new tab should be opened for external links")
    public void new_tab_should_be_opened_for_external_links() {
        logger.info("Verifying new tab opens for external links");
        
        int originalTabCount = driver.getWindowHandles().size();
        
        // Click on Facebook icon
        home.clickFacebookIcon();
        
        int newTabCount = driver.getWindowHandles().size();
        
        Assert.assertTrue(newTabCount > originalTabCount, 
                         "New tab should be opened for external link");
        
        // Close the new tab
        home.closeCurrentTab();
        
        logger.info("New tab verification completed");
    }

    @Then("social media links should have correct href attributes")
    public void social_media_links_should_have_correct_href_attributes() {
        logger.info("Verifying social media link href attributes");
        
        String facebookHref = home.getFacebookIconHref();
        String twitterHref = home.getTwitterIconHref();
        String rssHref = home.getRssIconHref();
        String youtubeHref = home.getYoutubeIconHref();
        
        Assert.assertTrue(facebookHref.contains("facebook.com"), 
                         "Facebook href should contain facebook.com");
        Assert.assertTrue(twitterHref.contains("twitter.com"), 
                         "Twitter href should contain twitter.com");
        Assert.assertTrue(rssHref.contains("nopcommerce.com"), 
                         "RSS href should contain nopcommerce.com");
        Assert.assertTrue(youtubeHref.contains("youtube.com"), 
                         "YouTube href should contain youtube.com");
        
        logger.info("Social media href attributes verification completed");
    }
}
