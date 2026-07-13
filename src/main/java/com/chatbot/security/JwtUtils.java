package com.chatbot.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Logger;

@Component
@Slf4j
public class JwtUtils {
    private static final Logger logger = Logger.getLogger(JwtUtils.class.getName());

    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.expiration}")
    private Long expiration;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            log.error("Failed to decode JWT secret string. Falling back to an ephemeral secure key.", e);
            this.secretKey = Jwts.SIG.HS256.key().build(); // Clean modern modern signature builder syntax
        }

    }
    public String generateJwtToken(String username,String email,String role){
        return Jwts
                .builder()
                .claim("username",username)
                .subject(email)
                .claim("roles",role)
                .signWith(secretKey)
                .issuedAt(Date.from(java.time.Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(expiration)))
                .compact();

    }
    public Claims extractClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String extractUsername(String token){
        logger.info("Extracting username from token ");
        return extractClaims(token).get("username",String.class);
    }
    public String extractRole(String token){
        logger.info("Extracting role from token ");
        return extractClaims(token).get("roles",String.class);
    }
    public String extractEmail(String token){
        logger.info("Extracting email from token ");
        return extractClaims(token).getSubject();
    }
    public boolean validateJwtToken(String token, UserDetails userDetails) {
        try{
            String email=extractEmail(token);
            logger.info("Validating token for email: " + email);
            return email.equals(userDetails.getUsername()) && !extractClaims(token).getExpiration().before(new Date());
        }catch(SignatureException se){
            logger.warning("Invalid signature :- "+se.getMessage());
        }catch (MalformedJwtException me){
            logger.warning("Malformed Jwt :- "+me.getMessage());
        }catch (ExpiredJwtException ee){
            logger.warning("Expired Jwt token :- " +ee.getMessage());
        }catch(IllegalArgumentException ie){
            logger.warning("Illegal argument :- "+ie.getMessage());
        }catch(UnsupportedJwtException ue){
            logger.warning("Unsupported jwt exception :- "+ue.getMessage());
        }
        return false;
    }

}
