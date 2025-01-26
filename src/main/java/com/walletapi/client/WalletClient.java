package main.java.com.walletapi.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class WalletClient {
    public static void main(String[] args) throws IOException {
        // Connect to the server
        InetSocketAddress serverAddress = new InetSocketAddress("localhost", 8080);
        SocketChannel clientChannel = SocketChannel.open(serverAddress);
        System.out.println("Connected to the server...");

        // Send a sample request
        String request = "GET /analytics/top-spenders HTTP/1.1\r\nHost: localhost\r\n\r\n";
        ByteBuffer buffer = ByteBuffer.wrap(request.getBytes());
        clientChannel.write(buffer);
        System.out.println("Request sent: \n" + request);

        // Read the response
        ByteBuffer responseBuffer = ByteBuffer.allocate(1024);
        clientChannel.read(responseBuffer);
        responseBuffer.flip();
        String response = new String(responseBuffer.array(), 0, responseBuffer.limit());
        System.out.println("Response received: \n" + response);

        clientChannel.close();
    }
}
