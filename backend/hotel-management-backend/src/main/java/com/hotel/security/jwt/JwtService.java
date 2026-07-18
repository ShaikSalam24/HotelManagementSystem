package com.hotel.security.jwt;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

@Service
@RequiredArgsConstructor
public class JwtService {


    private final JwtProperties jwtProperties;

	private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }
	
	public String generateToken(UserDetails userDetails) {

	    return Jwts.builder()
	            .subject(userDetails.getUsername())
	            .issuedAt(new Date())
	            .expiration(new Date(
	                    System.currentTimeMillis()
	                    + jwtProperties.getExpiration()))
	            .signWith(getSigningKey())
	            .compact();
	}
	
	private Claims extractAllClaims(String token) {

	    return Jwts.parser()
	            .verifyWith(getSigningKey())
	            .build()
	            .parseSignedClaims(token)
	            .getPayload();
	}
	
	public String extractUsername(String token) {
	    return extractAllClaims(token).getSubject();
	}
	
	public Date extractExpiration(String token) {
	    return extractAllClaims(token).getExpiration();
	}
	
	private boolean isTokenExpired(String token) {
	    return extractExpiration(token).before(new Date());
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) {

	    String username = extractUsername(token);

	    return username.equals(userDetails.getUsername())
	            && !isTokenExpired(token);
	}
	
}
