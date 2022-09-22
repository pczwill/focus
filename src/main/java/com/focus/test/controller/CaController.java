
package com.focus.test.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.focus.test.entity.Node;
import com.focus.test.repository.NodeRepository;
import com.focus.test.service.TestService;
@RestController
@RequestMapping("/ca")
public class CaController {
	
	@Autowired
	TestService testService;
	
	@Autowired
	NodeRepository nodeRepository;
	
	@GetMapping("/node") 
	public void searchCaNodes(HttpServletResponse response) throws Exception {

		List<String> str = new ArrayList<>();
		str.add("https://www.amazon.ca/s?i=hpc&bbn=6369682011&rh=n%3A6369682011%2Cp_n_availability%3A12035748011%2Cp_72%3A11192170011&dc&qid=1642260324&rnid=11192166011&ref=sr_pg_1");
		str.add("https://www.amazon.ca/s?i=hpc&bbn=6369740011&rh=n%3A6369740011%2Cp_n_availability%3A12035748011%2Cp_72%3A11192170011&dc&qid=1642260430&rnid=11192166011&ref=sr_nr_p_72_1");
		str.add("https://www.amazon.ca/s?i=hpc&bbn=6369959011&rh=n%3A6369959011%2Cp_n_availability%3A12035748011&dc&qid=1642260481&rnid=11192166011&ref=sr_nr_p_72_1");
		str.add("https://www.amazon.ca/s?i=hpc&bbn=6370518011&rh=n%3A6370518011%2Cp_n_availability%3A12035748011%2Cp_72%3A11192170011&dc&qid=1642260597&rnid=11192166011&ref=sr_nr_p_72_1");
		str.add("https://www.amazon.ca/s?i=hpc&bbn=6370792011&rh=n%3A6370792011%2Cp_n_availability%3A12035748011%2Cp_72%3A11192170011&dc&qid=1642260625&rnid=11192166011&ref=sr_nr_p_72_1");
		str.add("https://www.amazon.ca/s?i=hpc&bbn=6371353011&rh=n%3A6371353011%2Cp_n_availability%3A12035748011%2Cp_72%3A11192170011&dc&qid=1642260660&rnid=11192166011&ref=sr_nr_p_72_1");
		str.add("https://www.amazon.ca/s?i=hpc&bbn=15452193011&rh=n%3A15452193011%2Cp_n_availability%3A12035748011%2Cp_72%3A11192170011&dc&qid=1642260679&rnid=11192166011&ref=sr_nr_p_72_1");
		str.add("https://www.amazon.ca/s?i=hpc&bbn=15452195011&rh=n%3A15452195011%2Cp_n_availability%3A12035748011%2Cp_72%3A11192170011&dc&qid=1642260699&rnid=11192166011&ref=sr_nr_p_72_1");
		str.add("https://www.amazon.ca/s?i=beauty&bbn=6371179011&rh=n%3A6205177011%2Cn%3A6371179011%2Cp_n_availability%3A12035748011%2Cp_72%3A11192170011&dc&qid=1642260720&rnid=11192166011&ref=sr_nr_p_72_1");
		str.add("https://www.amazon.ca/s?i=beauty&bbn=6371153011&rh=n%3A6205177011%2Cn%3A6371153011%2Cp_n_availability%3A12035748011%2Cp_72%3A11192170011&dc&qid=1642260740&rnid=11192166011&ref=sr_nr_p_72_1");
		str.add("https://www.amazon.ca/s?i=hpc&bbn=6370001011&rh=n%3A6370001011%2Cp_n_availability%3A12035748011%2Cp_72%3A11192170011&dc&qid=1642260762&rnid=11192166011&ref=sr_nr_p_72_1");



		str.forEach(code -> {
			try {
				//String base = "https://www.amazon.com/b?node=";
				testService.searchUsNode(code, response, code);
			} catch (Exception e) {
				//throw new Exception("失败", e);
			}
		});

	}
	
	@GetMapping("ca/search") 
	public void searchCa(@RequestParam("code") String code1, HttpServletResponse response) throws Exception {

		List<String> str = new ArrayList<>();
		str.add("clitoris stimulator");
		

		str.forEach(code -> {
			try {
				String base = "https://www.amazon.ca/s/ref=nb_sb_noss?url=search-alias%3Dhpc&field-keywords=";
				testService.searchCa(code, response, base);
			} catch (Exception e) {
				//throw new Exception("失败", e);
			}
		});

	}
	
	
	/**
	 * 查看是否显示 品牌
	 * @throws Exception
	 */
	@GetMapping("/check") 
	public void checkCa() throws Exception {
		List<Node> nodes = nodeRepository.findByIdGreaterThanAndSix(860739, "");
		
		
		List<String> str = new ArrayList<>();
		
		


		try {
			testService.checkCa(nodes, "0116");
		}catch(Exception e) {
			throw new Exception("失败");
		}
	
	}
}
