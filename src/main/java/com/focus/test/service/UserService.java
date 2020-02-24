package com.focus.test.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focus.test.entity.User;
import com.focus.test.repository.UserRepository;

@Service
public class UserService  {

	protected final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	UserRepository userRepository;

	public User findByName(String username) {
		return userRepository.findByName(username);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User findById(Integer userId) {
		return userRepository.findById(userId).get();
	}


	
	
}
