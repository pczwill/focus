package com.focus.test.token.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.focus.test.entity.User;
import com.focus.test.token.TokenService;

@Component
public class RedisTokenServiceImpl implements TokenService {

    @Value("${security.token.secret}")
    private String secret;

    @Override
    public String getUsernameFromToken(String token) {
        String username;
        try {
            username = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    
    @Override
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                //.setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS))
                .claim("userId", user.getId())
                .claim("companyId", user.getCompanyId())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    
    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        return (getUsernameFromToken(token).equals(userDetails.getUsername()));
    }

    @Override
    public void refreshToken(String token) {
    }

	@Override
	public String getEmailFromToken(String token) {
		return "";
	}
}
