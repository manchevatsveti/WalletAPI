package main.java.com.walletapi.utils;

import main.java.com.walletapi.models.Transaction;
import main.java.com.walletapi.models.TransactionType;
import main.java.com.walletapi.models.Wallet;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResponseFormatterTest {

    @Mock
    Wallet walletMock;

    @Mock
    Transaction transactionMock;

    @Test
    void testFormatWallet() {
        walletMock = Mockito.mock();
        Mockito.when(walletMock.getId()).thenReturn("wallet123");
        Mockito.when(walletMock.getBalance()).thenReturn(BigDecimal.valueOf(100.50));

        String formattedWallet = ResponseFormatter.formatWallet(walletMock);

        assertEquals("Wallet ID: wallet123, Balance: 100.50", formattedWallet);
    }

    @Test
    void testFormatWallets() {
        Wallet wallet1 = Mockito.mock(Wallet.class);
        Wallet wallet2 = Mockito.mock(Wallet.class);

        Mockito.when(wallet1.getId()).thenReturn("wallet123");
        Mockito.when(wallet1.getBalance()).thenReturn(BigDecimal.valueOf(100.50));
        Mockito.when(wallet2.getId()).thenReturn("wallet456");
        Mockito.when(wallet2.getBalance()).thenReturn(BigDecimal.valueOf(200.75));

        Map<String, Wallet> wallets = Map.of(
            wallet1.getId(), wallet1,
            wallet2.getId(), wallet2
        );

        String formattedWallets = ResponseFormatter.formatWallets(wallets);

        String expected = "Wallet ID: wallet456, Balance: 200.75\nWallet ID: wallet123, Balance: 100.50";
        assertEquals(expected, formattedWallets);
    }

    @Test
    void testFormatTransaction() {
        transactionMock = Mockito.mock(Transaction.class);
        Mockito.when(transactionMock.id()).thenReturn("tx123");
        Mockito.when(transactionMock.walletId()).thenReturn("wallet123");
        Mockito.when(transactionMock.amount()).thenReturn(BigDecimal.valueOf(50.25));
        Mockito.when(transactionMock.type()).thenReturn(TransactionType.WITHDRAW);
        Mockito.when(transactionMock.timestamp()).thenReturn(LocalDateTime.parse("2025-01-26T10:00:00"));

        String formattedTransaction = ResponseFormatter.formatTransaction(transactionMock);

        String expected = "Transaction ID: tx123, Wallet ID: wallet123, Amount: 50.25, Type: WITHDRAW, Timestamp: 2025-01-26T10:00";
        assertEquals(expected, formattedTransaction);
    }

    @Test
    void testFormatTransactions() {
        Transaction transaction1 = Mockito.mock(Transaction.class);
        Transaction transaction2 = Mockito.mock(Transaction.class);

        Mockito.when(transaction1.id()).thenReturn("tx123");
        Mockito.when(transaction1.walletId()).thenReturn("wallet123");
        Mockito.when(transaction1.amount()).thenReturn(BigDecimal.valueOf(50.25));
        Mockito.when(transaction1.type()).thenReturn(TransactionType.WITHDRAW);
        Mockito.when(transaction1.timestamp()).thenReturn(LocalDateTime.parse("2025-01-26T10:00:00"));

        Mockito.when(transaction2.id()).thenReturn("tx124");
        Mockito.when(transaction2.walletId()).thenReturn("wallet456");
        Mockito.when(transaction2.amount()).thenReturn(BigDecimal.valueOf(100.75));
        Mockito.when(transaction2.type()).thenReturn(TransactionType.DEPOSIT);
        Mockito.when(transaction2.timestamp()).thenReturn(LocalDateTime.parse("2025-01-26T11:00:00"));

        String formattedTransactions = ResponseFormatter.formatTransactions(List.of(transaction1, transaction2));

        String expected = "Transaction ID: tx123, Wallet ID: wallet123, Amount: 50.25, Type: WITHDRAW, Timestamp: 2025-01-26T10:00\n" +
            "Transaction ID: tx124, Wallet ID: wallet456, Amount: 100.75, Type: DEPOSIT, Timestamp: 2025-01-26T11:00";
        assertEquals(expected, formattedTransactions);
    }

    @Test
    void testFormatTopSpenders() {
        Map.Entry<String, BigDecimal> topSpender1 = new AbstractMap.SimpleEntry<>("wallet123", BigDecimal.valueOf(500.75));
        Map.Entry<String, BigDecimal> topSpender2 = new AbstractMap.SimpleEntry<>("wallet456", BigDecimal.valueOf(300.50));

        String formattedTopSpenders = ResponseFormatter.formatTopSpenders(List.of(topSpender1, topSpender2));

        String expected = "Wallet ID: wallet123, Total Spent: 500.75\nWallet ID: wallet456, Total Spent: 300.50";
        assertEquals(expected, formattedTopSpenders);
    }
}
