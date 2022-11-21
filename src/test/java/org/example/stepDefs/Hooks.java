package org.example.stepDefs;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.concurrent.TimeUnit;


public class Hooks {

    public static WebDriver driver;

    @Before
    public static void OpenBrowser() throws InterruptedException {
            String chromePath = System.getProperty("user.dir") + "\\src\\main\\resources\\chromedriver.exe";


            System.setProperty("webdriver.chrome.driver", chromePath);

            // 2 create mew object from webdriver
            driver = new ChromeDriver();

            // 3 Navigate to google.com + Maximize screen + Delay closing for 3seconds
            driver.navigate().to("https://demo.nopcommerce.com/");
            driver.manage().window().maximize();

            Thread.sleep(2000);


    }
    @After
    public void closeDriver() throws InterruptedException {
        Thread.sleep(5000);
        driver.quit();
    }
}
