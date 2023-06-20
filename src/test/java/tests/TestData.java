package tests;

import com.github.javafaker.Faker;

import java.util.*;

public class TestData {
    Faker faker = new Faker(new Locale("ru"));
    String yourName = faker.name().fullName();
    String yourEmail = faker.internet().emailAddress("en");
    String yourPhone = faker.phoneNumber().cellPhone();
    String name = faker.name().firstName();
    String phone = faker.phoneNumber().cellPhone();
    String address = faker.address().streetAddress();

    public String getYourName() {
        return yourName;
    }

    public String getYourEmail() {
        return yourEmail;
    }

    public String getYourPhone() {
        return yourPhone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

}