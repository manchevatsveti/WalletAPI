package main.java.com.walletapi.services;

import main.java.com.walletapi.models.Transaction;
import main.java.com.walletapi.models.TransactionType;
import main.java.com.walletapi.models.Wallet;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsService {

    private final Map<String, Wallet> wallets;

    public AnalyticsService(Map<String, Wallet> wallets) {
        this.wallets = wallets;
    }

    public List<Map.Entry<String, BigDecimal>> getTopSpenders(int limit) {
        System.out.println("Limit: " + limit);

        List<Map.Entry<String, BigDecimal>> result = wallets.entrySet().stream()
            .map(entry -> Map.entry(entry.getKey(), calculateTotalSpent(entry.getValue())))
            .sorted(Map.Entry.comparingByValue())
            .limit(limit)
            .collect(Collectors.toList());

        result.forEach(entry -> System.out.println("Wallet ID: " + entry.getKey() + ", Total Spent: " + entry.getValue()));

        return result;
    }

    private BigDecimal calculateTotalSpent(Wallet wallet) {
        return wallet.getTransactions().stream()
            .filter(transaction -> transaction.type() == TransactionType.WITHDRAW)
            .map(Transaction::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
