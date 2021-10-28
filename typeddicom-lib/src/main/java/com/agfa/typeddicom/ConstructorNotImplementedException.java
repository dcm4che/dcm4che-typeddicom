package com.agfa.typeddicom;

public class ConstructorNotImplementedException extends RuntimeException {
    public ConstructorNotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }
}
