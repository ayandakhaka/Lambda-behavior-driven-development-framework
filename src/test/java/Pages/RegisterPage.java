package Pages;

import java.sql.DriverManager;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import utils.TestBase;

public class RegisterPage {

	//private WebDriverWait wait;
	private WebDriver driver;
	private TestBase testBase;


	public RegisterPage() {
		driver = utils.DriverManager.getDriver();
		//this.wait = new WebDriverWait(utils.DriverManager.getDriver(), Duration.ofSeconds(10));
		testBase = new TestBase();
		PageFactory.initElements(driver, this);
	}
	// ------------------------------
	// Locators for registration page elements
	// ------------------------------

	// First Name input field
	@FindBy(name = "firstname")
	private WebElement firstNameInput;

	// Last Name input field
	@FindBy(name = "lastname")
	private WebElement lastNameInput;

	// Email input field
	@FindBy(name = "email")
	private WebElement emailInput;

	// Telephone input field
	@FindBy(name = "telephone")
	private WebElement telephoneInput;

	// Password input field
	@FindBy(name = "password")
	private WebElement passwordInput;

	// Confirm Password input field
	@FindBy(name = "confirm")
	private WebElement confirmPasswordInput;

	// Privacy Policy checkbox
	@FindBy(xpath = "//label[@for='input-agree']")
	private WebElement privacyPolicyCheckbox;

	// Continue button
	@FindBy(css = "input[value='Continue']")
	private WebElement continueButton;

	// Success message after registration
	@FindBy(xpath = "//h1[contains(text(),'Your Account Has Been Created!')]")
	private WebElement successMessage;

	// Logout link
	@FindBy(xpath = "//span[normalize-space()='Logout']")
	private WebElement logoutLink;

	// Invalid email format warning
	@FindBy(css = "div.alert.alert-danger.alert-dismissible")
	private WebElement emailInvalidFormatWarning;
	
	@FindBy(css = "div.alert.alert-danger.alert-dismissible")
	private WebElement privacyPolicyWarning;

	// Field validation messages (e.g., required field warnings)
	@FindBy(css = ".text-danger")
	private WebElement fieldValidationMessage;

	@FindBy(css = ".text-danger")
	private WebElement errorMessages;

	// ------------------------------
	// Actions / Methods
	// ------------------------------

	public void enterFirstName(String firstName) {
		testBase.clearTextBoxUsingKeys(firstNameInput);
		testBase.sendKeys(firstNameInput, firstName, "Entered First name text", 10);
	}

	public void enterLastName(String lastName) {
		testBase.clearTextBoxUsingKeys(lastNameInput);
		testBase.sendKeys(lastNameInput, lastName, "Entered last name text", 10);
	}

	public void enterEmail(String email) {
		testBase.clearTextBoxUsingKeys(emailInput);
		testBase.sendKeys(emailInput, email, "Entered email text", 10);
	}

	public void enterTelephone(String telephone) {
		testBase.clearTextBoxUsingKeys(telephoneInput);
		testBase.sendKeys(telephoneInput, telephone, "Entered telephone number", 10);
	}

	public void enterPassword(String password) {
		testBase.clearTextBoxUsingKeys(passwordInput);
		testBase.sendKeys(passwordInput, password, "Entered passowrd text", 10);
	}

	public void enterConfirmPassword(String confirmPassword) {
		testBase.clearTextBoxUsingKeys(confirmPasswordInput);
		testBase.sendKeys(confirmPasswordInput, confirmPassword, "Entered confirm password text", 10);
	}

	public void acceptPrivacyPolicy() {
		testBase.clickElement(privacyPolicyCheckbox, "Accept privacy policy", true, 20);
	}

	public void clickContinue() {
		testBase.clickElement(continueButton, "Clicks on continue button", true, 10);
	}

	public void clickLogout() {
		testBase.clickElement(logoutLink, "Clicks on logout link", true, 20);
	}

	public void enterValidRegistrationDetails(String firstName, String lastName, String email, String telephone,
			String password, String confirmPassword) {
		enterFirstName(firstName);
		enterLastName(lastName);
		enterEmail(email);
		enterTelephone(telephone);
		enterPassword(password);
		enterConfirmPassword(confirmPassword);

		// acceptPrivacyPolicy();
		// clickContinue();
	}

	public void enterExistingEmailDetails(String firstName, String lastName, String telephone, String password,
			String confirmPassword) {
		enterFirstName(firstName);
		enterLastName(lastName);
		enterEmail("khakaalwande@gmail.com");
		enterTelephone(telephone);
		enterPassword(password);
		enterConfirmPassword(confirmPassword);

		// acceptPrivacyPolicy();
		// clickContinue();
	}

	public void enterInvalidEmailFormatDetails(String firstName, String lastName, String telephone, String password,
			String confirmPassword) {
		enterFirstName(firstName);
		enterLastName(lastName);
		enterEmail("khakaalwandegmail.com");
		enterTelephone(telephone);
		enterPassword(password);
		enterConfirmPassword(confirmPassword);

		acceptPrivacyPolicy();
		clickContinue();

	}

	public boolean isSuccessMessageDisplayed() {
		return testBase.validateText(successMessage, " Your Account Has Been Created!\r\n" + "",
				"Verifying register success message", 10);
	}

	public boolean isDuplicateErrorMessageDisplayed() {
		return testBase.validateText(emailInvalidFormatWarning,
				" Warning: E-Mail Address is already registered!\r\n" + "", "Verifying duplicate email address", 10);
	}

	public boolean isPrivatePolicyErrorMessageDisplayed() {
		return testBase.validateText(emailInvalidFormatWarning,
				" Warning: You must agree to the Privacy Policy!\r\n" + "", "Verifying privacy policy warning message",
				10);
	}

	public boolean isInvalidFormatErrorMessageDisplayed() {

		String validationMessage = (String) ((org.openqa.selenium.JavascriptExecutor) utils.DriverManager.getDriver())
				.executeScript("return arguments[0].validationMessage;", emailInput);
		if (validationMessage.contains("invalid-email-format")) {
			return true;
		} else {
			return false;
		}
	}

	public void areInputFieldErrorMessagesDisplayed(List<String> expectedMessages) {
    
    	try {
    		
    		// Wait for the error message to appear
    		WebDriverWait wait = new WebDriverWait(utils.DriverManager.getDriver(), Duration.ofSeconds(10));
    		List<WebElement> errorMessagesList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".text-danger")));
    		if(errorMessagesList.isEmpty()) {
    			System.out.println("No error messages found.");
    			Assert.fail("No error messages found.");
    		} else {
    			System.out.println("Found " + errorMessagesList.size() + " error messages.");
				for (WebElement errorMessage : errorMessagesList) {
					String messageText = errorMessage.getText().trim();
					System.out.println("Error message: " + messageText);
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
	
	public boolean isInvalidEmailMessageDisplayed() {
	    WebElement emailInput = utils.DriverManager.getDriver().findElement(By.id("input-email"));
	    String validationMessage = (String) ((JavascriptExecutor) utils.DriverManager.getDriver())
	        .executeScript("return arguments[0].validationMessage;", emailInput);
	    return validationMessage.contains("Please include an '@'");
	}
	
	public boolean isPrivacyPolicyMessageDisplayed() {
		return testBase.validateText(privacyPolicyWarning, " Warning: You must agree to the Privacy Policy!\r\n" + "",
				"Verifying privacy policy warning message", 10);
	}
	
	public boolean isFieldValidationMessageDisplayed(String expectedMessage) {
		return testBase.validateText(fieldValidationMessage, expectedMessage, "Verifying field validation message", 10);
	}
	
	public boolean isPasswordMismatchMessageDisplayed() {
		return testBase.validateText(errorMessages, " Password confirmation does not match password!\r\n" + "",
				"Verifying password mismatch message", 10);
	}
}
