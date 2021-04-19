package com.focus.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focus.test.entity.Node;

@Repository
public interface NodeRepository extends JpaRepository<Node, Integer> {


}