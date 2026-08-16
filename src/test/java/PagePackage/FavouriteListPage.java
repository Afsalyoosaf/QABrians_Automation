package PagePackage;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FavouriteListPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(css = "span.user-name")
    private WebElement profile_btn;

    @FindBy(xpath = "//*[@id='ecommerce-header']/div/div/div/span")
    private WebElement cartbtn;

    @FindBy(xpath = "//div[@role='menuitem' and text()='Favorites']")
    private WebElement fav_btn;

    @FindBy(xpath = "//a[normalize-space()='Continue Shopping']")
    private WebElement continueShoppingButton;

    @FindBy(xpath = "//h2[contains(normalize-space(),'You have no favorite products')]")
    private WebElement emptyFavouriteMessage;

    @FindBy(xpath = "//*[@id='favorites-wrapper']/div/div/div/button")
    private WebElement addToCart;

    public FavouriteListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void openFavouritePage() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.user-name")));
        safeClick(profile_btn);
        
        wait.until(ExpectedConditions.visibilityOf(fav_btn));
        safeClick(fav_btn);
        
        wait.until(ExpectedConditions.urlContains("/favorites"));
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void clickContinueShopping() {
        safeClick(continueShoppingButton);
    }

    public boolean isFavouriteListEmpty() {
        try {
            wait.until(ExpectedConditions.visibilityOf(emptyFavouriteMessage));
            return emptyFavouriteMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void addToCartFromFav() {
        safeClick(addToCart);
    }

    public void openCart() {
        safeClick(cartbtn);
        wait.until(ExpectedConditions.urlContains("/cart"));
    }

    /**
     * Helper method to scroll to an element and attempt a standard click,
     * falling back to JavaScript click if intercepted by overlays/headers.
     */
    private void safeClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        wait.until(ExpectedConditions.visibilityOf(element));
        js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
        
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (Exception e) {
            // Fallback to JS click if an overlay or fixed header intercepts native click
            js.executeScript("arguments[0].click();", element);
        }
    }
}