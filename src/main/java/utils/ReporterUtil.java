package utils;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class ReporterUtil extends DriverUtil implements ITestListener, ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        report = new ExtentReports("./reports/" + suite.getName() + "_Report.html");
    }

    @Override
    public void onFinish(ISuite suite) {
        report.flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger = report.startTest(result.getMethod().getMethodName());
        logger.log(LogStatus.INFO, "Executing test: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.log(LogStatus.INFO, "Finished executing test");
    }


    @Override
    public void onTestFailure(ITestResult result) {
        String fileName = String.format("Screenshot-%s.jpg", Calendar.getInstance().getTimeInMillis());
         driver = (WebDriver)result.getTestContext().getAttribute("WebDriver");
        File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File("./screenshots/" + fileName);
        try {
            FileUtils.copyFile(srcFile, destFile);
            System.out.println("Screenshot taken, saved in screenshots folder");
        } catch(IOException e) {
            System.out.println("Failed to take screenshot");
        }
        logger.log(LogStatus.FAIL, "Test failed, attaching screenshot in screenshots folder");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.log(LogStatus.SKIP, "Test skipped");
    }

}

