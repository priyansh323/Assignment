package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.InternationalPlanPage;
import utils.DriverUtil;

public class InternationalPlanTest extends DriverUtil {
    HomePage hp;
    InternationalPlanPage ipp;
    @BeforeMethod
    public void init(){
        hp = new HomePage(driver);
        ipp = new InternationalPlanPage(driver);
    }


    @Test(priority =0)
    public void validateMMTHomePage(){
        Assert.assertTrue(hp.isOnHomePage());
    }

    @Test(priority = 1)
    public void validateInternationalPlanButton() throws InterruptedException {
        hp.selectFromLocation();
        Assert.assertTrue(hp.isInternationaPlanOptionDisplayed());
    }

    @Test(priority = 2)
    public void validateInternationalPlanFiltersAndSearchResult() throws InterruptedException {
        hp.navigateToInternationalPlan();
        Assert.assertTrue(ipp.isOnInternationaPlanPage());
        ipp.applySearchFilters();
        Assert.assertTrue(ipp.isSearchResultDisplayed());
    }

    @Test(priority = 3)
    public void verifyFlightsDisplayed() {
        ipp.retrievePrices();
        Assert.assertTrue(ipp.isFlightsDisplayed());
    }

}
