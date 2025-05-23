package com.pricecomparator.viewmodel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductView {
    private String name;
    private float price;
    private String currency;
}
