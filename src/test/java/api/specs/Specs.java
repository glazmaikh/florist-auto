package api.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.RestAssured.baseURI;

import static io.restassured.RestAssured.with;

public class Specs {
    String testEnv = System.getProperty("TEST_ENV");
    String propertiesFilePath = "src/test/resources/" + testEnv + ".properties";

    public static RequestSpecification request = with()
            .baseUri(baseURI)
            .log().all()
            .contentType("application/x-www-form-urlencoded; charset=UTF-8");

    public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();
}