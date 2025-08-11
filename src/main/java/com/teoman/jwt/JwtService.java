package com.teoman.jwt;

//Token üretme ve çözme ve imza süresini kontrol edip tokenın geçerliliğini bildirir. http katmanına karışmaz.

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtService {

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                   .setSubject(userDetails.getUsername())
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*2))
                   .signWith(getKey(), SignatureAlgorithm.HS256)
                   .compact();
    }

    public Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T exportToken(String token, Function<Claims, T> claimsFunction) {
        final Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token).getBody();
        return claimsFunction.apply(claims);
    }

    public String getUsernameByToken(String token) {
        return exportToken(token, Claims::getSubject);
    }

    public boolean isTokenExpired(String token) {
        Date exp = exportToken(token, Claims::getExpiration);
        return new Date().after(exp);
    }


    public boolean isTokenValid(String token, UserDetails ud) {
        String username = getUsernameByToken(token);
        return username.equals(ud.getUsername()) && !isTokenExpired(token);
    }
}
