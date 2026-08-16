package PagePackage;

import java.time.Duration;import javax.swing.text.Document;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    WebDriver driver;
    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    @FindBy(tagName = "header")
    WebElement header;
    @FindBy(xpath = "//*[@id=\"ecommerce-header\"]/div/div/a/img")
    WebElement logo;
    @FindBy(xpath = "//span[@role='button' and contains(@class,'cursor-pointer')]")
    WebElement cart;
    @FindBy(tagName = "footer")
    WebElement footer;   
    @FindBy(xpath = "//*[@id=\"ecommerce-header\"]/div/div/div/span")
    WebElement profile;
    @FindBy(css = "a[href*='linkedin.com']")
    WebElement linkedin;
    @FindBy(xpath="//a[contains(@href,'facebook.com')]")
    WebElement facebook;
    @FindBy(xpath="//a[contains(@href,'youtube.com')]")
    WebElement youtube;
    public boolean isHeaderDisplayed() {
        return header.isDisplayed();
    }
    public String verifyUrl() {
		return driver.getCurrentUrl();    	
    }
	public String getCurentUrl() {
		return driver.getCurrentUrl();
	}
	public String getCurrentTitle() {
		return driver.getTitle();
	}
	public boolean isLogoDisplayed() {
		return logo.isDisplayed();
	}
	public void logoClick() {		
		logo.click();
	}
	public void cartClick() {
		((JavascriptExecutor) driver).executeScript(
		        "arguments[0].click();", cart);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.urlContains(("/cart")));
	}
	public boolean isCartDisplayed() {
		return cart.isDisplayed();
	}
	public boolean isProfileDisplayed() {
		return profile.isDisplayed();
	}
	public void scrollToFotter() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", footer);
	}
	public boolean isFooterDisplayed() {
		return footer.isDisplayed();
	}
	public void linkdein() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", linkedin);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(linkedin));
		js.executeScript("arguments[0].click();", linkedin);
		 String parentWindow = driver.getWindowHandle();
		    for (String window : driver.getWindowHandles()) {
		        if (!window.equals(parentWindow)) {
		            driver.switchTo().window(window);
		            break;
		        }
		    }
		    wait.until(ExpectedConditions.urlContains("linkedin.com"));
	}
	public void facebook() {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    // Scroll to the Facebook icon
	    js.executeScript("arguments[0].scrollIntoView({block:'center'});", facebook);
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    // Wait until it is clickable
	    wait.until(ExpectedConditions.elementToBeClickable(facebook));
	    // Click using JavaScript
	    js.executeScript("arguments[0].click();", facebook);
	    // Switch to the newly opened tab
	    String parentWindow = driver.getWindowHandle();
	    for (String window : driver.getWindowHandles()) {
	        if (!window.equals(parentWindow)) {
	            driver.switchTo().window(window);
	            break;
	        }
	    }
	    wait.until(ExpectedConditions.urlContains("facebook.com"));
	}
	public void youtube() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView({block:'center'});", youtube);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(youtube));
		js.executeScript("arguments[0].click();", youtube);
		 String parentWindow = driver.getWindowHandle();
		    for (String window : driver.getWindowHandles()) {
		        if (!window.equals(parentWindow)) {
		            driver.switchTo().window(window);
		            break;
		        }
		    }
		    wait.until(ExpectedConditions.urlContains("youtube.com"));
	}
}
