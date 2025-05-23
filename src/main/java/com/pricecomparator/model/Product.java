package com.pricecomparator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private String id;
    private String name;
    private String category;
    private String brand;
    private float quantity;
    private String unit;
}
