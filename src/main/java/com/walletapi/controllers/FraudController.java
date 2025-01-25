package main.java.com.walletapi.controllers;

import com.sun.net.httpserver.HttpServer;
import main.java.com.walletapi.controllers.handlers.fraud.FraudTransactionsHandler;
import main.java.com.walletapi.services.FraudDetectionService;
import main.java.com.walletapi.services.ResourceManager;

public class FraudController {

    private static final FraudDetectionService FRAUD_DETECTION_SERVICE = new FraudDetectionService();

    public static void initializeRoutes(HttpServer server) {
        server.createContext("/fraud/transactions", new FraudTransactionsHandler(ResourceManager.getFraudDetectionService(),
            ResourceManager.getWalletService()));
    }

}
