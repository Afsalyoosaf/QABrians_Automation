package PagePackage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductListPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By PRODUCTS_LOCATOR = By.cssSelector("div.flex.flex-col.gap-3.relative.group");
    private final By PRODUCT_NAMES_LOCATOR = By.cssSelector("a.text-lg.font-semibold");
    private final By PRICE_LOCATOR = By.cssSelector("span.text-lg.font-bold");

    @FindBy(css = "div.flex.flex-col.gap-3.relative.group button")
    private List<WebElement> favouriteButtons;

    @FindBy(css = "div.flex.flex-col.gap-3.relative.group")
    private List<WebElement> products;

    @FindBy(css = "a.text-lg.font-semibold")
    private List<WebElement> productNames;

    @FindBy(css = "span.user-name")
    private WebElement profile_btn;

    @FindBy(xpath = "//div[@role='menuitem' and text()='Favorites']")
    private WebElement fav_btn;

    @FindBy(xpath = "//button[@role='combobox']")
    private WebElement sortDropdown;

    @FindBy(xpath = "//button[normalize-space()='Add to cart']")
    private List<WebElement> addToCartButtons;

    public ProductListPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getCurrentTitle() {
        return driver.getTitle();
    }

    public boolean areProductsDisplayed() {
        try {
            List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(PRODUCTS_LOCATOR));
            return !elements.isEmpty() && elements.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areProductNamesDisplayed() {
        try {
            List<WebElement> names = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(PRODUCT_NAMES_LOCATOR));
            if (names.isEmpty()) return false;
            for (WebElement name : names) {
                if (!name.isDisplayed() || name.getText().trim().isEmpty()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyAllProductCards() {
        if (!areProductsDisplayed()) return false;
        List<WebElement> cards = driver.findElements(PRODUCTS_LOCATOR);
        for (WebElement card : cards) {
            boolean hasImg = !card.findElements(By.tagName("img")).isEmpty();
            boolean hasName = !card.findElements(PRODUCT_NAMES_LOCATOR).isEmpty();
            boolean hasPrice = !card.findElements(PRICE_LOCATOR).isEmpty();
            boolean hasBtn = !card.findElements(By.tagName("button")).isEmpty();
            if (!hasImg || !hasName || !hasPrice || !hasBtn) return false;
        }
        return true;
    }

    public boolean dropDownClickabel() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(sortDropdown));
            return sortDropdown.isDisplayed() && sortDropdown.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectSortOption(String option) {
        List<WebElement> initialCards = driver.findElements(PRODUCTS_LOCATOR);
        WebElement firstCardBefore = initialCards.isEmpty() ? null : initialCards.get(0);

        // 1. Open the dropdown
        wait.until(ExpectedConditions.elementToBeClickable(sortDropdown));
        try {
            sortDropdown.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sortDropdown);
        }

        // 2. Resolve target data-value attribute directly
        String inputLower = option.toLowerCase().trim();
        String targetDataValue;

        if (inputLower.equals("low") || inputLower.contains("low to high")) {
            targetDataValue = "low";
        } else if (inputLower.equals("high") || inputLower.contains("high to low")) {
            targetDataValue = "high";
        } else if (inputLower.equals("asc") || inputLower.contains("a to z")) {
            targetDataValue = "asc";
        } else if (inputLower.equals("dsc") || inputLower.contains("z to a")) {
            targetDataValue = "dsc";
        } else {
            targetDataValue = inputLower;
        }

        // 3. Select item by data-value attribute
        By optionLocator = By.cssSelector("div[data-slot='command-item'][data-value='" + targetDataValue + "']");
        WebElement optionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));

        try {
            optionElement.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", optionElement);
        }

        // 4. Wait for DOM re-render
        if (firstCardBefore != null) {
            try {
                wait.until(ExpectedConditions.stalenessOf(firstCardBefore));
            } catch (Exception ignored) {
            }
        }

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(PRODUCTS_LOCATOR));
    }

    public List<Double> getAllPrices() {
        List<Double> prices = new ArrayList<>();
        List<WebElement> productCards = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(PRODUCTS_LOCATOR));
        Pattern numericPattern = Pattern.compile("\\d+(?:\\.\\d+)?");

        for (WebElement product : productCards) {
            List<WebElement> priceElements = product.findElements(PRICE_LOCATOR);
            if (!priceElements.isEmpty()) {
                String rawPrice = priceElements.get(0).getText();
                Matcher matcher = numericPattern.matcher(rawPrice);
                if (matcher.find()) {
                    prices.add(Double.parseDouble(matcher.group()));
                }
            }
        }
        System.out.println("Extracted UI Prices: " + prices);
        return prices;
    }

    public List<String> getAllProductName() {
        List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(PRODUCT_NAMES_LOCATOR));
        List<String> names = new ArrayList<>();
        for (WebElement e : elements) {
            names.add(e.getText().trim());
        }
        System.out.println("Extracted UI Product Names: " + names);
        return names;
    }

    // List Comparison for Price: Low to High
    public boolean verifyPriceLowToHigh() {
        List<Double> actual = getAllPrices();
        if (actual == null || actual.isEmpty()) return false;

        List<Double> sorted = new ArrayList<>(actual);
        Collections.sort(sorted);

        return actual.equals(sorted);
    }

    // List Comparison for Price: High to Low
    public boolean verifyPriceHighToLow() {
        List<Double> actual = getAllPrices();
        if (actual == null || actual.isEmpty()) return false;

        List<Double> sorted = new ArrayList<>(actual);
        Collections.sort(sorted, Collections.reverseOrder());

        return actual.equals(sorted);
    }

    // List Comparison for Name: A to Z
    public boolean verifyNameAToZ() {
        List<String> actual = getAllProductName();
        if (actual == null || actual.isEmpty()) return false;

        List<String> sorted = new ArrayList<>(actual);
        Collections.sort(sorted, String.CASE_INSENSITIVE_ORDER);

        return actual.equals(sorted);
    }

    // List Comparison for Name: Z to A
    public boolean verifyNameZToA() {
        List<String> actual = getAllProductName();
        if (actual == null || actual.isEmpty()) return false;

        List<String> sorted = new ArrayList<>(actual);
        Collections.sort(sorted, Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));

        return actual.equals(sorted);
    }

    public void clickFirstFavourite() {
        wait.until(ExpectedConditions.visibilityOfAllElements(favouriteButtons));
        safeClick(favouriteButtons.get(0));
        safeClick(profile_btn);
        safeClick(fav_btn);
        wait.until(ExpectedConditions.urlContains("/favorites"));
    }

    public List<String> getduplicateProductname() {
        List<String> productsList = getAllProductName();
        Set<String> unique = new HashSet<>();
        List<String> duplicate = new ArrayList<>();
        for (String product : productsList) {
            if (!unique.add(product)) {
                duplicate.add(product);
            }
        }
        return duplicate;
    }

    public List<String> getAllImageUrls() {
        List<String> imageUrls = new ArrayList<>();
        List<WebElement> images = driver.findElements(By.cssSelector("div.flex.flex-col.gap-3.relative.group img"));
        for (WebElement image : images) {
            imageUrls.add(image.getAttribute("src"));
        }
        return imageUrls;
    }

    public List<String> getDuplicateImages() {
        List<String> imageUrls = getAllImageUrls();
        Set<String> unique = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String url : imageUrls) {
            if (!unique.add(url) && !duplicates.contains(url)) {
                duplicates.add(url);
            }
        }
        return duplicates;
    }

    public String clickFirstAddToCart() {
        wait.until(ExpectedConditions.visibilityOfAllElements(addToCartButtons));
        List<WebElement> freshProductNames = driver.findElements(PRODUCT_NAMES_LOCATOR);
        
        if (addToCartButtons.isEmpty() || freshProductNames.isEmpty()) {
            throw new RuntimeException("No products or Add To Cart buttons present on the page.");
        }

        String productName = freshProductNames.get(0).getText().trim();
        safeClick(addToCartButtons.get(0));
        return productName;
    }

    private void safeClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (Exception e) {
            js.executeScript("arguments[0].click();", element);
        }
    }
}