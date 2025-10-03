package com.esame.exception;

/**
 * Resource Not Found Exception
 * Eccezione Risorsa Non Trovata
 * 
 * Custom exception for when a requested resource is not found
 * Eccezione personalizzata per quando una risorsa richiesta non viene trovata
 */
public class ResourceNotFoundException extends RuntimeException {
    
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;
    
    /**
     * Constructor with resource name and field details
     * Costruttore con nome risorsa e dettagli campo
     * 
     * @param resourceName Name of the resource / Nome della risorsa
     * @param fieldName Name of the field used for search / Nome del campo usato per la ricerca
     * @param fieldValue Value of the field / Valore del campo
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s' / %s non trovato con %s : '%s'", 
                resourceName, fieldName, fieldValue, resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    
    /**
     * Constructor with simple message
     * Costruttore con messaggio semplice
     * 
     * @param message Error message / Messaggio di errore
     */
    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceName = null;
        this.fieldName = null;
        this.fieldValue = null;
    }
    
    /**
     * Constructor with message and cause
     * Costruttore con messaggio e causa
     * 
     * @param message Error message / Messaggio di errore
     * @param cause Cause of the exception / Causa dell'eccezione
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.resourceName = null;
        this.fieldName = null;
        this.fieldValue = null;
    }
    
    // Getters / Getter
    public String getResourceName() {
        return resourceName;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public Object getFieldValue() {
        return fieldValue;
    }
}
