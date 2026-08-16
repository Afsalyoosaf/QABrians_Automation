package TestPackage;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import BasePackage.BaseClass;
import PagePackage.LoginPage;
import UtilityPackage.ExcelUtils;

public class LoginNegativeTest extends BaseClass {

    @Test(dataProvider = "loginData")
    public void verifyLogin(String email,
                            String password,
                            String scenario,
                            String expected) {

        System.out.println("=================================");
        System.out.println("Scenario : " + scenario);
        System.out.println("Expected : " + expected);

        LoginPage login = new LoginPage(driver);

        login.login(email, password);

        if (expected.equalsIgnoreCase("PASS")) {

            Assert.assertTrue(login.isLoginSuccessful(),
                    "Expected successful login for : " + scenario);

            System.out.println("Login Successful");

        } else {

            Assert.assertTrue(login.isStillOnLoginPage(),
                    "Expected login failure for : " + scenario);

            System.out.println("Login Failed");
        }

        System.out.println("Current URL : " + login.getCurrentUrl());
    }

    @DataProvider(name = "loginData")
    public Object[][] loginData() throws IOException {

        return ExcelUtils.getExcelData("Negative_data");
    }
}