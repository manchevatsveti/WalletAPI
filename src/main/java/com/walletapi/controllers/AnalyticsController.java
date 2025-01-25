package main.java.com.walletapi.controllers;

import com.sun.net.httpserver.HttpServer;
import main.java.com.walletapi.controllers.handlers.analytics.TopSpendersHandler;
import main.java.com.walletapi.services.ResourceManager;

public class AnalyticsController {

    public static void initializeRoutes(HttpServer server) {
        server.createContext("/analytics/top-spenders", new TopSpendersHandler(ResourceManager.getAnalyticsService()));
    }
}
