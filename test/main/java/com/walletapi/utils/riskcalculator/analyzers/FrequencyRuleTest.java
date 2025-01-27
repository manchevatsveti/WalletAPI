package main.java.com.walletapi.utils.riskcalculator.analyzers;

import main.java.com.walletapi.models.Transaction;
import main.java.com.walletapi.models.Wallet;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FrequencyRuleTest {

    @Test
    void testAnalyzeWithinTimeWindow() {
        Wallet wallet = new Wallet();
        wallet.deposit(new BigDecimal("50.00"));
        wallet.deposit(new BigDecimal("25.00"));

        List<Transaction> transactionHistory = wallet.getTransactions();

        Transaction currentTransaction = transactionHistory.get(transactionHistory.size() - 1);

        FrequencyRule rule = new FrequencyRule(1, Duration.ofMinutes(1));

        boolean result = rule.analyze(currentTransaction, transactionHistory);

        assertTrue(result, "Expected true when the transaction count exceeds the threshold within the time window.");
    }

    @Test
    void testAnalyzeOutsideTimeWindow() {
        Transaction transaction = mock(Transaction.class);
        when(transaction.walletId()).thenReturn("wallet1");
        when(transaction.timestamp()).thenReturn(LocalDateTime.now());

        Transaction historyTransaction = mock(Transaction.class);
        when(historyTransaction.walletId()).thenReturn("wallet1");
        when(historyTransaction.timestamp()).thenReturn(LocalDateTime.now().minusMinutes(2));

        FrequencyRule rule = new FrequencyRule(1, Duration.ofMinutes(1));
        boolean result = rule.analyze(transaction, List.of(historyTransaction));

        assertFalse(result, "Expected false when the frequency does not exceed the threshold within the time window.");
    }
}
