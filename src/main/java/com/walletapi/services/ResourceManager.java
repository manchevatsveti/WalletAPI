package main.java.com.walletapi.services;

public class ResourceManager {
    private static final WalletService WALLET_SERVICE = new WalletService();
    private static final AnalyticsService ANALYTICS_SERVICE = new AnalyticsService(WALLET_SERVICE.getWallets());
    private static final FraudDetectionService FRAUD_DETECTION_SERVICE = new FraudDetectionService();

    public static WalletService getWalletService() {
        return WALLET_SERVICE;
    }

    public static AnalyticsService getAnalyticsService() {
        return ANALYTICS_SERVICE;
    }

    public static FraudDetectionService getFraudDetectionService() {
        return FRAUD_DETECTION_SERVICE;
    }
}
