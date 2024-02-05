package tests.e2e;

import fixtures.AssertFixturesPage;
import helpers.ApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.AccountOrderPage;
import pages.CatalogPage;
import tests.TestBase;
import tests.TestData;

public class RegisterTest extends TestBase {
    private final TestData testData = new TestData();
    private CatalogPage catalogPage;
    private AccountOrderPage accountOrderPage;
    private String yourName, yourEmail, phone, yourPhone, password;

    @BeforeEach
    void setData() {
        yourName = testData.getYourFullName();
        yourEmail = testData.getYourEmail();
        phone = testData.getPhone();
        yourPhone = testData.getYourPhone();
        password = testData.getPassword();

        ApiClient apiClient = new ApiClient();
        AssertFixturesPage assertFixturesPage = new AssertFixturesPage(apiClient);

        catalogPage = new CatalogPage(apiClient, assertFixturesPage);
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

    @ParameterizedTest(name = "Негативный тест на проверку password min length с параметром {0}")
    @ValueSource(strings = {"12345", "!", "АбвгD"})
    @Tag("register")
    void validateMin6SymbolsPasswordFieldsRegisterTest(String password) {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openRegisterModal()
                .fillRegisterForm(yourName, phone, yourEmail, password)
                .assertInvalidPasswords();
    }

    @ParameterizedTest(name = "Тест на проверку всплывающей ошибки 'Поле обязательно для заполнения' когда поля регистрации пустые")
    @ValueSource(strings = "")
    @Tag("register")
    void emptyFieldsRegisterTest(String empty) {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openRegisterModal()
                .fillRegisterForm(empty, empty, empty, empty)
                .assertEmptyRegisterFields();
    }

    @ParameterizedTest(name = "Негативный тест на проверку валидации поля 'телефон' при регистрации")
    @ValueSource(strings = {"", "!" , "абвгд", "123456789"})
    @Tag("register")
    void validatePhoneFieldRegisterTest(String phone) {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openRegisterModal()
                .assertAddedIncorrectRegisterPhone(phone);
    }

    @ParameterizedTest(name = "Негативный тест на проверку валидации поля 'имейл' при регистрации")
    @ValueSource(strings = {"a", "aa@aa", "aa@aa.a", "aa@1.aa"})
    @Tag("register")
    void validateEmailFieldRegisterTest(String email) {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openRegisterModal()
                .assertAddedIncorrectRegisterEmail(email);
    }

    @Test
    @Tag("register")
    void validateErrorWhenTryRegisterWithoutAcceptDataPolicyTest() {
        catalogPage.openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openRegisterModal()
                .assertNotRegisterWithoutAcceptPolicy(yourName, phone, yourEmail, password);
    }

    @Test
    @Tag("register")
    void tryRegistrationWhenRegisteredCredsTest() {
        catalogPage.apiRegisterUser(yourName, yourEmail, yourPhone, password)
                .openCatalogPage(baseUrl)
                .closeCookiePopUp()
                .openRegisterModal()
                .fillRegisterForm(yourName, phone, yourEmail, password)
                .assertAlreadyExistsEmailWhenRegister();
    }
}