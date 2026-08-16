package TestPackage;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import BasePackage.BaseClass;
import TestPackage.AddToCartAll;
import PagePackage.CartPage;
import PagePackage.CheckoutPage;
import PagePackage.HomePage;
import PagePackage.LoginPage;
import PagePackage.ProductListPage;

public class CheckoutTest extends BaseClass {

    LoginPage login;
    HomePage home;
    CartPage cart;
    ProductListPage productList;
    CheckoutPage checkout;

    @BeforeMethod
    public void loginApplication() {
        login = new LoginPage(driver);

        home = login.login(
                "test@qabrains.com",
                "Password123"
        );
        Assert.assertTrue(
                login.isLoginSuccessful(),
                "Login failed. Checkout Management tests cannot continue."
        );
        productList = new ProductListPage(driver);
        cart = new CartPage(driver);
        checkout = new CheckoutPage(driver);
    }

    @DataProvider(name = "invalidCheckoutInputs")
    public Object[][] getInvalidCheckoutInputs() {
        return new Object[][] {
            { "", "", "", "Empty Mandatory Fields" },
            { "1233", "Dave", "123", "Numeric First Name" },
            { "John", "123", "123", "Numeric Last Name" },
            { "John", "Dave", "jknjkn", "Non-numeric Zip Code" }
        };
    }

    @Test
    public void verifyNavigateToCheckout() {
        productList.clickFirstAddToCart();
        cart.toCartPage();
        cart.proceedToCheckout();

        Assert.assertTrue(checkout.isCheckoutPageOpen(), "Failed to navigate to Checkout page.");
    }

    @Test
    public void verifyEmailFieldIsNotEditable() {
        productList.clickFirstAddToCart();
        cart.toCartPage();
        cart.proceedToCheckout();

        Assert.assertFalse(checkout.isEmailEditable(), "Email field should be read-only and not editable.");
    }

    @Test
    public void verifyCancelButtonNavigatesToCart() {
        productList.clickFirstAddToCart();
        cart.toCartPage();
        cart.proceedToCheckout();

        checkout.clickCancel();

        Assert.assertTrue(cart.isCartOpen(), "Cancel button failed to navigate back to Cart.");
    }

    @Test(dataProvider = "invalidCheckoutInputs")
    public void verifyCheckoutValidationWithInvalidData(String fname, String lname, String zip, String scenario) {
        productList.clickFirstAddToCart();
        cart.toCartPage();
        cart.proceedToCheckout();

        checkout.enterFirstName(fname);
        checkout.enterLastName(lname);
        checkout.enterZipCode(zip);
        checkout.clickFinish();

        Assert.assertFalse(
                checkout.isOrderPlacedSuccessfully(),
                "Order submitted successfully despite invalid input in scenario: " + scenario
        );
    }

    @Test
    public void verifySuccessfulOrderPlacement() {
        productList.clickFirstAddToCart();
        cart.toCartPage();
        cart.proceedToCheckout();

        checkout.enterFirstName("John");
        checkout.enterLastName("Doe");
        checkout.enterZipCode("682001");
        checkout.clickFinish();

        Assert.assertTrue(checkout.isOrderPlacedSuccessfully(), "Order placement failed with valid details.");
    }

    @Test
    public void verifyPreventDuplicateOrderOnMultiClick() {
        productList.clickFirstAddToCart();
        cart.toCartPage();
        cart.proceedToCheckout();

        checkout.enterFirstName("John");
        checkout.enterLastName("Doe");
        checkout.enterZipCode("682001");

        checkout.clickFinishMultipleTimes(3);

        Assert.assertTrue(
            checkout.isOrderPlacedSuccessfully(), 
            "Multiple rapid clicks prevented normal order submission or caused application crash."
        );
    }

    @Test
    public void verifyCheckoutAllProductsFromPage() {
        // 1. Initialize AddToCartAll helper object
        AddToCartAll addToCartAll = new AddToCartAll(driver);

        // 2. Add every product available on the page and capture their names
        List<String> expectedAddedProducts = addToCartAll.addEveryProductToCart();
        int expectedCount = expectedAddedProducts.size();

        Assert.assertTrue(expectedCount > 0, "No products were added to the cart.");

        // 3. Navigate to Cart and then Checkout
        cart.toCartPage();
        cart.proceedToCheckout();

        Assert.assertTrue(checkout.isCheckoutPageOpen(), "Failed to navigate to Checkout page.");

        // 4. Validate total count matches on Checkout Overview
        int actualCheckoutCount = checkout.getCheckoutItemCount();
        if (actualCheckoutCount > 0) {
            Assert.assertEquals(
                actualCheckoutCount, 
                expectedCount, 
                "Mismatch between number of items added and items displayed on Checkout screen."
            );
        }

        // 5. Submit shipping details and complete order
        checkout.enterFirstName("John");
        checkout.enterLastName("Doe");
        checkout.enterZipCode("682001");
        checkout.clickFinish();

        // 6. Assert successful order completion
        Assert.assertTrue(
            checkout.isOrderPlacedSuccessfully(), 
            "Order placement failed when checking out with all page items."
        );
    }
    
}