package com.homework.jwtTest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final long VALID_MILISECOND = 1000L * 60 * 60; // 1 시간
    private final CustomUserDetailsService customUserDetailsService;

    private String secretKey = "szsTestJwt";

    public JwtTokenProvider(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    private String getSecretKey(String secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    private String getUsername(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey(secretKey))
                .build()
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String jwtToken) {
        try {
            log.info("validate..");
            Jws<Claims>  claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey(secretKey))
                    .build()
                    .parseClaimsJws(jwtToken);
            log.info("{}",claims.getBody().getExpiration());
            return !claims.getBody().getExpiration().before(new Date());
        }catch(Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String jwtToken) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(getUsername(jwtToken));
        log.info("PASSWORD : {}",userDetails.getPassword());
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }


    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + VALID_MILISECOND))
                .signWith(SignatureAlgorithm.HS256, getSecretKey(secretKey))
                .compact();
    }
}
