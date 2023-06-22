package tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class SystemPropertiesTests {

    @Test
    void test() {
        String browserName = System.getProperty("browser");
        System.out.println(browserName); // null
    }

    @Test
    void test1() {
        System.setProperty("browser", "opera");
        String browserName = System.getProperty("browser");
        System.out.println(browserName); // opera
    }

    @Test
    void test2() {
        String browserName = System.getProperty("browser", "firefox");
        System.out.println(browserName); // firefox
    }

    @Test
    void test3() {
        System.setProperty("browser", "opera");
        String browserName = System.getProperty("browser", "firefox");
        System.out.println(browserName); // opera
    }

    @Test
    @Tag("one_property")
    void test4() {
        String browserName = System.getProperty("browser", "firefox");
        System.out.println(browserName);

        // gradle clean one_property_test - firefox
        // clean one_property_test

        // gradle clean one_property_test -Dbrowser=safari - safari
    }

    @Test
    @Tag(("hello"))
    void test5() {
        System.out.println("Hello, " + System.getProperty("user_name", "unknown student"));

        /*
        gradle clean hello_test - unknown student

        gradle clean hello_test -Duser_name=Alex - Alex


         */
    }
}
