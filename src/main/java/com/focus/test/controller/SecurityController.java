package com.focus.test.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.focus.test.dto.LoginRequest;
import com.focus.test.service.SecurityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;

@RestController
@Api(tags = "认证接口", description = "登录等")
public class SecurityController {
	protected final Logger logger = LoggerFactory.getLogger(SecurityController.class);
    
    @Autowired
    private SecurityService securityService;
    
    @PostMapping("/auth/login")
    @ApiOperation("登录")
    public Map<String, String> login(@Valid @RequestBody LoginRequest loginRequest) throws AuthenticationException {
    	return securityService.login(loginRequest);
    }
    
    
}
