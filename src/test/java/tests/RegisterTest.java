package tests;

import helpers.ApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.AccountOrderPage;
import pages.CatalogPage;

public class RegisterTest extends TestBase {
    private final TestData testData = new TestData();
    private CatalogPage catalogPage;
    private AccountOrderPage accountOrderPage;
    private String yourName, yourEmail, phone, password;

    @BeforeEach
    void setData() {
        yourName = testData.getYourFullName();
        yourEmail = testData.getYourEmail();
        phone = testData.getPhone();
        password = testData.getPassword();
        ApiClient apiClient = new ApiClient();
        catalogPage = new CatalogPage(apiClient);
        accountOrderPage = new AccountOrderPage(apiClient);
    }

    @Test
    @Tag("register")
    void successRegisterTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openRegisterModal()
                .fillRegisterForm(yourName, phone, yourEmail, password)
                .fillAuthForm(yourEmail, password);

        accountOrderPage.assertAuth(baseUrl, yourName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "!", "АбвгD"})
    @Tag("register")
    void validateMin6SymbolsPasswordFieldsRegisterTest(String password) {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openRegisterModal()
                .fillRegisterForm(yourName, phone, yourEmail, password)
                .assertInvalidPasswords();
    }

    @ParameterizedTest
    @ValueSource(strings = "")
    @Tag("register")
    void emptyFieldsRegisterTest(String empty) {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openRegisterModal()
                .fillRegisterForm(empty, empty, empty, empty)
                .assertEmptyRegisterFields();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "!" , "абвгд", "123456789"})
    @Tag("register")
    void validatePhoneFieldRegisterTest(String phone) {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openRegisterModal()
                .assertAddedIncorrectRegisterPhone(phone);
    }

    //негативный тест имейла
    //негативный тест галка политка перс данных
}
