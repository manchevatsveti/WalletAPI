package main.java.com.walletapi.utils.riskcalculator.analyzers;

import main.java.com.walletapi.models.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SmallTransactionsRuleTest {

    @Test
    void testAnalyzeWithExceedingSmallTransactions() {
        Transaction transaction = mock(Transaction.class);
        when(transaction.walletId()).thenReturn("wallet1");

        Transaction history1 = mock(Transaction.class);
        Transaction history2 = mock(Transaction.class);
        Transaction history3 = mock(Transaction.class);

        when(history1.walletId()).thenReturn("wallet1");
        when(history2.walletId()).thenReturn("wallet1");
        when(history3.walletId()).thenReturn("wallet1");

        when(history1.amount()).thenReturn(new BigDecimal("4.99"));
        when(history2.amount()).thenReturn(new BigDecimal("3.50"));
        when(history3.amount()).thenReturn(new BigDecimal("2.00"));

        SmallTransactionsRule rule = new SmallTransactionsRule(new BigDecimal("5.00"), 2);

        boolean result = rule.analyze(transaction, List.of(history1, history2, history3));

        assertTrue(result, "Expected true when the count of small transactions exceeds the threshold.");
    }

    @Test
    void testAnalyzeWithinSmallTransactionLimit() {
        Transaction transaction = mock(Transaction.class);
        when(transaction.walletId()).thenReturn("wallet1");

        Transaction history1 = mock(Transaction.class);
        Transaction history2 = mock(Transaction.class);

        when(history1.walletId()).thenReturn("wallet1");
        when(history2.walletId()).thenReturn("wallet1");

        when(history1.amount()).thenReturn(new BigDecimal("4.99"));
        when(history2.amount()).thenReturn(new BigDecimal("3.50"));

        SmallTransactionsRule rule = new SmallTransactionsRule(new BigDecimal("5.00"), 3);
        boolean result = rule.analyze(transaction, List.of(history1, history2));

        assertFalse(result, "Expected false when the count of small transactions does not exceed the threshold.");
    }
}
