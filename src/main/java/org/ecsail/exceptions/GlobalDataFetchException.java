package org.ecsail.exceptions;

public class GlobalDataFetchException extends RuntimeException {
    public GlobalDataFetchException(String message, Throwable cause) {
        super(message, cause);
    }
    public GlobalDataFetchException(String message) {
        super(message);
    }
}
