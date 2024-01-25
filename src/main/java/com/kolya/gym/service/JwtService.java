package com.kolya.gym.service;

import com.kolya.gym.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${secret-key}")
    private String SECRET_KEY;

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
            return (audience!=null && audience.equals("service") && !isTokenExpired(token));
        }catch (SignatureException e){
            return false;
        }
    }
}