package main.java.com.walletapi.utils.riskcalculator.analyzers;

import main.java.com.walletapi.models.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class SmallTransactionsRule implements TransactionAnalyzer {
    private final BigDecimal smallTransactionThreshold;
    private final int maxSmallTransactions;

    public SmallTransactionsRule(BigDecimal smallTransactionThreshold, int maxSmallTransactions) {
        this.smallTransactionThreshold = smallTransactionThreshold;
        this.maxSmallTransactions = maxSmallTransactions;
    }

    @Override
    public boolean analyze(Transaction transaction, List<Transaction> transactionHistory) {
        long smallTransactionCount = transactionHistory.stream()
            .filter(t -> t.walletId().equals(transaction.walletId()))
            .filter(t -> t.amount().compareTo(smallTransactionThreshold) < 0)
            .count();
        return smallTransactionCount > maxSmallTransactions;
    }
}
