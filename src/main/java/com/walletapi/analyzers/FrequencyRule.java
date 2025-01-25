package main.java.com.walletapi.analyzers;

import main.java.com.walletapi.models.Transaction;

import java.time.Duration;
import java.util.List;

public class FrequencyRule implements TransactionAnalyzer {
    private final int maxTransactions;
    private final Duration timeWindow;

    public FrequencyRule(int maxTransactions, Duration timeWindow) {
        this.maxTransactions = maxTransactions;
        this.timeWindow = timeWindow;
    }

    @Override
    public boolean analyze(Transaction transaction, List<Transaction> transactionHistory) {
        long count = transactionHistory.stream()
            .filter(t -> t.walletId().equals(transaction.walletId()))
            .filter(t -> Duration.between(t.timestamp(), transaction.timestamp()).compareTo(timeWindow) <= 0)
            .count();
        return count > maxTransactions;
    }
}
