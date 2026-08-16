package TestPackage;

import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import BasePackage.BaseClass;
import PagePackage.CartPage;
import PagePackage.HomePage;
import PagePackage.LoginPage;
import PagePackage.ProductListPage;

public class CartTest extends BaseClass {

    LoginPage login;
    HomePage home;
    CartPage cart;
    ProductListPage productList;
    AddToCartAll addHandler;

    @BeforeMethod
    public void loginApplication() {
        login = new LoginPage(driver);

        home = login.login(
                "test@qabrains.com",
                "Password123"
        );
        Assert.assertTrue(
                login.isLoginSuccessful(),
                "Login failed. Cart Management tests cannot continue."
        );
        productList = new ProductListPage(driver);
        cart = new CartPage(driver);
        addHandler = new AddToCartAll(driver);
    }

    @Test
    public void navigateToCart() {
        cart.toCartPage();
        Assert.assertTrue(cart.isCartOpen(), "Cart drawer or page failed to open.");
    }

    @Test
    public void verifyProductAddedToCart() {
        String productName = productList.clickFirstAddToCart();
        cart.toCartPage();

        Assert.assertTrue(
                cart.isProductInCart(productName),
                "Product was not added to the cart: " + productName
        );
    }

    @Test
    public void verifyIncreaseProductQuantity() {
        productList.clickFirstAddToCart();
        cart.toCartPage();

        int initialQty = cart.getProductQuantity();
        Assert.assertEquals(initialQty, 1, "Initial quantity should be 1.");

        cart.incrementQuantity();

        int updatedQty = cart.getProductQuantity();
        Assert.assertEquals(updatedQty, initialQty + 1, "Product quantity did not increment.");
    }

    @Test
    public void verifyDecreaseProductQuantity() {
        productList.clickFirstAddToCart();
        cart.toCartPage();

        cart.incrementQuantity();
        Assert.assertEquals(cart.getProductQuantity(), 2, "Quantity should be 2 after increment.");

        cart.decrementQuantity();
        Assert.assertEquals(cart.getProductQuantity(), 1, "Product quantity did not decrement back to 1.");
    }

    @Test
    public void verifyRemoveItemFromCart() {
        String productName = productList.clickFirstAddToCart();
        cart.toCartPage();
        Assert.assertTrue(cart.isProductInCart(productName), "Product is not present in cart before removal.");
        
        cart.decrementQuantity();
        cart.confirmRemovalIfDialogAppears();
        
        boolean isRemoved = cart.waitForProductToDisappear(productName);
        Assert.assertTrue(isRemoved, "Product was not removed from the cart after confirming removal.");
    }

    @Test
    public void verifyAddAllProductsToCart() {
        List<String> expectedProducts = addHandler.addEveryProductToCart();
        Assert.assertFalse(expectedProducts.isEmpty(), "No products were found to add to cart!");
        
        cart.toCartPage();
        for (String productName : expectedProducts) {
            Assert.assertTrue(
                    cart.isProductInCart(productName),
                    "Product missing from cart: " + productName
            );
        }
    }
}