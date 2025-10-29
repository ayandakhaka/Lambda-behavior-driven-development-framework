package stepdefinition;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import Pages.LambdaTestHomePage;
import Pages.RegisterPage;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.DriverManager;
import utils.TestBase;
import utils.TestDataManager;

public class RegistrationSteps extends TestBase {

	private WebDriver driver;
	private TestDataManager userData;
	private LambdaTestHomePage lambdaTestHomePage;
	private RegisterPage registerPage;

	@Before
	public void setUp() {
		DriverManager.initializeDriver();
		this.driver = DriverManager.getDriver();
		this.userData = TestDataManager.getInstance();
		this.userData.loadUserData();
		this.lambdaTestHomePage = new LambdaTestHomePage();
		this.registerPage = new RegisterPage();
	}

	// ... all your step methods here ...


	// ---------------------- Background ----------------------
	@Given("User launches the lambdaTest home page")
	public void user_launches_the_lambda_test_home_page() {
		DriverManager.launchURL("https://ecommerce-playground.lambdatest.io/index.php?route=common/home");
	}

	@When("User hovers over the My Account menu")
	public void user_hovers_over_the_my_account_menu() {
		lambdaTestHomePage.hoverOverMyAccountMenu(); // Refactored method does proper hover
	}

	@When("User clicks on the Register link")
	public void user_clicks_on_the_register_link() {
		lambdaTestHomePage.clickRegisterLink();
	}

	// ---------------------- Scenario: Successful Registration ----------------------
	@When("User enters all the mandatory fields with valid data")
	public void user_enters_all_the_mandatory_fields_with_valid_data() {
		userData.generateNewUserData(); // Generates dynamic test data
		registerPage.enterValidRegistrationDetails(
				userData.getFirstName(),
				userData.getLastName(),
				userData.getEmail(),
				userData.getPhone(),
				userData.getPassword(),
				userData.getPassword()
				);
	}

	@When("User clicks on the Privacy Policy checkbox")
	public void user_clicks_on_the_privacy_policy_checkbox() {
		registerPage.acceptPrivacyPolicy();
	}

	@When("User clicks on the Continue button")
	public void user_clicks_on_the_continue_button() {
		registerPage.clickContinue();
	}

	@Then("User should see a confirmation message indicating successful registration")
	public void user_should_see_a_confirmation_message_indicating_successful_registration() {
		Assert.assertTrue(registerPage.isSuccessMessageDisplayed(),
				"❌ Failed to verify register success message");
	}

	@When("User refresh the page")
	public void user_refresh_the_page() {
		driver.navigate().refresh();
	}

	@Given("User is logged out")
	public void user_is_logged_out() {
		try {
			registerPage.clickLogout();
		} catch (Exception e) {
			System.out.println("User is not logged in. No logout needed.");
		}
	}


	// ---------------------- Scenario: Registration with Already Registered Email ----------------------
	@When("User enters all the mandatory fields with an already registered email")
	public void user_enters_all_the_mandatory_fields_with_an_already_registered_email() {
		registerPage.enterExistingEmailDetails(
				userData.getFirstName(),
				userData.getLastName(),
				userData.getPhone(),
				userData.getPassword(),
				userData.getPassword()
				);
	}

	@Then("User should see an error message indicating that the email is already registered")
	public void user_should_see_an_error_message_indicating_that_the_email_is_already_registered() {
		Assert.assertTrue(registerPage.isDuplicateErrorMessageDisplayed(),
				"❌ Failed to verify existing email warning message");
	}
	
	@When("User leaves one or more mandatory fields empty")
	public void user_leaves_one_or_more_mandatory_fields_empty() {
	    registerPage.enterFirstName("");
	    registerPage.enterLastName("");
	    registerPage.enterEmail("");
	    registerPage.enterTelephone("");
	    registerPage.enterPassword("");
	    registerPage.enterConfirmPassword("");
	    
	}

	@Then("User should see error messages indicating which mandatory fields are missing")
	public void user_should_see_error_messages_indicating_which_mandatory_fields_are_missing(List<String> expectedMessages) {
		registerPage.areInputFieldErrorMessagesDisplayed(expectedMessages);
	}
	
	@When("User enters an invalid email format in the email field")
	public void user_enters_an_invalid_email_format_in_the_email_field() {
	    registerPage.enterEmail("khakaalwandegmail.com");
	}

	@When("User fills in all other mandatory fields with valid data")
	public void user_fills_in_all_other_mandatory_fields_with_valid_data() {
		registerPage.enterFirstName(userData.getFirstName());
		registerPage.enterLastName(userData.getLastName());
		registerPage.enterTelephone(userData.getPhone());
		registerPage.enterPassword(userData.getPassword());
		registerPage.enterConfirmPassword(userData.getPassword());
	}

	@Then("User should see an error message indicating that the email format is invalid")
	public void user_should_see_an_error_message_indicating_that_the_email_format_is_invalid() {
		Assert.assertTrue(registerPage.isInvalidEmailMessageDisplayed(), "❌ Invalid email format: missing '@' symbol");
	}

	@Then("User should see an error message indicating that the Privacy Policy must be accepted")
	public void user_should_see_an_error_message_indicating_that_the_privacy_policy_must_be_accepted() {
		Assert.assertTrue(registerPage.isPrivatePolicyErrorMessageDisplayed(),
				"❌ Failed to verify privacy policy warning message");
	}
	
	@When("User enters all the mandatory fields with valid data but a weak password")
	public void user_enters_all_the_mandatory_fields_with_valid_data_but_a_weak_password() {
		userData.generateNewUserData(); // Generates dynamic test data
		registerPage.enterValidRegistrationDetails(
				userData.getFirstName(),
				userData.getLastName(),
				userData.getEmail(),
				userData.getPhone(), 
				"123", // Weak password
				"123" // Weak password
		);
	}

	@Then("User should see an error message indicating that the password is too weak")
	public void user_should_see_an_error_message_indicating_that_the_password_is_too_weak() {
		Assert.assertTrue(registerPage.isFieldValidationMessageDisplayed("Password must be between 4 and 20 characters!\r\n"
				+ ""),
				"❌ Failed to verify weak password warning message");
	}
	
	@When("User enters all the mandatory fields with valid data but mismatched password and confirmation")
	public void user_enters_all_the_mandatory_fields_with_valid_data_but_mismatched_password_and_confirmation() {
		userData.generateNewUserData(); // Generates dynamic test data
		registerPage.enterValidRegistrationDetails(
				userData.getFirstName(),
				userData.getLastName(),
				userData.getEmail(),
				userData.getPhone(),
				userData.getPassword(),
				"DifferentPassword123" // Mismatched confirmation
		);
	}

	@Then("User should see an error message indicating that the password and confirmation do not match")
	public void user_should_see_an_error_message_indicating_that_the_password_and_confirmation_do_not_match() {
		
		Assert.assertTrue(registerPage.isPasswordMismatchMessageDisplayed(),
				"❌ Failed to verify password mismatch warning message");
	}



	
	

}
