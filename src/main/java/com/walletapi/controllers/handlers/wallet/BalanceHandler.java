package main.java.com.walletapi.controllers.handlers.wallet;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.com.walletapi.services.WalletService;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BalanceHandler implements HttpHandler {

    private static final int STATUS_OK = 200;
    private static final int STATUS_BAD_REQUEST = 400;
    private static final int STATUS_NOT_FOUND = 404;
    private static final int STATUS_METHOD_NOT_ALLOWED = 405;

    private static final Pattern PATH_PATTERN = Pattern.compile("^/wallets/balance/([a-zA-Z0-9]+)$");

    private final WalletService walletService;

    public BalanceHandler(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String path = exchange.getRequestURI().getPath();
            Matcher matcher = PATH_PATTERN.matcher(path);

            if (matcher.matches()) {
                String walletId = matcher.group(1);

                try {
                    String balance = walletService.getBalance(walletId).toString();
                    sendJsonResponse(exchange, STATUS_OK,
                        "{\"walletId\":\"" + walletId + "\",\"balance\":\"" + balance + "\"}");
                } catch (IllegalArgumentException e) {
                    sendJsonResponse(exchange, STATUS_NOT_FOUND, "{\"error\":\"Wallet not found\"}");
                }
            } else {
                sendJsonResponse(exchange, STATUS_BAD_REQUEST, "{\"error\":\"Invalid wallet ID format\"}");
            }
        } else {
            sendJsonResponse(exchange, STATUS_METHOD_NOT_ALLOWED, "{\"error\":\"Method Not Allowed\"}");
        }
    }

    private void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, jsonResponse.getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes(StandardCharsets.UTF_8));
        }
    }
}
