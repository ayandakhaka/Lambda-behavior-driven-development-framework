package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import utils.TestBase;

public class LambdaTestHomePage {

	private WebDriver driver;
	private WebDriverWait wait;
	private Actions actions;
	private TestBase testBase;

	public LambdaTestHomePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		this.actions = new Actions(driver);
		this.testBase = new TestBase(driver);
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//a[@data-toggle='dropdown']//span[contains(text(),'My account')]")
	private WebElement myAccount;

	@FindBy(linkText = "Register")
	private WebElement registerLink;

	@FindBy(linkText = "Login")
	private WebElement loginLink;

	// Logout link
	@FindBy(xpath = "//span[normalize-space()='Logout']")
	private WebElement logoutLink;
	
	@FindBy(xpath = "//span[@class='title' and normalize-space()='Mega Menu']")
	private WebElement megaMenu;
	
	@FindBy(xpath = "//*[@class='lazy-load' and @alt='iMac']")
	private WebElement imacImage;

	public void clickMyAccountMenu() {
		wait.until(ExpectedConditions.elementToBeClickable(myAccount));
		actions.moveToElement(myAccount).perform();
	}
	
	public void clickImacImage() {
		//testBase.clickElement(imacImage, "iMac Image", true, 10);
		wait.until(ExpectedConditions.elementToBeClickable(imacImage));
		actions.moveToElement(imacImage).perform();
		imacImage.click();
	}
	
	public void hoverOverMegaMenu() {
		wait.until(ExpectedConditions.elementToBeClickable(megaMenu));
		actions.moveToElement(megaMenu).perform();
	}

	public void clickRegisterLink() {
		// Hover again just to be safe
		actions.moveToElement(myAccount).perform();
		wait.until(ExpectedConditions.visibilityOf(registerLink));
		wait.until(ExpectedConditions.elementToBeClickable(registerLink));
		registerLink.click();
	}

	public void clickLoginLink() {
		// Hover again just to be safe
		actions.moveToElement(myAccount).perform();
		wait.until(ExpectedConditions.visibilityOf(loginLink));
		wait.until(ExpectedConditions.elementToBeClickable(loginLink));
		loginLink.click();
	}

	public boolean isUserLoggedIn() {
		try {
			return logoutLink.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

}
