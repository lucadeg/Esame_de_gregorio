package com.esame.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT Token Provider
 * Provider Token JWT
 * 
 * Handles JWT token creation, validation and parsing
 * Gestisce creazione, validazione e parsing dei token JWT
 */
@Component
public class JwtTokenProvider {
    
    @Value("${app.jwt.secret:mySecretKey}")
    private String jwtSecret;
    
    @Value("${app.jwt.expiration:86400000}") // 24 hours / 24 ore
    private int jwtExpirationInMs;
    
    @Value("${app.jwt.refresh-expiration:604800000}") // 7 days / 7 giorni
    private int jwtRefreshExpirationInMs;
    
    /**
     * Generate JWT token
     * Genera token JWT
     * 
     * @param authentication Spring Security authentication / Autenticazione Spring Security
     * @return JWT token / Token JWT
     */
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);
        
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * Generate JWT token from username
     * Genera token JWT da username
     * 
     * @param username Username / Nome utente
     * @return JWT token / Token JWT
     */
    public String generateTokenFromUsername(String username) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * Generate refresh token
     * Genera token di refresh
     * 
     * @param username Username / Nome utente
     * @return Refresh token / Token di refresh
     */
    public String generateRefreshToken(String username) {
        Date expiryDate = new Date(System.currentTimeMillis() + jwtRefreshExpirationInMs);
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * Get username from JWT token
     * Recupera username da token JWT
     * 
     * @param token JWT token / Token JWT
     * @return Username / Nome utente
     */
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.getSubject();
    }
    
    /**
     * Get expiration date from JWT token
     * Recupera data scadenza da token JWT
     * 
     * @param token JWT token / Token JWT
     * @return Expiration date / Data scadenza
     */
    public Date getExpirationDateFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.getExpiration();
    }
    
    /**
     * Validate JWT token
     * Valida token JWT
     * 
     * @param authToken JWT token / Token JWT
     * @return True if valid / True se valido
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Check if token is expired
     * Controlla se token è scaduto
     * 
     * @param token JWT token / Token JWT
     * @return True if expired / True se scaduto
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getExpirationDateFromJWT(token);
            return expiration.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }
    
    /**
     * Get signing key
     * Recupera chiave di firma
     * 
     * @return Secret key / Chiave segreta
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * Get token expiration time in milliseconds
     * Recupera tempo scadenza token in millisecondi
     * 
     * @return Expiration time / Tempo scadenza
     */
    public int getJwtExpirationInMs() {
        return jwtExpirationInMs;
    }
    
    /**
     * Get refresh token expiration time in milliseconds
     * Recupera tempo scadenza refresh token in millisecondi
     * 
     * @return Refresh expiration time / Tempo scadenza refresh
     */
    public int getJwtRefreshExpirationInMs() {
        return jwtRefreshExpirationInMs;
    }
}
