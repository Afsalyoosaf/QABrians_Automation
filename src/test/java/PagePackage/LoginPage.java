package PagePackage;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    WebDriver driver;
    WebDriverWait wait;

    public LoginPage(WebDriver driver) {

        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @FindBy(id = "email")
    WebElement email;

    @FindBy(id = "password")
    WebElement password;

    @FindBy(xpath = "//button[@type='submit']")
    WebElement loginButton;

    public HomePage login(String emailId, String pass) {

        wait.until(ExpectedConditions.visibilityOf(email));

        email.clear();
        password.clear();

        email.sendKeys(emailId);
        password.sendKeys(pass);

        wait.until(ExpectedConditions.elementToBeClickable(loginButton));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                loginButton);

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();",
                loginButton);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Current URL = " + driver.getCurrentUrl());

        return new HomePage(driver);
    }

    public boolean isStillOnLoginPage() {
        return driver.getCurrentUrl().contains("/login");
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

	public boolean isLoginSuccessful() {

    try {

        wait.until(ExpectedConditions.not(
                ExpectedConditions.urlContains("/login")));

        System.out.println("Current URL = " + driver.getCurrentUrl());

        return !driver.getCurrentUrl().contains("/login");

    } catch (Exception e) {

        System.out.println("Current URL = " + driver.getCurrentUrl());

        return false;
    }
}
}