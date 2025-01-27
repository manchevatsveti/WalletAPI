package main.java.com.walletapi.utils.riskcalculator;

import main.java.com.walletapi.models.Transaction;
import main.java.com.walletapi.utils.riskcalculator.analyzers.TransactionAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class RiskCalculatorTest {

    @Mock
    private Transaction mockTransaction;

    @Mock
    private TransactionAnalyzer mockRule1;

    @Mock
    private TransactionAnalyzer mockRule2;

    private RiskCalculator riskCalculator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        riskCalculator = new RiskCalculator();
    }

    @Test
    void testEvaluateWithMatchingRule() {
        riskCalculator.addRule(mockRule1);
        riskCalculator.addRule(mockRule2);

        when(mockRule1.analyze(mockTransaction, List.of())).thenReturn(false);
        when(mockRule2.analyze(mockTransaction, List.of())).thenReturn(true);

        boolean result = riskCalculator.evaluate(mockTransaction, List.of());

        assertTrue(result, "Expected true when at least one rule matches.");
    }

    @Test
    void testEvaluateWithNoMatchingRules() {
        riskCalculator.addRule(mockRule1);

        when(mockRule1.analyze(mockTransaction, List.of())).thenReturn(false);

        boolean result = riskCalculator.evaluate(mockTransaction, List.of());

        assertFalse(result, "Expected false when no rules match.");
    }

    @Test
    void testEvaluateWithNoRules() {
        boolean result = riskCalculator.evaluate(mockTransaction, List.of());

        assertFalse(result, "Expected false when no rules are added.");
    }
}
