package com.focus.test.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	
	@GetMapping("/upc") 
	public void upc() throws Exception{
		testService.upc();
	}
	
	@GetMapping("/comment") 
	public void chrome(@RequestParam("code") String code, HttpServletResponse response) throws Exception {
		try {
			testService.chrome(code, response);
		}catch(Exception e) {
			throw new Exception("失败");
		}
		
	}
	
	
	@GetMapping("us/comment") 
	public void chromeUs(@RequestParam("code") String code, HttpServletResponse response) throws Exception {
		try {
			testService.chromeUs(code, response);
		}catch(Exception e) {
			throw new Exception("失败");
		}
		
	}
	
	
	@GetMapping("/search") 
	public void search(@RequestParam("code") String code, HttpServletResponse response) throws Exception {
		try {
			testService.search(code, response);
		}catch(Exception e) {
			throw new Exception("失败", e);
		}
		
	}
	
	@GetMapping("/hot") 
	public void hot(HttpServletResponse response) throws Exception {
		try {
			testService.hot(response);
		}catch(Exception e) {
			throw new Exception("失败");
		}
		
	}
	
}
