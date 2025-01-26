package main.java.com.walletapi.utils.riskcalculator.analyzers;

import main.java.com.walletapi.models.Transaction;

import java.util.List;

public class LocationRule implements TransactionAnalyzer {
    private final int maxDistinctLocations;

    public LocationRule(int maxDistinctLocations) {
        this.maxDistinctLocations = maxDistinctLocations;
    }

    @Override
    public boolean analyze(Transaction transaction, List<Transaction> transactionHistory) {
        long distinctLocations = transactionHistory.stream()
            .map(Transaction::walletId) // Mock location with walletId for simplicity
            .distinct()
            .count();
        return distinctLocations > maxDistinctLocations;
    }
}
