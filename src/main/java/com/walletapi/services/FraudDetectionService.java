package main.java.com.walletapi.services;

import main.java.com.walletapi.analyzers.FrequencyRule;
import main.java.com.walletapi.analyzers.LocationRule;
import main.java.com.walletapi.analyzers.SmallTransactionsRule;
import main.java.com.walletapi.models.Transaction;
import main.java.com.walletapi.utils.RiskCalculator;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class FraudDetectionService {

    private static final int MAX_TRANSACTIONS_IN_WINDOW = 5;
    private static final Duration TIME_WINDOW = Duration.ofMinutes(10);
    private static final BigDecimal SMALL_TRANSACTION_THRESHOLD = new BigDecimal("10");
    private static final int MAX_SMALL_TRANSACTIONS = 10;
    private static final int MAX_DISTINCT_LOCATIONS = 3;

    private final RiskCalculator riskCalculator = new RiskCalculator();

    public FraudDetectionService() {
        riskCalculator.addRule(new FrequencyRule(MAX_TRANSACTIONS_IN_WINDOW, TIME_WINDOW));
        riskCalculator.addRule(new SmallTransactionsRule(SMALL_TRANSACTION_THRESHOLD, MAX_SMALL_TRANSACTIONS));
        riskCalculator.addRule(new LocationRule(MAX_DISTINCT_LOCATIONS));
    }

    /**
     * Evaluates all wallets and returns a list of flagged transactions.
     *
     * @param walletService The wallet service providing access to all wallets.
     * @return A list of flagged transactions across all wallets.
     */
    public List<Transaction> evaluateAllWallets(WalletService walletService) {
        return walletService.getWallets().values().stream()
            .flatMap(wallet -> wallet.getTransactions().stream()
                .filter(transaction -> riskCalculator.evaluate(transaction, wallet.getTransactions())))
            .collect(Collectors.toList());
    }
}
