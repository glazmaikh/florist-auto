package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        root = given()
                .auth().basic("florist_api", "123")
//                .formParam("iflorist", 0)
//                .formParam("includeMeta", 0)
                .when()
                .get("https://www.test.florist.local/api/city/top")
                .then()
                .statusCode(200)
                .extract().as(Root.class);
    }

    @Test
    void test() {
        System.out.println(name + " name");
        System.out.println(id + " id");
    }
}