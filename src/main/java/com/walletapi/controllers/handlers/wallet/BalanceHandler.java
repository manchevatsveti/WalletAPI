package main.java.com.walletapi.controllers.handlers.wallet;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.com.walletapi.services.WalletService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BalanceHandler implements HttpHandler {

    private static final int STATUS_OK = 200;
    private static final int STATUS_BAD_REQUEST = 400;
    private static final int STATUS_NOT_FOUND = 404;
    private static final int STATUS_METHOD_NOT_ALLOWED = 405;

    private final WalletService walletService;

    public BalanceHandler(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String walletId = exchange.getRequestHeaders().getFirst("Wallet-Id");

            if (walletId == null || walletId.isBlank()) {
                sendResponse(exchange, STATUS_BAD_REQUEST, "Bad Request: Wallet-Id is required.");
                return;
            }

            try {
                String balance = walletService.getBalance(walletId).toString();
                sendResponse(exchange, STATUS_OK, "Wallet balance: " + balance);
            } catch (IllegalArgumentException e) {
                sendResponse(exchange, STATUS_NOT_FOUND, "Wallet not found.");
            }
        } else {
            sendResponse(exchange, STATUS_METHOD_NOT_ALLOWED, "Method Not Allowed.");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}
