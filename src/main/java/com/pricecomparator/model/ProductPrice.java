package com.pricecomparator.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ProductPrice {
    private String productId;
    private float price;
    private String currency;
    private String storeName;
    private LocalDate dateOfPrice;
    private boolean isDiscounted;
    private float discountedPrice;
}
