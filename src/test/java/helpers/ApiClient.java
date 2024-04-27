package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import entity.bouquet.*;
import entity.city.CityData;
import entity.city.CityResponse;
import entity.cityAlias.Data;
import entity.cityAlias.CityDataAliasDto;
import entity.deliveryDate.DeliveryInfo;
import entity.deliveryDate.DeliveryTime;
import entity.disabledDelivery.DisabledDeliveryDateResponse;
import entity.extras.ExtrasDataDto;
import entity.extras.ExtrasDataItemDto;
import entity.extras.ExtrasPriceItemDto;
import entity.order.CartItem;
import entity.order.OrderData;
import entity.register.User;
import entity.register.UserWrapper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiClient {
    private final ObjectMapper mapper = new ObjectMapper();
    private final CityData city = getCity();
    private final CityData cityPS = getPSCity();
    private BouquetDataItemDto bouquet;
    private ExtrasDataItemDto extras = new ExtrasDataItemDto();
    private final List<ExtrasDataItemDto> extrasList = new ArrayList<>();
    private ExtrasPriceItemDto extrasPriceItemDto;
    private final List<ExtrasPriceItemDto> extrasPricesFirstVariations = new ArrayList<>();
    private OrderData orderData;
    private final Data data = getDeliveryPriceByCitySlug();
    private DeliveryTime deliveryTime;
    private final List<BouquetDataItemDto> bouquetList = new ArrayList<>();
    private List<BouquetDataItemDto> allBouquetsFromRequest = new ArrayList<>();
    private final List<PriceItemDto> pricesFirstVariations = new ArrayList<>();

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

        CityResponse cityResponse = mapper.readValue(bodyCity.asString(), CityResponse.class);
        return cityResponse.getData().get(0);
    }

    // Получение обьекта города Новосибирск
    @SneakyThrows
    private CityData getPSCity() {
        RequestSpecification httpRequest = given();
        Response responseCity = httpRequest
                .auth().basic("florist_api", "123")
                .param("p", "Новосибирск")
                .param("mode", "geocity")
                .get("api/city/search");
        ResponseBody bodyCity = responseCity.getBody();

        CityResponse cityResponse = mapper.readValue(bodyCity.asString(), CityResponse.class);
        return cityResponse.getData().get(0);
    }

    // методы для взаимодействия с обьектом города Астрахань
    public String getCityName() {
        return city.getShort_name();
    }

    public String getCityPSName() {
        return cityPS.getShort_name();
    }

    public int getCityId() {
        return city.getGeo().getCity().getId();
    }

    public int getCityPSId() {
        return cityPS.getGeo().getCity().getId();
    }

    public String getCitySlug() {
        return city.getSlug();
    }

    public String getCityPSSlug() {
        return cityPS.getSlug();
    }

    // получение рандомного букета floristRu/Iflorist по ID города
    private PriceItemDto getFirstVariation(Map<String, PriceItemDto> prices) {
        List<PriceItemDto> values = new ArrayList<>(prices.values());
        return values.get(0);
    }

//    @SneakyThrows
//    private void getRandomFloristBouquet() {
//        RequestSpecification httpRequest = given();
//        Response responseBouquet = httpRequest
//                .auth().basic("florist_api", "123")
//                .param("city", getCityId())
//                .param("showPrices", 1)
//                .param("includeIflorist", 1)
//                .get("api/bouquet");
//        ResponseBody bodyBouquet = responseBouquet.getBody();
//
//        BouquetDataDto bouquetData = mapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
//        allBouquetsFromRequest = new ArrayList<>(bouquetData.getData().values());
//        bouquet = getRandomBouquetFloristRu(bouquetData.getData());
//        bouquetList.add(bouquet);
//        pricesFirstVariations.add(getFirstVariation(bouquet.getPrices()));
//    }

    @SneakyThrows
    private void getRandomPSBouquetByCityID(boolean isActive) {
        RequestSpecification httpRequest = given();
        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", getCityPSId())
                .param("showPrices", 1)
                .param("includePS", 1)
                .param("limit", 2000)
                .get("api/bouquet");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        BouquetDataDto bouquetData = mapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
        allBouquetsFromRequest = new ArrayList<>(bouquetData.getData().values());
        bouquet = getRandomBouquetPS(bouquetData.getData(), isActive);
        bouquetList.add(bouquet);
        pricesFirstVariations.add(getFirstVariation(bouquet.getPrices()));
    }

    @SneakyThrows
    private void getRandomFloristRuBouquetByCityID(boolean isAction) {
        RequestSpecification httpRequest = given();
        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", getCityId())
                .param("showPrices", 1)
                .param("includeIflorist", 1)
                .get("api/bouquet");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        BouquetDataDto bouquetData = mapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
        allBouquetsFromRequest = new ArrayList<>(bouquetData.getData().values());
        bouquet = getRandomBouquetFloristRu(bouquetData.getData(), isAction);
        bouquetList.add(bouquet);
    }

    @SneakyThrows
    private void getRandomIFloristBouquetByCityID(boolean isAction) {
        RequestSpecification httpRequest = given();
        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", getCityId())
                .param("showPrices", 1)
                .param("includeIflorist", 1)
                .get("api/bouquet");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        BouquetDataDto bouquetData = mapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
        allBouquetsFromRequest = new ArrayList<>(bouquetData.getData().values());
        bouquet = getRandomBouquetIFlorist(bouquetData.getData(), isAction);
        bouquetList.add(bouquet);
    }

    @SneakyThrows
    private void getRandomPSBouquetByCityID() {
        RequestSpecification httpRequest = given();
        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", getCityPSId())
                .param("showPrices", 1)
                .param("includePS", 1)
                .param("limit", 2000)
                .get("api/bouquet");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        BouquetDataDto bouquetData = mapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
        allBouquetsFromRequest = new ArrayList<>(bouquetData.getData().values());
        bouquet = getRandomBouquetPS(bouquetData.getData());
        bouquetList.add(bouquet);
        pricesFirstVariations.add(getFirstVariation(bouquet.getPrices()));
    }

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

        BouquetDataDto bouquetData = mapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
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

        BouquetDataDto bouquetData = mapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
        allBouquetsFromRequest = new ArrayList<>(bouquetData.getData().values());
        bouquet = getRandomBouquetIFlorist(bouquetData.getData());
        bouquetList.add(bouquet);
    }

    @SneakyThrows
    private void getRandomAllBouquetByCityID(boolean isAction) {
        RequestSpecification httpRequest = given();
        Response responseBouquet = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", getCityId())
                .param("showPrices", 1)
                .param("includeIflorist", 1)
                .get("api/bouquet");
        ResponseBody bodyBouquet = responseBouquet.getBody();

        BouquetDataDto bouquetData = mapper.readValue(bodyBouquet.asString(), BouquetDataDto.class);
        allBouquetsFromRequest = new ArrayList<>(bouquetData.getData().values());
        bouquet = getRandomBouquet(bouquetData.getData(), isAction);
        bouquetList.add(bouquet);
    }

//    public void initBouquet(BouquetType bouquetType) {
//        switch (bouquetType) {
//            case FLORIST_RU, ALL_BOUQUETS -> getRandomFloristBouquet();
//            case PS -> getRandomPSBouquet();
//        }
//    }

    public void initBouquet(BouquetType bouquetType) {
        switch (bouquetType) {
            case FLORIST_RU -> getRandomFloristRuBouquetByCityID();
            case IFLORIST -> getRandomIFloristBouquetByCityID();
            case PS -> getRandomPSBouquetByCityID();
        }
    }
    public void initBouquet(BouquetType bouquetType, boolean isAction) {
        switch (bouquetType) {
            case FLORIST_RU -> getRandomFloristRuBouquetByCityID(isAction);
            case IFLORIST -> getRandomIFloristBouquetByCityID(isAction);
            case PS -> getRandomPSBouquetByCityID(isAction);
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

    public String getBouquetMinPrice(CurrencyType currencyType) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return switch (currencyType) {
            case EUR -> decimalFormat.format(bouquet.getMin_price().getEur()).replace(",", ".");
            case KZT -> String.valueOf(bouquet.getMin_price().getKzt());
            case USD -> decimalFormat.format(bouquet.getMin_price().getUsd()).replace(",", ".");
            case RUB -> String.valueOf(bouquet.getMin_price().getRub());
        };
    }

    public List<String> getBouquetNameList() {
        return bouquetList.stream()
                .map(BouquetDataItemDto::getName).toList();
    }

    public List<String> getBouquetPriceList(CurrencyType currencyType, DeliveryDateType deliveryDateType) {
        return switch (currencyType) {
            case EUR, KZT, USD, RUB -> {
                List<Double> prices = switch (deliveryDateType) {
                    case HiGH_FEBRUARY -> pricesFirstVariations.stream()
                            .map(e -> e.getDatePrice().get("1").getCurrency(currencyType))
                            .collect(Collectors.toList());
                    case HIGH_MARCH -> pricesFirstVariations.stream()
                            .map(e -> e.getDatePrice().get("2").getCurrency(currencyType))
                            .collect(Collectors.toList());
                    case LOW -> bouquetList.stream()
                            .map(e -> e.getMin_price().getCurrency(currencyType))
                            .collect(Collectors.toList());
                };
                yield prices.stream()
                        .map(String::valueOf)
                        .collect(Collectors.toList());
            }
        };
    }

    public String getBouquetPrice(CurrencyType currencyType, DeliveryDateType deliveryDateType) {
        return switch (currencyType) {
            case EUR, KZT, USD, RUB -> {
                String price = null;
                switch (deliveryDateType) {
                    case HiGH_FEBRUARY -> price = String.valueOf(bouquet.getMinDatePrice().get("1").getCurrency(currencyType));
                    case HIGH_MARCH -> price = String.valueOf(bouquet.getMinDatePrice().get("2").getCurrency(currencyType));
                    case LOW -> price = String.valueOf(bouquet.getMin_price().getCurrency(currencyType));
                };
                yield price;
            }
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

        ExtrasDataDto extrasData = mapper.readValue(bodyBouquet.asString(), ExtrasDataDto.class);
        extras = getRandomExtrasFromMap(extrasData.getData());
        extrasList.add(extras);
        extrasPricesFirstVariations.add(extrasPriceItemDto = getFirstExtrasVariation(extras.getPrices()));
    }

    public ExtrasPriceItemDto getExtrasPrice() {
        return extrasPriceItemDto;
    }

    public String getExtrasName() {
        return extras.getName();
    }

    public String getPriceExtrasFirstVariation(CurrencyType currencyType) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return switch (currencyType) {
            case EUR, USD -> decimalFormat.format(extrasPriceItemDto.getPrice().get(currencyType.name())).replace(",", ".");
            case KZT, RUB ->
                    String.valueOf(extrasPriceItemDto.getPrice().get(currencyType.name())).replaceAll("(\\d+)\\.\\d+", "$1");
        };
    }

    public List<String> getExtrasPriceList(CurrencyType currencyType, DeliveryDateType deliveryDateType) {
        return switch (currencyType) {
            case EUR, KZT, USD, RUB -> {
                List<Double> prices = switch (deliveryDateType) {
                    case HiGH_FEBRUARY -> extrasPricesFirstVariations.stream()
                            .map(e -> e.getDatePrice().get("1").getCurrency(currencyType))
                            .collect(Collectors.toList());
                    case HIGH_MARCH -> extrasPricesFirstVariations.stream()
                            .map(e -> e.getDatePrice().get("2").getCurrency(currencyType))
                            .collect(Collectors.toList());
                    case LOW -> extrasPricesFirstVariations.stream()
                            .map(e -> e.getPrice().get(currencyType.name()))
                            .collect(Collectors.toList());
                };
                yield prices.stream()
                        .map(String::valueOf)
                        .collect(Collectors.toList());
            }
        };
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

        CityDataAliasDto cityDataAliasDto = mapper.readValue(responseBody.asString(), CityDataAliasDto.class);
        return cityDataAliasDto.getData();
    }

    // методы взаимодействия с обьектом Data - цен доставки по slug города
    public String getDeliveryPrice(CurrencyType currencyType) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return switch (currencyType) {
            case EUR, USD -> decimalFormat.format(data.getDelivery().get(currencyType.name())).replace(",", ".");
            case KZT, RUB ->
                    String.valueOf(data.getDelivery().get(currencyType.name())).replaceAll("(\\d+)\\.\\d+", "$1");
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

        orderData = mapper.readValue(orderBody.asString(), OrderData.class);
    }

    // методы для взаимодействия с обьектом OrderData в заказе ERP
    public int getOrderId() {
        return orderData.getData().getId();
    }

    public String getOrderTotalPrice(CurrencyType currencyType) {
        double total = switch (currencyType) {
            case EUR -> orderData.getData().getTotal().getEUR();
            case KZT -> orderData.getData().getTotal().getKZT();
            case USD -> orderData.getData().getTotal().getUSD();
            case RUB -> orderData.getData().getTotal().getRUB();
        };
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat roundedTotal = new DecimalFormat("#.##", symbols);
        return roundedTotal.format(total);
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

    public String getRecipientAddress() {
        return orderData.getData().getRecipient_address();
    }

    public String getRecipientPhone() {
        return orderData.getData().getRecipient_phone();
    }

    public String getMaxPaidDate() {
        return orderData.getData().getMax_paid_date();
    }

    public List<String> getBouquetIds() {
        List<String> bouquetIds = new ArrayList<>();
        for (Map.Entry<String, CartItem> entry : orderData.getData().getCart().entrySet()) {
            CartItem cartItem = entry.getValue();
            if (cartItem != null && !cartItem.getType().equals("delivery")) {
                bouquetIds.add(cartItem.getItem_id());
            }
        }
        return bouquetIds;
    }

    public List<String> getBouquetNames() {
        List<String> bouquetNames = new ArrayList<>();
        for (Map.Entry<String, CartItem> entry : orderData.getData().getCart().entrySet()) {
            CartItem cartItem = entry.getValue();
            if (cartItem != null && !cartItem.getType().equals("delivery")) {
                bouquetNames.add(cartItem.getName());
            }
        }
        return bouquetNames;
    }

    public List<String> getBouquetPrices(CurrencyType currencyType) {
        List<String> bouquetPrices = new ArrayList<>();
        for (Map.Entry<String, CartItem> entry : orderData.getData().getCart().entrySet()) {
            CartItem cartItem = entry.getValue();
            if (cartItem != null && !"delivery".equals(cartItem.getType())) {
                switch (currencyType) {
                    case EUR -> bouquetPrices.add(String.valueOf(Math.round(cartItem.getPrice().getEur())));
                    case KZT -> bouquetPrices.add(String.valueOf(cartItem.getPrice().getKzt()));
                    case USD -> bouquetPrices.add(String.valueOf(Math.round(cartItem.getPrice().getUsd())));
                    case RUB -> bouquetPrices.add(String.valueOf(cartItem.getPrice().getRub()));
                }
            }
        }
        return bouquetPrices;
    }

    public String getDeliveryPrices(CurrencyType currencyType) {
        for (Map.Entry<String, CartItem> entry : orderData.getData().getCart().entrySet()) {
            CartItem cartItem = entry.getValue();
            if (cartItem != null && "delivery".equals(cartItem.getType())) {
                return switch (currencyType) {
                    case EUR -> String.valueOf(Math.round(cartItem.getPrice().getEur()));
                    case KZT -> String.valueOf(cartItem.getPrice().getKzt());
                    case USD -> String.valueOf(Math.round(cartItem.getPrice().getUsd()));
                    case RUB -> String.valueOf(cartItem.getPrice().getRub());
                };
            }
        }
        return "Delivery price not found";
    }

    public String getDeliveryDate() {
        return orderData.getData().getDelivery_date();
    }

    // Получение списка недоступных дней для доставки
    @SneakyThrows
    public List<LocalDate> getDisabledDeliveryDaysList() {
        RequestSpecification httpRequest = given();
        Response responseDisabledData = httpRequest
                .auth().basic("florist_api", "123")
                .param("city", getCityId())
                .param("ids", bouquet.getId())
                .get("/api/delivery/date");
        ResponseBody responseBody = responseDisabledData.getBody();

        mapper.registerModule(new JavaTimeModule());
        DisabledDeliveryDateResponse disabledDate = mapper.readValue(responseBody.asString(), DisabledDeliveryDateResponse.class);
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

        DeliveryInfo deliveryInfo = mapper.readValue(bodyBouquet.asString(), DeliveryInfo.class);
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

        try {
            String requestBody = mapper.writeValueAsString(userWrapper);
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
    private BouquetDataItemDto getRandomBouquetPS(Map<String, BouquetDataItemDto> bouquetMap) {
        Map<String, BouquetDataItemDto> filteredMap = bouquetMap.entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith("7"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return getRandomBouquet(filteredMap);
    }
    private BouquetDataItemDto getRandomBouquetPS(Map<String, BouquetDataItemDto> bouquetMap,
                                                  boolean isAction) {
        Map<String, BouquetDataItemDto> filteredMap = bouquetMap.entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith("7"))
                .filter(e -> e.getValue().is_action() == isAction)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return getRandomBouquet(filteredMap);
    }

    private BouquetDataItemDto getRandomBouquetFloristRu(Map<String, BouquetDataItemDto> bouquetMap) {
        Map<String, BouquetDataItemDto> filteredMap = bouquetMap.entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith("6"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return getRandomBouquet(filteredMap);
    }

    private BouquetDataItemDto getRandomBouquetFloristRu(Map<String, BouquetDataItemDto> bouquetMap,
                                                         boolean isAction) {
        Map<String, BouquetDataItemDto> filteredMap = bouquetMap.entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith("6"))
                .filter(e -> e.getValue().is_action() == isAction)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return getRandomBouquet(filteredMap);
    }

    private ExtrasDataItemDto getRandomExtrasFromMap(Map<String, ExtrasDataItemDto> extrasMap) {
        List<ExtrasDataItemDto> values = new ArrayList<>(extrasMap.values());
        return values.get(new Random().nextInt(values.size()));
    }

    private ExtrasPriceItemDto getFirstExtrasVariation(Map<String, ExtrasPriceItemDto> map) {
        ExtrasPriceItemDto extrasPriceItemDto = null;
        for (ExtrasPriceItemDto price : map.values()) {
            if ("Стандартный".equals(price.getName())) {
                extrasPriceItemDto = price;
                break;
            }
        }
        return extrasPriceItemDto;
    }

    private BouquetDataItemDto getRandomBouquetIFlorist(Map<String, BouquetDataItemDto> bouquetMap) {
        Map<String, BouquetDataItemDto> filteredMap = bouquetMap.entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith("333"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return getRandomBouquet(filteredMap);
    }

    private BouquetDataItemDto getRandomBouquetIFlorist(Map<String, BouquetDataItemDto> bouquetMap,
                                                      boolean isAction) {
        Map<String, BouquetDataItemDto> filteredMap = bouquetMap.entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith("333"))
                .filter(e -> e.getValue().is_action() == isAction)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return getRandomBouquet(filteredMap);
    }

    private BouquetDataItemDto getRandomBouquet(Map<String, BouquetDataItemDto> map) {
        List<BouquetDataItemDto> values = new ArrayList<>(map.values());
        return values.get(new Random().nextInt(values.size()));
    }

    private BouquetDataItemDto getRandomBouquet(Map<String, BouquetDataItemDto> map, boolean isAction) {
        List<BouquetDataItemDto> values = map.values().stream()
                .filter(e -> e.is_action() == isAction)
                .toList();
        return values.get(new Random().nextInt(values.size()));
    }
}