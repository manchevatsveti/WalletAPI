package main.java.com.walletapi.services;

import main.java.com.walletapi.exceptions.InsufficientFundsException;
import main.java.com.walletapi.exceptions.InvalidWalletException;
import main.java.com.walletapi.models.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WalletServiceTest {
    private WalletService walletService;
    private Wallet testWallet;

    @BeforeEach
    void setUp() {
        walletService = new WalletService();

        // Create and initialize a test wallet
        testWallet = walletService.createWallet();
        walletService.deposit(testWallet.getId(), BigDecimal.valueOf(100)); // Initial deposit
    }

    @Test
    void testCreateWallet() {
        Wallet wallet = walletService.createWallet();
        assertNotNull(wallet);
        assertNotNull(wallet.getId());
        assertEquals(BigDecimal.ZERO, wallet.getBalance());
    }


    @Test
    void testGetWallet_ThrowsExceptionIfNotFound() {
        assertThrows(InvalidWalletException.class, () -> walletService.getWallet("non-existent-id"));
    }

    @Test
    void testDeposit() {
        walletService.deposit(testWallet.getId(), BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(150), testWallet.getBalance());
    }

    @Test
    void testDeposit_ThrowsExceptionForNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> walletService.deposit(testWallet.getId(), BigDecimal.valueOf(-10)));
    }

    @Test
    void testWithdraw() {
        walletService.withdraw(testWallet.getId(), BigDecimal.valueOf(30));
        assertEquals(BigDecimal.valueOf(70), testWallet.getBalance());
    }

    @Test
    void testWithdraw_ThrowsExceptionForInvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> walletService.withdraw(testWallet.getId(), BigDecimal.valueOf(-1)));
        assertThrows(IllegalArgumentException.class, () -> walletService.withdraw(testWallet.getId(), BigDecimal.valueOf(0)));
        assertThrows(InsufficientFundsException.class, () -> walletService.withdraw(testWallet.getId(), BigDecimal.valueOf(200))); // Overdraft
    }

    @Test
    void testGetBalance_ThrowsExceptionIfWalletNotFound() {
        assertThrows(InvalidWalletException.class, () -> walletService.getBalance("non-existent-id"));
    }
}
