package com.pricecomparator.controller;

import com.pricecomparator.service.ProductService;
import com.pricecomparator.service.ServiceContainer;
import com.pricecomparator.utils.PathHelper;
import com.pricecomparator.viewmodel.OptimalShoppingList;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ShoppingBasketController extends BaseController implements HttpHandler {
    private ProductService productService;

    public ShoppingBasketController() {
        super();
        productService = ServiceContainer.getProductService();
    }

    @Override
    public void handle(HttpExchange request) throws IOException {
        System.out.println("Received shopping basket request!");
        try {
            String[] productIds = PathHelper.extractProductIdsFromBasketRequest(request);
            LocalDate date = PathHelper.extractDateFromBasketRequest(request);

            OptimalShoppingList result = productService.splitShoppingBasket(List.of(productIds), date);
            super.handleSuccessfulRequest(request, gson.toJson(result));
        } catch (Exception e) {
            System.out.println(e);
            super.handleFailedRequest(request, e);
        }
    }
}
