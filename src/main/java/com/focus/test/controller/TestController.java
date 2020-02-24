package com.focus.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.focus.test.service.TestService;
@RestController
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	TestService testService;

	@GetMapping()
	public String test() {
		return "test";
	}
	
	@GetMapping("/tran") 
	public void tran() throws Exception{
		testService.tran();
	}
	
}
