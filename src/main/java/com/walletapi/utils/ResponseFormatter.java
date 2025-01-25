package main.java.com.walletapi.utils;

import main.java.com.walletapi.models.Transaction;
import main.java.com.walletapi.models.Wallet;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResponseFormatter {

    public static String formatWallet(Wallet wallet) {
        return String.format("Wallet ID: %s, Balance: %.2f", wallet.getId(), wallet.getBalance());
    }

    public static String formatWallets(Map<String, Wallet> wallets) {
        return wallets.values().stream()
            .map(ResponseFormatter::formatWallet)
            .collect(Collectors.joining("\n"));
    }

    public static String formatTransaction(Transaction transaction) {
        return String.format("Transaction ID: %s, Wallet ID: %s, Amount: %.2f, Type: %s, Timestamp: %s",
            transaction.id(), transaction.walletId(), transaction.amount(),
            transaction.type(), transaction.timestamp());
    }

    public static String formatTransactions(List<Transaction> transactions) {
        return transactions.stream()
            .map(ResponseFormatter::formatTransaction)
            .collect(Collectors.joining("\n"));
    }

    public static String formatTopSpenders(List<Map.Entry<String, BigDecimal>> topSpenders) {
        return topSpenders.stream()
            .map(entry -> String.format("Wallet ID: %s, Total Spent: %.2f", entry.getKey(), entry.getValue()))
            .collect(Collectors.joining("\n"));
    }
}
