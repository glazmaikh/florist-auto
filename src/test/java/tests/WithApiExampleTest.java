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
import java.util.Optional;
import java.util.Random;

import static tests.TestData.random;

public class WithApiExampleTest {

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

        DataItemDto dto = getRandomCity(data.getData());
        System.out.println(dto.getName());
        System.out.println(dto.getId());
    }

    public DataItemDto getRandomCity(Map<String, DataItemDto> cityMap) {
        Object[] values = cityMap.values().toArray();
        return (DataItemDto) values[new Random().nextInt(values.length)];
    }
}