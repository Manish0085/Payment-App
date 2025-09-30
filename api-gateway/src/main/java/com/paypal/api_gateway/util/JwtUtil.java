package com.paypal.api_gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class JwtUtil {

    private static final String SECRET = "q8vR$kL!mZ@3x9W^bN7eT#uF2rP&sYhC4pJdLzQwX%aG5oH8tK";


    public static Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public static Claims validateToken(String  token){
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



}
