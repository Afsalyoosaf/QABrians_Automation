package TestPackage;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import BasePackage.BaseClass;
import PagePackage.CheckoutPage;
import PagePackage.EtoELogin;
import PagePackage.FavouriteListPage;
import PagePackage.HomePage;

public class E_to_E_Test extends BaseClass {

    private EtoELogin e2eLogin;
    private HomePage home;
    private FavouriteListPage favouriteListPage;
    private CheckoutPage checkoutPage;

    @DataProvider(name = "ValidTestMailId")
    public Object[][] getValidTestMailId() {
        return new Object[][] {
            { "test@qabrains.com" },
            { "practice@qabrains.com" },
            { "student@qabrains.com" }
        };
    }

    @BeforeMethod
    public void setup() {
        // 1. Launch a fresh Chrome browser instance for each iteration
        driver = new org.openqa.selenium.chrome.ChromeDriver(); 
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.get("https://practice.qabrains.com/ecommerce/login");
        
        // 2. Initialize Page Objects
        e2eLogin = new EtoELogin(driver);
        favouriteListPage = new FavouriteListPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }

    @Test(dataProvider = "ValidTestMailId")
    public void verifyFullE2EFlow(String email) {
        // Step 1: Login
        home = e2eLogin.performEtoELogin(email, "Password123");
        Assert.assertTrue(e2eLogin.isLoginSuccessful(), "E2E Login failed for: " + email);

        // Step 2: Navigate to Favorites Page
        favouriteListPage.openFavouritePage();
        Assert.assertTrue(favouriteListPage.getCurrentUrl().contains("/favorites"), 
                "Failed to navigate to Favorites page for user: " + email);

        // Step 3: Handle Favorites to Cart transition
        if (!favouriteListPage.isFavouriteListEmpty()) {
            
            // Add favorite item to cart
            favouriteListPage.addToCartFromFav();
            
            // Navigate to Cart page
            favouriteListPage.openCart();
            Assert.assertTrue(favouriteListPage.getCurrentUrl().contains("/cart"), 
                    "Failed to navigate to Cart page for user: " + email);

            // Step 4: Complete Checkout using your CheckoutPage methods
            checkoutPage.enterFirstName("John");
            checkoutPage.enterLastName("Doe");
            checkoutPage.enterZipCode("682001");

            // Clicks Continue (Step 1) and Finish (Step 2) automatically
            checkoutPage.clickFinish();

            // Step 5: Assert successful order placement
            Assert.assertTrue(checkoutPage.isOrderPlacedSuccessfully(), 
                    "Checkout order placement failed for user: " + email);

        } else {
            System.out.println("Favorites list is empty for user: " + email + ". Navigating back to shop.");
            favouriteListPage.clickContinueShopping();
        }
    }

    @AfterMethod
    public void tearDown() {
        // Safely close the browser session after each iteration
        if (driver != null) {
            driver.quit();
        }
    }
}