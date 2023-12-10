package models.disabledDelivery;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @lombok.Data class Data {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Map<String, LocalDate> disabled_dates;
}
