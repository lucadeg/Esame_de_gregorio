package com.esame.dto;

import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

/**
 * Subscription Data Transfer Object
 * DTO per Abbonamento
 * 
 * Represents subscription data for API communication
 * Rappresenta i dati dell'abbonamento per la comunicazione API
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionDTO {
    
    private Long userId;
    
    @NotNull(message = "Subscription type is required / Il tipo di abbonamento è obbligatorio")
    private String subscriptionType;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime subscriptionExpiresAt;
    
    private Boolean isActive;
    
    private Integer maxCourses;
    
    private Boolean hasAdvancedFeatures;
    
    private Long daysRemaining;
    
    private String displayName;
    
    private Double price;
    
    // Constructors / Costruttori
    public SubscriptionDTO() {}
    
    public SubscriptionDTO(String subscriptionType, LocalDateTime subscriptionExpiresAt) {
        this.subscriptionType = subscriptionType;
        this.subscriptionExpiresAt = subscriptionExpiresAt;
    }
    
    // Getters and Setters / Getter e Setter
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getSubscriptionType() {
        return subscriptionType;
    }
    
    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
    
    public LocalDateTime getSubscriptionExpiresAt() {
        return subscriptionExpiresAt;
    }
    
    public void setSubscriptionExpiresAt(LocalDateTime subscriptionExpiresAt) {
        this.subscriptionExpiresAt = subscriptionExpiresAt;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Integer getMaxCourses() {
        return maxCourses;
    }
    
    public void setMaxCourses(Integer maxCourses) {
        this.maxCourses = maxCourses;
    }
    
    public Boolean getHasAdvancedFeatures() {
        return hasAdvancedFeatures;
    }
    
    public void setHasAdvancedFeatures(Boolean hasAdvancedFeatures) {
        this.hasAdvancedFeatures = hasAdvancedFeatures;
    }
    
    public Long getDaysRemaining() {
        return daysRemaining;
    }
    
    public void setDaysRemaining(Long daysRemaining) {
        this.daysRemaining = daysRemaining;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    // Helper methods / Metodi helper
    public boolean isExpired() {
        return subscriptionExpiresAt != null && subscriptionExpiresAt.isBefore(LocalDateTime.now());
    }
    
    public boolean isExpiringSoon() {
        return subscriptionExpiresAt != null && 
               subscriptionExpiresAt.isBefore(LocalDateTime.now().plusDays(7));
    }
    
    public boolean isFree() {
        return "FREE".equals(subscriptionType);
    }
    
    public boolean isPaid() {
        return !isFree() && subscriptionType != null;
    }
}
