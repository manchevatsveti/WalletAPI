package main.java.com.walletapi.analyzers;

import main.java.com.walletapi.models.Transaction;

import java.util.List;

public interface TransactionAnalyzer {
    boolean analyze(Transaction transaction, List<Transaction> transactionHistory);
}
