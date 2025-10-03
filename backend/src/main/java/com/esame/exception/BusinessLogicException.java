package com.esame.exception;

/**
 * Business Logic Exception
 * Eccezione Logica di Business
 * 
 * Custom exception for business logic violations
 * Eccezione personalizzata per violazioni della logica di business
 */
public class BusinessLogicException extends RuntimeException {
    
    private final String errorCode;
    private final String fieldName;
    
    /**
     * Constructor with error code and message
     * Costruttore con codice errore e messaggio
     * 
     * @param errorCode Error code / Codice errore
     * @param message Error message / Messaggio di errore
     */
    public BusinessLogicException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.fieldName = null;
    }
    
    /**
     * Constructor with error code, message and field name
     * Costruttore con codice errore, messaggio e nome campo
     * 
     * @param errorCode Error code / Codice errore
     * @param message Error message / Messaggio di errore
     * @param fieldName Field name related to the error / Nome campo relativo all'errore
     */
    public BusinessLogicException(String errorCode, String message, String fieldName) {
        super(message);
        this.errorCode = errorCode;
        this.fieldName = fieldName;
    }
    
    /**
     * Constructor with message and cause
     * Costruttore con messaggio e causa
     * 
     * @param message Error message / Messaggio di errore
     * @param cause Cause of the exception / Causa dell'eccezione
     */
    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
        this.fieldName = null;
    }
    
    /**
     * Constructor with error code, message and cause
     * Costruttore con codice errore, messaggio e causa
     * 
     * @param errorCode Error code / Codice errore
     * @param message Error message / Messaggio di errore
     * @param cause Cause of the exception / Causa dell'eccezione
     */
    public BusinessLogicException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.fieldName = null;
    }
    
    // Getters / Getter
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getFieldName() {
        return fieldName;
    }
}
