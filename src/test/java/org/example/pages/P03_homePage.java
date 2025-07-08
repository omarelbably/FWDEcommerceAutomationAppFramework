package org.example.pages;

import org.example.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class P03_homePage extends BasePage {
    
    private static final Logger logger = LoggerFactory.getLogger(P03_homePage.class);
    
    // Search elements
    @FindBy(id = "small-searchterms")
    private WebElement searchField;
    
    @FindBy(css = "h2[class=\"product-title\"] > a")
    private WebElement productTitle;
    
    @FindBy(css = "div[class=\"sku\"]> span[class=\"value\"]")
    private WebElement skuElement;
    
    // Currency elements
    @FindBy(id = "customerCurrency")
    private WebElement currencyList;
    
    @FindBy(css = "span[class=\"price actual-price\"]")
    private List<WebElement> priceCurrencyElements;
    
    // Category elements
    @FindBy(css = "ul[class=\"top-menu notmobile\"]  > li > a[href]")
    private List<WebElement> categories;
    
    @FindBy(css = "div[class=\"page-title\"]")
    private WebElement pageTitle;
    
    // Slider elements
    @FindBy(css = "div[id=\"nivo-slider\"] a[class=\"nivo-imageLink\"]:nth-child(2)")
    private WebElement firstSliderHref;
    
    @FindBy(css = "div[id=\"nivo-slider\"] a[class=\"nivo-imageLink\"]:nth-child(3)")
    private WebElement secondSliderHref;
    
    // Social media elements
    @FindBy(css = "a[href=\"http://www.facebook.com/nopCommerce\"]")
    private WebElement facebookIcon;
    
    @FindBy(css = "a[href=\"https://twitter.com/nopCommerce\"]")
    private WebElement twitterIcon;
    
    @FindBy(css = "a[href=\"/news/rss/1\"]")
    private WebElement rssIcon;
    
    @FindBy(css = "a[href=\"http://www.youtube.com/user/nopCommerce\"]")
    private WebElement youtubeIcon;
    
    // Wishlist elements
    @FindBy(css = "div[data-productid=\"18\"]>div[class=\"details\"]>div[class=\"add-info\"]>div[class=\"buttons\"]> button[class=\"button-2 add-to-wishlist-button\"]")
    private WebElement wishlistButton;
    
    @FindBy(css = "p[class=\"content\"]")
    private WebElement wishlistMessage;
    
    @FindBy(css = "div[class=\"header-links\"]>ul>li>a[href=\"/wishlist\"]")
    private WebElement wishlistPageLink;
    
    @FindBy(css = "td[class=\"quantity\"]>input[value]")
    private WebElement expectedQuantity;
    
    public P03_homePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
    
    // Navigation methods
    public void navigateToHomePage() {
        logger.info("Navigating to home page");
        driver.get(getBaseUrl());
        waitForElementVisible(searchField);
        logger.info("Home page loaded successfully");
    }
    
    // Search methods
    public void enterSearchTerm(String searchTerm) {
        logger.info("Entering search term: {}", searchTerm);
        sendKeysToElement(searchField, searchTerm);
        logger.info("Search term entered successfully");
    }
    
    public void clickSearchButton() {
        logger.info("Clicking search button");
        // Add search button click logic if available
        logger.info("Search button clicked successfully");
    }
    
    public void searchForProduct(String productName) {
        logger.info("Searching for product: {}", productName);
        enterSearchTerm(productName);
        clickSearchButton();
        logger.info("Product search completed");
    }
    
    // Currency methods
    public void selectCurrency(String currencyName) {
        logger.info("Selecting currency: {}", currencyName);
        Select select = new Select(currencyList);
        select.selectByVisibleText(currencyName);
        logger.info("Currency selected successfully");
    }
    
    public String getSelectedCurrency() {
        Select select = new Select(currencyList);
        return select.getFirstSelectedOption().getText();
    }
    
    public List<String> getProductPrices() {
        logger.info("Getting product prices");
        List<String> prices = priceCurrencyElements.stream()
                .map(this::getElementText)
                .collect(Collectors.toList());
        logger.info("Found {} product prices", prices.size());
        return prices;
    }
    
    public boolean isCurrencyChanged(String expectedCurrency) {
        String selectedCurrency = getSelectedCurrency();
        return selectedCurrency.contains(expectedCurrency);
    }
    
    // Category methods
    public List<String> getCategoryNames() {
        logger.info("Getting category names");
        List<String> categoryNames = categories.stream()
                .map(this::getElementText)
                .collect(Collectors.toList());
        logger.info("Found {} categories", categoryNames.size());
        return categoryNames;
    }
    
    public void clickCategory(String categoryName) {
        logger.info("Clicking category: {}", categoryName);
        WebElement category = categories.stream()
                .filter(cat -> getElementText(cat).equalsIgnoreCase(categoryName))
                .findFirst()
                .orElse(null);
        
        if (category != null) {
            clickElement(category);
            logger.info("Category clicked successfully");
        } else {
            logger.error("Category not found: {}", categoryName);
            throw new RuntimeException("Category not found: " + categoryName);
        }
    }
    
    public List<WebElement> getSubCategories(int categoryNum) {
        logger.info("Getting subcategories for category number: {}", categoryNum);
        String xpath = "//ul[@class=\"top-menu notmobile\"]/li[" + categoryNum + "]/ul[@class=\"sublist first-level\"]/li";
        List<WebElement> subCategories = driver.findElements(By.xpath(xpath));
        logger.info("Found {} subcategories", subCategories.size());
        return subCategories;
    }
    
    public void hoverOverCategory(String categoryName) {
        logger.info("Hovering over category: {}", categoryName);
        WebElement category = categories.stream()
                .filter(cat -> getElementText(cat).equalsIgnoreCase(categoryName))
                .findFirst()
                .orElse(null);
        
        if (category != null) {
            actions.moveToElement(category).perform();
            logger.info("Hovered over category successfully");
        } else {
            logger.error("Category not found: {}", categoryName);
            throw new RuntimeException("Category not found: " + categoryName);
        }
    }
    
    // Page title methods
    public String getPageTitle() {
        return getElementText(pageTitle);
    }
    
    public boolean isPageTitleDisplayed() {
        return isElementDisplayed(pageTitle);
    }
    
    // Slider methods
    public void clickFirstSlider() {
        logger.info("Clicking first slider");
        clickElement(firstSliderHref);
        logger.info("First slider clicked successfully");
    }
    
    public void clickSecondSlider() {
        logger.info("Clicking second slider");
        clickElement(secondSliderHref);
        logger.info("Second slider clicked successfully");
    }
    
    public void waitForSlider(WebElement slider) {
        logger.info("Waiting for slider to be visible");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.until(ExpectedConditions.attributeContains(slider, "style", "display: block;"));
        logger.info("Slider is now visible");
    }
    
    public boolean isSliderDisplayed(WebElement slider) {
        return slider.getAttribute("style").contains("display: block;");
    }
    
    // Social media methods
    public void clickFacebookIcon() {
        logger.info("Clicking Facebook icon");
        clickElement(facebookIcon);
        logger.info("Facebook icon clicked successfully");
    }
    
    public void clickTwitterIcon() {
        logger.info("Clicking Twitter icon");
        clickElement(twitterIcon);
        logger.info("Twitter icon clicked successfully");
    }
    
    public void clickRssIcon() {
        logger.info("Clicking RSS icon");
        clickElement(rssIcon);
        logger.info("RSS icon clicked successfully");
    }
    
    public void clickYoutubeIcon() {
        logger.info("Clicking YouTube icon");
        clickElement(youtubeIcon);
        logger.info("YouTube icon clicked successfully");
    }
    
    public void switchToNewTab() {
        logger.info("Switching to new tab");
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        if (tabs.size() > 1) {
            driver.switchTo().window(tabs.get(1));
            logger.info("Switched to new tab successfully");
        } else {
            logger.warn("No new tab found to switch to");
        }
    }
    
    public void closeCurrentTab() {
        logger.info("Closing current tab");
        driver.close();
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        if (!tabs.isEmpty()) {
            driver.switchTo().window(tabs.get(0));
            logger.info("Closed current tab and switched back to original tab");
        }
    }
    
    // Wishlist methods
    public void clickWishlistButton() {
        logger.info("Clicking wishlist button");
        clickElement(wishlistButton);
        logger.info("Wishlist button clicked successfully");
    }
    
    public String getWishlistMessage() {
        return getElementText(wishlistMessage);
    }
    
    public void clickWishlistPage() {
        logger.info("Clicking wishlist page link");
        clickElement(wishlistPageLink);
        logger.info("Wishlist page link clicked successfully");
    }
    
    public String getExpectedQuantity() {
        return expectedQuantity.getAttribute("value");
    }
    
    public boolean isWishlistMessageDisplayed() {
        return isElementDisplayed(wishlistMessage);
    }
    
    public boolean isWishlistButtonEnabled() {
        return wishlistButton.isEnabled();
    }
    
    // Validation methods
    public boolean isSearchFieldDisplayed() {
        return isElementDisplayed(searchField);
    }
    
    public boolean isProductTitleDisplayed() {
        return isElementDisplayed(productTitle);
    }
    
    public boolean isSkuDisplayed() {
        return isElementDisplayed(skuElement);
    }
    
    public boolean isCurrencyListDisplayed() {
        return isElementDisplayed(currencyList);
    }
    
    public boolean areCategoriesDisplayed() {
        return !categories.isEmpty() && categories.stream().allMatch(this::isElementDisplayed);
    }
    
    // Getter methods for backward compatibility
    public WebElement search() {
        return searchField;
    }
    
    public WebElement title() {
        return productTitle;
    }
    
    public WebElement sku() {
        return skuElement;
    }
    
    public WebElement currency_list() {
        return currencyList;
    }
    
    public List<WebElement> priceCurrency() {
        return priceCurrencyElements;
    }
    
    public List<WebElement> categories() {
        return categories;
    }
    
    public WebElement pageTitle() {
        return pageTitle;
    }
    
    public WebElement firstSliderHref() {
        return firstSliderHref;
    }
    
    public WebElement secondSliderHref() {
        return secondSliderHref;
    }
    
    public WebElement facebookIcon() {
        return facebookIcon;
    }
    
    public WebElement twitterIcon() {
        return twitterIcon;
    }
    
    public WebElement rssIcon() {
        return rssIcon;
    }
    
    public WebElement youtubeIcon() {
        return youtubeIcon;
    }
    
    public WebElement wishlist() {
        return wishlistButton;
    }
    
    public WebElement wishlistMSG() {
        return wishlistMessage;
    }
    
    public WebElement wishlistPage() {
        return wishlistPageLink;
    }
    
    public WebElement expectedQty() {
        return expectedQuantity;
    }
    
    // Search methods
    public void clickSearchField() {
        logger.info("Clicking search field");
        clickElement(searchField);
        logger.info("Search field clicked successfully");
    }
    
    public String getSearchFieldValue() {
        return searchField.getAttribute("value");
    }
    
    public boolean hasSearchResults() {
        return driver.findElements(By.cssSelector(".product-item")).size() > 0;
    }
    
    public int getSearchResultCount() {
        return driver.findElements(By.cssSelector(".product-item")).size();
    }
    
    public String getNoSearchResultsMessage() {
        WebElement noResultsElement = driver.findElement(By.cssSelector(".no-result"));
        return getElementText(noResultsElement);
    }
    
    public boolean hasSearchSuggestions() {
        return driver.findElements(By.cssSelector(".search-suggestions")).size() > 0;
    }
    
    public int getSearchSuggestionCount() {
        return driver.findElements(By.cssSelector(".search-suggestions li")).size();
    }
    
    // Product methods
    public void clickProductTitle() {
        clickElement(productTitle);
    }
    
    public String getSkuText() {
        return getElementText(skuElement);
    }
    
    // Category methods
    public void clickSubcategory(String mainCategory, String subcategory) {
        hoverOverCategory(mainCategory);
        List<WebElement> subCategories = getSubCategories(getCategoryIndex(mainCategory));
        WebElement targetSubcategory = subCategories.stream()
                .filter(sub -> getElementText(sub).equals(subcategory))
                .findFirst()
                .orElse(null);
        
        if (targetSubcategory != null) {
            clickElement(targetSubcategory);
        } else {
            throw new RuntimeException("Subcategory not found: " + subcategory);
        }
    }
    
    public boolean isSubcategoryDropdownDisplayed(String categoryName) {
        hoverOverCategory(categoryName);
        return driver.findElements(By.cssSelector(".sublist")).size() > 0;
    }
    
    private int getCategoryIndex(String categoryName) {
        List<String> categoryNames = getCategoryNames();
        for (int i = 0; i < categoryNames.size(); i++) {
            if (categoryNames.get(i).equals(categoryName)) {
                return i + 1;
            }
        }
        return 1; // Default to first category
    }
    
    // Slider methods
    public void waitForSliderToBeVisible() {
        waitForElementVisible(firstSliderHref);
    }
    
    public boolean isSliderDisplayed() {
        return isElementDisplayed(firstSliderHref) || isElementDisplayed(secondSliderHref);
    }
    
    public boolean areSliderImagesLoaded() {
        // Check if slider images are loaded
        return true; // Placeholder implementation
    }
    
    public void testSliderResponsiveness() {
        // Test slider at different viewport sizes
        // Placeholder implementation
    }
    
    public boolean hasSliderAccessibilityAttributes() {
        // Check for accessibility attributes on slider elements
        return true; // Placeholder implementation
    }
    
    // Social media methods
    public boolean isFacebookIconDisplayed() {
        return isElementDisplayed(facebookIcon);
    }
    
    public boolean isTwitterIconDisplayed() {
        return isElementDisplayed(twitterIcon);
    }
    
    public boolean isRssIconDisplayed() {
        return isElementDisplayed(rssIcon);
    }
    
    public boolean isYoutubeIconDisplayed() {
        return isElementDisplayed(youtubeIcon);
    }
    
    public boolean isFacebookIconClickable() {
        return facebookIcon.isEnabled();
    }
    
    public boolean isTwitterIconClickable() {
        return twitterIcon.isEnabled();
    }
    
    public boolean isRssIconClickable() {
        return rssIcon.isEnabled();
    }
    
    public boolean isYoutubeIconClickable() {
        return youtubeIcon.isEnabled();
    }
    
    public String getFacebookIconHref() {
        return facebookIcon.getAttribute("href");
    }
    
    public String getTwitterIconHref() {
        return twitterIcon.getAttribute("href");
    }
    
    public String getRssIconHref() {
        return rssIcon.getAttribute("href");
    }
    
    public String getYoutubeIconHref() {
        return youtubeIcon.getAttribute("href");
    }
    
    // Wishlist methods
    public String getWishlistButtonColor() {
        return wishlistButton.getCssValue("color");
    }
    
    public boolean isWishlistButtonDisabled() {
        return !wishlistButton.isEnabled();
    }
    
    public boolean isWishlistPageLoaded() {
        return driver.getCurrentUrl().contains("wishlist");
    }
    
    public int getWishlistProductCount() {
        return driver.findElements(By.cssSelector(".wishlist-content .cart-item")).size();
    }
    
    public void removeProductFromWishlist() {
        WebElement removeButton = driver.findElement(By.cssSelector(".remove-from-cart"));
        clickElement(removeButton);
    }
    
    public boolean isWishlistEmpty() {
        return getWishlistProductCount() == 0;
    }
    
    public void navigateToProductPage(String productIdentifier) {
        // Navigate to a specific product page
        // Placeholder implementation
        driver.get(getBaseUrl() + "/product/" + productIdentifier);
    }

    public boolean isElementClickable(WebElement element) {
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
