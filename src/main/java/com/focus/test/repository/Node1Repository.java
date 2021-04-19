package com.focus.test.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focus.test.entity.Node;
import com.focus.test.entity.Node1;

@Repository
public interface Node1Repository extends JpaRepository<Node1, Integer> {

	List<Node1> findByEight(String string);


}