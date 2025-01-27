package main.java.com.walletapi.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResourceManagerTest {
    @Test
    void testGetWalletService() {
        assertNotNull(ResourceManager.getWalletService());
    }

    @Test
    void testGetAnalyticsService() {
        assertNotNull(ResourceManager.getAnalyticsService());
    }

    @Test
    void testGetFraudDetectionService() {
        assertNotNull(ResourceManager.getFraudDetectionService());
    }
}
