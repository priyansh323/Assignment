package utils;


import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import constants.ProjectConstants;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class DriverUtil {
    public static WebDriver driver;
    public static ExtentTest logger;
    public static ExtentReports report;

    @BeforeClass(alwaysRun=true)
    public void setup(ITestContext context) {
        System.setProperty("webdriver.chrome.driver", ProjectConstants.CHROME_DRIVER_PATH.get());
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(ProjectConstants.MMT_URL.get());
        context.setAttribute("WebDriver",driver);
    }

    @AfterClass(alwaysRun=true)
    public void tearDown() {
        driver.quit();
    }
}
