package com.pricecomparator.controller;

import com.pricecomparator.service.DiscountService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DiscountsController extends BaseController implements HttpHandler {
    private DiscountService discountService;
    private Gson gson;

    public DiscountsController() {
        super();
        discountService = DiscountService.getDiscountService();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class,
                        (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                                context.serialize(src.format(formatter)))
                .registerTypeAdapter(LocalDate.class,
                        (JsonDeserializer<LocalDate>) (json, type, context) ->
                                LocalDate.parse(json.getAsString(), formatter))
                .create();
    }

    @Override
    public void handle(HttpExchange request) throws IOException {
        System.out.println("Received best discounts request!");
        try {
            int limit = 10;
            var bestDiscounts = discountService.getBestCurrentDiscounts(limit);
            super.handleSuccessfulRequest(request, gson.toJson(bestDiscounts));
        } catch (Exception e) {
            System.out.println(e);
            super.handleFailedRequest(request, e);
        }
    }
}
