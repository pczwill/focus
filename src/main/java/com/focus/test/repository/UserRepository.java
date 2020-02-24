package com.focus.test.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.focus.test.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	public <T> List<T> findByOrderById(Class<T> clazz);

	public int countByEmail(String email);

	public User findByName(String username);

	public User findByEmail(String email);
}
