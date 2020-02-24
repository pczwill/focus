package com.focus.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focus.test.entity.Test;


@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {

	
}