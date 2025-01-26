package main.java.com.walletapi.services;

import main.java.com.walletapi.models.Transaction;
import main.java.com.walletapi.models.TransactionType;
import main.java.com.walletapi.models.Wallet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
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

    public Map<String, Long> getTransactionTrends() {
        LocalDateTime now = LocalDateTime.now();

        Map<String, Long> trends = new HashMap<>();
        trends.put("daily", getTransactionCountInPeriod(now.minusDays(1)));
        trends.put("weekly", getTransactionCountInPeriod(now.minusWeeks(1)));
        trends.put("monthly", getTransactionCountInPeriod(now.minusMonths(1)));

        return trends;
    }

    private long getTransactionCountInPeriod(LocalDateTime from) {
        return wallets.values().stream()
            .flatMap(wallet -> wallet.getTransactions().stream())
            .filter(t -> t.timestamp().isAfter(from))
            .count();
    }

}
