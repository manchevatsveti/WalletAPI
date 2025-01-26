package main.java.com.walletapi.utils.riskcalculator;

import main.java.com.walletapi.utils.riskcalculator.analyzers.TransactionAnalyzer;
import main.java.com.walletapi.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class RiskCalculator {
    private final List<TransactionAnalyzer> rules = new ArrayList<>();

    public void addRule(TransactionAnalyzer rule) {
        rules.add(rule);
    }

    public void removeRule(TransactionAnalyzer rule) {
        rules.remove(rule);
    }

    public boolean evaluate(Transaction transaction, List<Transaction> transactionHistory) {
        for (TransactionAnalyzer rule : rules) {
            if (rule.analyze(transaction, transactionHistory)) {
                return true;
            }
        }
        return false;
    }
}
