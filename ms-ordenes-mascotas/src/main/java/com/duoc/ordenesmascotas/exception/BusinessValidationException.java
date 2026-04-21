package com.duoc.ordenesmascotas.exception;

// Se lanza cuando falla una regla de negocio (stock insuficiente, orden no cancelable, etc).
public class BusinessValidationException extends RuntimeException {

    public BusinessValidationException(String message) {
        super(message);
    }
}
