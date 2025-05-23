package com.pricecomparator.service;

import com.pricecomparator.model.Discount;
import com.pricecomparator.model.Product;
import com.pricecomparator.model.ProductPrice;
import com.pricecomparator.repository.DiscountRepository;
import com.pricecomparator.repository.ProductRepository;
import com.pricecomparator.viewmodel.OptimalShoppingList;
import com.pricecomparator.viewmodel.ProductView;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ProductService {
    private ProductRepository productRepository;
    private DiscountRepository discountRepository;

    // allow only access via IoC service container
    protected ProductService(ProductRepository productRepository, DiscountRepository discountRepository) {
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }

    /**
     * Given a list of product ids that need to be purchased in a certain date, the function
     * maps each product to a shop(lidl, profi, etc) for which the product is the cheapest in that
     * date
     * @param productIds: list of product ids in the basket
     * @param date: target date
     */
    public OptimalShoppingList splitShoppingBasket(List<String> productIds, LocalDate date) {
        List<ProductPrice> productPrices = new ArrayList<>(productRepository.filterProductPricesByIdsAndDate(productIds, date)); // just copy do not modify the DB refernces
        List<Discount> discounts = discountRepository.getDiscountsForProductsOnDate(productIds, date);
        // apply discounts to prices
        for (Discount discount : discounts) {
            for (ProductPrice prod : productPrices) {
                if (prod.getProductId().equals(discount.getProductId()) && prod.getStoreName().equals(discount.getStoreName())) {
                    prod.setDiscounted(true);
                    prod.setDiscountedPrice(getDiscountPrice(prod.getPrice(), discount.getPercentage()));
                }
            }
        }
        // figure out which price is the cheapest for each product
        Set<ProductPrice> cheapestProductPrices = new HashSet<>();
        for (String id : productIds) {
            ProductPrice cheapestPrice = productPrices.stream()
                    .filter(productPrice -> productPrice.getProductId().equals(id))
                    .sorted(
                            (productA, productB) -> Float.compare(
                                    productA.isDiscounted() ? productA.getDiscountedPrice() : productA.getPrice(),
                                    productB.isDiscounted() ? productB.getDiscountedPrice() : productB.getPrice()))
                    .collect(Collectors.toList())
                    .getFirst();
            cheapestProductPrices.add(cheapestPrice);
        }

        Map<String, List<ProductView>> storeProducts = new HashMap<>();
        float totalPrice = 0;
        for (ProductPrice cheapPrice : cheapestProductPrices) {
            Product product = productRepository.getProductById(cheapPrice.getProductId());
            String storeName = cheapPrice.getStoreName();

            ProductView productView = ProductView.builder()
                    .price(cheapPrice.isDiscounted() ? cheapPrice.getDiscountedPrice() : cheapPrice.getPrice())
                    .currency(cheapPrice.getCurrency())
                    .name(product.getName())
                    .build();
            if (!storeProducts.containsKey(storeName)) {
                storeProducts.put(storeName, new ArrayList<>());
            }

            storeProducts.get(storeName).add(productView);

            totalPrice += (cheapPrice.isDiscounted() ? cheapPrice.getDiscountedPrice() : cheapPrice.getPrice());
        }

        return OptimalShoppingList.builder()
                .storeProducts(storeProducts)
                .currency("RON")
                .totalPrice(totalPrice)
                .date(date.toString())
                .build();
    }

    private float getDiscountPrice(float price, int discount) {
        return price * Float.valueOf(100 - discount) * Float.valueOf(0.01f);
    }
}
