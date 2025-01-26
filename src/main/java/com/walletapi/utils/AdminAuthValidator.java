package main.java.com.walletapi.utils;

public class AdminAuthValidator {
    private static final String ADMIN_KEY = "1234";

    public static boolean isValidAdminKey(String providedKey) {
        return ADMIN_KEY.equals(providedKey);
    }
}
