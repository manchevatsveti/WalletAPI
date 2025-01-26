package main.java.com.walletapi.controllers;

import com.sun.net.httpserver.HttpServer;
import main.java.com.walletapi.controllers.handlers.wallet.CreateWalletHandler;
import main.java.com.walletapi.controllers.handlers.wallet.DepositHandler;
import main.java.com.walletapi.controllers.handlers.wallet.ListAllWalletsHandler;
import main.java.com.walletapi.controllers.handlers.wallet.WithdrawHandler;
import main.java.com.walletapi.services.ResourceManager;

public class WalletController {

    public static void initializeRoutes(HttpServer server) {
        server.createContext("/wallets/create", new CreateWalletHandler(ResourceManager.getWalletService()));
        server.createContext("/all-wallets", new ListAllWalletsHandler(ResourceManager.getWalletService()));
        server.createContext("/wallets/deposit", new DepositHandler(ResourceManager.getWalletService()));
        server.createContext("/wallets/withdraw", new WithdrawHandler(ResourceManager.getWalletService()));
    }
}


