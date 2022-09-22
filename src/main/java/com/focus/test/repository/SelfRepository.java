package com.focus.test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focus.test.entity.Self;

@Repository
public interface SelfRepository extends JpaRepository<Self, Integer> {



	List<Self> findByTenIsNullAndEightIsNotNull();

	List<Self> findByTenIsNullAndEightNot(String string);


}