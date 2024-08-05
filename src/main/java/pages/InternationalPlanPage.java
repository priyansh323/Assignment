package pages;

import constants.ProjectConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InternationalPlanPage {
    By DropDownSearchFilters = By.xpath("//div[@class='flex-v fieldset']");
    By HeadingVisaDocuments = By.xpath("//p[text()='Visa & Documents']");
    By DropDownToLocation = By.xpath("//div[@class='picker-option ']//*[contains(@src,'location-icon')]");
    By TextBoxToLocation = By.xpath("//div[@class='picker-option ']//input[@placeholder='Enter City']");
    By DDOptionFirstToCity = By.xpath("//div[@class='picker-option ']//ul[@class='flex-v']/div[1]");
    By SliderDuration = By.xpath("//div[@class='trip-duration']//div[@class='rangeslider__handle']");
    By TextDatesPrice = By.xpath("//ul[@class='tripFareCalDates']//p[@class='calPrice']");
    By TablePriceWithDates = By.xpath("//ul[@class='tripFareCalDates']");

    By DateLowestPrice = By.xpath("//ul[@class='tripFareCalDates']//*[@class='selected']");
    By SectionAvailableFlights = By.xpath("//div[@class='roundTripFlightsSection'][1]");
    String OptionXpath = "//*[@class='options']//*[contains(text(),'$(option)')]";
    String MonthXpath ="//*[contains(text(),'$(date)')]";
    String ButtonTextXpath ="//button[contains(text(),'$(text)')]";



    WebDriver driver;
    WebDriverWait wait;

    public InternationalPlanPage(WebDriver driver) {
        this.driver=driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public boolean isOnInternationaPlanPage(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(HeadingVisaDocuments)).isDisplayed();
    }

    public void applySearchFilters() throws InterruptedException {
        List<WebElement> filters = driver.findElements(DropDownSearchFilters);
        wait.until(ExpectedConditions.visibilityOf(filters.get(0))).click();

        //For xpath resuability
        String OptionRoundTrip=OptionXpath.replace("$(option)", ProjectConstants.TRIP_TYPE_ROUND.get());
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OptionRoundTrip))).click();

        wait.until(ExpectedConditions.elementToBeClickable(filters.get(2))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(TextBoxToLocation)).sendKeys(ProjectConstants.DESTINATION_DUBAI.get());
        Thread.sleep(2000);
        wait.until(ExpectedConditions.elementToBeClickable(DDOptionFirstToCity)).click();

        //selecting month
        wait.until(ExpectedConditions.elementToBeClickable(filters.get(3))).click();
        String OptionDate=MonthXpath.replace("$(date)","December, 2024" );
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OptionDate))).click();

        //selecting Duration
        Actions ac = new Actions(driver);
        ac.clickAndHold(wait.until(ExpectedConditions.visibilityOfElementLocated(SliderDuration))).moveByOffset(110,0).release().build().perform();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ButtonTextXpath.replace("$(text)","Apply")))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ButtonTextXpath.replace("$(text)","Search")))).click();
    }

    public void retrievePrices()  {
        Map<Integer,String> datesWithPrice = new LinkedHashMap<>();
        List<WebElement> dateElements =wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(TextDatesPrice));

        for(int i=0; i<dateElements.size();i++){
            int key = i+1;
            WebElement element = dateElements.get(i);
            String value = element.getText();
            datesWithPrice.put(key,value);
        }
        //removing keys with - value
        Set<Integer> keysToRemove = datesWithPrice.entrySet().stream()
                .filter(entry -> "-".equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        keysToRemove.forEach(datesWithPrice::remove);

        //removing special characters from value
        Map<Integer, String> updatedMap = datesWithPrice.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().replaceAll("[₹,\\s]", "")
                ));
        datesWithPrice.clear();
        datesWithPrice.putAll(updatedMap);

        List<String> prices = datesWithPrice.values().stream()
                .collect(Collectors.toList());

        // getting Median Price
        Collections.sort(prices);
        int medianPrice = calculateMedian(prices);

        //getting weekend Dates of the Month
        int[] weekendDates = getWeekendDates(2024,Month.DECEMBER);

        //getting Dates which are lower than Median Prices
        List<Integer> datesLessMedian = datesWithPrice.entrySet().stream()
                .filter(entry -> Integer.parseInt(entry.getValue()) < medianPrice)
                .map(Map.Entry::getKey)
                .toList();

        //Matching if the dates less than Median is weekend Day
        Optional<Integer> matchingDate = datesLessMedian.stream()
                .filter(mDate -> Arrays.stream(weekendDates).anyMatch(value -> value == mDate))
                .findFirst();

        if(matchingDate.isPresent()){
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//ul[@class='tripFareCalDates']//li["+matchingDate.get()+"]"))).click();
        }
        else {
            wait.until(ExpectedConditions.elementToBeClickable(DateLowestPrice)).click();
        }

    }

    public int calculateMedian(List<String> prices){
        int median ;
        int size = prices.size();
        if(prices.size()%2==0){
            median = Integer.parseInt(prices.get(size/2))+Integer.parseInt(prices.get((size/2)-1))/2;
        }
        else{
            median = Integer.parseInt(prices.get(size/2));
        }
        return median;
    }

    public int[] getWeekendDates(int year, Month month) {
     int[] weekendDates = IntStream.rangeClosed(1, YearMonth.of(year, month).lengthOfMonth())
                .mapToObj(day -> LocalDate.of(year, month, day))
                .filter(date -> date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
                .mapToInt(LocalDate::getDayOfMonth)
                .toArray();
        return weekendDates;
    }

    public boolean isFlightsDisplayed(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(SectionAvailableFlights)).isDisplayed();
    }

    public boolean isSearchResultDisplayed(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(TablePriceWithDates)).isDisplayed();
    }
}
