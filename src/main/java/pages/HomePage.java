package pages;

import constants.ProjectConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    By TitleMMT = By.xpath("//*[@data-cy='mmtLogo']");
    By ButtonCloseLoginPrompt = By.xpath("//span[@class='commonModal__close']");
    By DropDownToLocation = By.xpath("//*[@for='toCity']");
    By DropDownFromLocation = By.xpath("//*[@for='fromCity']");
    By DDOptionPlanInternational = By.xpath("//p[text()='Planning an international holiday?']");
    By TextBoxFromLocation = By.xpath("//input[@placeholder='From']");
    By OptionFirstLocation =By.xpath("//ul[@role='listbox']/li[1]");
    WebDriver driver;
    WebDriverWait wait;

    public HomePage(WebDriver driver) {
        this.driver=driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void navigateToInternationalPlan()  {
        wait.until(ExpectedConditions.elementToBeClickable(DDOptionPlanInternational)).click();
    }

    public boolean isOnHomePage() {
        closeLoginPrompt();
        return wait.until(ExpectedConditions.visibilityOfElementLocated(TitleMMT)).isDisplayed();
    }

    public boolean isInternationaPlanOptionDisplayed() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(DropDownToLocation)).click();
        return wait.until(ExpectedConditions.visibilityOfElementLocated(DDOptionPlanInternational)).isDisplayed();
    }

    public void closeLoginPrompt(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(ButtonCloseLoginPrompt)).click();
    }

    public void selectFromLocation() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(DropDownFromLocation)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(TextBoxFromLocation)).sendKeys(ProjectConstants.DESTINATION_FROM.get());
        Thread.sleep(3000);
        wait.until(ExpectedConditions.elementToBeClickable(OptionFirstLocation)).click();
    }
}
