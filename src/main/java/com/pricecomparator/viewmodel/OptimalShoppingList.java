package com.pricecomparator.viewmodel;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class OptimalShoppingList {
    private Map<String, List<ProductView>> storeProducts;
    private String date; // because Gson has issues serializing Date
    private float totalPrice;
    private String currency;
}
