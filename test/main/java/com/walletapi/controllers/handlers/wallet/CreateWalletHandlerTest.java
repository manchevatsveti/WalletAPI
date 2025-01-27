package main.java.com.walletapi.controllers.handlers.wallet;


import com.sun.net.httpserver.HttpExchange;
import main.java.com.walletapi.models.Wallet;
import main.java.com.walletapi.services.WalletService;
import main.java.com.walletapi.utils.ResponseFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateWalletHandlerTest {

    private WalletService walletService;
    private CreateWalletHandler createWalletHandler;
    private HttpExchange exchange;

    @BeforeEach
    void setUp() {
        walletService = mock(WalletService.class);
        createWalletHandler = new CreateWalletHandler(walletService);
        exchange = mock(HttpExchange.class);
    }

    @Test
    void testHandlePostRequest() throws Exception {
        Wallet mockWallet = new Wallet();
        when(walletService.createWallet()).thenReturn(mockWallet);

        String expectedResponse = String.format(
            "{\"id\":\"%s\",\"balance\":%s}",
            mockWallet.getId(),
            mockWallet.getBalance()
        );
        mockStatic(ResponseFormatter.class);
        when(ResponseFormatter.formatWallet(mockWallet)).thenReturn(expectedResponse);

        setUpHttpExchangeMock("POST");

        ByteArrayOutputStream responseStream = setResponseStreamMock();

        createWalletHandler.handle(exchange);

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(exchange).sendResponseHeaders(eq(200), captor.capture());
        assertEquals(expectedResponse.getBytes().length, captor.getValue());

        assertEquals(expectedResponse, responseStream.toString());
    }

    @Test
    void testHandleMethodNotAllowed() throws Exception {
        setUpHttpExchangeMock("GET");

        ByteArrayOutputStream responseStream = setResponseStreamMock();

        createWalletHandler.handle(exchange);

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(exchange).sendResponseHeaders(eq(405), captor.capture());
        assertEquals("Method Not Allowed.".getBytes().length, captor.getValue());

        assertEquals("Method Not Allowed.", responseStream.toString());
    }

    private void setUpHttpExchangeMock(String method) {
        when(exchange.getRequestMethod()).thenReturn(method);
    }

    private ByteArrayOutputStream setResponseStreamMock() {
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        when(exchange.getResponseBody()).thenReturn(responseStream);
        return responseStream;
    }
}
