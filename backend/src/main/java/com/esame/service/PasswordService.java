package com.esame.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Password Service
 * Servizio Password
 * 
 * Handles password encoding and validation
 * Gestisce codifica e validazione password
 */
@Service
public class PasswordService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Encode password
     * Codifica password
     * 
     * @param rawPassword Raw password / Password in chiaro
     * @return Encoded password / Password codificata
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    /**
     * Check if raw password matches encoded password
     * Controlla se password in chiaro corrisponde a password codificata
     * 
     * @param rawPassword Raw password / Password in chiaro
     * @param encodedPassword Encoded password / Password codificata
     * @return True if matches / True se corrisponde
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    
    /**
     * Validate password strength
     * Valida forza password
     * 
     * @param password Password to validate / Password da validare
     * @return True if password is strong enough / True se password è abbastanza forte
     */
    public boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpperCase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowerCase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0);
        
        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }
    
    /**
     * Get password strength level
     * Recupera livello forza password
     * 
     * @param password Password to analyze / Password da analizzare
     * @return Password strength level (1-5) / Livello forza password (1-5)
     */
    public int getPasswordStrength(String password) {
        if (password == null || password.length() < 6) {
            return 1;
        }
        
        int strength = 1;
        
        if (password.length() >= 8) strength++;
        if (password.chars().anyMatch(Character::isUpperCase)) strength++;
        if (password.chars().anyMatch(Character::isLowerCase)) strength++;
        if (password.chars().anyMatch(Character::isDigit)) strength++;
        if (password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0)) strength++;
        
        return Math.min(strength, 5);
    }
    
    /**
     * Generate random password
     * Genera password casuale
     * 
     * @param length Password length / Lunghezza password
     * @return Generated password / Password generata
     */
    public String generateRandomPassword(int length) {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        String allChars = upperCase + lowerCase + digits + specialChars;
        
        StringBuilder password = new StringBuilder();
        
        // Ensure at least one character from each category / Assicura almeno un carattere da ogni categoria
        password.append(upperCase.charAt((int) (Math.random() * upperCase.length())));
        password.append(lowerCase.charAt((int) (Math.random() * lowerCase.length())));
        password.append(digits.charAt((int) (Math.random() * digits.length())));
        password.append(specialChars.charAt((int) (Math.random() * specialChars.length())));
        
        // Fill the rest randomly / Riempi il resto casualmente
        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt((int) (Math.random() * allChars.length())));
        }
        
        // Shuffle the password / Mescola la password
        String result = password.toString();
        char[] chars = result.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int j = (int) (Math.random() * chars.length);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        
        return new String(chars);
    }
}
