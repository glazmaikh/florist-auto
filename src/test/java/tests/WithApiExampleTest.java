package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import models.DataDto;
import models.DataItemDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class WithApiExampleTest {

    //public Root root;
    public String name;
    public int id;

    @BeforeEach
    public void setUp() {
        // в BeforeEach делать запрос "Список всех городов" и сохранять id и name
        // отправлять запрос в /bouquet, указывать в параметре id города
        // результат сохранять в массив их имен
        // искать буект по имени и кликать на него
//        root = given()
//                .auth().basic("florist_api", "123")
////                .formParam("iflorist", 0)
////                .formParam("includeMeta", 0)
//                .when()
//                .get("https://www.test.florist.local/api/city/top")
//                .then()
//                .statusCode(200)
//                .extract().as(Root.class);

        //Document doc = Jsoup.parse(client.successRedirect(user).asString());
        //            Element successText = doc.select("div:containsOwn(Your registration completed)")
        //                    .first();

        //List<UserDataModel> dataModels = response.getData();
        //List<String> firstNames = dataModels.stream().map(UserDataModel::getFirstName).collect(Collectors.toList());
    }

    @Test
    @SneakyThrows
    void test1() {
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest
                .auth().basic("florist_api", "123")
                .get("https://www.test.florist.local/api/city");
        ResponseBody body = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        DataDto data = objectMapper.readValue(body.asString(), DataDto.class);
        System.out.println(data.getData().get("724").getName());

        Map<String, DataItemDto> cityMap = data.getData();
        for (Map.Entry<String, DataItemDto> entry : cityMap.entrySet())
            System.out.println("Key = " + entry.getKey() +
                    ", value = " + entry.getValue());
    }
}