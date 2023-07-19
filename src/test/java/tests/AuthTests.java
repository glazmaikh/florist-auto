package tests;

import helpers.ApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.*;

public class AuthTests extends TestBase {
    private final TestData testData = new TestData();
    private CatalogPage catalogPage;
    private AccountOrderPage accountOrderPage;
    private String yourName, yourEmail, yourPhone, password;

    @BeforeEach
    void setData() {
        yourName = testData.getYourFullName();
        yourEmail = testData.getYourEmail();
        yourPhone = testData.getYourPhone();
        password = testData.getPassword();
        ApiClient apiClient = new ApiClient();
        catalogPage = new CatalogPage(apiClient);
        accountOrderPage = new AccountOrderPage(apiClient);
    }

    @Test
    @Tag("auth")
    void authNewUserTest() {
        catalogPage.apiRegisterUser(yourName, yourEmail, yourPhone, password)
                .openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openAuthModal()
                .fillAuthForm(yourEmail, password);
        accountOrderPage.assertAuth(baseUrl, yourName);
    }

    @Test
    @Tag("auth")
    void authUnregisterUserTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openAuthModal()
                .fillAuthForm(yourEmail, password);
        catalogPage.assertUnAuth();
    }

    @Test
    @Tag("auth")
    void authNewUserWithIncorrectPasswordTest() {
        catalogPage.apiRegisterUser(yourName, yourEmail, yourPhone, password)
                .openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openAuthModal()
                .fillAuthForm(yourEmail, testData.getPassword());
        catalogPage.assertAuthIncorrectPass();
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    @Tag("auth")
    void authEmptyFieldsTest(String empty) {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openAuthModal()
                .fillUnAuthForm(empty, empty);
        catalogPage.assertEmptyAuthFields();
    }
}
