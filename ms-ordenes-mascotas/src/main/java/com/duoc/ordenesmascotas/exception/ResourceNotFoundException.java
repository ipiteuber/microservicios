package com.duoc.ordenesmascotas.exception;

// Se lanza cuando no se encuentra un recurso. GlobalExceptionHandler la convierte a 404.
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
