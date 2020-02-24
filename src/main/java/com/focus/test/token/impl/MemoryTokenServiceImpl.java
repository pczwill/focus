package com.focus.test.token.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.focus.test.entity.User;
import com.focus.test.service.UserService;
import com.focus.test.token.TokenService;

@Component
@Primary
public class MemoryTokenServiceImpl implements TokenService {
	protected final Logger logger = LoggerFactory.getLogger(MemoryTokenServiceImpl.class);

	//超时10分钟 暂时不限制
	private static Long VALIDITY_TIME_MS = 600000L;

    @Value("${security.token.secret}")
    private String secret;
    
    @Autowired
    private UserService userService;

    @Override
    public String getUsernameFromToken(String token) {
        String username;
        try {
            username =  Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody().getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                //.setExpiration(new Date(System.currentTimeMillis() + VALIDITY_TIME_MS))
                .claim("userId", userService.findByEmail(userDetails.getUsername()))
                //.claim("companyId", userService.getCompanyIdFromName((userDetails.getUsername())))
                .claim("role", userDetails.getAuthorities())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    
	public String getEmailFromToken(String token) {
    	String username;
        try {
            username =  String.valueOf(Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody().getSubject());
        } catch (Exception e) {
            username = null;
        }
        return username;
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
    
    
    public User getUserFromToken(String token) {
    	User user = new User();
        try {
        	Integer userId =  getUserIdFromToken(token);
        	user = userService.findById(userId);
        } catch (Exception e) {
        	user = null;
        }
        return user;
    }
    
    
    public Integer getUserIdFromToken(String token) {
    	Integer userId = null;
    	try {
    	userId  = (Integer)Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token).getBody().get("userId");
    	}catch (Exception e) {
    		logger.error("非法token");
    		userId = null;
        }
    	return userId;
    }
    
    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        return (getUsernameFromToken(token).equals(userDetails.getUsername()));
    }

    @Override
    public void refreshToken(String token) {
    }
}
