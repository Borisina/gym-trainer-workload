package com.kolya.gym.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    private final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${secret-key}")
    private String SECRET_KEY;

    @Value("${audience-type}")
    private String AUDIENCE_TYPE;

    public Date extractExpiration(String token) throws SignatureException, ExpiredJwtException {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractAudience(String token) throws SignatureException, ExpiredJwtException {
        return extractClaim(token, Claims::getAudience);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws SignatureException, ExpiredJwtException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) throws SignatureException, ExpiredJwtException {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) throws SignatureException {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token) {
        try{
            String audience = extractAudience(token);
            return (audience!=null && audience.equals(AUDIENCE_TYPE) && !isTokenExpired(token));
        }catch (SignatureException e){
            return false;
        }
    }

    public String generateTokenForServices() {
        Map<String, Object> claims = new HashMap<>();
        String token = createTokenForServices(claims);
        return token;
    }

    private String createTokenForServices(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setAudience("service").setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }
}