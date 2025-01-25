package main.java.com.walletapi.exceptions;

public class InvalidWalletException extends RuntimeException {

    public InvalidWalletException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidWalletException(String message) {
        super(message);
    }
}
