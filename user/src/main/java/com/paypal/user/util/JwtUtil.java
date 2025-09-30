package com.paypal.user.util;


import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {

    private static final String SECRET = "q8vR$kL!mZ@3x9W^bN7eT#uF2rP&sYhC4pJdLzQwX%aG5oH8tK";

    private Key getSignedKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }


    public String extractEmail(String token){
        return Jwts.parser()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token, String username){
        try{
            extractEmail(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }



    public String extractUsername(String token){
        return Jwts
                .parser()
                .setSigningKey(getSignedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String generateToken(Long userId, String email, String role){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email) // Still keeping email as subject for backward compatibility
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(getSignedKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String extractRole(String token){
        return (String) Jwts.parser()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }


}
