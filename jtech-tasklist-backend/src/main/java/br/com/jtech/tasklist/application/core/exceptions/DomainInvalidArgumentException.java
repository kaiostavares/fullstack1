package br.com.jtech.tasklist.application.core.exceptions;

public class DomainInvalidArgumentException extends DomainException {
    public DomainInvalidArgumentException(String message) {
        super(message);
    }

    public DomainInvalidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
