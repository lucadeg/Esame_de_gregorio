package com.esame.exception;

import java.util.List;

/**
 * Validation Exception
 * Eccezione Validazione
 * 
 * Custom exception for validation errors
 * Eccezione personalizzata per errori di validazione
 */
public class ValidationException extends RuntimeException {
    
    private final List<String> validationErrors;
    private final String fieldName;
    
    /**
     * Constructor with validation errors list
     * Costruttore con lista errori di validazione
     * 
     * @param validationErrors List of validation errors / Lista errori di validazione
     */
    public ValidationException(List<String> validationErrors) {
        super("Validation failed / Validazione fallita: " + String.join(", ", validationErrors));
        this.validationErrors = validationErrors;
        this.fieldName = null;
    }
    
    /**
     * Constructor with validation errors and field name
     * Costruttore con errori di validazione e nome campo
     * 
     * @param validationErrors List of validation errors / Lista errori di validazione
     * @param fieldName Field name related to the errors / Nome campo relativo agli errori
     */
    public ValidationException(List<String> validationErrors, String fieldName) {
        super("Validation failed for field " + fieldName + " / Validazione fallita per il campo " + fieldName + ": " + String.join(", ", validationErrors));
        this.validationErrors = validationErrors;
        this.fieldName = fieldName;
    }
    
    /**
     * Constructor with single validation error
     * Costruttore con singolo errore di validazione
     * 
     * @param message Validation error message / Messaggio errore di validazione
     */
    public ValidationException(String message) {
        super(message);
        this.validationErrors = List.of(message);
        this.fieldName = null;
    }
    
    /**
     * Constructor with single validation error and field name
     * Costruttore con singolo errore di validazione e nome campo
     * 
     * @param message Validation error message / Messaggio errore di validazione
     * @param fieldName Field name related to the error / Nome campo relativo all'errore
     */
    public ValidationException(String message, String fieldName) {
        super("Validation failed for field " + fieldName + " / Validazione fallita per il campo " + fieldName + ": " + message);
        this.validationErrors = List.of(message);
        this.fieldName = fieldName;
    }
    
    // Getters / Getter
    public List<String> getValidationErrors() {
        return validationErrors;
    }
    
    public String getFieldName() {
        return fieldName;
    }
}
