package main.java.com.walletapi.utils.riskcalculator.analyzers;

import main.java.com.walletapi.models.Transaction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LocationRuleTest {

    @Test
    void testAnalyzeWithExceedingLocations() {
        Transaction transaction = mock(Transaction.class);

        Transaction history1 = mock(Transaction.class);
        Transaction history2 = mock(Transaction.class);
        Transaction history3 = mock(Transaction.class);

        when(history1.walletId()).thenReturn("location1");
        when(history2.walletId()).thenReturn("location2");
        when(history3.walletId()).thenReturn("location3");

        LocationRule rule = new LocationRule(2);
        boolean result = rule.analyze(transaction, List.of(history1, history2, history3));

        assertTrue(result, "Expected true when distinct locations exceed the threshold.");
    }

    @Test
    void testAnalyzeWithinLocationLimit() {
        Transaction transaction = mock(Transaction.class);

        Transaction history1 = mock(Transaction.class);
        Transaction history2 = mock(Transaction.class);

        when(history1.walletId()).thenReturn("location1");
        when(history2.walletId()).thenReturn("location1");

        LocationRule rule = new LocationRule(2);
        boolean result = rule.analyze(transaction, List.of(history1, history2));

        assertFalse(result, "Expected false when distinct locations do not exceed the threshold.");
    }
}
