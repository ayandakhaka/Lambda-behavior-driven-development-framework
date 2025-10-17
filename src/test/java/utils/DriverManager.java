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

        try {
            switch (browser) {
                case "chrome":
                    setupChromeDriver();
                    driver = new ChromeDriver(new ChromeOptions());
                    break;
                case "firefox":
                    setupFirefoxDriver();
                    driver = new FirefoxDriver(new FirefoxOptions());
                    break;
                case "edge":
                default:
                    setupEdgeDriver();
                    driver = new EdgeDriver(new EdgeOptions());
                    break;
            }
            driver.manage().window().maximize();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize WebDriver: " + e.getMessage(), e);
        }
    }

    private static void setupChromeDriver() {
        try {
            WebDriverManager.chromedriver()
                // Optional: set proxy if needed
                //.proxy("proxy.host:8080")
                .setup();
        } catch (Exception e) {
            // fallback to manually downloaded driver
            System.setProperty("webdriver.chrome.driver", "C:/drivers/chromedriver.exe");
        }
    }

    private static void setupFirefoxDriver() {
        try {
            WebDriverManager.firefoxdriver()
                //.proxy("proxy.host:8080")
                .setup();
        } catch (Exception e) {
            System.setProperty("webdriver.gecko.driver", "drivers/geckodriver.exe");
        }
    }

    private static void setupEdgeDriver() {
        try {
            WebDriverManager.edgedriver()
                //.proxy("proxy.host:8080")
                .setup();
        } catch (Exception e) {
            System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");
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
            throw new RuntimeException("Failed to launch URL : " + url, e);
        }
    }
}
