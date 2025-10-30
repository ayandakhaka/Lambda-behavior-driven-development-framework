package hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Scenario;
import utils.DriverManager;
import utils.ExtentManager;

public class Hooks {

    @BeforeAll
    public static void setupReport() {
        System.out.println("🟢 Initializing Extent Report...");
        ExtentManager.getInstance(); // Initialize Extent report
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        System.out.println("🧩 Starting Scenario: " + scenario.getName());
        ExtentManager.createTest(scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            ExtentManager.getTest().fail("❌ Scenario failed: " + scenario.getName());
        } else {
            ExtentManager.getTest().pass("✅ Scenario passed: " + scenario.getName());
        }
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("🧾 Flushing Extent Report...");
        ExtentManager.flush(); // 🔥 Writes ExtentReport.html
        DriverManager.quitDriver(); // 🚪 Quit WebDriver
    }
}
