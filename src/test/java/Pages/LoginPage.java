package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.DriverManager;
import utils.TestBase;

public class LoginPage {

	//private WebDriverWait wait;
	private WebDriver driver;
	private TestBase testBase;
	
	public LoginPage() {
		driver = DriverManager.getDriver();
		testBase = new TestBase();
		//wait = new WebDriverWait(DriverManager.getDriver(), java.time.Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}
	
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
	
	@FindBy(css = ".alert.arlet-danger.alert-dismissible")
	@CacheLookup
	private WebElement loginErrorMessage;
	
	@FindBy(xpath = "//a[contains(@class,'list-group-item') and contains(text(),'Edit Account')]")
	@CacheLookup
	private WebElement editAccountLink;
	
	@FindBy(xpath = "//*[@class='card-header h5']")
	@CacheLookup
	private WebElement MyAccountHeaderText;
	
	@FindBy(css = "div.alert.alert-danger.alert-dismissible")
	@CacheLookup
	private WebElement invalidLoginWarning;
	
	/*
	 * Actions
	 */
	public void enterEmail(String email) {
		testBase.clearTextBoxUsingKeys(emailInput);
		testBase.sendKeys(emailInput, email, "Email entered in email input field", 10);
	}
	
	public void enterPassword(String password) {
		testBase.clearTextBoxUsingKeys(passwordInput);
		testBase.sendKeys(passwordInput, password, "Password entered in password input field", 10);
	}
	
	public void clickLoginButton() {
		testBase.clickElement(loginButton, "Clicks on Login button", true, 10);
	}
	
	public void clickLogoutButton() {
		testBase.clickElement(logoutLink, "Clicks on logout button", true, 10);
	}
	
	public boolean verifySuccessfulLogin() {
		return testBase.validateText(MyAccountHeaderText, "My Account\r\n"
				+ "", "Verifying success login", 10);
	}
	
	public boolean verifyInvalidLoginMessage() {
		return testBase.validateText(invalidLoginWarning, "Warning: No match for E-Mail Address and/or Password.\r\n" + "",
				"Verifying invalid login message", 10);
	}
}
