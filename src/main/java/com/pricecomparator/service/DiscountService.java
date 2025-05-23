package com.pricecomparator.service;

import com.pricecomparator.model.Discount;
import com.pricecomparator.repository.DiscountRepository;
import com.pricecomparator.repository.ProductRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DiscountService {
    private DiscountRepository discountRepository;

    // allow only access via IoC service container
    protected DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    // Method to get top current discounts by percentage
    public List<Discount> getTopCurrentDiscounts(int limit) {
        LocalDate today = LocalDate.now();

        return discountRepository.getAllDiscounts().stream()
                .filter(discount ->
                        !today.isBefore(discount.getFromDate()) &&
                                !today.isAfter(discount.getToDate()))
                .sorted(Comparator.comparing(Discount::getPercentage).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
