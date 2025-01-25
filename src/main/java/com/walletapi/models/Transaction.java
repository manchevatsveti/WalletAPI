package main.java.com.walletapi.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Transaction(String id, String walletId, BigDecimal amount,
                          TransactionType type, LocalDateTime timestamp) {

    public Transaction(String walletId, BigDecimal amount, TransactionType type) {
        this(UUID.randomUUID().toString(), walletId, amount, type, LocalDateTime.now());
    }
}
