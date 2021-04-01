package steps;

import io.cucumber.java.en.Given;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SiteSteps extends WebDriverSet {


    @Given("^open site \"([^\"]+)\"$")
    public void openSite(String siteName) {
        runWebDriver();
        driver.get(siteName);
    }

    @Given("^the user is authorized with username \"([^\"]+)\" and password \"([^\"]+)\"$")
    public void inputUserName(String userName, String userPass) {
        driver.findElement(By.id("UserLogin_username")).sendKeys(userName);
        driver.findElement(By.id("UserLogin_password")).sendKeys(userPass);
        driver.findElement(By.xpath("//input[@type='submit']")).click();
        Assert.assertEquals("admin panel did not load", "Dashboard - Casino", driver.getTitle());
    }

    @Given("^open gamers table$")
    public void openGamers() {
        driver.findElement(By.xpath("//*[@id=\"nav\"]//span[text()='Users']")).click();
        driver.findElement(By.xpath("//a[contains(@href,'/user/player/admin')]")).click();
        Assert.assertEquals("Table 'Player management' not open", "Dashboard - Player management", driver.getTitle());
    }

    @Given("^check table sorting by column 'Name'$")
    public void checkSort() throws InterruptedException {
        driver.findElement(By.xpath("//input[@name='PlayerSearch[name]']")).sendKeys("tester");
        // click anywhere for sorting
        driver.findElement(By.xpath("//input[@name='PlayerSearch[surname]']")).click();
        // wait filter
        Thread.sleep(2000);
        List<String> textColumn = new ArrayList<>();
        for (WebElement element : driver.findElements(By.xpath("//*[@id=\"payment-system-transaction-grid\"]/table/tbody//td[4]"))) {
            textColumn.add(element.getText().toLowerCase(Locale.ROOT));
        }
        textColumn.forEach(x -> Assert.assertEquals(x, "tester"));
        driver.quit();
    }

}
