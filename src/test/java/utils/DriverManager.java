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
	// Path to the local driver folder inside your project
    private static final String DRIVER_PATH = System.getProperty("user.dir") + File.separator + "drivers";

    public static WebDriver getDriver() {
        if (driver == null) {
            initializeDriver();
        }
        return driver;
    }

    public static void initializeDriver() {
    	
    	if(driver != null) {
    		return; // Driver is already initialized)
    	}
    	
        String browser = ConfigReader.get("browser").toLowerCase();

        try {
            switch (browser) {
                case "chrome":
                case "chrome-headless":
                    System.setProperty("webdriver.chrome.driver", DRIVER_PATH + File.separator + "chromedriver.exe");
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (browser.equals("chrome-headless")) {
                        chromeOptions.addArguments("--headless=new");
                        chromeOptions.addArguments("--disable-gpu");
                        chromeOptions.addArguments("--no-sandbox");
                        chromeOptions.addArguments("--window-size=1920,1080");
                    }
                    driver = new ChromeDriver(chromeOptions);
                    break;

                case "firefox":
                case "firefox-headless":
                    System.setProperty("webdriver.gecko.driver", DRIVER_PATH + File.separator + "geckodriver.exe");
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (browser.equals("firefox-headless")) {
                        firefoxOptions.addArguments("--headless");
                        firefoxOptions.addArguments("--window-size=1920,1080");
                    }
                    driver = new FirefoxDriver(firefoxOptions);
                    break;

                case "edge":
                case "edge-headless":
                    System.setProperty("webdriver.edge.driver", DRIVER_PATH + File.separator + "msedgedriver.exe");
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (browser.equals("edge-headless")) {
                        edgeOptions.addArguments("--headless=new");
                        edgeOptions.addArguments("--no-sandbox");
                        edgeOptions.addArguments("--disable-dev-shm-usage");
                        edgeOptions.addArguments("--window-size=1920,1080");
                    }
                    driver = new EdgeDriver(edgeOptions);
                    break;

                default:
                    throw new RuntimeException("Unsupported browser: " + browser);
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
}
