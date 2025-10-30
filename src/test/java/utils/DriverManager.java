package utils;

import java.io.File;

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
    private static final String DRIVER_PATH = System.getProperty("user.dir") + File.separator + "drivers";

    public static WebDriver getDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }

    public static void initializeDriver() {
        if (driver != null) return;
        
        String browser = ConfigReader.get("browser").toLowerCase();
        boolean isCI = Boolean.parseBoolean(System.getenv("CI")); // true in GitHub Actions

        try {
            switch (browser) {
                case "chrome":
                case "chrome-headless":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (isCI || browser.equals("chrome-headless")) {
                        chromeOptions.addArguments("--headless=new");
                        chromeOptions.addArguments("--disable-gpu");
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--disable-dev-shm-usage");
                        chromeOptions.addArguments("--window-size=1920,1080");
                    }
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(chromeOptions);
                    break;

                case "firefox":
                case "firefox-headless":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (isCI || browser.equals("firefox-headless")) {
                        firefoxOptions.addArguments("--headless");
                        firefoxOptions.addArguments("--window-size=1920,1080");
                    }
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver(firefoxOptions);
                    break;

                case "edge":
                case "edge-headless":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (isCI || browser.equals("edge-headless")) {
                        edgeOptions.addArguments("--headless=new");
                        edgeOptions.addArguments("--no-sandbox");
                        edgeOptions.addArguments("--disable-dev-shm-usage");
                        edgeOptions.addArguments("--window-size=1920,1080");
                    }
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver(edgeOptions);
                    break;

                default:
                    throw new RuntimeException("Unsupported browser: " + browser);
            }

            if (!isCI) {
                driver.manage().window().maximize(); // maximize only locally
            }

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
        getDriver().get(url);
    }
}
