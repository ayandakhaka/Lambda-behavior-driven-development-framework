package Pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import utils.ConfigReader;
import utils.TestBase;

/**
 * RegisterPage class represents the "Register Account" page of the application.
 * 
 * It uses the Page Object Model (POM) design pattern and provides methods
 * to interact with the registration page UI elements.
 * 
 * This class handles various registration scenarios such as:
 *  - Valid registration
 *  - Invalid email format
 *  - Missing privacy policy agreement
 *  - Duplicate email registration
 *  - Password mismatch validation
 * 
 * Author: Ayanda Khaka
 * Date: October 2025
 */
public class RegisterPage {

    private WebDriver driver;
    private TestBase testBase;

    /**
     * Constructor initializes WebDriver instance and PageFactory elements.
     */
    public RegisterPage() {
        driver = utils.DriverManager.getDriver();
        testBase = new TestBase();
        PageFactory.initElements(driver, this);
    }

    // ===========================
    // Locators for registration page elements
    // ===========================

    /** First Name input field */
    @FindBy(name = "firstname")
    private WebElement firstNameInput;

    /** Last Name input field */
    @FindBy(name = "lastname")
    private WebElement lastNameInput;

    /** Email input field */
    @FindBy(name = "email")
    private WebElement emailInput;

    /** Telephone input field */
    @FindBy(name = "telephone")
    private WebElement telephoneInput;

    /** Password input field */
    @FindBy(name = "password")
    private WebElement passwordInput;

    /** Confirm Password input field */
    @FindBy(name = "confirm")
    private WebElement confirmPasswordInput;

    /** Privacy Policy checkbox */
    @FindBy(xpath = "//label[@for='input-agree']")
    private WebElement privacyPolicyCheckbox;

    /** Continue button */
    @FindBy(css = "input[value='Continue']")
    private WebElement continueButton;

    /** Success message header after registration */
    @FindBy(xpath = "//h1[contains(text(),'Your Account Has Been Created!')]")
    private WebElement successMessage;

    /** Logout link */
    @FindBy(xpath = "//span[normalize-space()='Logout']")
    private WebElement logoutLink;

    /** Duplicate or invalid email warning */
    @FindBy(css = "div.alert.alert-danger.alert-dismissible")
    private WebElement emailInvalidFormatWarning;

    /** Privacy Policy warning (same CSS as above, reused for validation) */
    @FindBy(css = "div.alert.alert-danger.alert-dismissible")
    private WebElement privacyPolicyWarning;

    /** Validation message below each invalid field */
    @FindBy(css = ".text-danger")
    private WebElement fieldValidationMessage;

    /** Generic error message elements */
    @FindBy(css = ".text-danger")
    private WebElement errorMessages;

    // Common timeout value from config.properties
    private final int timeout = Integer.parseInt(ConfigReader.get("timeoutInSeconds"));

    // ===========================
    // Actions / Page Methods
    // ===========================

    /** Clears and enters text into the First Name field */
    public void enterFirstName(String firstName) {
        testBase.clearTextBoxUsingKeys(firstNameInput);
        testBase.sendKeys(firstNameInput, firstName, "Entered first name text", timeout);
    }

    /** Clears and enters text into the Last Name field */
    public void enterLastName(String lastName) {
        testBase.clearTextBoxUsingKeys(lastNameInput);
        testBase.sendKeys(lastNameInput, lastName, "Entered last name text", timeout);
    }

    /** Clears and enters text into the Email field */
    public void enterEmail(String email) {
        testBase.clearTextBoxUsingKeys(emailInput);
        testBase.sendKeys(emailInput, email, "Entered email text", timeout);
    }

    /** Clears and enters text into the Telephone field */
    public void enterTelephone(String telephone) {
        testBase.clearTextBoxUsingKeys(telephoneInput);
        testBase.sendKeys(telephoneInput, telephone, "Entered telephone number", timeout);
    }

    /** Clears and enters text into the Password field */
    public void enterPassword(String password) {
        testBase.clearTextBoxUsingKeys(passwordInput);
        testBase.sendKeys(passwordInput, password, "Entered password text", timeout);
    }

    /** Clears and enters text into the Confirm Password field */
    public void enterConfirmPassword(String confirmPassword) {
        testBase.clearTextBoxUsingKeys(confirmPasswordInput);
        testBase.sendKeys(confirmPasswordInput, confirmPassword, "Entered confirm password text", timeout);
    }

    /** Clicks the Privacy Policy checkbox */
    public void acceptPrivacyPolicy() {
        testBase.clickElement(privacyPolicyCheckbox, "Accept privacy policy", true, timeout);
    }

    /** Clicks the Continue button to submit registration */
    public void clickContinue() {
        testBase.clickElement(continueButton, "Clicks on continue button", true, timeout);
    }

    /** Clicks the Logout link */
    public void clickLogout() {
        testBase.clickElement(logoutLink, "Clicks on logout link", true, timeout);
    }

    /**
     * Fills in valid registration details but does not click Continue.
     */
    public void enterValidRegistrationDetails(String firstName, String lastName, String email,
                                              String telephone, String password, String confirmPassword) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterTelephone(telephone);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
    }

    /**
     * Fills in details with an existing email to trigger duplicate email validation.
     */
    public void enterExistingEmailDetails(String firstName, String lastName,
                                          String telephone, String password, String confirmPassword) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail("khakaalwande@gmail.com");
        enterTelephone(telephone);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
    }

    /**
     * Enters details with an invalid email format to trigger HTML validation.
     */
    public void enterInvalidEmailFormatDetails(String firstName, String lastName,
                                               String telephone, String password, String confirmPassword) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail("khakaalwandegmail.com"); // Missing '@'
        enterTelephone(telephone);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);

        acceptPrivacyPolicy();
        clickContinue();
    }

    // ===========================
    // Validation / Assertion Methods
    // ===========================

    /** Validates if the registration success message is displayed */
    public boolean isSuccessMessageDisplayed() {
        return testBase.validateText(successMessage,
                "Your Account Has Been Created!",
                "Verifying register success message", timeout);
    }

    /** Validates if the duplicate email error is displayed */
    public boolean isDuplicateErrorMessageDisplayed() {
        return testBase.validateText(emailInvalidFormatWarning,
                "Warning: E-Mail Address is already registered!",
                "Verifying duplicate email address", timeout);
    }

    /** Validates if the privacy policy warning message is displayed */
    public boolean isPrivacyPolicyMessageDisplayed() {
        return testBase.validateText(privacyPolicyWarning,
                "Warning: You must agree to the Privacy Policy!",
                "Verifying privacy policy warning message", timeout);
    }

    /** Uses browser validation API to check for invalid email format */
    public boolean isInvalidEmailMessageDisplayed() {
        WebElement emailInput = driver.findElement(By.id("input-email"));
        String validationMessage = (String) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage;", emailInput);
        return validationMessage.contains("Please include an '@'");
    }

    /**
     * Verifies if all expected field validation messages are displayed.
     * 
     * @param expectedMessages list of expected validation error messages
     */
    public void areInputFieldErrorMessagesDisplayed(List<String> expectedMessages) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            List<WebElement> errorMessagesList = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".text-danger")));

            if (errorMessagesList.isEmpty()) {
                System.out.println("No error messages found.");
                Assert.fail("No error messages found.");
            } else {
                System.out.println("Found " + errorMessagesList.size() + " error messages.");
                for (WebElement errorMessage : errorMessagesList) {
                    String messageText = errorMessage.getText().trim();
                    if (expectedMessages.contains(messageText)) {
                        System.out.println("Matched expected message: " + messageText);
                    } else {
                        System.out.println("Unexpected message: " + messageText);
                        Assert.fail("Unexpected message: " + messageText);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Exception while verifying error messages: " + e.getMessage());
            Assert.fail("Exception while verifying error messages: " + e.getMessage());
        }
    }

    /** Validates specific field-level message (like password mismatch) */
    public boolean isFieldValidationMessageDisplayed(String expectedMessage) {
        return testBase.validateText(fieldValidationMessage, expectedMessage,
                "Verifying field validation message", timeout);
    }

    /** Validates if password mismatch message is displayed */
    public boolean isPasswordMismatchMessageDisplayed() {
        return testBase.validateText(errorMessages,
                "Password confirmation does not match password!",
                "Verifying password mismatch message", timeout);
    }
}
