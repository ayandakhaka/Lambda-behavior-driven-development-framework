package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import utils.DriverManager;

/**
 * Page Object Model (POM) class representing the LambdaTest E-commerce Home Page.
 * This class contains all elements and actions that can be performed on the home page.
 */
public class LambdaTestHomePage {

    // WebDriver instance for browser interactions
    private WebDriver driver;

    // Explicit wait for synchronization
    private WebDriverWait wait;

    // Actions class for performing mouse hover and advanced user interactions
    private Actions actions;

    /**
     * Constructor initializes WebDriver, WebDriverWait, Actions,
     * and initializes web elements using PageFactory.
     */
    public LambdaTestHomePage() {
        driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    // ====== Web Elements (Page Locators) ======

    @FindBy(xpath = "//a[@data-toggle='dropdown']//span[contains(text(),'My account')]")
    private WebElement myAccount;

    @FindBy(linkText = "Register")
    private WebElement registerLink;

    @FindBy(xpath = "//span[@class='title' and normalize-space(text())='Login']")
    private WebElement loginLink;

    @FindBy(xpath = "//span[normalize-space()='Logout']")
    private WebElement logoutLink;

    @FindBy(xpath = "//span[@class='title' and normalize-space()='Mega Menu']")
    private WebElement megaMenu;

    @FindBy(xpath = "//*[@class='lazy-load' and @alt='iMac']")
    private WebElement imacImage;


    // ====== Page Actions ======

    /**
     * Hovers over the "My Account" dropdown menu to display account options.
     */
    public void hoverOverMyAccountMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(myAccount));
        actions.moveToElement(myAccount).perform();
    }

    /**
     * Clicks on the iMac image from the homepage.
     */
    public void clickImacImage() {
        wait.until(ExpectedConditions.elementToBeClickable(imacImage));
        actions.moveToElement(imacImage).perform();
        imacImage.click();
    }

    /**
     * Hovers over the "Mega Menu" section.
     */
    public void hoverOverMegaMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(megaMenu));
        actions.moveToElement(megaMenu).perform();
    }

    /**
     * Clicks on the "Register" link under the "My Account" dropdown.
     */
    public void clickRegisterLink() {
        // Hover again to ensure the dropdown is open
        actions.moveToElement(myAccount).perform();
        wait.until(ExpectedConditions.visibilityOf(registerLink));
        wait.until(ExpectedConditions.elementToBeClickable(registerLink));
        registerLink.click();
    }

    /**
     * Clicks on the "Login" link under the "My Account" dropdown.
     */
    public void clickLoginLink() {
        // Hover again to ensure the dropdown is open
        actions.moveToElement(myAccount).perform();
        // Wait until Login link is clickable and click it
        WebElement login = wait.until(ExpectedConditions.elementToBeClickable(loginLink));
        login.click();
    }

    /**
     * Checks whether the user is currently logged in
     * by verifying if the Logout link is visible.
     *
     * @return true if the Logout link is displayed, false otherwise
     */
    public boolean isUserLoggedIn() {
        try {
            return logoutLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
