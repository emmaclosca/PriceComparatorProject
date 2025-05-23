package com.pricecomparator.service;

import com.pricecomparator.repository.DiscountRepository;
import com.pricecomparator.repository.ProductRepository;

/**
 * Simple IoC container serving specific service singletons
 */
public class ServiceContainer {
    private static ProductService productServiceInstance;
    private static DiscountService discountServiceInstance;

    public static ProductService getProductService() {
        if (productServiceInstance == null) {
            productServiceInstance = new ProductService(ProductRepository.getInstance(), DiscountRepository.getInstance());
        }

        return productServiceInstance;
    }

    public static DiscountService getDiscountService() {
        if (discountServiceInstance == null) {
            discountServiceInstance = new DiscountService(DiscountRepository.getInstance());
        }

        return discountServiceInstance;
    }
}
