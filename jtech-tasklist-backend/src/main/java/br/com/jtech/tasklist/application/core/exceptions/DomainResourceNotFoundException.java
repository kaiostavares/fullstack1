package br.com.jtech.tasklist.application.core.exceptions;

public class DomainResourceNotFoundException extends DomainException {
    public DomainResourceNotFoundException(String message) {
        super(message);
    }

    public DomainResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
