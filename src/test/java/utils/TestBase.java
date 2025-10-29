package utils;

import java.time.Duration;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestBase {

    protected WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    public TestBase() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
    }

    /**
     * Clicks an element safely with explicit waits and hover handling.
     *
     * @param element      The WebElement to click.
     * @param description  A readable description for logging.
     * @param hoverFirst   Whether to attempt hovering before clicking.
     * @param timeoutInSec Timeout in seconds for waits.
     */
    public void clickElement(WebElement element, String description, boolean hoverFirst, int timeoutInSec) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSec));

            // Wait for the element to be present in the DOM
            customWait.until(ExpectedConditions.presenceOfElementLocated(getByFromElement(element)));

            // Hover if requested or element not visible
            if (hoverFirst || !element.isDisplayed()) {
                actions.moveToElement(element).pause(Duration.ofMillis(300)).perform();
            }

            // Wait until element is visible and clickable
            customWait.until(ExpectedConditions.visibilityOf(element));
            customWait.until(ExpectedConditions.elementToBeClickable(element));

            element.click();
            System.out.println("✅ Clicked: " + description);

        } catch (TimeoutException e) {
            System.err.println("⏳ Timeout: Failed to click " + description + " within " + timeoutInSec + " seconds.");
            throw e;
        } catch (ElementClickInterceptedException e) {
            System.err.println("⚠️ Element intercepted while clicking: " + description + ". Retrying with JS click...");
            jsClick(element);
        } catch (Exception e) {
            System.err.println("❌ Failed to click element: " + description + " → " + e.getMessage());
            throw e;
        }
    }

    /**
     * Perform a JavaScript-based click (fallback).
     */
    private void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Extracts the By locator from a WebElement for better wait handling.
     */
    private By getByFromElement(WebElement element) {
        String elementDesc = element.toString();
        try {
            String locator = elementDesc.substring(elementDesc.indexOf("->") + 3, elementDesc.length() - 1);
            String[] parts = locator.split(": ");
            String locatorType = parts[0];
            String locatorValue = parts[1];

            switch (locatorType) {
                case "xpath":
                    return By.xpath(locatorValue);
                case "css selector":
                    return By.cssSelector(locatorValue);
                case "id":
                    return By.id(locatorValue);
                case "link text":
                    return By.linkText(locatorValue);
                case "partial link text":
                    return By.partialLinkText(locatorValue);
                case "tag name":
                    return By.tagName(locatorValue);
                case "name":
                    return By.name(locatorValue);
                default:
                    return By.xpath(locatorValue);
            }
        } catch (Exception e) {
            // fallback if parsing fails
            return By.xpath("//*");
        }
    }
    
    /**
     * Safely sends text input to an element with waits and logging.
     *
     * @param element      The WebElement input field.
     * @param value        The text value to send.
     * @param description  A readable description for logging.
     * @param timeoutInSec Timeout in seconds for waits.
     */
    public void sendKeys(WebElement element, String value, String description, int timeoutInSec) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSec));

            // Wait until element is visible and enabled
            customWait.until(ExpectedConditions.visibilityOf(element));
            customWait.until(ExpectedConditions.elementToBeClickable(element));

            // Clear any existing text before entering new value
            element.clear();
            element.sendKeys(value);

            System.out.println("✅ Typed into: " + description + " → " + value);

        } catch (TimeoutException e) {
            System.err.println("⏳ Timeout: Failed to send keys to " + description + " within " + timeoutInSec + " seconds.");
            throw e;
        } catch (ElementNotInteractableException e) {
            System.err.println("⚠️ Element not interactable: " + description + ". Retrying with JS...");
            jsSendKeys(element, value);
        } catch (Exception e) {
            System.err.println("❌ Failed to send keys to: " + description + " → " + e.getMessage());
            throw e;
        }
    }

    /**
     * JavaScript fallback for sendKeys (useful for tricky inputs).
     */
    private void jsSendKeys(WebElement element, String value) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", element, value);
    }

    /**
     * Clears a textbox using keyboard shortcuts (Ctrl + A + Delete).
     *
     * @param element The WebElement input field to clear.
     */
    public void clearTextBoxUsingKeys(WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(element));
            wait.until(ExpectedConditions.elementToBeClickable(element));

            // Focus on the element first
            element.click();
            // Select all text and delete
            element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            element.sendKeys(Keys.DELETE);

            System.out.println("🧹 Cleared textbox using keyboard for element: " + element.toString());
        } catch (Exception e) {
            System.err.println("⚠️ Failed to clear textbox using keyboard: " + e.getMessage());
            // Fallback to normal clear
            try {
                element.clear();
            } catch (Exception inner) {
                System.err.println("❌ Fallback clear() also failed: " + inner.getMessage());
            }
        }
    }
    
    /**
     * Validates the text of a WebElement against an expected value.
     *
     * @param element       The WebElement to validate.
     * @param expectedText  The expected string value.
     * @param description   A description for logging purposes.
     * @param timeoutInSec  Timeout in seconds for waiting.
     * @return true if text matches, false otherwise.
     */
    public boolean validateText(WebElement element, String expectedText, String description, int timeoutInSec) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSec));
            customWait.until(ExpectedConditions.visibilityOf(element));

            String actualText = element.getText().trim();
            
            System.out.println(actualText.trim() + " = " + expectedText.trim());

            if (actualText.equals(expectedText.trim())) {
                System.out.println("✅ Text validation passed for " + description + ": " + actualText);
                return true;
            } else {
                System.err.println("❌ Text validation failed for " + description + 
                                   ". Expected: '" + expectedText + "', but found: '" + actualText + "'");
                return false;
            }

        } catch (TimeoutException e) {
            System.err.println("⏳ Timeout waiting for element to be visible: " + description);
            return false;
        } catch (Exception e) {
            System.err.println("❌ Error validating text for " + description + ": " + e.getMessage());
            return false;
        }
    }

    

}
