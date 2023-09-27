package tests;

import com.github.javafaker.Faker;

import java.util.*;

public class TestData {
    Faker faker = new Faker(new Locale("ru"));

    public String getPassword() {
        return faker.internet().password(6, 20);
    }

    public String getYourFullName() {
        return faker.name().firstName();
    }

    public String getYourEmail() {
        String uniqueId = UUID.randomUUID().toString().replace("-","");
        String domain = "test.com";
        return uniqueId + "@" + domain;
    }

    public String getYourPhone() {
        return "+7" + faker.phoneNumber().cellPhone().replaceAll("[\\s\\p{Punct}]","");
    }

    public String getRecipientName() {
        return faker.name().fullName();
    }

    public String getPhone() {
        return faker.numerify("##########");
    }

    public String getAddress() {
        return faker.address().streetAddress();
    }
}