package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverManager {

    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }

    public static void initializeDriver() {
        String browser = System.getProperty("browser", "edge").toLowerCase();
        boolean isCI = isCI();

        try {
            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (isCI) {
                        chromeOptions.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
                    }
                    driver = new ChromeDriver(chromeOptions);
                    break;

                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (isCI) {
                        firefoxOptions.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080");
                    }
                    driver = new FirefoxDriver(firefoxOptions);
                    break;

                case "edge":
                default:
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (isCI) {
                        edgeOptions.addArguments("--headless=new", "--disable-gpu", "--window-size=1920,1080");
                    }
                    driver = new EdgeDriver(edgeOptions);
                    break;
            }

            driver.manage().window().maximize();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize WebDriver: " + e.getMessage(), e);
        }
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public static void launchURL(String url) {
        try {
            getDriver().get(url);
        } catch (Exception e) {
            throw new RuntimeException("Failed to launch URL: " + url, e);
        }
    }

    /**
     * Detect if running in a CI environment (GitHub Actions, Jenkins, etc.)
     */
    private static boolean isCI() {
        String ciEnv = System.getenv("CI");
        return ciEnv != null && ciEnv.equalsIgnoreCase("true");
    }
}
