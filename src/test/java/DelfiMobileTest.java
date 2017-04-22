import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DelfiMobileTest {
    private static final By COUNTER = By.className("comment-count");
    private static final By ARTICLES = By.xpath("//h3[contains(@class, 'top2012-title')]");
    private static final By ARTICLE_HEADER = By.className("top2012-title");
    private static final By MOBILE_ARTICLES = By.className("md-mosaic-title");
    private static final By MOBILE_TITLE = By.className("md-scrollpos");
    private static final By MOBILE_COUNTER = By.className("commentCount");
    private static final Logger LOGGER = Logger.getLogger(DelfiMobileTest.class);

    /**
     * This test will compare comment count on main and mobile pages
     */
    @Test
    public void commentTestMobile() {
        LOGGER.info("We are starting our test");

        WebDriver driver = getDriver();

        LOGGER.info("We are opening rus.delfi.lv home page");
        driver.get("http://rus.delfi.lv");

        LOGGER.info("We are taking first 5 articles: names and comment count");
        List<WebElement> articles = driver.findElements(ARTICLES);
        Map<String, String> fiveArticles = new HashMap<String, String>();

        for (int i = 0; i < 5; i++) {
            if (articles.get(i).findElements(COUNTER).isEmpty()) {
                fiveArticles.put(articles.get(i).findElement(ARTICLE_HEADER).getText(), "0");
            } else {
                fiveArticles.put(articles.get(i).findElement(ARTICLE_HEADER).getText(), articles.get(i).findElement(COUNTER).getText());
            }
        }

        LOGGER.info("Here is our 5 articles:");
        for (Map.Entry entry : fiveArticles.entrySet()) {
            LOGGER.info(entry.getKey() + ": " + entry.getValue());
        }

        LOGGER.info("We are opening mobile version");
        driver.get("http://m.rus.delfi.lv");

        LOGGER.info("We are taking first 5 mobile articles: names and comment count");
        List<WebElement> mobileArticles = driver.findElements(MOBILE_ARTICLES);
        Map<String, String> mobileFiveArticles = new HashMap<String, String>();

        for (int i = 0; i < 5; i++) {
            if (mobileArticles.get(i).findElements(MOBILE_COUNTER).isEmpty()) {
                mobileFiveArticles.put(mobileArticles.get(i).findElement(MOBILE_TITLE).getText(), "0");
            } else {
                mobileFiveArticles.put(mobileArticles.get(i).findElement(MOBILE_TITLE).getText(), mobileArticles.get(i).findElement(MOBILE_COUNTER).getText());
            }
        }

        LOGGER.info("Checking comment count");
        Assert.assertEquals("Articles don't match", fiveArticles, mobileFiveArticles);
        LOGGER.info("All articles are OK");
    }

    /**
     * Creating WebDriver for the test
     *
     * @return - WebDriver
     */
    private WebDriver getDriver() {
        LOGGER.info("Setting global property for driver");
        System.setProperty("webdriver.gecko.driver", "C:/geckodriver.exe");

        LOGGER.info("Opening FireFox browser");
        WebDriver driver = new FirefoxDriver();
        driver.manage().window().maximize();

        return driver;
    }
}
