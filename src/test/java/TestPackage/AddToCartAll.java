package TestPackage;

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

public class AddToCartAll {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(xpath = "//button[contains(text(),'Add to Cart') or contains(text(),'Add')]")
    private List<WebElement> addToCartButtons;

    @FindBy(xpath = "//h3 | //h4 | //h5 | //div[contains(@class,'title')] | //span[contains(@class,'title')]")
    private List<WebElement> productTitles;

    public AddToCartAll(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public List<String> addEveryProductToCart() {
        wait.until(ExpectedConditions.visibilityOfAllElements(addToCartButtons));
        List<String> addedProductsNames = new ArrayList<>();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        for (int i = 0; i < addToCartButtons.size(); i++) {
            WebElement button = addToCartButtons.get(i);
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", button);

            String title = "";

            try {
                WebElement parentCard = button.findElement(By.xpath("./ancestor::div[contains(@class,'card') or contains(@class,'product') or contains(@class,'item') or position()=2]"));
                WebElement titleElement = parentCard.findElement(By.xpath(".//*[self::h3 or self::h4 or self::h5 or contains(@class,'title')]"));
                title = titleElement.getText().trim();
            } catch (Exception e) {
                if (i < productTitles.size()) {
                    title = productTitles.get(i).getText().trim();
                }
            }

            if (!title.isEmpty() && !title.equalsIgnoreCase("Products")) {
                addedProductsNames.add(title);
            }

            try {
                wait.until(ExpectedConditions.elementToBeClickable(button)).click();
            } catch (Exception e) {
                js.executeScript("arguments[0].click();", button);
            }
        }
        return addedProductsNames;
    }

    public int getTotalProductOnPage() {
        wait.until(ExpectedConditions.visibilityOfAllElements(addToCartButtons));
        return addToCartButtons.size();
    }
}