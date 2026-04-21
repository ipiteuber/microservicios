package com.duoc.citasmedicas.exception;

// Se lanza cuando falla una regla de negocio (doctor no disponible, conflicto de horario, etc).
// GlobalExceptionHandler la convierte a 400.
public class BusinessValidationException extends RuntimeException {

    public BusinessValidationException(String message) {
        super(message);
    }
}
