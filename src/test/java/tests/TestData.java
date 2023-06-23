package tests;

import com.github.javafaker.Faker;

import java.util.*;

public class TestData {
    Faker faker = new Faker(new Locale("ru"));

    public String getLastName() {
        return faker.name().lastName();
    }

    public String getPassword() {
        return faker.internet().password(6, 20);
    }

    public String getYourFullName() {
        return faker.name().fullName();
    }

    public String getYourEmail() {
        return faker.internet().emailAddress("en");
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