package fixtures;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import helpers.ApiClient;
import helpers.CurrencyType;
import helpers.DeliveryDateType;
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

    public void performAssertBouquetMinPrice(SelenideElement orderSection, CurrencyType currencyType) {
        String bouquetsPrice = HelperPage.priceCurrencyFormat(currencyType, apiClient.getBouquetMinPrice(currencyType));
        assertTrue(orderSection.getText().contains(bouquetsPrice), "bouquets price is not equals");
    }

    public void performAssertBouquetPriceList(SelenideElement orderSection, CurrencyType currencyType, DeliveryDateType deliveryDateType) {
        List<String> bouquetsPrices = apiClient.getBouquetPriceList(currencyType, deliveryDateType).stream()
                .map(String::valueOf)
                .map(e -> HelperPage.priceCurrencyFormat(currencyType, e))
                .toList();
        assertTrue(HelperPage.isOrderSectionContainsAllFromBouquets(orderSection, bouquetsPrices),
                "bouquets prices not equals");
    }

//    public void performAssertBouquetPriceList(SelenideElement orderSection, CurrencyType currencyType) {
//        List<String> bouquetsPrices = apiClient.getBouquetPriceList(currencyType).stream()
//                .map(String::valueOf)
//                .map(e -> HelperPage.priceCurrencyFormat(currencyType, e))
//                .toList();
//        assertTrue(HelperPage.isOrderSectionContainsAllFromBouquets(orderSection, bouquetsPrices),
//                "bouquets prices not equals");
//    }

//        List<String> bouquetsPrices = apiClient.getBouquetMinPriceList()PriceList(currencyType).stream()
//                .map(String::valueOf)
//                .map(e -> HelperPage.priceCurrencyFormat(currencyType, e))
//                .toList();
//        assertTrue(HelperPage.isOrderSectionContainsAllFromBouquets(orderSection, bouquetsPrices),
//                "bouquets prices not equals");
//    }

    public void performAssertDeliveryPrice(SelenideElement orderSection, CurrencyType currencyType) {
        String deliveryPrice = apiClient.getDeliveryPrice(currencyType);
        System.out.println(deliveryPrice + " deliveryPrice");
        if (Double.parseDouble(deliveryPrice) > 1) {
            orderSection.shouldHave(text(HelperPage.priceCurrencyFormat(currencyType, deliveryPrice)));
        } else {
            orderSection.shouldBe(text("бесплатно"));
        }
    }

    public void performAssertDeliveryPSPrice(SelenideElement orderSection, CurrencyType currencyType) {
        String deliveryPrice = apiClient.getDeliveryPSPrice(currencyType);
        if (Double.parseDouble(deliveryPrice) > 1) {
            orderSection.shouldHave(text(HelperPage.priceCurrencyFormat(currencyType, deliveryPrice)));
        } else {
            orderSection.shouldBe(text("бесплатно"));
        }
    }



    public void performAssertExtrasPrice(SelenideElement orderSection, CurrencyType currencyType, DeliveryDateType deliveryDateType) {
        List<String> extrasPrices = apiClient.getExtrasPriceList(currencyType, deliveryDateType).stream()
                .map(e -> HelperPage.priceCurrencyFormat(currencyType, e))
                .toList();
        assertTrue(HelperPage.isOrderSectionContainsAllFromBouquets(orderSection, extrasPrices),
                "prices of extras are not equals");
    }
}