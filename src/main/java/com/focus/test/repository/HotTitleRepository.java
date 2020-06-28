package com.focus.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.focus.test.entity.HotTitle;

@Repository
public interface HotTitleRepository extends JpaRepository<HotTitle, Integer> {

	int countByTitle(String text);

}