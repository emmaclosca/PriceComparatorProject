package com.pricecomparator.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Discount {
    private String productId;
    private String storeName;
    private int percentage;
    private LocalDate fromDate;
    private LocalDate toDate;
}
