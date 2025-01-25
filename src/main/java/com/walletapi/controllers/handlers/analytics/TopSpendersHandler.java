package main.java.com.walletapi.controllers.handlers.analytics;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.java.com.walletapi.services.AnalyticsService;
import main.java.com.walletapi.utils.ResponseFormatter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TopSpendersHandler implements HttpHandler {

    private static final int DEFAULT_LIMIT = 5;
    private static final int STATUS_OK = 200;
    private static final int STATUS_BAD_REQUEST = 400;
    private static final int STATUS_METHOD_NOT_ALLOWED = 405;

    private final AnalyticsService analyticsService;

    public TopSpendersHandler(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            try {
                String query = exchange.getRequestURI().getQuery();
                int limit = query != null && query.contains("limit=")
                    ? Integer.parseInt(query.split("=")[1])
                    : DEFAULT_LIMIT;

                System.out.println("eho2");
                String response = ResponseFormatter.formatTopSpenders(analyticsService.getTopSpenders(limit));
                sendResponse(exchange, STATUS_OK, response);
            } catch (NumberFormatException e) {
                sendResponse(exchange, STATUS_BAD_REQUEST, "Invalid 'limit' parameter.");
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
