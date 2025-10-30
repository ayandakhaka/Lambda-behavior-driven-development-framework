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
import utils.ConfigReader;
import utils.DriverManager;
import utils.TestBase;
import utils.TestDataManager;

/**
 * Step Definition class for handling Registration feature scenarios
 * using the Cucumber BDD framework.
 * 
 * This class defines the glue between Gherkin feature steps and Java methods
 * to automate LambdaTest registration workflows.
 * 
 * It covers multiple registration scenarios including:
 * - Successful registration
 * - Duplicate email registration
 * - Missing mandatory fields
 * - Invalid email format
 * - Weak and mismatched passwords
 * - Privacy policy validation
 */
public class RegistrationSteps extends TestBase {

    // WebDriver instance
    private WebDriver driver;
    // Manages user data for test execution (e.g., names, email, passwords)
    private TestDataManager userData;
    // Page Objects for Home and Register pages
    private LambdaTestHomePage lambdaTestHomePage;
    private RegisterPage registerPage;
    // Base URL fetched from the config.properties file
    private String baseURL = ConfigReader.get("baseUrl");

    /**
     * Setup method that runs before each scenario.
     * Initializes WebDriver and page objects, and loads test data.
     */
    @Before
    public void setUp() {
        DriverManager.initializeDriver();
        this.driver = DriverManager.getDriver();
        this.userData = TestDataManager.getInstance();
        this.userData.loadUserData();
        this.lambdaTestHomePage = new LambdaTestHomePage();
        this.registerPage = new RegisterPage();
    }

    // ---------------------- Background Steps ----------------------

    /**
     * Launches the LambdaTest home page before test execution.
     */
    @Given("User launches the lambdaTest home page")
    public void user_launches_the_lambda_test_home_page() {
        DriverManager.launchURL(baseURL + "/index.php?route=common/home");
    }

    /**
     * Performs hover action on "My Account" menu.
     */
    @When("User hovers over the My Account menu")
    public void user_hovers_over_the_my_account_menu() {
        lambdaTestHomePage.hoverOverMyAccountMenu();
    }

    /**
     * Clicks the "Register" link under My Account.
     */
    @When("User clicks on the Register link")
    public void user_clicks_on_the_register_link() {
        lambdaTestHomePage.clickRegisterLink();
    }

    // ---------------------- Scenario: Successful Registration ----------------------

    /**
     * Fills out registration form with dynamically generated valid data.
     */
    @When("User enters all the mandatory fields with valid data")
    public void user_enters_all_the_mandatory_fields_with_valid_data() {
        userData.generateNewUserData(); // Generate random valid data
        registerPage.enterValidRegistrationDetails(
            userData.getFirstName(),
            userData.getLastName(),
            userData.getEmail(),
            userData.getPhone(),
            userData.getPassword(),
            userData.getPassword()
        );
    }

    /**
     * Checks the Privacy Policy checkbox before form submission.
     */
    @When("User clicks on the Privacy Policy checkbox")
    public void user_clicks_on_the_privacy_policy_checkbox() {
        registerPage.acceptPrivacyPolicy();
    }

    /**
     * Clicks the Continue button to submit the registration form.
     */
    @When("User clicks on the Continue button")
    public void user_clicks_on_the_continue_button() {
        registerPage.clickContinue();
    }

    /**
     * Verifies that registration is successful by checking for a success message.
     */
    @Then("User should see a confirmation message indicating successful registration")
    public void user_should_see_a_confirmation_message_indicating_successful_registration() {
        Assert.assertTrue(registerPage.isSuccessMessageDisplayed(),
            "❌ Failed to verify register success message");
    }

    /**
     * Refreshes the current browser page.
     */
    @When("User refresh the page")
    public void user_refresh_the_page() {
        driver.navigate().refresh();
    }

    /**
     * Ensures the user is logged out before starting a new scenario.
     */
    @Given("User is logged out")
    public void user_is_logged_out() {
        try {
            registerPage.clickLogout();
        } catch (Exception e) {
            System.out.println("User is not logged in. No logout needed.");
        }
    }

    // ---------------------- Scenario: Registration with Existing Email ----------------------

    /**
     * Fills the registration form with an already registered email.
     */
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

    /**
     * Verifies that an error message appears for duplicate email registration.
     */
    @Then("User should see an error message indicating that the email is already registered")
    public void user_should_see_an_error_message_indicating_that_the_email_is_already_registered() {
        Assert.assertTrue(registerPage.isDuplicateErrorMessageDisplayed(),
            "❌ Failed to verify existing email warning message");
    }

    // ---------------------- Scenario: Missing Mandatory Fields ----------------------

    /**
     * Leaves mandatory registration fields empty to test form validation.
     */
    @When("User leaves one or more mandatory fields empty")
    public void user_leaves_one_or_more_mandatory_fields_empty() {
        registerPage.enterFirstName("");
        registerPage.enterLastName("");
        registerPage.enterEmail("");
        registerPage.enterTelephone("");
        registerPage.enterPassword("");
        registerPage.enterConfirmPassword("");
    }

    /**
     * Verifies that appropriate error messages are shown for missing fields.
     */
    @Then("User should see error messages indicating which mandatory fields are missing")
    public void user_should_see_error_messages_indicating_which_mandatory_fields_are_missing(List<String> expectedMessages) {
        registerPage.areInputFieldErrorMessagesDisplayed(expectedMessages);
    }

    // ---------------------- Scenario: Invalid Email Format ----------------------

    /**
     * Enters an invalid email address (missing '@') to trigger validation error.
     */
    @When("User enters an invalid email format in the email field")
    public void user_enters_an_invalid_email_format_in_the_email_field() {
        registerPage.enterEmail("khakaalwandegmail.com");
    }

    /**
     * Fills remaining mandatory fields correctly except email.
     */
    @When("User fills in all other mandatory fields with valid data")
    public void user_fills_in_all_other_mandatory_fields_with_valid_data() {
        registerPage.enterFirstName(userData.getFirstName());
        registerPage.enterLastName(userData.getLastName());
        registerPage.enterTelephone(userData.getPhone());
        registerPage.enterPassword(userData.getPassword());
        registerPage.enterConfirmPassword(userData.getPassword());
    }

    /**
     * Validates the warning message for invalid email format.
     */
    @Then("User should see an error message indicating that the email format is invalid")
    public void user_should_see_an_error_message_indicating_that_the_email_format_is_invalid() {
        Assert.assertTrue(registerPage.isInvalidEmailMessageDisplayed(),
            "❌ Invalid email format: missing '@' symbol");
    }

    // ---------------------- Scenario: Privacy Policy Validation ----------------------

    /**
     * Verifies that the Privacy Policy acceptance message is displayed.
     */
    @Then("User should see an error message indicating that the Privacy Policy must be accepted")
    public void user_should_see_an_error_message_indicating_that_the_privacy_policy_must_be_accepted() {
        Assert.assertTrue(registerPage.isPrivacyPolicyMessageDisplayed(),
            "❌ Failed to verify privacy policy warning message");
    }

    // ---------------------- Scenario: Weak Password ----------------------

    /**
     * Enters valid user details but uses a weak password.
     */
    @When("User enters all the mandatory fields with valid data but a weak password")
    public void user_enters_all_the_mandatory_fields_with_valid_data_but_a_weak_password() {
        userData.generateNewUserData();
        registerPage.enterValidRegistrationDetails(
            userData.getFirstName(),
            userData.getLastName(),
            userData.getEmail(),
            userData.getPhone(),
            "123", // Weak password
            "123"
        );
    }

    /**
     * Confirms that weak password validation message appears.
     */
    @Then("User should see an error message indicating that the password is too weak")
    public void user_should_see_an_error_message_indicating_that_the_password_is_too_weak() {
        Assert.assertTrue(registerPage.isFieldValidationMessageDisplayed(
            "Password must be between 4 and 20 characters!\r\n"),
            "❌ Failed to verify weak password warning message");
    }

    // ---------------------- Scenario: Password Mismatch ----------------------

    /**
     * Enters valid registration details but mismatched password confirmation.
     */
    @When("User enters all the mandatory fields with valid data but mismatched password and confirmation")
    public void user_enters_all_the_mandatory_fields_with_valid_data_but_mismatched_password_and_confirmation() {
        userData.generateNewUserData();
        registerPage.enterValidRegistrationDetails(
            userData.getFirstName(),
            userData.getLastName(),
            userData.getEmail(),
            userData.getPhone(),
            userData.getPassword(),
            "DifferentPassword123"
        );
    }

    /**
     * Checks for mismatch error message between password and confirmation fields.
     */
    @Then("User should see an error message indicating that the password and confirmation do not match")
    public void user_should_see_an_error_message_indicating_that_the_password_and_confirmation_do_not_match() {
        Assert.assertTrue(registerPage.isPasswordMismatchMessageDisplayed(),
            "❌ Failed to verify password mismatch warning message");
    }
}
