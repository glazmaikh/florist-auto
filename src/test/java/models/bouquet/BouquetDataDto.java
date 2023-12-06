package models.bouquet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class BouquetDataDto {
    public int ok;
    public String error;
    public Map<String, BouquetDataItemDto> data;

    public List<PriceItemDto> getPricesList() {
        if (data != null && !data.isEmpty()) {
            BouquetDataItemDto firstBouquetDataItem = data.values().iterator().next();
            if (firstBouquetDataItem != null) {
                Map<String, PriceItemDto> prices = firstBouquetDataItem.getPrices();
                if (prices != null && !prices.isEmpty()) {
                    return new ArrayList<>(prices.values());
                }
            }
        }
        return Collections.emptyList();
    }
}
