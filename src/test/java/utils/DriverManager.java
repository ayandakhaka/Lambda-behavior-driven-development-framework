package utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

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

			// BrowserStack remote setup

			case "browserstack-chrome":
				driver = createBrowserStackDriver();
				String sessionId = ((RemoteWebDriver) DriverManager.getDriver()).getSessionId().toString();
				System.out.println("BrowserStack Video URL: https://automate.browserstack.com/sessions/" + sessionId + ".mp4");

				break;
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
	
	
    // 🌐 Create BrowserStack driver
    private static WebDriver createBrowserStackDriver() {
        String username = EnvironmentLoader.getEnv("BROWSERSTACK_USERNAME");
        String accessKey = EnvironmentLoader.getEnv("BROWSERSTACK_ACCESS_KEY");

        if (username == null || accessKey == null) {
            throw new RuntimeException("Missing BrowserStack credentials. Set BROWSERSTACK_USERNAME and BROWSERSTACK_ACCESS_KEY environment variables.");
        }

        String buildName = EnvironmentLoader.getEnv("BROWSERSTACK_BUILD_NAME");
        String sessionName = EnvironmentLoader.getEnv("BROWSERSTACK_SESSION_NAME");
        String browserName = EnvironmentLoader.getEnv("BROWSERSTACK_BROWSER_NAME");
        String browserVersion = EnvironmentLoader.getEnv("BROWSERSTACK_BROWSER_VERSION");
        String os = EnvironmentLoader.getEnv("BROWSERSTACK_OS");
        String osVersion = EnvironmentLoader.getEnv("BROWSERSTACK_OS_VERSION");

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browserName", browserName);
        caps.setCapability("browserVersion", browserVersion);

        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("os", os);
        bstackOptions.put("osVersion", osVersion);
        bstackOptions.put("buildName", buildName);
        bstackOptions.put("sessionName", sessionName);
        bstackOptions.put("seleniumVersion", "4.23.0");

        caps.setCapability("bstack:options", bstackOptions);

        try {
            String hubURL = "https://" + username + ":" + accessKey + "@hub-cloud.browserstack.com/wd/hub";
            return new RemoteWebDriver(new URL(hubURL), caps);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid BrowserStack Hub URL", e);
        }
    }
    // 🌐 Mark test status on BrowserStack
    public static void markTestStatus(String status, String reason) {
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript(
                "browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"" 
                + status + "\", \"reason\": \"" + reason + "\"}}");
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
