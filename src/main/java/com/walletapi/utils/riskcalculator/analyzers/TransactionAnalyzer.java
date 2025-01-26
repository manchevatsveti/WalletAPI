package main.java.com.walletapi.utils.riskcalculator.analyzers;

import main.java.com.walletapi.models.Transaction;

import java.util.List;

public interface TransactionAnalyzer {
    boolean analyze(Transaction transaction, List<Transaction> transactionHistory);
}
