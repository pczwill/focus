package com.focus.test.token;

import org.springframework.security.core.userdetails.UserDetails;

import com.focus.test.entity.User;

public interface TokenService {

    String getUsernameFromToken(String token);

    String generateToken(UserDetails userDetails);
    
    String generateToken(User user);

    boolean validateToken(String token, UserDetails userDetails);

    void refreshToken(String token);
    
    String getEmailFromToken(String token);
}
