package main.java.com.walletapi.controllers.handlers.wallet;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import main.java.com.walletapi.services.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BalanceHandlerTest {

    private WalletService walletService;
    private BalanceHandler balanceHandler;
    private HttpExchange exchange;

    @BeforeEach
    void setUp() {
        walletService = mock(WalletService.class);
        balanceHandler = new BalanceHandler(walletService);
        exchange = mock(HttpExchange.class);
    }

    @Test
    void testHandleValidWalletId() throws Exception {
        String walletId = "123";
        String expectedResponse = "{\"walletId\":\"123\",\"balance\":\"1000.0\"}";
        when(walletService.getBalance(walletId)).thenReturn(BigDecimal.valueOf(1000.0));

        setUpHttpExchangeMock("GET", "/wallets/balance/" + walletId);

        ByteArrayOutputStream responseStream = setResponseStreamMock();

        balanceHandler.handle(exchange);

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(exchange).sendResponseHeaders(eq(200), captor.capture());

        assertEquals(expectedResponse, responseStream.toString());
    }

    @Test
    void testHandleInvalidWalletIdFormat() throws Exception {
        String invalidPath = "/wallets/balance/invalid_id!";
        String expectedResponse = "{\"error\":\"Invalid wallet ID format\"}";

        setUpHttpExchangeMock("GET", invalidPath);

        ByteArrayOutputStream responseStream = setResponseStreamMock();
        balanceHandler.handle(exchange);

        verify(exchange).sendResponseHeaders(400, expectedResponse.getBytes().length);
        assertEquals(expectedResponse, responseStream.toString());
    }

    @Test
    void testHandleWalletNotFound() throws Exception {
        String walletId = "nonexistentWallet";
        String expectedResponse = "{\"error\":\"Wallet not found\"}";
        when(walletService.getBalance(walletId)).thenThrow(new IllegalArgumentException());

        setUpHttpExchangeMock("GET", "/wallets/balance/" + walletId);

        ByteArrayOutputStream responseStream = setResponseStreamMock();
        balanceHandler.handle(exchange);

        verify(exchange).sendResponseHeaders(404, expectedResponse.getBytes().length);
        assertEquals(expectedResponse, responseStream.toString());
    }

    @Test
    void testHandleMethodNotAllowed() throws Exception {
        String walletId = "123";
        String expectedResponse = "{\"error\":\"Method Not Allowed\"}";

        setUpHttpExchangeMock("POST", "/wallets/balance/" + walletId);

        ByteArrayOutputStream responseStream = setResponseStreamMock();
        balanceHandler.handle(exchange);

        verify(exchange).sendResponseHeaders(405, expectedResponse.getBytes().length);
        assertEquals(expectedResponse, responseStream.toString());
    }

    private void setUpHttpExchangeMock(String method, String path) {
        when(exchange.getRequestMethod()).thenReturn(method);
        when(exchange.getRequestURI()).thenReturn(URI.create(path));
    }

    private ByteArrayOutputStream setResponseStreamMock() {
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        Headers headers = new Headers();
        when(exchange.getResponseHeaders()).thenReturn(headers);

        when(exchange.getResponseBody()).thenReturn(responseStream);
        return responseStream;
    }
}
