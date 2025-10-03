package com.esame.exception;

import com.esame.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global Exception Handler
 * Gestore Globale delle Eccezioni
 * 
 * Centralized exception handling for the entire application
 * Gestione centralizzata delle eccezioni per tutta l'applicazione
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handle Resource Not Found Exception
     * Gestisce Eccezione Risorsa Non Trovata
     * 
     * @param ex ResourceNotFoundException / Eccezione Risorsa Non Trovata
     * @param request Web request / Richiesta web
     * @return Error response / Risposta di errore
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        
        ApiResponse<Object> response = ApiResponse.error(
            ex.getMessage(), 
            HttpStatus.NOT_FOUND.value()
        );
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setTimestamp(LocalDateTime.now().toString());
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    
    /**
     * Handle Business Logic Exception
     * Gestisce Eccezione Logica di Business
     * 
     * @param ex BusinessLogicException / Eccezione Logica di Business
     * @param request Web request / Richiesta web
     * @return Error response / Risposta di errore
     */
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessLogicException(
            BusinessLogicException ex, WebRequest request) {
        
        ApiResponse<Object> response = ApiResponse.error(
            ex.getMessage(), 
            HttpStatus.BAD_REQUEST.value()
        );
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setTimestamp(LocalDateTime.now().toString());
        
        // Add error code to metadata if available / Aggiungi codice errore ai metadati se disponibile
        if (ex.getErrorCode() != null) {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("errorCode", ex.getErrorCode());
            if (ex.getFieldName() != null) {
                metadata.put("fieldName", ex.getFieldName());
            }
            response.setMetadata(metadata);
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handle Validation Exception
     * Gestisce Eccezione Validazione
     * 
     * @param ex ValidationException / Eccezione Validazione
     * @param request Web request / Richiesta web
     * @return Error response / Risposta di errore
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(
            ValidationException ex, WebRequest request) {
        
        ApiResponse<Object> response = ApiResponse.error(
            ex.getMessage(), 
            ex.getValidationErrors(),
            HttpStatus.BAD_REQUEST.value()
        );
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setTimestamp(LocalDateTime.now().toString());
        
        // Add field name to metadata if available / Aggiungi nome campo ai metadati se disponibile
        if (ex.getFieldName() != null) {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("fieldName", ex.getFieldName());
            response.setMetadata(metadata);
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handle Method Argument Not Valid Exception (Bean Validation)
     * Gestisce Eccezione Argomento Metodo Non Valido (Validazione Bean)
     * 
     * @param ex MethodArgumentNotValidException / Eccezione Argomento Metodo Non Valido
     * @param request Web request / Richiesta web
     * @return Error response / Risposta di errore
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        String errorMessage = "Validation failed / Validazione fallita: " + 
            errors.entrySet().stream()
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining(", "));
        
        ApiResponse<Object> response = ApiResponse.error(
            errorMessage,
            errors.values().stream().collect(Collectors.toList()),
            HttpStatus.BAD_REQUEST.value()
        );
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setTimestamp(LocalDateTime.now().toString());
        response.setMetadata(errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handle IllegalArgumentException
     * Gestisce Eccezione Argomento Non Valido
     * 
     * @param ex IllegalArgumentException / Eccezione Argomento Non Valido
     * @param request Web request / Richiesta web
     * @return Error response / Risposta di errore
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        ApiResponse<Object> response = ApiResponse.error(
            "Invalid argument / Argomento non valido: " + ex.getMessage(),
            HttpStatus.BAD_REQUEST.value()
        );
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setTimestamp(LocalDateTime.now().toString());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    /**
     * Handle IllegalStateException
     * Gestisce Eccezione Stato Non Valido
     * 
     * @param ex IllegalStateException / Eccezione Stato Non Valido
     * @param request Web request / Richiesta web
     * @return Error response / Risposta di errore
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalStateException(
            IllegalStateException ex, WebRequest request) {
        
        ApiResponse<Object> response = ApiResponse.error(
            "Invalid state / Stato non valido: " + ex.getMessage(),
            HttpStatus.CONFLICT.value()
        );
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setTimestamp(LocalDateTime.now().toString());
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
    
    /**
     * Handle generic RuntimeException
     * Gestisce RuntimeException generica
     * 
     * @param ex RuntimeException / RuntimeException
     * @param request Web request / Richiesta web
     * @return Error response / Risposta di errore
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(
            RuntimeException ex, WebRequest request) {
        
        ApiResponse<Object> response = ApiResponse.error(
            "Internal server error / Errore interno del server: " + ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setTimestamp(LocalDateTime.now().toString());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    
    /**
     * Handle generic Exception
     * Gestisce Eccezione generica
     * 
     * @param ex Exception / Eccezione
     * @param request Web request / Richiesta web
     * @return Error response / Risposta di errore
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(
            Exception ex, WebRequest request) {
        
        ApiResponse<Object> response = ApiResponse.error(
            "Unexpected error / Errore inaspettato: " + ex.getMessage(),
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setTimestamp(LocalDateTime.now().toString());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
