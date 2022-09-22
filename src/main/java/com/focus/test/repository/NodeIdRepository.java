package com.focus.test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focus.test.entity.NodeId;

@Repository
public interface NodeIdRepository extends JpaRepository<NodeId, Integer> {



}