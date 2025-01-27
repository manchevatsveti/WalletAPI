package main.java.com.walletapi.controllers;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import main.java.com.walletapi.controllers.handlers.fraud.FraudTransactionsHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FraudControllerTest {

    @Mock
    private HttpServer mockServer;

    @Mock
    private HttpContext mockContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInitializeRoutes() {
        when(mockServer.createContext(eq("/fraud/transactions"), any(FraudTransactionsHandler.class)))
            .thenReturn(mockContext);

        FraudController.initializeRoutes(mockServer);

        verify(mockServer, times(1)).createContext(eq("/fraud/transactions"), any(FraudTransactionsHandler.class));
    }
}
