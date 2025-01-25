package main.java.com.walletapi.models;

import main.java.com.walletapi.exceptions.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Wallet {
    private final String id;
    private BigDecimal balance;
    private final List<Transaction> transactions;

    private static Integer uidFactory = 0; //making the id-s human-readable

    public Wallet() {
        this.id = (uidFactory++).toString();
        this.balance = BigDecimal.ZERO;
        this.transactions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public synchronized void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        balance = balance.add(amount);
        transactions.add(new Transaction(id, amount, TransactionType.DEPOSIT));
    }

    public synchronized void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal.");
        }
        balance = balance.subtract(amount);
        transactions.add(new Transaction(id, amount.negate(), TransactionType.WITHDRAW));
    }
}
