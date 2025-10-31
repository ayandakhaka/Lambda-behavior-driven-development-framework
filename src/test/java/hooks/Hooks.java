package hooks;

import io.cucumber.java.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DriverManager;
import utils.ExtentManager;

public class Hooks {

    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

    @BeforeAll
    public static void setupReport() {
        logger.info("🟢 Initializing Extent Report...");
        ExtentManager.getInstance();
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        logger.info("🧩 Starting Scenario: {}", scenario.getName());
        ExtentManager.createTest(scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            logger.error("❌ Scenario failed: {}", scenario.getName());
            ExtentManager.getTest().fail("❌ Scenario failed: " + scenario.getName());
        } else {
            logger.info("✅ Scenario passed: {}", scenario.getName());
            ExtentManager.getTest().pass("✅ Scenario passed: " + scenario.getName());
        }
    }

    @AfterAll
    public static void tearDown() {
        logger.info("🧾 Flushing Extent Report and quitting WebDriver...");
        ExtentManager.flush();
        DriverManager.quitDriver();
    }
}
