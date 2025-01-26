package main.java.com.walletapi.server;

import main.java.com.walletapi.controllers.handlers.analytics.TopSpendersHandler;
import main.java.com.walletapi.services.AnalyticsService;
import main.java.com.walletapi.services.ResourceManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class WalletServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        // Initialize the Analytics Handler
        AnalyticsService analyticsService = ResourceManager.getAnalyticsService();
        TopSpendersHandler topSpendersHandler = new TopSpendersHandler(analyticsService);

        // Create a non-blocking ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(PORT));
        serverSocketChannel.configureBlocking(false);

        // Create a Selector to manage channels
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("NIO Server started at http://localhost:" + PORT);

        while (true) {
            // Wait for events
            selector.select();

            // Process events
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                if (key.isAcceptable()) {
                    // Accept a new client connection
                    SocketChannel clientChannel = serverSocketChannel.accept();
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("New connection from: " + clientChannel.getRemoteAddress());
                } else if (key.isReadable()) {
                    // Read data from the client
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int bytesRead = clientChannel.read(buffer);

                    if (bytesRead == -1) {
                        clientChannel.close();
                    } else {
                        buffer.flip();
                        String request = new String(buffer.array(), 0, bytesRead);
                        System.out.println("Request received: \n" + request);

                        // Route the request
                        String response = routeRequest(request, topSpendersHandler);

                        // Send the response
                        ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes());
                        clientChannel.write(responseBuffer);
                        clientChannel.close();
                    }
                }
                keyIterator.remove();
            }
        }
    }

    private static String routeRequest(String request, TopSpendersHandler handler) {
        // Parse the HTTP request
        String[] lines = request.split("\r\n");
        String[] requestLine = lines[0].split(" ");
        String method = requestLine[0];
        String path = requestLine[1];
        Map<String, String> queryParams = parseQueryParams(path);

        // Route to the appropriate handler
        if (path.startsWith("/analytics/top-spenders")) {
            if (!"GET".equals(method)) {
                return "HTTP/1.1 405 Method Not Allowed\r\nContent-Type: text/plain\r\n\r\nMethod Not Allowed.";
            }

            // Extract the limit query parameter
            String adminKey = extractHeader(lines, "Admin-Key");
            if (!isValidAdminKey(adminKey)) {
                return "HTTP/1.1 401 Unauthorized\r\nContent-Type: text/plain\r\n\r\nUnauthorized: Invalid Admin Key.";
            }

            int limit = Integer.parseInt(queryParams.getOrDefault("limit", "5"));
            return "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\n" + handler.handle(limit);
        }

        return "HTTP/1.1 404 Not Found\r\nContent-Type: text/plain\r\n\r\nEndpoint not found.";
    }

    private static Map<String, String> parseQueryParams(String path) {
        Map<String, String> queryParams = new HashMap<>();
        int queryStart = path.indexOf("?");
        if (queryStart != -1) {
            String queryString = path.substring(queryStart + 1);
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }

    private static String extractHeader(String[] lines, String headerName) {
        for (String line : lines) {
            if (line.toLowerCase().startsWith(headerName.toLowerCase() + ":")) {
                return line.split(":")[1].trim();
            }
        }
        return null;
    }

    private static boolean isValidAdminKey(String adminKey) {
        // Replace this with your actual admin key validation logic
        return "1234".equals(adminKey);
    }
}
