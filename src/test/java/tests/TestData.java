package tests;

import com.github.javafaker.Faker;

import java.util.*;

public class TestData {
    Faker faker = new Faker(new Locale("ru"));

    public String getPassword() {
        return faker.internet().password(6, 20);
    }

    public String getYourFullName() {
        return faker.name().fullName();
    }

    public String getYourEmail() {
        String uniqueId = UUID.randomUUID().toString().replace("-","");
        String domain = "test.com";
        return uniqueId + "@" + domain;
    }

    public String getYourPhone() {
        return faker.phoneNumber().cellPhone().replaceAll("\\p{Punct}","");
    }

    public String getFirstName() {
        return faker.name().firstName();
    }

    public String getPhone() {
        return faker.phoneNumber().cellPhone();
    }

    public String getAddress() {
        return faker.address().streetAddress();
    }
}