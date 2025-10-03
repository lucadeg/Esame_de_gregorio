package com.esame.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

/**
 * API Response Wrapper
 * Wrapper per Risposta API
 * 
 * Standardized response structure for all API endpoints
 * Struttura di risposta standardizzata per tutti gli endpoint API
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
    private String timestamp;
    private Integer statusCode;
    private String path;
    private Object metadata;
    
    // Constructors / Costruttori
    public ApiResponse() {
        this.timestamp = LocalDateTime.now().toString();
    }
    
    public ApiResponse(boolean success, String message) {
        this();
        this.success = success;
        this.message = message;
    }
    
    public ApiResponse(boolean success, String message, T data) {
        this(success, message);
        this.data = data;
    }
    
    public ApiResponse(boolean success, String message, T data, Integer statusCode) {
        this(success, message, data);
        this.statusCode = statusCode;
    }
    
    // Static factory methods / Metodi factory statici
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operation successful / Operazione riuscita", data);
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }
    
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message);
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message);
    }
    
    public static <T> ApiResponse<T> error(String message, List<String> errors) {
        ApiResponse<T> response = new ApiResponse<>(false, message);
        response.setErrors(errors);
        return response;
    }
    
    public static <T> ApiResponse<T> error(String message, Integer statusCode) {
        ApiResponse<T> response = new ApiResponse<>(false, message);
        response.setStatusCode(statusCode);
        return response;
    }
    
    public static <T> ApiResponse<T> error(String message, List<String> errors, Integer statusCode) {
        ApiResponse<T> response = new ApiResponse<>(false, message);
        response.setErrors(errors);
        response.setStatusCode(statusCode);
        return response;
    }
    
    // Getters and Setters / Getter e Setter
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public Integer getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public Object getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }
}
