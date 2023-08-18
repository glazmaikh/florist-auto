package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import models.bouquet.BouquetDataDto;
import models.bouquet.BouquetDataItemDto;
import models.bouquet.PriceItemDto;
import models.city.CityData;
import models.city.CityResponse;
import models.cityAlias.Data;
import models.cityAlias.CityDataAliasDto;
import models.deliveryDate.DeliveryInfo;
import models.deliveryDate.DeliveryTime;
import models.disabledDelivery.DisabledDeliveryDateResponse;
import models.extras.ExtrasDataDto;
import models.extras.ExtrasDataItemDto;
import models.extras.ExtrasPrice;
import models.order.OrderData;
import models.register.User;
import models.register.UserWrapper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiClient {
    private final CityData city = getCity();
    private BouquetDataItemDto bouquet;
    private final List<ExtrasPrice> extrasList = new ArrayList<>();
    private ExtrasDataItemDto extras = new ExtrasDataItemDto();
    private ExtrasPrice extrasPrice = null;
    private OrderData orderData;
    private final Data data = getDeliveryPriceByCitySlug();
    private DeliveryTime deliveryTime;
    private final List<BouquetDataItemDto> bouquetList = new ArrayList<>();
    private List<BouquetDataItemDto> allBouquetsFromRequest = new ArrayList<>();

    // Получение обьекта города Астрахань
    @SneakyThrows
    private CityData getCity() {
        RequestSpecification httpRequest = given();
        Response responseCity = httpRequest
                .auth().basic("florist_api", "123")
                .param("p", "Астрахань")
                .param("mode", "geocity")
                .get("api/city/search");
        ResponseBody bodyCity = responseCity.getBody();

        ObjectMapper mapper = new ObjectMapper();
        CityResponse cityResponse = mapper.readValue(bodyCity.asString(), CityResponse.class);
        return cityResponse.getData().get(0);
    }

    // методы для взаимодействия с обьектом города Астрахань
    public String getCityName() {
        return city.getShort_name();
    }

    public int getCityId() {
        return city.getGeo().getCity().getId();
    }

    public String getCitySlug() {
        return city.getSlug();
    }

    // получение рандомного букета floristRu/Iflorist по ID города
    @SneakyThrows
    private void getRandomFloristRuBouquetByCityID() {
        RequestSpecification httpRequest = given();
        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", getCityId())
                .param("showPrices", 1)
                .param("includeIflorist", 1)
                .get("api/bouquet");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        BouquetDataDto bouquetData = objectMapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
        allBouquetsFromRequest = new ArrayList<>(bouquetData.getData().values());
        bouquet = getRandomBouquetFloristRu(bouquetData.getData());
        bouquetList.add(bouquet);
    }

    @SneakyThrows
    private void getRandomIFloristBouquetByCityID() {
        RequestSpecification httpRequest = given();
        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", getCityId())
                .param("showPrices", 1)
                .param("includeIflorist", 1)
                .get("api/bouquet");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        BouquetDataDto bouquetData = objectMapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
        allBouquetsFromRequest = new ArrayList<>(bouquetData.getData().values());
        bouquet = getBouquetIFloristList(bouquetData.getData());
        bouquetList.add(bouquet);
    }

    @SneakyThrows
    private void getRandomAllBouquetByCityID() {
        RequestSpecification httpRequest = given();
        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", getCityId())
                .param("showPrices", 1)
                .param("includeIflorist", 1)
                .get("api/bouquet");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        BouquetDataDto bouquetData = objectMapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
        allBouquetsFromRequest = new ArrayList<>(bouquetData.getData().values());
        bouquet = getRandomBouquet(bouquetData.getData());
        bouquetList.add(bouquet);
    }

    public void initBouquet(BouquetType bouquetType) {
        switch (bouquetType) {
            case FLORIST_RU -> getRandomFloristRuBouquetByCityID();
            case IFLORIST -> getRandomIFloristBouquetByCityID();
            case ALL_BOUQUETS -> getRandomAllBouquetByCityID();
        }
    }

    // методы для взаимодействия с обьектом Букет
    public int getBouquetId() {
        return bouquet.getId();
    }

    public int getBouquetListReminder() {
        return allBouquetsFromRequest.size() % 60;
    }

    public String getBouquetName() {
        return bouquet.getName();
    }

    public String getBouquetPrice(CurrencyType currencyType) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return switch (currencyType) {
            case EUR -> decimalFormat.format(bouquet.getMin_price().getEur()).replace(",",".");
            case KZT -> String.valueOf(bouquet.getMin_price().getKzt());
            case USD -> decimalFormat.format(bouquet.getMin_price().getUsd()).replace(",",".");
            case RUB -> String.valueOf(bouquet.getMin_price().getRub());
        };
    }

    public List<PriceItemDto> getPriceList() {
        return bouquet.getPriceList();
    }

    public List<String> getBouquetNameList() {
        return bouquetList.stream()
                .map(BouquetDataItemDto::getName).toList();
    }

    public List<String> getBouquetPriceList(CurrencyType currencyType) {
        return switch (currencyType) {
            case EUR -> bouquetList.stream()
                    .map(e -> e.getMin_price().getEur())
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            case KZT -> bouquetList.stream()
                    .map(e -> e.getMin_price().getKzt())
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            case USD -> bouquetList.stream()
                    .map(e -> e.getMin_price().getUsd())
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            case RUB -> bouquetList.stream()
                    .map(e -> e.getMin_price().getRub())
                    .map(String::valueOf)
                    .collect(Collectors.toList());
        };
    }

    public void initExtras() {
        getRandomExtras();
    }

    @SneakyThrows
    private void getRandomExtras() {
        RequestSpecification httpRequest = given();
        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", getCityId())
                .param("showPrices", 1)
                .get("api/extras");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        ExtrasDataDto extrasData = objectMapper.readValue(bodyBouquet.asString(), ExtrasDataDto.class);
        extras = getRandomExtrasFromMap(extrasData.getData());
        extrasPrice = getFirstExtrasVariation(extras.getPrices());
    }

    public String getExtrasName() {
        return extras.getName();
    }

    public String getPriceExtrasFirstVariation(CurrencyType currencyType) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return switch (currencyType) {
            case EUR, USD -> decimalFormat.format(extrasPrice.getPrice().get(currencyType.name())).replace(",",".");
            case KZT, RUB -> String.valueOf(extrasPrice.getPrice().get(currencyType.name())).replaceAll("(\\d+)\\.\\d+", "$1");
        };
    }

    public String getExtrasVariationName() {
        return extrasPrice.getName();
    }

    public List<String> getExtrasPriceList(CurrencyType currencyType) {
        return extrasList.stream()
                .map(e -> e.getPrice().get(currencyType.name()))
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    // получение обьекта Data - цены доставки по slug города
    @SneakyThrows
    public Data getDeliveryPriceByCitySlug() {
        RequestSpecification httpRequest = given();
        Response responseCitySlug = httpRequest
                .auth().basic("florist_api", "123")
                .param("alias", city.getSlug())
                .get("api/city/0");
        ResponseBody responseBody = responseCitySlug.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        CityDataAliasDto cityDataAliasDto = objectMapper.readValue(responseBody.asString(), CityDataAliasDto.class);
        return cityDataAliasDto.getData();
    }

    // методы взаимодействия с обьектом Data - цен доставки по slug города
    public String getDeliveryPrice(CurrencyType currencyType) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return switch (currencyType) {
            case EUR, USD -> decimalFormat.format(data.getDelivery().get(currencyType.name())).replace(",",".");
            case KZT, RUB -> String.valueOf(data.getDelivery().get(currencyType.name())).replaceAll("(\\d+)\\.\\d+", "$1");
        };
    }

    // получение обьекта OrderData о заказе из ERP
    @SneakyThrows
    public void getOrderData() {
        RequestSpecification httpRequest = given();
        Response responseOrderData = httpRequest
                .auth().basic("florist_api", "123")
                .param("id", HelperPage.getOrderNumber())
                .param("access_key", HelperPage.getOrderAccessKey())
                .get("api/order/byAccessKey");
        ResponseBody orderBody = responseOrderData.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        orderData = objectMapper.readValue(orderBody.asString(), OrderData.class);
    }

    // методы для взаимодействия с обьектом OrderData о заказе из ERP
    public int getOrderId() {
        return orderData.getData().getId();
    }

    public String getOrderTotalPrice(CurrencyType currencyType) {
        return switch (currencyType) {
            case EUR -> String.valueOf(orderData.getData().getTotal().getEUR());
            case KZT -> String.valueOf(orderData.getData().getTotal().getKZT());
            case USD -> String.valueOf(orderData.getData().getTotal().getUSD());
            case RUB -> String.valueOf(orderData.getData().getTotal().getRUB());
        };
    }

    public String getOrderStatus() {
        return orderData.getData().getStatus_text();
    }

    public String getOrderCreatedAt() {
        return orderData.getData().getCreated_at();
    }

    public String getRecipientName() {
        return orderData.getData().getRecipient_name();
    }

    public String getMaxPaidDate() {
        return orderData.getData().getMax_paid_date();
    }

    // Получение списка недоступных дней для доставки
    @SneakyThrows
    public List<String> getDisabledDeliveryDaysList() {
        RequestSpecification httpRequest = given();
        Response responseDisabledData = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", getCityId())
                .param("ids", bouquet.getId())
                .get("/api/delivery/date");
        ResponseBody responseBody = responseDisabledData.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        DisabledDeliveryDateResponse disabledDate = objectMapper.readValue(responseBody.asString(), DisabledDeliveryDateResponse.class);
        return new ArrayList<>(disabledDate.getData().getDisabled_dates().values());
    }

    // получение рандомного обьекта с возможным интервалом доставки
    @SneakyThrows
    public void getDeliveryDateInterval(String withoutDisabledDay) {
        RequestSpecification httpRequest = given();
        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", getCityId())
                .param("date", withoutDisabledDay)
                .param("ids", bouquet.getId())
                .get("api/delivery/time");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        DeliveryInfo deliveryInfo = objectMapper.readValue(bodyBouquet.asString(), DeliveryInfo.class);
        deliveryTime = deliveryInfo.getData().getDelivery_time();
    }

    public double getDeliveryTimeFrom() {
        return deliveryTime.getFrom();
    }

    public double getDeliveryTimeTo() {
        return deliveryTime.getTo();
    }

    // метод регистрации клиента на сайте
    public void apiRegisterUser(String login, String email, String phone, String password) {
        User user = new User(login, email, phone, password);
        UserWrapper userWrapper = new UserWrapper(user);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String requestBody = objectMapper.writeValueAsString(userWrapper);
            Response response = given()
                    .auth().basic("florist_api", "123")
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .post("api/user");

            response.then().statusCode(200);

            response.then().assertThat()
                    .body("data.name", equalTo(user.getName()))
                    .body("data.email", equalTo(user.getEmail()))
                    .body("data.phone", equalTo(user.getPhone()))
                    .body("data.password", equalTo(user.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // методы получения рандомного обьекта Букета
    // иногда error index must be positive
    private BouquetDataItemDto getRandomBouquetFloristRu(Map<String, BouquetDataItemDto> bouquetMap) {
        Map<String, BouquetDataItemDto> filteredMap = bouquetMap.entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith("6"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return getRandomBouquet(filteredMap);
    }

    private ExtrasDataItemDto getRandomExtrasFromMap(Map<String, ExtrasDataItemDto> extrasMap) {
        List<ExtrasDataItemDto> values = new ArrayList<>(extrasMap.values());
        return values.get(new Random().nextInt(values.size()));
    }

    private ExtrasPrice getFirstExtrasVariation(Map<String, ExtrasPrice> map) {
        ExtrasPrice extrasPrice = null;

        for (ExtrasPrice price : map.values()) {
            if ("Стандартный".equals(price.getName())) {
                extrasPrice = price;
                break;
            }
        }
        extrasList.add(extrasPrice);
        return extrasPrice;
    }

    private BouquetDataItemDto getBouquetIFloristList(Map<String, BouquetDataItemDto> bouquetMap) {
        Map<String, BouquetDataItemDto> filteredMap = bouquetMap.entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith("333"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return getRandomBouquet(filteredMap);
    }

    private BouquetDataItemDto getRandomBouquet(Map<String, BouquetDataItemDto> map) {
        List<BouquetDataItemDto> values = new ArrayList<>(map.values());
        return values.get(new Random().nextInt(values.size()));
    }
}