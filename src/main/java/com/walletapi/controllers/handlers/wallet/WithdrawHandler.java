package main.java.com.walletapi.controllers.handlers.wallet;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.com.walletapi.services.WalletService;
import main.java.com.walletapi.utils.ResponseFormatter;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

public class WithdrawHandler implements HttpHandler {

    private static final int STATUS_OK = 200;
    private static final int STATUS_BAD_REQUEST = 400;
    private static final int STATUS_METHOD_NOT_ALLOWED = 405;

    private final WalletService walletService;

    public WithdrawHandler(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            try {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String[] params = requestBody.split(",");
                String walletId = params[0];
                BigDecimal amount = new BigDecimal(params[1]);

                walletService.withdraw(walletId, amount);
                String response = ResponseFormatter.formatWallet(walletService.getWallet(walletId));
                sendResponse(exchange, STATUS_OK, response);
            } catch (Exception e) {
                sendResponse(exchange, STATUS_BAD_REQUEST, "Error: " + e.getMessage());
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
