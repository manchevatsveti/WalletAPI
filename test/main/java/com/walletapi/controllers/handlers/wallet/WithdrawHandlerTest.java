package main.java.com.walletapi.controllers.handlers.wallet;

import com.sun.net.httpserver.HttpExchange;
import main.java.com.walletapi.models.Wallet;
import main.java.com.walletapi.services.WalletService;
import main.java.com.walletapi.utils.ResponseFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class WithdrawHandlerTest {

    private WalletService walletService;
    private WithdrawHandler withdrawHandler;
    private HttpExchange exchange;

    @BeforeEach
    void setUp() {
        walletService = mock(WalletService.class);
        withdrawHandler = new WithdrawHandler(walletService);
        exchange = mock(HttpExchange.class);
    }

    @Test
    void testHandleValidWithdrawal() throws Exception {
        Wallet mockWallet = new Wallet();
        String walletId = mockWallet.getId();
        BigDecimal amount = BigDecimal.valueOf(500);
        String requestBody = walletId + "," + amount;

        when(walletService.getWallet(walletId)).thenReturn(mockWallet);
        doNothing().when(walletService).withdraw(walletId, amount);

        String expectedResponse = String.format(
            "{\"id\":\"%s\",\"balance\":%s}", walletId, mockWallet.getBalance()
        );

        mockStatic(ResponseFormatter.class);
        when(ResponseFormatter.formatWallet(mockWallet)).thenReturn(expectedResponse);

        setUpHttpExchangeMock(requestBody);

        ByteArrayOutputStream responseStream = setResponseStreamMock();

        withdrawHandler.handle(exchange);

        verify(exchange).sendResponseHeaders(200, expectedResponse.getBytes().length);
        assertEquals(expectedResponse, responseStream.toString());
    }

    private void setUpHttpExchangeMock(String body) {
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestBody()).thenReturn(new ByteArrayInputStream(body.getBytes()));
    }

    private ByteArrayOutputStream setResponseStreamMock() {
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        when(exchange.getResponseBody()).thenReturn(responseStream);
        return responseStream;
    }
}
