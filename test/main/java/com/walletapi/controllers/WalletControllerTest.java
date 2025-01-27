package main.java.com.walletapi.controllers;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import main.java.com.walletapi.controllers.handlers.wallet.CreateWalletHandler;
import main.java.com.walletapi.controllers.handlers.wallet.DepositHandler;
import main.java.com.walletapi.controllers.handlers.wallet.ListAllWalletsHandler;
import main.java.com.walletapi.controllers.handlers.wallet.WithdrawHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WalletControllerTest {

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
        when(mockServer.createContext(eq("/wallets/create"), any(CreateWalletHandler.class)))
            .thenReturn(mockContext);
        when(mockServer.createContext(eq("/all-wallets"), any(ListAllWalletsHandler.class)))
            .thenReturn(mockContext);
        when(mockServer.createContext(eq("/wallets/deposit"), any(DepositHandler.class)))
            .thenReturn(mockContext);
        when(mockServer.createContext(eq("/wallets/withdraw"), any(WithdrawHandler.class)))
            .thenReturn(mockContext);

        WalletController.initializeRoutes(mockServer);

        verify(mockServer, times(1)).createContext(eq("/wallets/create"), any(CreateWalletHandler.class));
        verify(mockServer, times(1)).createContext(eq("/all-wallets"), any(ListAllWalletsHandler.class));
        verify(mockServer, times(1)).createContext(eq("/wallets/deposit"), any(DepositHandler.class));
        verify(mockServer, times(1)).createContext(eq("/wallets/withdraw"), any(WithdrawHandler.class));
    }
}
