package com.spring.Secure.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private UserService userService;
    @Autowired
    private void setUserService(UserService userService) {
        this.userService = userService;
    }
    public String generateToken(String username) {
        Map<String,Object> claims = new HashMap<>();
        return
                Jwts
                        .builder()
                        .claims(claims)
                        .subject(username)
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis()+1000*60*60))
                        .signWith(getKey())
                        .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(
                System.getenv("SECRETKEY")
        );
        return Keys.hmacShaKeyFor(keyBytes);
    }



    public String extractUserName(String jwtToken) {
        Key key = getKey();
        Claims claims = extractAllClaims(jwtToken);
        return claims.getSubject();
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }


    public boolean validateToken(String jwtToken) {
        Claims claims = extractAllClaims(jwtToken);
        String username = claims.getSubject();
        UserDetails  user = userService.loadUserByUsername(username);
        if(user!=null
                && user.getUsername().equals(username)
                && claims.getExpiration()
                .after(new Date(System.currentTimeMillis()))
        ){
            return true;
        }
        return false;
    }
}
