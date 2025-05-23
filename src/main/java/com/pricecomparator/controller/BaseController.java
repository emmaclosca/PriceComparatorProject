package com.pricecomparator.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

abstract class BaseController {
    private final int SUCCESS_CODE = 200;
    private final int FAIL_CODE = 400;
    protected Gson gson;

    BaseController() {
        gson = new Gson();
    }

    protected void handleSuccessfulRequest(HttpExchange request, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        request.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        request.sendResponseHeaders(SUCCESS_CODE, responseBytes.length);
        try (OutputStream os = request.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    protected void handleFailedRequest(HttpExchange request, Exception e) throws IOException {
        String errorMessage = e.getMessage();
        byte[] errorBytes = errorMessage.getBytes(StandardCharsets.UTF_8);
        request.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        request.sendResponseHeaders(FAIL_CODE, errorBytes.length);
        try (OutputStream os = request.getResponseBody()) {
            os.write(errorBytes);
        }
    }
}
