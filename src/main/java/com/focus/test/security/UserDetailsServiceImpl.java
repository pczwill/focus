package com.focus.test.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.focus.test.dto.LoginRequest;
import com.focus.test.entity.User;
import com.focus.test.repository.UserRepository;
import com.focus.test.service.UserService;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    
    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    
	@Override
	@Transactional(rollbackFor = Exception.class)
	public UserDetails loadUserByUsername(String username) {
		// 以email 为用户名
		//User user = null;
		// 暂时简单兼容多公司情况
		User user = userService.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			return new org.springframework.security.core.userdetails.User(
					username,
					user.getPassword(),
					AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN"));
		}
	}
    
    public User getUserByEmail(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
        } else {
             new org.springframework.security.core.userdetails.User(
            		user.getEmail(),
                    user.getPassword(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList(this.getRoles(user.getId()))
            );
        }
        return user;
    }
    
    public User getUserByRequest(LoginRequest loginRequest) throws UsernameNotFoundException {
    	
    	User user = userService.findByEmail(loginRequest.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", loginRequest.getUsername()));
        } else {
             new org.springframework.security.core.userdetails.User(
            		user.getEmail(),
                    user.getPassword(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN")
            );
        }
        return user;
    }
    
  
    
    
    private String getRoles(Integer userId){
        String roleNameStr = "";

        String sql =
            " SELECT " +
            "   b.role_name " +
            " FROM " +
            "   auth_user a," +
            "   auth_role b," +
            "   auth_user_role c " +
            " WHERE " +
            "   a.id = c.user_id " +
            " AND b.id = c.role_id" +
            " AND a.id = %d";
        sql = String.format(sql,userId);
        List<Map<String, Object>> roleNameList = jdbcTemplate.queryForList(sql);

        Iterator<Map<String, Object>> iter = roleNameList.iterator();
        while (iter.hasNext()){
            roleNameStr += "ROLE_" + iter.next().get("role_name") + ",";
        }

        return roleNameStr.trim();
    }
}
