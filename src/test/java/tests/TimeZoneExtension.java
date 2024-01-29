package tests;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.TimeZone;

public class TimeZoneExtension implements BeforeTestExecutionCallback {
    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Samara"));
    }

    @RegisterExtension
    static TimeZoneExtension timeZoneExtension = new TimeZoneExtension();
}