/**
 * 
 */
package com.focus.test.controller;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Administrator
 *
 */
@Controller
public class PageController {

	@RequestMapping("/index")
	public String login() {
		
		Map<String, String> map = new HashedMap<String, String>();
		map.put("test", "test");
		map.entrySet();
		
		
		
		return "/index.html";
	}
}
