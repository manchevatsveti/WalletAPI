package main.java.com.walletapi.services;

import main.java.com.walletapi.models.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnalyticsServiceTest {

    private AnalyticsService analyticsService;
    private Wallet testWallet1;
    private Wallet testWallet2;

    @BeforeEach
    void setUp() {
        testWallet1 = new Wallet();
        testWallet1.deposit(new BigDecimal(10));
        testWallet1.deposit(new BigDecimal(3000));
        testWallet1.withdraw(new BigDecimal(10));

        testWallet2 = new Wallet();
        testWallet2.deposit(new BigDecimal(200));
        testWallet2.withdraw(new BigDecimal(20));

        Map<String, Wallet> walletMap = new HashMap<>();
        walletMap.put(testWallet1.getId(), testWallet1);
        walletMap.put(testWallet2.getId(), testWallet2);

        analyticsService = new AnalyticsService(walletMap);
    }

    @Test
    void testGetTopSpenders() {
        List<Map.Entry<String, BigDecimal>> topSpenders = analyticsService.getTopSpenders(1);
        assertEquals(1, topSpenders.size());
        assertEquals(testWallet2.getId(), topSpenders.getFirst().getKey());
        assertEquals(BigDecimal.valueOf(-20), topSpenders.getFirst().getValue());
    }

}
