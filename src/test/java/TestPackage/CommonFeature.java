package TestPackage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import BasePackage.BaseClass;
import PagePackage.HomePage;
import PagePackage.LoginPage;

public class CommonFeature extends BaseClass {

    LoginPage login;
    HomePage home;

    @BeforeMethod
    public void loginToApplication() {

        login = new LoginPage(driver);

        home = login.login("test@qabrains.com", "Password123");

        Assert.assertTrue(login.isLoginSuccessful(),
                "Login failed. Common Feature tests cannot continue.");
    }

    @Test
    public void verifyHeaderDisplayed() {

        Assert.assertTrue(home.isHeaderDisplayed());
    }

    @Test
    public void verifyUrl() {

        String expectedUrl = "https://practice.qabrains.com/ecommerce";
        String actualUrl = home.getCurentUrl();

        System.out.println("Expected URL : " + expectedUrl);
        System.out.println("Actual URL   : " + actualUrl);

        Assert.assertEquals(actualUrl, expectedUrl);
    }

    @Test
    public void verifyTitle() {

        String expectedTitle = "QA Practice Site";
        String actualTitle = home.getCurrentTitle();

        Assert.assertEquals(actualTitle, expectedTitle);
    }

    @Test
    public void verifyLogo() {

        Assert.assertTrue(home.isLogoDisplayed());
    }

    @Test
    public void verifyLogoClickable() {

        home.logoClick();

        String expectedUrl = "https://practice.qabrains.com/ecommerce";
        String actualUrl = home.getCurentUrl();

        Assert.assertEquals(actualUrl, expectedUrl);
    }

    @Test
    public void verifyCart() {

        Assert.assertTrue(home.isCartDisplayed());
    }

    @Test
    public void verifyCartClickable() {

        home.cartClick();

        String expectedCartUrl = "https://practice.qabrains.com/ecommerce/cart";
        String actualCartUrl = home.getCurentUrl();

        Assert.assertEquals(actualCartUrl, expectedCartUrl);
    }

    @Test
    public void verifyProfile() {

        Assert.assertTrue(home.isProfileDisplayed());
    }

    @Test
    public void verifyFooterByScrolling() {

        home.scrollToFotter();

        Assert.assertTrue(home.isFooterDisplayed(),
                "Footer is not displayed after scrolling.");
    }

    @Test
    public void verifyLinkedIn() {

        home.linkdein();

        String expectedLinkedinUrl =
                "https://www.linkedin.com/showcase/qabrainscom";

        String actualLinkedinUrl = home.getCurentUrl();

        Assert.assertEquals(actualLinkedinUrl, expectedLinkedinUrl);
    }

    @Test
    public void verifyFacebook() {

        home.facebook();

        String expectedFacebookUrl =
                "https://www.facebook.com/qabrainscom";

        String actualFacebookUrl = home.getCurentUrl();

        Assert.assertEquals(actualFacebookUrl, expectedFacebookUrl);
    }

    @Test
    public void verifyYoutube() {

        home.youtube();

        String expectedYoutubeUrl =
                "https://www.youtube.com/@QABrains";

        String actualYoutubeUrl = home.getCurentUrl();

        Assert.assertEquals(actualYoutubeUrl, expectedYoutubeUrl);
    }
}