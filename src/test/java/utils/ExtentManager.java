package utils;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

    private static ExtentReports extent;
    private static ExtentTest test;
    private static ExtentSparkReporter spark;

    // ✅ Initialize the ExtentReports instance
    public static ExtentReports getInstance() {
        if (extent == null) {
            String reportDir = System.getProperty("user.dir") + "/target/";
            new File(reportDir).mkdirs(); // Ensure directory exists)
            String reportPath = reportDir + "ExtentReport.html";
            spark = new ExtentSparkReporter(reportPath);
            spark.config().setDocumentTitle("LambdaTest Automation Report");
            spark.config().setReportName("UI Automation Results");
            spark.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Tester", "Ayanda Khaka");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Browser", "Edge");
        }
        return extent;
    }

    // ✅ Create a new test entry
    public static void createTest(String testName) {
        test = getInstance().createTest(testName);
    }

    // ✅ Return the current test
    public static ExtentTest getTest() {
        return test;
    }

    // ✅ Flush report (write it to disk)
    public static void flush() {
        if (extent != null) {
            extent.flush();
            System.out.println("📄 Extent Report generated at: target/ExtentReport.html");
        }
    }
}
