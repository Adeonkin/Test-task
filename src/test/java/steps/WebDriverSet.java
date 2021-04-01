package steps;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class WebDriverSet {

    WebDriver driver;

    public void runWebDriver() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

}