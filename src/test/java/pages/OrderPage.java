package pages;

import helpers.ApiClient;
import models.auth.User;

import static com.codeborne.selenide.Selenide.webdriver;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderPage {
    private final ApiClient apiClient;
    public OrderPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public OrderPage assertAuth() {
//        assertTrue(webdriver().driver().url().contains("/account/orders"));
//        System.out.println(apiClient.getUserName());
//        System.out.println("Здравствуйте, " + apiClient.getUserName() + "!");

        User user = apiClient.getUser("test123123@test.ru", "123123");
        System.out.println(user.getCity());
        return this;
    }

}
