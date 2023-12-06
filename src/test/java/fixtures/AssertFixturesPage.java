package fixtures;

import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.CurrencyType;
import helpers.HelperPage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertFixturesPage {
    private final ApiClient apiClient;

    public AssertFixturesPage(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public void performAssertBouquetPrice(SelenideElement orderSection, CurrencyType currencyType) {
        String bouquetsPrice = HelperPage.priceCurrencyFormat(currencyType, apiClient.getBouquetPrice(currencyType));
        assertTrue(orderSection.getText().contains(bouquetsPrice), "bouquets price is not equals");
    }

    public void performAssertBouquetPriceListTEST(SelenideElement orderSection, CurrencyType currencyType, String deliveryDate) {
        List<String> bouquetsPrices = apiClient.getBouquetPriceListTEST(currencyType, deliveryDate).stream()
                .map(String::valueOf)
                .map(e -> HelperPage.priceCurrencyFormat(currencyType, e))
                .toList();
    }

//    public void performAssertBouquetPriceList(SelenideElement orderSection, CurrencyType currencyType) {
//        List<String> bouquetsPrices = apiClient.getBouquetPriceList(currencyType).stream()
//                .peek(e -> System.out.println(e + " beforeToStr+Regex from api"))
//                .map(String::valueOf)
//                .map(e -> HelperPage.priceCurrencyFormat(currencyType, e))
//                .peek(e -> System.out.println(e + " afterToStr+Regex from api"))
//                .toList();
//        System.out.println(bouquetsPrices + " bouquetsPrices list");
//        System.out.println(orderSection.getText() + " orderSection.getText()");
//        assertTrue(HelperPage.isOrderSectionContainsAllFromBouquets(orderSection, bouquetsPrices),
//                "bouquets prices not equals");
//    }

//        List<String> bouquetsPrices = apiClient.getBouquetMinPriceList()PriceList(currencyType).stream()
//                .peek(e -> System.out.println(e + " beforeToStr+Regex from api"))
//                .map(String::valueOf)
//                .map(e -> HelperPage.priceCurrencyFormat(currencyType, e))
//                .peek(e -> System.out.println(e + " afterToStr+Regex from api"))
//                .toList();
//        System.out.println(bouquetsPrices + " bouquetsPrices list");
//        System.out.println(orderSection.getText() + " orderSection.getText()");
//        assertTrue(HelperPage.isOrderSectionContainsAllFromBouquets(orderSection, bouquetsPrices),
//                "bouquets prices not equals");
//    }

    public void performAssertDeliveryPrice(SelenideElement orderSection, CurrencyType currencyType) {
        String deliveryPrice = apiClient.getDeliveryPrice(currencyType);
        if (Double.parseDouble(deliveryPrice) > 1) {
            orderSection.shouldHave(text(HelperPage.priceCurrencyFormat(currencyType, deliveryPrice)));
        } else {
            orderSection.shouldBe(text("бесплатно"));
        }
    }

    public void performAssertExtrasPrice(SelenideElement orderSection, CurrencyType currencyType) {
        List<String> extrasPrices = apiClient.getExtrasPriceList(currencyType).stream()
                .map(e -> HelperPage.priceCurrencyFormat(currencyType, e))
                .toList();
        assertTrue(HelperPage.isOrderSectionContainsAllFromBouquets(orderSection, extrasPrices),
                "prices of extras are not equals");
    }
}