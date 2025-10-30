package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utils.ConfigReader;
import utils.DriverManager;
import utils.TestBase;

/**
 * Page Object Model (POM) class for the Login Page.
 * This class defines all elements and actions associated with the login functionality.
 * It leverages TestBase utilities for clean, reusable, and maintainable interactions.
 */
public class LoginPage {

    // WebDriver instance for interacting with the browser
    private WebDriver driver;

    // TestBase instance for common test utilities (click, sendKeys, validation, etc.)
    private TestBase testBase;

    /**
     * Constructor initializes WebDriver and PageFactory elements.
     */
    public LoginPage() {
        driver = DriverManager.getDriver();
        testBase = new TestBase();
        PageFactory.initElements(driver, this);
    }

    // ====== Web Elements ======
    // Using @CacheLookup for static elements to improve performance.
    // Avoid using @CacheLookup on dynamic elements that may reload or change during runtime.

    @FindBy(css = "input[name='email']")
    @CacheLookup
    private WebElement emailInput;

    @FindBy(css = "input[name='password']")
    @CacheLookup
    private WebElement passwordInput;

    @FindBy(css = "input[value='Login']")
    @CacheLookup
    private WebElement loginButton;

    @FindBy(linkText = "Logout")
    @CacheLookup
    private WebElement logoutLink;

    @FindBy(xpath = "//h2[text()='My Account']")
    @CacheLookup
    private WebElement myAccountHeader;

    @FindBy(css = ".alert.alert-danger.alert-dismissible")
    @CacheLookup
    private WebElement loginErrorMessage;

    @FindBy(xpath = "//a[contains(@class,'list-group-item') and contains(text(),'Edit Account')]")
    @CacheLookup
    private WebElement editAccountLink;

    @FindBy(xpath = "//*[@class='card-header h5']")
    @CacheLookup
    private WebElement myAccountHeaderText;

    @FindBy(css = "div.alert.alert-danger.alert-dismissible")
    @CacheLookup
    private WebElement invalidLoginWarning;


    // ====== Page Actions ======

    /**
     * Enters the user's email address into the email input field.
     * @param email The email address to input.
     */
    public void enterEmail(String email) {
        testBase.clearTextBoxUsingKeys(emailInput);
        testBase.sendKeys(
            emailInput,
            email,
            "Email entered in email input field",
            Integer.parseInt(ConfigReader.get("timeoutInSeconds"))
        );
    }

    /**
     * Enters the user's password into the password input field.
     * @param password The password to input.
     */
    public void enterPassword(String password) {
        testBase.clearTextBoxUsingKeys(passwordInput);
        testBase.sendKeys(
            passwordInput,
            password,
            "Password entered in password input field",
            Integer.parseInt(ConfigReader.get("timeoutInSeconds"))
        );
    }

    /**
     * Clicks on the Login button.
     */
    public void clickLoginButton() {
        testBase.clickElement(
            loginButton,
            "Clicks on Login button",
            true,
            Integer.parseInt(ConfigReader.get("timeoutInSeconds"))
        );
    }

    /**
     * Clicks on the Logout button after a successful login.
     */
    public void clickLogoutButton() {
        testBase.clickElement(
            logoutLink,
            "Clicks on logout button",
            true,
            Integer.parseInt(ConfigReader.get("timeoutInSeconds"))
        );
    }

    /**
     * Verifies if login was successful by checking for the "My Account" header.
     * @return true if login succeeded, false otherwise.
     */
    public boolean verifySuccessfulLogin() {
        return testBase.validateText(
            myAccountHeaderText,
            "My Account\r\n",
            "Verifying success login",
            Integer.parseInt(ConfigReader.get("timeoutInSeconds"))
        );
    }

    /**
     * Verifies if an invalid login message is displayed for wrong credentials.
     * @return true if invalid login message is displayed, false otherwise.
     */
    public boolean verifyInvalidLoginMessage() {
        return testBase.validateText(
            invalidLoginWarning,
            "Warning: No match for E-Mail Address and/or Password.\r\n",
            "Verifying invalid login message",
            Integer.parseInt(ConfigReader.get("timeoutInSeconds"))
        );
    }
}
