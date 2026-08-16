//package TestPackage;
//
//import org.testng.Assert;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import BasePackage.BaseClass;
//import PagePackage.CartPage;
//import PagePackage.CheckoutPage;
//import PagePackage.HomePage;
//import PagePackage.LoginPage;
//import PagePackage.ProductListPage;
//
//public class e2e2 extends BaseClass {
//
//    LoginPage login;
//    HomePage home;
//    ProductListPage productList;
//    CartPage cart;
//    CheckoutPage checkoutPage;
//
//    @BeforeMethod
//    public void loginApplication() {
//        login = new LoginPage(driver);
//
//        home = login.login(
//                "test@qabrains.com",
//                "Password123"
//        );
//        Assert.assertTrue(
//                login.isLoginSuccessful(),
//                "Login failed. E2E tests cannot continue."
//        );
//
//        productList = new ProductListPage(driver);
//        cart = new CartPage(driver);
//        checkoutPage = new CheckoutPage(driver);
//    }
//
//    @Test
//    public void verifyHappyPathE2ECheckout() {
//        // Step 1: Add product directly using existing ProductListPage method
//        String addedProduct = productList.clickFirstAddToCart();
//
//        // Step 2: Navigate to cart
//        cart.toCartPage();
//        Assert.assertTrue(
//                cart.isCartOpen() || driver.getCurrentUrl().contains("/cart"),
//                "Failed to navigate to Cart page."
//        );
//
//        // --- MISSING STEP ADDED HERE ---
//        // Step 3: Click Checkout to open the checkout form
//        cart.proceedToCheckout(); // or checkoutPage.proceedToCheckout() depending on your page object method name
//
//        // Step 4: Fill out Checkout details
//        checkoutPage.enterFirstName("John");
//        checkoutPage.enterLastName("Doe");
//        checkoutPage.enterZipCode("682001");
//        checkoutPage.clickFinish();
//
//        // Step 5: Verify Order Completion
//        Assert.assertTrue(
//                checkoutPage.isOrderPlacedSuccessfully(),
//                "Happy Path E2E failed: Order placement was not successful!"
//        );
//    }
//}
package TestPackage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import BasePackage.BaseClass;
import PagePackage.CartPage;
import PagePackage.CheckoutPage;
import PagePackage.HomePage;
import PagePackage.LoginPage;
import PagePackage.ProductListPage;

public class e2e2 extends BaseClass {

    LoginPage login;
    HomePage home;
    ProductListPage productList;
    CartPage cart;
    CheckoutPage checkoutPage;

    @BeforeMethod
    public void loginApplication() {
        login = new LoginPage(driver);

        home = login.login(
                "test@qabrains.com",
                "Password123"
        );
        Assert.assertTrue(
                login.isLoginSuccessful(),
                "Login failed. E2E tests cannot continue."
        );

        productList = new ProductListPage(driver);
        cart = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }

    @Test
    public void verifyHappyPathE2ECheckout() throws InterruptedException {
        // Step 1: Add product directly from Product List page
        productList.clickFirstAddToCart();

        // Step 2: Open Cart page
        cart.toCartPage();
        Assert.assertTrue(
                cart.isCartOpen() || driver.getCurrentUrl().contains("/cart"),
                "Failed to navigate to Cart page."
        );

        // Step 3: Proceed from Cart to Checkout
        cart.proceedToCheckout();

        // Step 4: Fill out delivery and billing details
        checkoutPage.enterFirstName("John");
        checkoutPage.enterLastName("Doe");
        checkoutPage.enterZipCode("682001");

        // Step 5: Submit order
        checkoutPage.clickFinish();
        // Step 6: Verify Order Completion
        Assert.assertTrue(
                checkoutPage.isOrderPlacedSuccessfully(),
                "Happy Path E2E failed: Order placement was not successful! Current URL: " + driver.getCurrentUrl()
        );
    }
}