package stepdefinition;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import Pages.LambdaTestHomePage;
import Pages.LoginPage;
import Pages.RegisterPage;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.DriverManager;
import utils.TestBase;
import utils.TestDataManager;

public class LoginSteps {
	
	//private WebDriver driver;
	private TestDataManager userData;
	private LambdaTestHomePage lambdaTestHomePage;
	private LoginPage loginPage;
	
	@Before
	public void setUp() {
		DriverManager.initializeDriver();
		//this.driver = DriverManager.getDriver();
		userData = TestDataManager.getInstance();
		userData.loadUserData();
		lambdaTestHomePage = new LambdaTestHomePage();
		loginPage = new LoginPage();
	}
	
	@When("User clicks on the Login link")
	public void user_clicks_on_the_login_link() {
	    lambdaTestHomePage.clickLoginLink();
	}

	@When("User enters valid email and password")
	public void user_enters_valid_email_and_password() {
	    
		loginPage.enterEmail(userData.getEmail());
		loginPage.enterPassword(userData.getPassword());
	}

	@When("User clicks on the Login button")
	public void user_clicks_on_the_login_button() {
	       loginPage.clickLoginButton();
	}

	@Then("User should be redirected to their account dashboard")
	public void user_should_be_redirected_to_their_account_dashboard() {
		Assert.assertTrue(loginPage.verifySuccessfulLogin(), "My Account header is not displayed, login may have failed.");
	}

	@When("User enters a valid email and an invalid password")
	public void user_enters_a_valid_email_and_an_invalid_password() {
		loginPage.enterEmail(userData.getEmail());
		loginPage.enterPassword("WrongPassword123");
	}

	@Then("User should see an error message indicating incorrect password")
	public void user_should_see_an_error_message_indicating_incorrect_password() {
		Assert.assertTrue(loginPage.verifyInvalidLoginMessage(), "Invalid login warning is not displayed.");
	}
	
	@When("User enters an unregistered email and any password")
	public void user_enters_an_unregistered_email_and_any_password() {
		userData.generateNewUserData(); // Generates dynamic test data
        loginPage.enterEmail(userData.getEmail());
        loginPage.enterPassword(userData.getPassword());
        
	}
	
	@Then("User should see an error message indicating that the email is not registered")
	public void user_should_see_an_error_message_indicating_that_the_email_is_not_registered() {
		Assert.assertTrue(loginPage.verifyInvalidLoginMessage(), "Invalid login warning is not displayed.");
	}
	
	@When("User leaves the email and password fields empty")
	public void user_leaves_the_email_and_password_fields_empty() {
	   
		loginPage.enterEmail("");
		loginPage.enterPassword("");
	}

	@Then("User should see error messages indicating that both fields are required")
	public void user_should_see_error_messages_indicating_that_both_fields_are_required() {
	          Assert.assertTrue(loginPage.verifyInvalidLoginMessage(), "Invalid login warning is not displayed.");
	}





}
