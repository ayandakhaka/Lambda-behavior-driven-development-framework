package stepdefinition;

import org.testng.Assert;

import Pages.LambdaTestHomePage;
import Pages.LoginPage;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.DriverManager;
import utils.EnvironmentLoader;
import utils.TestDataManager;

/**
 * Step Definition Class: LoginSteps
 * 
 * This class defines Cucumber step definitions for testing login functionality.
 * It interacts with Page Object classes (LambdaTestHomePage, LoginPage)
 * and uses TestDataManager for managing user credentials.
 * 
 * The steps include:
 * - Navigating to the login page
 * - Entering credentials
 * - Validating success and error messages
 * 
 * The structure follows the Given-When-Then pattern for readability and maintainability.
 * 
 * Author: Ayanda Khaka
 * Date: October 2025
 */
public class LoginSteps {

    //===============================
    // Instance Variables
    //===============================

    /** Manages dynamic or static test data for login scenarios */
    private TestDataManager userData;

    /** Represents the Home Page of the LambdaTest site */
    private LambdaTestHomePage lambdaTestHomePage;

    /** Represents the Login Page of the LambdaTest site */
    private LoginPage loginPage;

    //===============================
    // Setup Method
    //===============================

    /**
     * Initializes the WebDriver and page objects before each scenario.
     * This ensures a clean browser session for every test.
     */
    @Before
    public void setUp() {
        // Initialize WebDriver
        DriverManager.initializeDriver();

        // Load test data from data source (JSON)
        userData = TestDataManager.getInstance();
        userData.loadUserData();

        // Initialize Page Objects
        lambdaTestHomePage = new LambdaTestHomePage();
        loginPage = new LoginPage();
    }

    //===============================
    // Step Definitions
    //===============================

    /**
     * Step: User clicks the login link from the home page.
     * Navigates to the login screen.
     */
    @When("User clicks on the Login link")
    public void user_clicks_on_the_login_link() {
        lambdaTestHomePage.clickLoginLink();
        System.out.println(EnvironmentLoader.getEnv("BROWSERSTACK_USERNAME"));
        System.out.println(EnvironmentLoader.getEnv("BROWSERSTACK_ACCESS_KEY"));

    }

    /**
     * Step: User enters valid credentials from the test data manager.
     */
    @When("User enters valid email and password")
    public void user_enters_valid_email_and_password() {
        loginPage.enterEmail(userData.getEmail());
        loginPage.enterPassword(userData.getPassword());
    }

    /**
     * Step: User clicks the login button to submit the credentials.
     */
    @And("User clicks on the Login button")
    public void user_clicks_on_the_login_button() {
        loginPage.clickLoginButton();
    }

    /**
     * Step: Validates that the user is redirected to the account dashboard after login.
     */
    @Then("User should be redirected to their account dashboard")
    public void user_should_be_redirected_to_their_account_dashboard() {
        Assert.assertTrue(
            loginPage.verifySuccessfulLogin(),
            "❌ My Account header is not displayed. Login may have failed."
        );
    }

    /**
     * Step: User enters a valid email but an invalid password.
     * Used to test password validation logic.
     */
    @When("User enters a valid email and an invalid password")
    public void user_enters_a_valid_email_and_an_invalid_password() {
        loginPage.enterEmail(userData.getEmail());
        loginPage.enterPassword("WrongPassword123");
    }

    /**
     * Step: User enters an unregistered email with any password.
     * This generates a new email each time to simulate an unregistered user.
     */
    @And("User enters an unregistered email and any password")
    public void user_enters_an_unregistered_email_and_any_password() {
        userData.generateNewUserData(); // Creates unique test user data dynamically
        loginPage.enterEmail(userData.getEmail());
        loginPage.enterPassword(userData.getPassword());
    }

    /**
     * Step: User leaves both email and password fields empty.
     * Used to test required field validations.
     */
    @When("User leaves the email and password fields empty")
    public void user_leaves_the_email_and_password_fields_empty() {
        loginPage.enterEmail("");
        loginPage.enterPassword("");
    }

    //===============================
    // Validation / Assertion Steps
    //===============================

    /**
     * Step: Validates that an error is shown for an unregistered email.
     */
    @Then("User should see an error message indicating unregistered email")
    public void user_should_see_an_error_message_indicating_unregistered_email() {
        Assert.assertTrue(
            loginPage.verifyInvalidLoginMessage(),
            "❌ Invalid login warning is not displayed for unregistered email."
        );
    }

    /**
     * Step: Validates that an error message is displayed for incorrect password.
     */
    @Then("User should see an error message indicating incorrect password")
    public void user_should_see_an_error_message_indicating_incorrect_password() {
        Assert.assertTrue(
            loginPage.verifyInvalidLoginMessage(),
            "❌ Invalid login warning is not displayed for incorrect password."
        );
    }

    /**
     * Step: Validates that the user sees a message when using an unregistered email.
     */
    @Then("User should see an error message indicating that the email is not registered")
    public void user_should_see_an_error_message_indicating_that_the_email_is_not_registered() {
        Assert.assertTrue(
            loginPage.verifyInvalidLoginMessage(),
            "❌ Invalid login warning is not displayed for unregistered email."
        );
    }

    /**
     * Step: Validates that error messages appear when both fields are empty.
     */
    @Then("User should see error messages indicating that both fields are required")
    public void user_should_see_error_messages_indicating_that_both_fields_are_required() {
        Assert.assertTrue(
            loginPage.verifyInvalidLoginMessage(),
            "❌ Required field validation messages are not displayed."
        );
    }
}
