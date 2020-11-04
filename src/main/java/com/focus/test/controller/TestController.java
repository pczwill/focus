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
			String base = "https://www.amazon.co.jp/s?k=%E3%82%AA%E3%83%8A%E3%83%9B+%E3%82%A6%E3%82%A9%E3%83%BC%E3%83%9E%E3%83%BC&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss_2";
			testService.search(code, response, base);
		}catch(Exception e) {
			throw new Exception("失败", e);
		}
		
	}
	

	@GetMapping("/search1") 
	public void search1(@RequestParam("code") String code, HttpServletResponse response) throws Exception {
		try {
			String base = "https://www.amazon.co.jp/s?k=%E4%B9%B3%E9%A6%96+%E3%83%90%E3%82%A4%E3%83%96&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss";
			testService.search(code, response, base);
		}catch(Exception e) {
			throw new Exception("失败", e);
		}
		
	}
	

	@GetMapping("/search2") 
	public void search2(@RequestParam("code") String code, HttpServletResponse response) throws Exception {
		try {
			String base = "https://www.amazon.co.jp/s?k=%E3%82%B3%E3%83%B3%E3%83%89%E3%83%BC%E3%83%A0&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss_1";
			testService.search(code, response, base);
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
