package com.focus.test.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;

import com.focus.test.security.UserDetailsServiceImpl;
import com.focus.test.token.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthorizationTokenFilter extends OncePerRequestFilter {

    @Value("${security.token.header}")
    private String tokenHeader;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
    @Autowired
    private com.focus.test.service.UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    	String authToken = request.getHeader(tokenHeader);
        String username = this.tokenService.getUsernameFromToken(authToken);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (this.tokenService.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //User user = userService.findByEmail(username);
                //authentication.setDetails(user.getId());
                SecurityContextHolder.getContext().setAuthentication(authentication);
				/*
				 * String paramCmpanyId = request.getParameter("subCompanyId");
				 * 
				 * Integer companyId = paramCmpanyId == null ? 0 :
				 * Integer.valueOf(paramCmpanyId); if(companyId.equals(0)){ companyId =
				 * user.getCompanyId(); }else
				 * if(!companyService.checkSubCompanyId(user.getCompanyId(), companyId)){ throw
				 * new UnAuthorizedException("无权限查看!"); }
				 * RequestContextHolder.getRequestAttributes().setAttribute("userId",user.getId(
				 * ), 0); RequestContextHolder.getRequestAttributes().setAttribute("companyId",
				 * companyId, 0); String groupType = request.getParameter("groupType"); if
				 * (groupType == null || Integer.valueOf(groupType).equals(0)) {
				 * RequestContextHolder.getRequestAttributes().setAttribute("groupId",
				 * java.util.Arrays.asList(companyId), 0); } else { //查有之间有关联公司的数据
				 * RequestContextHolder.getRequestAttributes().setAttribute("groupId",
				 * companyService.getGroupCompanyIds(), 0); }
				 */
            }
        }
        chain.doFilter(request, response);
    }
}
