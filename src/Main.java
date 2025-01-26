import com.sun.net.httpserver.HttpServer;
import main.java.com.walletapi.controllers.AnalyticsController;
import main.java.com.walletapi.controllers.FraudController;
import main.java.com.walletapi.controllers.WalletController;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        // Create the HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Initialize routes for Wallet, Fraud Detection, and Analytics
        WalletController.initializeRoutes(server);
        FraudController.initializeRoutes(server);
        AnalyticsController.initializeRoutes(server);

        // Set the executor and start the server
        server.setExecutor(null); // Creates a default executor
        System.out.println("Server started at http://localhost:" + PORT);
        server.start();
    }
}
