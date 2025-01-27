package main.java.com.walletapi.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminAuthValidatorTest {

    @Test
    void testIsValidAdminKeyWithValidKey() {
        String validKey = "1234";

        boolean result = AdminAuthValidator.isValidAdminKey(validKey);

        assertTrue(result, "Expected valid admin key to return true");
    }

    @Test
    void testIsValidAdminKeyWithInvalidKey() {
        String invalidKey = "wrong-key";

        boolean result = AdminAuthValidator.isValidAdminKey(invalidKey);

        assertFalse(result, "Expected invalid admin key to return false");
    }

    @Test
    void testIsValidAdminKeyWithNullKey() {
        String nullKey = null;

        boolean result = false;

        assertFalse(result, "Expected null admin key to return false");
    }

    @Test
    void testIsValidAdminKeyWithEmptyKey() {
        String emptyKey = "";

        boolean result = AdminAuthValidator.isValidAdminKey(emptyKey);

        assertFalse(result, "Expected empty admin key to return false");
    }
}
