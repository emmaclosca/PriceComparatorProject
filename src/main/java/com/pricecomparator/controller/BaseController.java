package com.pricecomparator.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

abstract class BaseController {
    private final int SUCCESS_CODE = 200;
    private final int FAIL_CODE = 400;
    protected Gson gson;

    BaseController() {
        gson = new Gson();
    }

    protected void handleSuccessfulRequest(HttpExchange request, String response) throws IOException {
        request.sendResponseHeaders(SUCCESS_CODE, response.length());
        OutputStream os = request.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    protected void handleFailedRequest(HttpExchange request, Exception e) throws IOException {
        request.sendResponseHeaders(FAIL_CODE, e.getMessage().length());
        OutputStream os = request.getResponseBody();
        os.write(e.getMessage().getBytes());
        os.close();
    }
}
