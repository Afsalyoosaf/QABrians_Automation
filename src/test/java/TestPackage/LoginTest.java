package TestPackage;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import BasePackage.BaseClass;
import PagePackage.LoginPage;
import UtilityPackage.ExcelUtils;

public class LoginTest extends BaseClass {

    @Test
    public void verifyLogin() {

        LoginPage login = new LoginPage(driver);

        login.login("test@qabrains.com", "Password123");

        Assert.assertTrue(login.isLoginSuccessful(),
                "Login was unsuccessful");

        System.out.println("Login Successful");
    }
    
//    @DataProvider(name = "positiveData")
//    public Object[][] positiveData() throws IOException {
//        return ExcelUtils.getExcelData("PositiveLogin");
//    }
}