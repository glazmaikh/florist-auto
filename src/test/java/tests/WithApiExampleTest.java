package tests;

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

import java.util.Map;

public class WithApiExampleTest {

    @BeforeEach
    public void setUp() {

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
        System.out.println(data.getData().get("1963").getName());

        //перебирать мапу стримом
        Map<String, DataItemDto> cityMap = data.getData();
        for (Map.Entry<String, DataItemDto> entry : cityMap.entrySet())
            System.out.println("Key = " + entry.getKey() +
                    ", value = " + entry.getValue());
    }
}