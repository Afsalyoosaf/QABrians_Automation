package PagePackage;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EtoELogin {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public EtoELogin(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement loginButton;

    /**
     * Executes the login step specifically tailored for E2E workflows.
     */
    public HomePage performEtoELogin(String emailId, String pass) {
        // Ensure email input is visible and interactable
        wait.until(ExpectedConditions.visibilityOf(emailInput));

        emailInput.clear();
        passwordInput.clear();

        emailInput.sendKeys(emailId);
        passwordInput.sendKeys(pass);

        // Wait until clickable and submit
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        
        // Scroll to element to prevent viewport obstruction
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", loginButton
        );

        try {
            loginButton.click();
        } catch (Exception e) {
            // Fallback to JS click if blocked by an overlay
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginButton);
        }

        return new HomePage(driver);
    }

    /**
     * Confirms that login was successful by checking URL transition away from /login
     */
    public boolean isLoginSuccessful() {
        try {
            boolean navigatedAway = wait.until(
                ExpectedConditions.not(ExpectedConditions.urlContains("/login"))
            );
            String currentUrl = driver.getCurrentUrl();
            System.out.println("[E2E Auth] Navigated to: " + currentUrl);
            return navigatedAway;
        } catch (Exception e) {
            System.err.println("[E2E Auth] Login timed out. Stuck on: " + driver.getCurrentUrl());
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}