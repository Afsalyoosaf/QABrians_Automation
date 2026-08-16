package PagePackage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import BasePackage.BaseClass;

public class CheckoutPage extends BaseClass {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Separate locators for Step 1 (Continue) and Step 2 (Finish) to avoid ambiguity
    private final By continueBtnBy = By.xpath(
        "//input[@id='continue'] " +
        "| //button[@id='continue'] " +
        "| //button[contains(translate(., 'CONTINUE', 'continue'), 'continue')] " +
        "| //input[@type='submit' and @value='Continue']"
    );

    private final By finishBtnBy = By.xpath(
        "//button[@id='finish'] " +
        "| //input[@id='finish'] " +
        "| //button[contains(translate(., 'FINISH', 'finish'), 'finish')] " +
        "| //button[contains(translate(., 'PLACE ORDER', 'place order'), 'place order')] " +
        "| //input[@type='submit' and contains(translate(@value, 'FINISH', 'finish'), 'finish')]"
    );

    private final By cancelBy = By.xpath(
        "//button[contains(translate(., 'CANCEL', 'cancel'), 'cancel')] " +
        "| //a[contains(translate(., 'CANCEL', 'cancel'), 'cancel')]"
    );

    private final By checkoutItemsBy = By.xpath(
        "//div[contains(@class,'cart_item') or contains(@class,'inventory_item_name') or contains(@class,'cart_item_label')]"
    );

    @FindBy(xpath = "//input[contains(@id,'email') or contains(@name,'email')] | //label[contains(text(),'Email')]/following-sibling::input")
    private WebElement emailInput;

    @FindBy(xpath = "//input[contains(@id,'first-name') or contains(@id,'firstName') or contains(@name,'firstName')] | //input[@placeholder='Ex. John']")
    private WebElement firstNameInput;

    @FindBy(xpath = "//input[contains(@id,'last-name') or contains(@id,'lastName') or contains(@name,'lastName')] | //input[@placeholder='Ex. Doe']")
    private WebElement lastNameInput;

    @FindBy(xpath = "//input[contains(@id,'postal-code') or contains(@id,'zip') or contains(@name,'zip')] | //label[contains(text(),'Zip')]/following-sibling::input")
    private WebElement zipCodeInput;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public boolean isCheckoutPageOpen() {
        try {
            return wait.until(d -> d.getCurrentUrl().toLowerCase().contains("checkout")) 
                   || firstNameInput.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEmailEditable() {
        try {
            String disabledAttr = emailInput.getAttribute("disabled");
            String readOnlyAttr = emailInput.getAttribute("readonly");
            return emailInput.isEnabled() && disabledAttr == null && readOnlyAttr == null;
        } catch (Exception e) {
            return false;
        }
    }

    public void enterFirstName(String fname) {
        WebElement element = wait.until(ExpectedConditions.visibilityOf(firstNameInput));
        element.clear();
        element.sendKeys(fname);
    }

    public void enterLastName(String lname) {
        WebElement element = wait.until(ExpectedConditions.visibilityOf(lastNameInput));
        element.clear();
        element.sendKeys(lname);
    }

    public void enterZipCode(String zip) {
        WebElement element = wait.until(ExpectedConditions.visibilityOf(zipCodeInput));
        element.clear();
        element.sendKeys(zip);
    }

    public void clickCancel() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(cancelBy));
        safeClick(btn);
    }

    public void clickFinish() {
        try {
            // Step 1: Click "Continue" on customer info step (if present)
            try {
                WebElement continueBtn = wait.until(ExpectedConditions.presenceOfElementLocated(continueBtnBy));
                safeClick(continueBtn);
            } catch (Exception ignored) {
                // If we are already on Step 2 or single-step checkout, proceed
            }

            // Step 2: Explicitly wait for "Finish" button on the overview step and click it
            WebElement finishBtn = wait.until(ExpectedConditions.presenceOfElementLocated(finishBtnBy));
            safeClick(finishBtn);

        } catch (Exception e) {
            System.err.println("Could not complete checkout flow: " + e.getMessage());
        }
    }

    public void clickFinishMultipleTimes(int times) {
        // Advance past Step 1 first
        try {
            List<WebElement> continueBtns = driver.findElements(continueBtnBy);
            if (!continueBtns.isEmpty() && continueBtns.get(0).isDisplayed()) {
                safeClick(continueBtns.get(0));
            }
        } catch (Exception ignored) {}

        // Rapidly click the Finish button on Step 2
        for (int i = 0; i < times; i++) {
            try {
                List<WebElement> btns = driver.findElements(finishBtnBy);
                if (!btns.isEmpty() && btns.get(0).isDisplayed()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btns.get(0));
                }
            } catch (Exception e) {
                break; 
            }
        }
    }

    public boolean isOrderPlacedSuccessfully() {
        try {
            WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(12));
            return longWait.until(d -> {
                String currentUrl = d.getCurrentUrl().toLowerCase();

                if (currentUrl.contains("complete") || currentUrl.contains("success") || currentUrl.contains("confirmation")) {
                    return true;
                }

                try {
                    String pageSource = d.getPageSource().toLowerCase();
                    return pageSource.contains("thank you for your order") 
                        || pageSource.contains("order placed") 
                        || pageSource.contains("checkout: complete")
                        || pageSource.contains("order confirmed")
                        || pageSource.contains("dispatch");
                } catch (Exception ignored) {
                    return false;
                }
            });
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getCheckoutItemTitles() {
        List<String> titles = new ArrayList<>();
        try {
            List<WebElement> titleElements = driver.findElements(
                By.xpath("//div[contains(@class,'inventory_item_name') or contains(@class,'cart_item_label')]")
            );
            for (WebElement el : titleElements) {
                titles.add(el.getText().trim());
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch checkout item titles: " + e.getMessage());
        }
        return titles;
    }

    public int getCheckoutItemCount() {
        try {
            List<WebElement> items = driver.findElements(checkoutItemsBy);
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    private void safeClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", element);
        }
    }
    
}