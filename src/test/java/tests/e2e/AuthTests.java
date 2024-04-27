package tests.e2e;

import fixtures.AssertFixturesPage;
import helpers.ApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.*;
import tests.TestBase;
import tests.TestData;

@Tag("e2e")
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
        AssertFixturesPage assertFixturesPage = new AssertFixturesPage(apiClient);

        catalogPage = new CatalogPage(apiClient, assertFixturesPage);
        accountOrderPage = new AccountOrderPage(apiClient);
    }

    @Test
    void authNewUserTest() {
        catalogPage.apiRegisterUser(yourName, yourEmail, yourPhone, password)
                .openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openAuthModal()
                .fillAuthForm(yourEmail, password);
        accountOrderPage.assertAuth(baseUrl, yourName);
    }

//    @Test
//    void authUnregisterUserTest() {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .openAuthModal()
//                .fillAuthForm(yourEmail, password);
//        catalogPage.assertUnAuth();
//    }

//    @Test
//    void authNewUserWithIncorrectPasswordTest() {
//        catalogPage.apiRegisterUser(yourName, yourEmail, yourPhone, password)
//                .openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .openAuthModal()
//                .fillAuthForm(yourEmail, testData.getPassword());
//        catalogPage.assertAuthIncorrectPass();
//    }

//    @ParameterizedTest(name = "Тест на проверку всплывающей ошибки 'Поле обязательно для заполнения' когда поля авторизации пустые")
//    @ValueSource(strings = {""})
//    void authEmptyFieldsTest(String empty) {
//        catalogPage.openCatalogPage(baseUrl)
//                .closeCookiePopUp()
//                .openAuthModal()
//                .fillUnAuthForm(empty, empty);
//        catalogPage.assertEmptyAuthFields();
//    }
}