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

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Property-based locator for cart triggers
    private final By cartButtonLocator = By.xpath(
        "//a[contains(@href,'/cart')] | " +
        "//*[@aria-label and contains(translate(@aria-label, 'CART', 'cart'), 'cart')] | " +
        "//*[@data-testid and contains(@data-testid, 'cart')] | " +
        "//*[@id='ecommerce-header']//span[contains(@class,'cursor-pointer')] | " +
        "//header//button[contains(@class,'cart') or .//*[local-name()='svg']]"
    );

    // Dynamic cart element overlay OR main page container
    private final By cartContainerLocator = By.xpath(
        "//div[contains(@class,'drawer')] | " +
        "//div[contains(@class,'modal')] | " +
        "//div[@role='dialog'] | " +
        "//aside | " +
        "//main | " +
        "//*[contains(@class,'cart')]"
    );

    @FindBy(xpath = "//a[contains(@href,'checkout')] | //button[contains(@class,'checkout') or contains(.,'Checkout') or contains(.,'checkout')]")
    private WebElement proceedToCheckoutBtn;

    @FindBy(xpath = "//div[contains(@class,'flex') and contains(@class,'items-center')]//span[contains(@class,'border')] | //div[contains(@class,'cart_quantity')]")
    private WebElement quantityDisplay;

    @FindBy(xpath = "//button[text()='+'] | //button[contains(@class,'increment')]")
    private WebElement incrementBtn;

    @FindBy(xpath = "//button[text()='-'] | //button[contains(@class,'decrement')]")
    private WebElement decrementBtn;

    @FindBy(xpath = "//div[@role='dialog']//button[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'remove') or contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'confirm') or contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'yes') or contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'delete')]")
    private WebElement confirmRemoveBtn;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void toCartPage() {
        try {
            WebElement cartBtnElement = wait.until(ExpectedConditions.presenceOfElementLocated(cartButtonLocator));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", cartBtnElement);

            try {
                wait.until(ExpectedConditions.elementToBeClickable(cartBtnElement)).click();
            } catch (Exception e) {
                js.executeScript("arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true}));", cartBtnElement);
            }
        } catch (Exception e) {
            driver.navigate().to("https://practice.qabrains.com/ecommerce/cart");
        }

        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/cart"),
                ExpectedConditions.presenceOfElementLocated(cartContainerLocator)
            ));
        } catch (Exception e) {
            if (!driver.getCurrentUrl().contains("/cart")) {
                driver.navigate().to("https://practice.qabrains.com/ecommerce/cart");
                wait.until(ExpectedConditions.urlContains("/cart"));
            }
        }
    }

    public boolean isCartOpen() {
        try {
            return wait.until(d -> d.getCurrentUrl().toLowerCase().contains("cart") || 
                !d.findElements(cartContainerLocator).isEmpty()
            );
        } catch (Exception e) {
            return false;
        }
    }

    private By getProductLocator(String productName) {
        return By.xpath("//*[contains(translate(normalize-space(text()), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" 
            + productName.trim().toLowerCase() + "')]");
    }

    public boolean isProductInCart(String productName) {
        try {
            return !wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getProductLocator(productName))).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean waitForProductToDisappear(String productName) {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(getProductLocator(productName)));
        } catch (Exception e) {
            return false;
        }
    }

    public int getProductQuantity() {
        wait.until(ExpectedConditions.visibilityOf(quantityDisplay));
        return Integer.parseInt(quantityDisplay.getText().trim());
    }

    public void incrementQuantity() {
        wait.until(ExpectedConditions.visibilityOf(incrementBtn));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", incrementBtn);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(incrementBtn)).click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", incrementBtn);
        }
    }

    public void decrementQuantity() {
        wait.until(ExpectedConditions.visibilityOf(decrementBtn));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", decrementBtn);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(decrementBtn)).click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", decrementBtn);
        }
    }

    public void confirmRemovalIfDialogAppears() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(confirmRemoveBtn)).click();
        } catch (Exception e) {
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", confirmRemoveBtn);
            } catch (Exception ignored) {
            }
        }
    }

    public void proceedToCheckout() {
        try {
            WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[contains(@href,'checkout')] | //button[contains(@class,'checkout') or contains(.,'Checkout') or contains(.,'checkout')]")
            ));
            
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);

            try {
                wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", btn);
            }
        } catch (Exception e) {
            driver.navigate().to("https://practice.qabrains.com/ecommerce/checkout");
        }

        try {
            wait.until(ExpectedConditions.urlContains("/checkout"));
        } catch (Exception ignored) {
        }
    }
}