package com.pricecomparator.utils;

import com.sun.net.httpserver.HttpExchange;

import java.time.LocalDate;

public class PathHelper {
    public static String[] extractProductIdsFromBasketRequest(HttpExchange request) {
        return request
                .getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("&")[0]
                .split("=")[1]
                .split(",");
    }

    public static LocalDate extractDateFromBasketRequest(HttpExchange request) {
        String date =  request
                .getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("&")[1]
                .split("=")[1];
        return DateHelper.getDateFromString(date);
    }
}
