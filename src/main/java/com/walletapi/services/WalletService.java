package main.java.com.walletapi.services;

import main.java.com.walletapi.exceptions.InvalidWalletException;
import main.java.com.walletapi.models.Wallet;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;

public class WalletService {

    private final ConcurrentHashMap<String, Wallet> wallets = new ConcurrentHashMap<>();

    public Wallet createWallet() {
        Wallet wallet = new Wallet();
        wallets.put(wallet.getId(), wallet);
        return wallet;
    }

    public Wallet getWallet(String walletId) {
        Wallet wallet = wallets.get(walletId);
        if (wallet == null) {
            throw new InvalidWalletException("Wallet not found: " + walletId);
        }
        return wallet;
    }

    public synchronized void deposit(String walletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        Wallet wallet = getWallet(walletId);
        wallet.deposit(amount);
    }

    public synchronized void withdraw(String walletId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        Wallet wallet = getWallet(walletId);
        wallet.withdraw(amount);
    }

    public ConcurrentHashMap<String, Wallet> getWallets() {
        return wallets;
    }
}
