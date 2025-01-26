package main.java.com.walletapi.controllers.handlers.wallet;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.com.walletapi.services.WalletService;
import main.java.com.walletapi.utils.AdminAuthValidator;
import main.java.com.walletapi.utils.ResponseFormatter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ListAllWalletsHandler implements HttpHandler {
    private static final int STATUS_OK = 200;
    private static final int STATUS_UNAUTHORIZED = 401;
    private static final int STATUS_METHOD_NOT_ALLOWED = 405;

    private final WalletService walletService;

    public ListAllWalletsHandler(WalletService walletService) {
        this.walletService = walletService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String adminKey = exchange.getRequestHeaders().getFirst("Admin-Key");

            if (!AdminAuthValidator.isValidAdminKey(adminKey)) {
                sendResponse(exchange, STATUS_UNAUTHORIZED,
                    "Unauthorized: Invalid Admin Key. Add this to your command for access: " +
                        "-H \"Admin-Key: xxx\", where xxx is the admin key");
                return;
            }

            String response = ResponseFormatter.formatWallets(walletService.getWallets());
            sendResponse(exchange, STATUS_OK, response);
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
