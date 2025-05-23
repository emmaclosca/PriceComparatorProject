package com.pricecomparator;

import com.pricecomparator.controller.DiscountsController;
import com.pricecomparator.controller.ShoppingBasketController;
import com.pricecomparator.utils.ServerConfig;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class Server {
    private static HttpServer server;
    private static ThreadPoolExecutor threadPoolExecutor;

    public static void main(String[] args) throws IOException {
       init();
       runServer();
    }

    private static void init() throws IOException {
        server = HttpServer
                .create(new InetSocketAddress(ServerConfig.SERVER_URL, ServerConfig.PORT), ServerConfig.BACKLOG_QUEUE_SIZE);
        // map URL to controller logic
        server.createContext(ServerConfig.OPTIMAL_BASKET_PATH, new ShoppingBasketController());
        server.createContext(ServerConfig.BEST_DISCOUNTS_PATH, new DiscountsController());
        // handle parallel requests
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(ServerConfig.THREADS_NUMBER);
        server.setExecutor(threadPoolExecutor);
    }

    private static void runServer() {
        server.start();
        System.out.println("Server is running on port " + ServerConfig.PORT);
    }
}