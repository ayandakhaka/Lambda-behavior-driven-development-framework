package utils;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CustomListener implements ITestListener {

    private static ExtentReports extent = ExtentManager.getInstance();
    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        System.out.println("🚀 Test Execution Started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flush();
        System.out.println("📊 Test Execution Finished: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentTest test = extent.createTest(testName);
        testThread.set(test);
        test.log(Status.INFO, "Starting test: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testThread.get().log(Status.PASS, "✅ Test passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testThread.get().log(Status.FAIL, "❌ Test failed: " + result.getThrowable());

        WebDriver driver = getDriverFromResult(result);
        if (driver != null) {
            try {
                String screenshotPath = captureScreenshot(driver, result.getMethod().getMethodName());
                testThread.get().addScreenCaptureFromPath(screenshotPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testThread.get().log(Status.SKIP, "⚠️ Test skipped: " + result.getMethod().getMethodName());
    }

    // Helper method to take a screenshot
    private String captureScreenshot(WebDriver driver, String methodName) throws IOException {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String screenshotDir = System.getProperty("user.dir") + "/target/screenshots/";
        Files.createDirectories(Paths.get(screenshotDir));
        String destPath = screenshotDir + methodName + ".png";
        Files.copy(srcFile.toPath(), Paths.get(destPath));
        return destPath;
    }

    // Helper method to extract WebDriver from test classes
    private WebDriver getDriverFromResult(ITestResult result) {
        Object testInstance = result.getInstance();
        try {
            return (WebDriver) testInstance.getClass().getDeclaredField("driver").get(testInstance);
        } catch (Exception e) {
            return null;
        }
    }
}
