package main.java.com.walletapi.services;

import main.java.com.walletapi.models.Transaction;
import main.java.com.walletapi.models.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FraudDetectionServiceTest {

    private FraudDetectionService fraudDetectionService;
    private Wallet testWallet;
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        fraudDetectionService = new FraudDetectionService();
        walletService = new WalletService();

        testWallet = walletService.createWallet();
        walletService.deposit(testWallet.getId(), BigDecimal.valueOf(1000));

        walletService.withdraw(testWallet.getId(), BigDecimal.valueOf(5));
        walletService.withdraw(testWallet.getId(), BigDecimal.valueOf(5));
        walletService.withdraw(testWallet.getId(), BigDecimal.valueOf(5));
        walletService.withdraw(testWallet.getId(), BigDecimal.valueOf(5));
        walletService.withdraw(testWallet.getId(), BigDecimal.valueOf(5));
    }

    @Test
    void testEvaluateAllWallets() {
        List<Transaction> flaggedTransactions = fraudDetectionService.evaluateAllWallets(walletService);
        assertEquals(testWallet.getTransactions().size(), flaggedTransactions.size());
    }
}
