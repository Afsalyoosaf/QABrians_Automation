package TestPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import BasePackage.BaseClass;
import PagePackage.HomePage;
import PagePackage.LoginPage;
import PagePackage.ProductListPage;

public class ProductListPageTest extends BaseClass {

    private LoginPage login;
    private HomePage home;
    private ProductListPage productListPage;

    @BeforeMethod
    public void loginApplication() {
        login = new LoginPage(driver);

        home = login.login(
                "test@qabrains.com",
                "Password123"
        );
        Assert.assertTrue(
                login.isLoginSuccessful(),
                "Login failed. Product List Page tests cannot continue."
        );

        productListPage = new ProductListPage(driver);
    }

    @Test(priority = 1)
    public void testProductCardsDisplayed() {
        Assert.assertTrue(
                productListPage.verifyAllProductCards(), 
                "Product cards are not displayed properly."
        );
    }

    @Test(priority = 2)
    public void testDropdownClickable() {
        Assert.assertTrue(
                productListPage.dropDownClickabel(), 
                "Sort dropdown is not clickable."
        );
    }

    @Test(priority = 3)
    public void testAddToCart() {
        String addedProduct = productListPage.clickFirstAddToCart();
        Assert.assertNotNull(
                addedProduct, 
                "Failed to click Add to Cart button."
        );
    }

    @Test(priority = 4)
    public void testClickFirstFavourite() {
        productListPage.clickFirstFavourite();
        Assert.assertTrue(
                productListPage.getCurrentUrl().contains("/favorites"), 
                "Did not navigate to favorites page."
        );
    }

    @Test(priority = 5, description = "Verify sorting Price Low to High")
    public void testSortByPriceLowToHigh() {
        productListPage.selectSortOption("low");
        
        List<Double> actualPrices = productListPage.getAllPrices();
        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices);

        Assert.assertEquals(
            actualPrices, 
            expectedPrices, 
            "BUG DETECTED: UI price order does not match expected Low to High sorting."
        );
    }

    @Test(priority = 6, description = "Verify sorting Price High to Low")
    public void testSortByPriceHighToLow() {
        productListPage.selectSortOption("high");
        
        List<Double> actualPrices = productListPage.getAllPrices();
        List<Double> expectedPrices = new ArrayList<>(actualPrices);
        Collections.sort(expectedPrices, Collections.reverseOrder());

        Assert.assertEquals(
            actualPrices, 
            expectedPrices, 
            "BUG DETECTED: UI price order does not match expected High to Low sorting."
        );
    }

    @Test(priority = 7, description = "Verify sorting Name A to Z")
    public void testSortByNameAToZ() {
        productListPage.selectSortOption("asc");
        
        List<String> actualNames = productListPage.getAllProductName();
        List<String> expectedNames = new ArrayList<>(actualNames);
        Collections.sort(expectedNames, String.CASE_INSENSITIVE_ORDER);

        Assert.assertEquals(
            actualNames, 
            expectedNames, 
            "BUG DETECTED: UI product name order does not match expected A to Z sorting."
        );
    }

    @Test(priority = 8, description = "Verify sorting Name Z to A")
    public void testSortByNameZToA() {
        productListPage.selectSortOption("dsc");
        
        List<String> actualNames = productListPage.getAllProductName();
        List<String> expectedNames = new ArrayList<>(actualNames);
        Collections.sort(expectedNames, Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));

        Assert.assertEquals(
            actualNames, 
            expectedNames, 
            "BUG DETECTED: UI product name order does not match expected Z to A sorting."
        );
    }

    @Test(priority = 9, description = "Verify no duplicate product names exist on page")
    public void testDuplicateProductNames() {
        List<String> duplicates = productListPage.getduplicateProductname();
        Assert.assertTrue(
                duplicates.isEmpty(), 
                "BUG DETECTED: Found duplicate product names: " + duplicates
        );
    }

    @Test(priority = 10, description = "Verify no duplicate product images exist on page")
    public void testDuplicateImages() {
        List<String> duplicates = productListPage.getDuplicateImages();
        Assert.assertTrue(
                duplicates.isEmpty(), 
                "BUG DETECTED: Found duplicate image URLs: " + duplicates
        );
    }
}