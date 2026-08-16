//package TestPackage;
//
//import java.io.IOException;
//
//import org.testng.Assert;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;
//
//import BasePackage.BaseClass;
//import PagePackage.LoginPage;
//import UtilityPackage.ExcelUtils;
//
//public class LoginPositiveTest extends BaseClass {
//
//    @Test(dataProvider = "positiveData")
//    public void verifyValidLogin(String email, String password, String scenario) {
//
//        System.out.println("==================================");
//        System.out.println("Scenario : " + scenario);
//        System.out.println("Email    : " + email);
//
//        LoginPage login = new LoginPage(driver);
//
//        try {
//
//            login.login(email, password);
//
//            System.out.println("After Login URL   : " + driver.getCurrentUrl());
//            System.out.println("After Login Title : " + driver.getTitle());
//
//            Assert.assertTrue(login.isLoginSuccessful(),
//                    "Login failed for: " + scenario);
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            Assert.fail("Exception occurred during login.");
//
//        }
//    }
//
//    @DataProvider(name = "positiveData")
//    public Object[][] positiveData() throws IOException {
//        return ExcelUtils.getExcelData("Positive_data");
//    }
//}