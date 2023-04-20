package org.dcm4che.typeddicom;

public class ConstructorNotImplementedException extends RuntimeException {
    public ConstructorNotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }
}
