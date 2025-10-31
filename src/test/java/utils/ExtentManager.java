package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.util.HashMap;
import java.util.Map;

public class ExtentManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    private static ExtentReports createInstance() {
        String reportPath = System.getProperty("user.dir") + "/target/extent-report.html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
        reporter.config().setDocumentTitle("Automation Report");
        reporter.config().setReportName("LambdaTest BDD Automation Results");

        extent = new ExtentReports();
        extent.attachReporter(reporter);
        return extent;
    }

    // ✅ Create a new test for the scenario
    public static void createTest(String name) {
        ExtentTest extentTest = getInstance().createTest(name);
        test.set(extentTest);
    }

    // ✅ Get current test
    public static ExtentTest getTest() {
        return test.get();
    }

    // ✅ Flush report
    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
