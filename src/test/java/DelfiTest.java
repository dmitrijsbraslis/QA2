import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * Testing comments on delfi.lv
 */
public class DelfiTest {

    private static final By COUNTER = By.className("comment-count");
    private static final By COMMENT_PAGE_COUNTERS = By.xpath("//*[contains(@class, 'comment-thread-switcher-selected-reg ')]/a/span");
    private static final Logger LOGGER = Logger.getLogger(DelfiTest.class);
    private int commentCount;

    /**
     * This test will test comment count on main page and article page
     */
    @Test
    public void commentTesting() {
        LOGGER.info("We are starting our test");

        WebDriver driver = getDriver();

        LOGGER.info("We are opening rus.delfi.lv home page");
        driver.get("http://rus.delfi.lv");

        LOGGER.info("Getting comment count for the first article");
        commentCount = getCommentCount(driver);

        LOGGER.info("Open first article");
        driver.findElement(By.xpath("(//*[contains(@class, 'top2012-title')])[2]")).click();

        LOGGER.info("Getting comment count from title on article page");
        Assert.assertEquals("Wrong comment count on article page", commentCount,
                getCommentCount(driver), 0);

        LOGGER.info("Comment count on article page is correct!");

        LOGGER.info("Moving to article comment page");
        driver.findElement(COUNTER).click();
        boolean waitFor = (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return driver.findElement(COMMENT_PAGE_COUNTERS).isDisplayed();
            }
        });

        LOGGER.info("Getting registered users comment count");
        List<WebElement> counters = driver.findElements(COMMENT_PAGE_COUNTERS);
        int registComCnt = removeChars(counters.get(0).getText());

        LOGGER.info("Getting unregistered users comment count");
        int unRegistComCnt = removeChars(counters.get(1).getText());

        LOGGER.info("Checking whole comment count");
        Assert.assertEquals("Wrong comment count on comment page", commentCount, registComCnt+unRegistComCnt, 0);

        LOGGER.info("We are closing our browser");
        driver.quit();
    }

    /**
     * Returns comment count of article
     *
     * @return - comment count
     */
    private Integer getCommentCount(WebDriver driver) {
        WebElement counter = driver.findElement(COUNTER);
        String counterString = counter.getText();
        Integer count = removeChars(counterString);
        LOGGER.info("Article comment count is " + count);
        return count;
    }

    /**
     * Removes all unnecessary chars
     *
     * @return - comment count
     */
    private Integer removeChars(String cuntStr) {
        return Integer.parseInt(cuntStr.substring(1, cuntStr.length()-1));
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
