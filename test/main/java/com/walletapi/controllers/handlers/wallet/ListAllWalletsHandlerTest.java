package main.java.com.walletapi.controllers.handlers.wallet;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import main.java.com.walletapi.models.Wallet;
import main.java.com.walletapi.services.WalletService;
import main.java.com.walletapi.utils.AdminAuthValidator;
import main.java.com.walletapi.utils.ResponseFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ListAllWalletsHandlerTest {

    private WalletService walletService;
    private ListAllWalletsHandler listAllWalletsHandler;
    private HttpExchange exchange;

    @BeforeEach
    void setUp() {
        walletService = mock(WalletService.class);
        listAllWalletsHandler = new ListAllWalletsHandler(walletService);
        exchange = mock(HttpExchange.class);
    }

    @Test
    void testHandleValidAdminKey() throws Exception {
        ConcurrentHashMap<String, Wallet> mockWallets = new ConcurrentHashMap<>();
        Wallet wallet1 = new Wallet();
        Wallet wallet2 = new Wallet();
        mockWallets.put(wallet1.getId(), wallet1);
        mockWallets.put(wallet2.getId(), wallet2);

        String adminKey = "valid-key";

        Headers headers = mock(Headers.class);
        when(exchange.getRequestHeaders()).thenReturn(headers);
        when(headers.getFirst("Admin-Key")).thenReturn(adminKey);

        mockStatic(AdminAuthValidator.class);
        when(AdminAuthValidator.isValidAdminKey(adminKey)).thenReturn(true);
        when(walletService.getWallets()).thenReturn(mockWallets);

        String expectedResponse = "[{\"id\":\"0\",\"balance\":0,\"transactions\":[]},{\"id\":\"1\",\"balance\":0,\"transactions\":[]}]";
        mockStatic(ResponseFormatter.class);
        when(ResponseFormatter.formatWallets(mockWallets)).thenReturn(expectedResponse);

        when(exchange.getRequestMethod()).thenReturn("GET");
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        when(exchange.getResponseBody()).thenReturn(responseStream);

        listAllWalletsHandler.handle(exchange);

        verify(exchange).sendResponseHeaders(200, expectedResponse.getBytes().length);
        assertEquals(expectedResponse, responseStream.toString());
    }
}
