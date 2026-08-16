package TestPackage;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import BasePackage.BaseClass;
import PagePackage.FavouriteListPage;
import PagePackage.HomePage;
import PagePackage.LoginPage;
import PagePackage.ProductListPage;
public class FavouriteListTest extends BaseClass {

    LoginPage login;
    HomePage home;
    FavouriteListPage favList;
    ProductListPage productList;

    @BeforeMethod
    public void loginApplication() {

        login = new LoginPage(driver);

        home = login.login("test@qabrains.com", "Password123");

        Assert.assertTrue(
                login.isLoginSuccessful(),
                "Login failed. Favorites List tests cannot continue."
        );
        productList = new ProductListPage(driver);
        favList = new FavouriteListPage(driver);
    }
    @Test
    public void verifyFavouritePageNavigation() {

        favList.openFavouritePage();

        Assert.assertTrue(
                favList.getCurrentUrl().contains("favorites"),
                "User was not navigated to the Favorites page."
        );
    }
    @Test
    public void verifyContinueShoppingFromEmptyFavouriteList() {

        favList.openFavouritePage();

        System.out.println("PAGE TEXT:");
        System.out.println(driver.findElement(By.tagName("body")).getText());

        Assert.assertTrue(
                favList.isFavouriteListEmpty(),
                "Favorites list is not empty."
        );

        favList.clickContinueShopping();

        Assert.assertTrue(
                favList.getCurrentUrl().contains("/ecommerce"),
                "Continue Shoppsing did not navigate back to the Ecommerce page."
        );
    } 
    @Test
    public void verifyProductAddedToFavourite() throws InterruptedException {
        productList.clickFirstFavourite();
        favList.openFavouritePage();
        Assert.assertFalse(
                favList.isFavouriteListEmpty(),
                "Product was not added to Favorites."
        );
    }
    @Test
    public void verifyProductRemovedFromFavoutite() throws InterruptedException{
    	productList.clickFirstFavourite();
    	favList.openFavouritePage();
    	productList.clickFirstFavourite();    	
    }
    @Test
    public void verifyProductToCart() throws InterruptedException {
        productList.clickFirstFavourite();
        favList.openFavouritePage();
        favList.addToCartFromFav();
    }
    @Test
    public void checkCart() throws InterruptedException {
    	productList.clickFirstFavourite();
        favList.openFavouritePage();
        favList.addToCartFromFav();
        favList.openCart();
    }
}