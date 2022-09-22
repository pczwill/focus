package com.focus.test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focus.test.entity.Node;

@Repository
public interface NodeRepository extends JpaRepository<Node, Integer> {

	/**
	 * @param string 
	 * @return
	 */
	List<Node> findByEight(String string);

	/**
	 * @param string
	 * @param string2
	 * @return
	 */
	List<Node> findByEightAndTen(String string, String string2);

	/**
	 * @param string
	 * @param string2
	 * @return
	 */
	List<Node> findByEightAndTenNot(String string, String string2);

	/**
	 * @param string
	 * @return
	 */
	List<Node> findByEightAndTenIsNull(String string);

	List<Node> findByIdGreaterThanAndSix(int i, String string);


}