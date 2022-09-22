
package com.focus.test.controller;

import java.util.ArrayList;
import java.util.List;

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
	
	@GetMapping("/isbn") 
	public void isbn() throws Exception{
		testService.isbn();
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
	
	
	
	
	@GetMapping("us/node") 
	public void searchUsNodes(@RequestParam("code") String code1, HttpServletResponse response) throws Exception {

		List<String> str = new ArrayList<>();
		List<String> str1 = new ArrayList<>();
		List<String> str2 = new ArrayList<>();
		str2.add("199641031");
		
		str.add("15356141");
		str.add("3760991");



		str2.forEach(code -> {
			try {
				String base = "https://www.amazon.fr/b?node=%s&ref=dp_bc_3";
				base = String.format(base, code);
				//String base = "https://www.amazon.com/b?node=";
				testService.searchUsNode(code, response, base);
			} catch (Exception e) {
				//throw new Exception("失败", e);
			}
		});

	}
	
	
	
	
	@GetMapping("fr/node") 
	public void searchFrNodes(@RequestParam("code") String code1, HttpServletResponse response) throws Exception {

		List<String> str = new ArrayList<>();
		

		try {
			// String base = "https://www.amazon.com/b?node=";
			testService.searchFrNode(str, response);
		} catch (Exception e) {
			// throw new Exception("失败", e);
		}

	}
	
	@GetMapping("uk/node") 
	public void searchUkNodes(@RequestParam("code") String code1, HttpServletResponse response) throws Exception {

		List<String> str = new ArrayList<>();
		
		try {
			// String base = "https://www.amazon.com/b?node=";
			testService.searchUkNode(str, response);
		} catch (Exception e) {
			// throw new Exception("失败", e);
		}

	}
	
	@GetMapping("fr/node1") 
	public void searchFrNodes1(@RequestParam("code") String code1, HttpServletResponse response) throws Exception {
		List<String> str2 = new ArrayList<>();
		// 318 灯
		List<String> str = new ArrayList<>();

		try {

			// String base = "https://www.amazon.com/b?node=";
			testService.searchFrNode(str, response);
		} catch (Exception e) {
			// throw new Exception("失败", e);
		}
	}
	
	@GetMapping("jp/node") 
	public void searchJpNodes1(HttpServletResponse response) throws Exception {
		// 318 灯
		List<String> str = new ArrayList<>();
		str.add("https://www.amazon.co.jp/s?i=hpc&bbn=2132605051&rh=n%3A2132605051%2Cp_n_availability%3A2227307051%2Cp_72%3A82411051&dc&qid=1641637073&rnid=82409051&ref=sr_nr_p_72_1");
		str.add("https://www.amazon.co.jp/s?i=hpc&bbn=2132605051&rh=n%3A2132605051%2Cp_n_availability%3A2227307051%2Cp_72%3A82411051&dc&qid=1641637073&rnid=82409051&ref=sr_nr_p_72_1");

		str.add("https://www.amazon.co.jp/s?i=hpc&bbn=2132590051&rh=n%3A2132590051%2Cp_n_availability%3A2227307051%2Cp_72%3A82411051&dc&qid=1641637200&rnid=82409051&ref=sr_nr_p_72_1");

		try {

			// String base = "https://www.amazon.com/b?node=";
			testService.searchJpNode(str, response);
		} catch (Exception e) {
			// throw new Exception("失败", e);
		}
	}
	
	
	
	/**
	 * 查看是否显示
	 * @throws Exception
	 */
	@GetMapping("/check/jp") 
	public void checkjp() throws Exception {
		List<String> str = new ArrayList<>();
		str.add("B076136N7N"); //成人

        if(str != null) {
        	str.add("B07KXN79YD");
        	str.add("B079SJ5GHD");
        	str.add("B079RR12QW");
        	str.add("B07KWCRZZJ");
        	str.add("B079QHVXS6");
        	str.add("B079Q7NT3C");
        	str.add("B07KSW42Q5");
        	str.add("B079PQWVKH");
        	str.add("B079NSY66G");
        	str.add("B079NQSM93");
        	str.add("B079N2Y763");
        	str.add("B079MBWZS9");
        	str.add("B079M4HPJM");
        	str.add("B079LGZJ7P");
        	str.add("B07KQGDLKX");
        	str.add("B079L5SWXN");
        	str.add("B079K4SCD9");
        	str.add("B079JYZ8GH");
        	str.add("B079JFP1CR");
        	str.add("B079HGZ78F");
        	str.add("B079H9FBPV");
        	str.add("B079H6MR67");
        	str.add("B07KN2RK1F");
        	str.add("B079H41M71");
        	str.add("B079GYGDC7");
        	str.add("B079DX8DD8");
        	str.add("B079CBZ458");
        	str.add("B079BY2KQM");
        	str.add("B07995R5PP");
        	str.add("B07989SVGF");
        	str.add("B07KFBMLRD");
        	str.add("B0797V6V12");
        	str.add("B0796W3S9J");
        	str.add("B07KCGLZPD");
        	str.add("B07KCCT17T");
        	str.add("B07KCCC362");
        	str.add("B0794XLBLJ");
        	str.add("B0794L8H6N");
        	str.add("B07943LN79");
        	str.add("B07K8VV5FC");
        	str.add("B0793NFPZY");
        	str.add("B0792YDZZX");
        	str.add("B0792XL591");
        	str.add("B0792VVLDL");
        	str.add("B0792SNHC4");
        	str.add("B07K7CH3LC");
        	str.add("B07K6Z3FYN");
        	str.add("B0792M3L63");
        	str.add("B0792L59XM");
        	str.add("B07K6MRY77");
        	str.add("B0792K9572");
        	str.add("B07K6LTF21");
        	str.add("B07K6LS1ZV");
        	str.add("B07K5H93VZ");
        	str.add("B078YQD69Z");
        	str.add("B078YF7ZBS");
        	str.add("B07K2DCL6R");
        	str.add("B078Y8G8ZC");
        	str.add("B078Y72CVB");
        	str.add("B07JZKDD8X");
        	str.add("B07JZJPC4T");
        	str.add("B078XLXGY1");
        	str.add("B07JZ748T7");
        	str.add("B07JYK9TZL");
        	str.add("B07JY9JMXG");
        	str.add("B07JY6S2R5");
        	str.add("B07JVNPR5W");
        	str.add("B07JV8P5SL");
        	str.add("B078S74283");
        	str.add("B078S6NWKJ");
        	str.add("B07JQPJJ47");
        	str.add("B07JQDG7GC");
        	str.add("B07JP2VTVY");
        	str.add("B078PVYGH1");
        	str.add("B078PT9BQV");
        	str.add("B078MH9MCJ");
        	str.add("B078LYH1LC");
        	str.add("B078J7X7C2");
        	str.add("B07JHQKQ8B");
        	str.add("B078GSCCY9");
        	str.add("B07JH6W74R");
        	str.add("B078G1V4S5");
        	str.add("B07JGWRPMP");
        	str.add("B07JF8XC8C");
        	str.add("B07885LHPD");
        	str.add("B0787K2YNZ");
        	str.add("B0787GLJ8C");
        	str.add("B07JD6S94L");
        	str.add("B07JCXK3HP");
        	str.add("B0786B9MZF");
        	str.add("B0785XLG5C");
        	str.add("B07JCCHCLG");
        	str.add("B0785FHV8C");
        	str.add("B07JBNP98Y");
        	str.add("B0784HHNG8");
        	str.add("B0784CV6FP");
        	str.add("B0784CP9PS");
        	str.add("B0784885BX");
        	str.add("B0781PGMG2");
        	str.add("B07J5QVWRG");
        	str.add("B077ZYL315");
        	str.add("B077ZF84M4");
        	str.add("B07J4XKWWD");
        	str.add("B077YPKQFF");
        	str.add("B07J4PG1FG");
        	str.add("B07J2ZG1C5");
        	str.add("B077XPX9WH");
        	str.add("B07J1XTMYB");
        	str.add("B077XFJ275");
        	str.add("B07J1BWL8W");
        	str.add("B07J1BSTZN");
        	str.add("B07HZJ1JX8");
        	str.add("B07HZ5NF45");
        	str.add("B07HZ5BS56");
        	str.add("B077TTNVCG");
        	str.add("B07HWZB38J");
        	str.add("B077SRQVYY");
        	str.add("B077SKKV9Z");
        	str.add("B077SGZGB4");
        	str.add("B07HWPJJR7");
        	str.add("B07HWKDJMQ");
        	str.add("B077RTX44N");
        	str.add("B077Q61RPB");
        	str.add("B077Q4SKXZ");
        	str.add("B077PWK1D8");
        	str.add("B077PT98ZR");
        	str.add("B077PS59DL");
        	str.add("B07HRQ75Y9");
        	str.add("B07HRPHB2W");
        	str.add("B07HRKJZ1J");
        	str.add("B07HQN74CX");
        	str.add("B07HPG2T5M");
        	str.add("B077LW7H78");
        	str.add("B07HPCTWLT");
        	str.add("B07HPCPZJ3");
        	str.add("B07HPBKDNZ");
        	str.add("B077L369LG");
        	str.add("B07HPBHKBV");
        	str.add("B07HPBBVQW");
        	str.add("B07HPB2R4Q");
        	str.add("B077KYQLC3");
        	str.add("B07HP9B58D");
        	str.add("B077K8N88Q");
        	str.add("B07HNCGHD8");
        	str.add("B07HN2JDZQ");
        	str.add("B077GPLQGF");
        	str.add("B07HMWPGHN");
        	str.add("B07HMKVJ4D");
        	str.add("B077G69FJT");
        	str.add("B07HM6WF4Y");
        	str.add("B07HM6TPWS");
        	str.add("B077F83D49");
        	str.add("B07HLQJDFK");
        	str.add("B07HKQMY4B");
        	str.add("B077CT59G1");
        	str.add("B07HJ77ZQJ");
        	str.add("B07HHWXC31");
        	str.add("B077994FGG");
        	str.add("B0779922JQ");
        	str.add("B0778QX35F");
        	str.add("B07HDN5XM7");
        	str.add("B0777V1K6B");
        	str.add("B0777MVPTL");
        	str.add("B07HB76WVN");
        	str.add("B07H9RPX65");
        	str.add("B0776BMYH6");
        	str.add("B07H93XYSR");
        	str.add("B07H8XB939");
        	str.add("B07H8KKCQ1");
        	str.add("B0775P2YTX");
        	str.add("B07H8BTBMM");
        	str.add("B0774JZ4DB");
        	str.add("B07H6ZHWHL");
        	str.add("B0773LKDBR");
        	str.add("B0773DG3LX");
        	str.add("B07739N94G");
        	str.add("B0771VZNN9");
        	str.add("B07H6MKMV3");
        	str.add("B07H6MKM1S");
        	str.add("B0771VM84L");
        	str.add("B07H5LVZRS");
        	str.add("B07H5LKNKY");
        	str.add("B07H5JNRDP");
        	str.add("B0771MKJJV");
        	str.add("B076YMJ8MJ");
        	str.add("B076X3X2Z7");
        	str.add("B076X2S6W5");
        	str.add("B076WZH1Z8");
        	str.add("B076WLZRWN");
        	str.add("B076WDMV1C");
        	str.add("B076W3KJZD");
        	str.add("B07GZLQLQG");
        	str.add("B07GXSH2J3");
        	str.add("B076S17LWD");
        	str.add("B07GV7JWHB");
        	str.add("B076PR8LS4");
        	str.add("B07GTYXNRS");
        	str.add("B076PPS9GN");
        	str.add("B076NPFLNH");
        	str.add("B076MKYBWB");
        	str.add("B07GPTTTYD");
        	str.add("B076M1BRH2");
        	str.add("B076M11B6F");
        	str.add("B07GPC5L1W");
        	str.add("B07GP78VYV");
        	str.add("B07GNC3RFB");
        	str.add("B076KSLTRL");
        	str.add("B076KK78PT");
        	str.add("B07GKMLM4Y");
        	str.add("B07GH6T7JP");
        	str.add("B07GGTTV1L");
        	str.add("B07GGFXKJ2");
        	str.add("B076H7BCLW");
        	str.add("B07GDHVPL5");
        	str.add("B076GL1SG9");
        	str.add("B07GB9BQR1");
        	str.add("B07G9BHSZ3");
        	str.add("B07G94NXDP");
        	str.add("B076F1Y4HF");
        	str.add("B07G84L4PF");
        	str.add("B07G75LJ8S");
        	str.add("B076D5VC5K");
        	str.add("B076BTQD2J");
        	str.add("B07G2JLYDX");
        	str.add("B07G247V7Y");
        	str.add("B07FZG3VH6");
        	str.add("B0768PGDMF");
        	str.add("B0768P9BPL");
        	str.add("B07FYSKZ28");
        	str.add("B07FYMGXRS");
        	str.add("B07FY8LL8D");
        	str.add("B076837RQX");
        	str.add("B07681TV6S");
        	str.add("B07FXPZNPM");
        	str.add("B0767YJRNM");
        	str.add("B07FXPTM7P");
        	str.add("B07FWFW1PF");
        	str.add("B07FVYH433");
        	str.add("B07FVVTJ1C");
        	str.add("B07FVMLNQJ");
        	str.add("B0766DZ367");
        	str.add("B07FVFMZC6");
        	str.add("B0765XQQXR");
        	str.add("B07FSLG82H");
        	str.add("B07FQ71RK7");
        	str.add("B07FQ522B9");
        	str.add("B07FQ459TC");
        	str.add("B0764H2C65");
        	str.add("B0764GZCT6");
        	str.add("B0764GDSSM");
        	str.add("B0764G1C85");
        	str.add("B07FPGW68S");
        	str.add("B07FPFL9J4");
        	str.add("B07FNW6MQ8");
        	str.add("B07FNHMGNC");
        	str.add("B07FNF6GJD");
        	str.add("B07FN9T1KQ");
        	str.add("B0762C6G3F");
        	str.add("B07FN773MS");
        	str.add("B0761YXS9P");
        	str.add("B07FL8BKWD");
        	str.add("B07FL8BFGL");
        	str.add("B07FKJVV38");
        	str.add("B0761SMNT5");
        	str.add("B07FJY5HQC");
        	str.add("B07FGV9Q57");
        	str.add("B07618N6H3");
        	str.add("B07614G5N8");
        	str.add("B07614F4JS");
        	str.add("B07613XG8X");
        	str.add("B07FDSF9WQ");
        	str.add("B07FDPQTF1");
        	str.add("B07FDNFDNJ");
        	str.add("B07FDM7346");
        	str.add("B07FDHLXG9");
        	str.add("B07FCQCZCC");
        	str.add("B07F8VJTS8");
        	str.add("B07F7JRBW6");
        	str.add("B07F6PR88Z");
        	str.add("B07F6GXH83");
        	str.add("B07F5YVKTK");
        	str.add("B07F5YF5RN");
        	str.add("B075YFT3QD");
        	str.add("B07F5DXYDL");
        	str.add("B07F3HBFNN");
        	str.add("B07F3GD7R1");
        	str.add("B07F356HQX");
        	str.add("B07F2NJ3GR");
        	str.add("B07F1S4BVT");
        	str.add("B075WPJGM3");
        	str.add("B07F1DHW5L");
        	str.add("B07DZZD1FG");
        	str.add("B07DY8Z8FL");
        	str.add("B075T97WYK");
        	str.add("B07DY1P3DQ");
        	str.add("B075T3TVGD");
        	str.add("B075SM66F7");
        	str.add("B07DXDBWFY");
        	str.add("B07DX4F5V3");
        	str.add("B07DX3T51J");
        	str.add("B075S9BM58");
        	str.add("B075S7TMS2");
        	str.add("B07DWZPXY3");
        	str.add("B07DWXYBPH");
        	str.add("B075RV345X");
        	str.add("B075RQF6DP");
        	str.add("B07DWXDKVT");
        	str.add("B075RPBK3N");
        	str.add("B07DWVBJZX");
        	str.add("B075RK92VT");
        	str.add("B075RBW1TC");
        	str.add("B075RBMTB4");
        	str.add("B075RBCCTP");
        	str.add("B075R8X24C");
        	str.add("B075R679G3");
        	str.add("B07DW3BWN7");
        	str.add("B075QLBTK6");
        	str.add("B07DVT1ZH4");
        	str.add("B07DVQD9NV");
        	str.add("B075PSCR28");
        	str.add("B075N5J4DS");
        	str.add("B075MPNL1J");
        	str.add("B075MKNGSN");
        	str.add("B075MHLMQQ");
        	str.add("B075M9X1NX");
        	str.add("B07DR8K7SJ");
        	str.add("B075M3XM56");
        	str.add("B075LRFW64");
        	str.add("B075LQJMVV");
        	str.add("B075LP12W1");
        	str.add("B07DQ3DRND");
        	str.add("B075LNBT5D");
        	str.add("B075LMT38V");
        	str.add("B075LLQDBM");
        	str.add("B075LL8GJW");
        	str.add("B07DP6LK2G");
        	str.add("B075KLX9LD");
        	str.add("B07DNWCH6C");
        	str.add("B075KBCCGD");
        	str.add("B075JLZ69N");
        	str.add("B075JLTLVK");
        	str.add("B07DMZWYSV");
        	str.add("B075HKBM7F");
        	str.add("B07DMFG3ZP");
        	str.add("B07DMF9WYS");
        	str.add("B07DMB3VMV");
        	str.add("B075GJ73C5");
        	str.add("B07DM54BST");
        	str.add("B075FYNRW2");
        	str.add("B075FY9XQT");
        	str.add("B075FVDZQW");
        	str.add("B07DLC41SP");
        	str.add("B07DL9QX37");
        	str.add("B075FNWJRV");
        	str.add("B075FNCSN4");
        	str.add("B07DKF685R");
        	str.add("B075DHPZSX");
        	str.add("B075DHH6TT");
        	str.add("B075DG92NY");
        	str.add("B075DFYZ9W");
        	str.add("B075DB9P4K");
        	str.add("B075DB9HVD");
        	str.add("B07DJ94W72");
        	str.add("B07DHYBSD6");
        	str.add("B075CWZZC4");
        	str.add("B07DHS62BJ");
        	str.add("B075CSR8BM");
        	str.add("B075CCBGYN");
        	str.add("B075CBDZJ8");
        	str.add("B075CBBT85");
        	str.add("B075C4NL16");
        	str.add("B0759VM6CJ");
        	str.add("B0758DZTTD");
        	str.add("B0758DB22W");
        	str.add("B0758DB1L6");
        	str.add("B0757P3JWR");
        	str.add("B0757NVF8V");
        	str.add("B075765PP9");
        	str.add("B07575YDV7");
        	str.add("B0756Q1V1Y");
        	str.add("B07DB69FCR");
        	str.add("B07DB1CM47");
        	str.add("B07D9PJM5M");
        	str.add("B07D9H34T2");
        	str.add("B07568W6H9");
        	str.add("B07D9BSMMT");
        	str.add("B07D94GK42");
        	str.add("B07543CBZ2");
        	str.add("B07D7XQSPG");
        	str.add("B07D7SZZBY");
        	str.add("B07D7D2FCQ");
        	str.add("B0752Q8BF4");
        	str.add("B0752PC5QM");
        	str.add("B0751LB83D");
        	str.add("B07D6RDGK8");
        	str.add("B07D6MNJRV");
        	str.add("B07D6LJCR9");
        	str.add("B07D6HLNB5");
        	str.add("B074YBHHQY");
        	str.add("B074X9JGZR");
        	str.add("B07D5F41SY");
        	str.add("B07D42RZVT");
        	str.add("B07D3ZX35J");
        	str.add("B074V3CWT4");
        	str.add("B074V2DNNZ");
        	str.add("B074TGQLGW");
        	str.add("B07D3CG4MG");
        	str.add("B07D395945");
        	str.add("B07D38H4QX");
        	str.add("B074SDNVLZ");
        	str.add("B07D363Y3W");
        	str.add("B07D31LH6B");
        	str.add("B074QMNCZL");
        	str.add("B07D2M3T16");
        	str.add("B074QKSQXK");
        	str.add("B07D2FH2YX");
        	str.add("B074PV7CQH");
        	str.add("B07D2FG1MK");
        	str.add("B07D2D65CW");
        	str.add("B074PRTSHY");
        	str.add("B074PD15S5");
        	str.add("B074P5QP3D");
        	str.add("B074NBHHV2");
        	str.add("B07D1MPFNH");
        	str.add("B074NBDKKH");
        	str.add("B074N6T48D");
        	str.add("B07CZ3V127");
        	str.add("B07CYN8TC9");
        	str.add("B07CX8Q987");
        	str.add("B074J7MSRD");
        	str.add("B074HLN2NC");
        	str.add("B074HDC8N7");
        	str.add("B074H2BH2J");
        	str.add("B074F18PKB");
        	str.add("B074DT3TM2");
        	str.add("B07CW8H634");
        	str.add("B07CVFSY1X");
        	str.add("B07CV874PR");
        	str.add("B07CV4PN27");
        	str.add("B0749K6S5B");
        	str.add("B0749JV6GD");
        	str.add("B0748MF4LV");
        	str.add("B07CTRRKDX");
        	str.add("B0748FCFLG");
        	str.add("B07481NMPG");
        	str.add("B07CRDB9BH");
        	str.add("B07CQMHW36");
        	str.add("B07CQK8M4D");
        	str.add("B0744N4RQ9");
        	str.add("B0744F6FB3");
        	str.add("B0744CSXBN");
        	str.add("B07CQ1QYM8");
        	str.add("B0743MMGSF");
        	str.add("B074169BJD");
        	str.add("B073ZG6J9N");
        	str.add("B073YDCR79");
        	str.add("B073X7ZS1M");
        	str.add("B073WW2VVP");
        	str.add("B073WSZ847");
        	str.add("B073WD3TJ8");
        	str.add("B073VSCFL2");
        	str.add("B073VR94NX");
        	str.add("B073TYH4XQ");
        	str.add("B073TXTZ51");
        	str.add("B073TWDVKZ");
        	str.add("B073T758RV");
        	str.add("B073RVR849");
        	str.add("B073RF8QX1");
        	str.add("B073QPSDMV");
        	str.add("B073QPHVJH");
        	str.add("B073QP86R2");
        	str.add("B073Q9MJBR");
        	str.add("B073Q3PF39");
        	str.add("B073LW8J1B");
        	str.add("B073GHPJS8");
        	str.add("B073FXF6TL");
        	str.add("B073FVT83F");
        	str.add("B073FVRWB8");
        	str.add("B073DSR78S");
        	str.add("B073CY9496");
        	str.add("B07354LF5T");
        	str.add("B0731DFJ9P");
        	str.add("B07217VVD2");
        	str.add("B071ZSHSDB");
        	str.add("B071ZG3TFK");
        	str.add("B072T5HT43");
        	str.add("B071Z7ZL6R");
        	str.add("B071Z5TZLD");
        	str.add("B072R1N85D");
        	str.add("B071Y4FBDZ");
        	str.add("B072MXDRJR");
        	str.add("B071X9DM2N");
        	str.add("B072MG5NLR");
        	str.add("B072LT62SQ");
        	str.add("B071VX4KKR");
        	str.add("B072L8872F");
        	str.add("B071VTJX97");
        	str.add("B072KKCXR8");
        	str.add("B071V6Z19P");
        	str.add("B072K592M6");
        	str.add("B072JW3D17");
        	str.add("B071RKKQ36");
        	str.add("B071PB73QP");
        	str.add("B071P627CJ");
        	str.add("B072F76RTK");
        	str.add("B072F5F8HW");
        	str.add("B071NVMGBK");
        	str.add("B0728DJJ4P");
        	str.add("B07289R274");
        	str.add("B071KWGRTC");
        	str.add("B071JNLNPB");
        	str.add("B07261WVJ6");
        	str.add("B0725YS82F");
        	str.add("B07252QHMX");
        	str.add("B071HW96DN");
        	str.add("B071HHP7LG");
        	str.add("B071HF5L4N");
        	str.add("B072351TYS");
        	str.add("B0722L6Q1N");
        	str.add("B071H71R9L");
        	str.add("B071GR7DRX");
        	str.add("B01A3DE5VS");
        	str.add("B01A1E4CLC");
        	str.add("B01A0UHARA");
        	str.add("B019ZS0QFG");
        	str.add("B019ZKP0LE");
        	str.add("B071G1149W");
        	str.add("B019ULIF9C");
        	str.add("B019Q68NWU");
        	str.add("B071CMH1QV");
        	str.add("B0719BJCLP");
        	str.add("B019CTJOOC");
        	str.add("B0199G2GXE");
        	str.add("B019842HTA");
        	str.add("B0190OQKA0");
        	str.add("B0714M3Y2Y");
        	str.add("B018ZVV2I4");
        	str.add("B0714GRZKY");
        	str.add("B0714D3HJ1");
        	str.add("B018W6QGUG");
        	str.add("B07145HVFP");
        	str.add("B0713SJVTB");
        	str.add("B018VWGF3Y");
        	str.add("B018T3NNGC");
        	str.add("B018SU8C3U");
        	str.add("B018QPF6C2");
        	str.add("B018OS9L8G");
        	str.add("B018MJKKD2");
        	str.add("B06ZZNC3QZ");
        	str.add("B018KW0H9S");
        	str.add("B06ZZB3J86");
        	str.add("B018IW1G6I");
        	str.add("B06ZYBG5FJ");
        	str.add("B06Y6M6GXT");
        	str.add("B06Y6JMW2H");
        	str.add("B06Y5YCJ4G");
        	str.add("B018AWWPMA");
        	str.add("B06Y5K5PXT");
        	str.add("B06Y5FC1RQ");
        	str.add("B0189B60X2");
        	str.add("B0188MS5IA");
        	str.add("B0188MS5EO");
        	str.add("B0188MS5BC");
        	str.add("B06Y4D1ZLP");
        	str.add("B06Y3ZLQXL");
        	str.add("B01873GK00");
        	str.add("B01867ULGQ");
        	str.add("B018672OSE");
        	str.add("B0182XRV1C");
        	str.add("B0182X6XOI");
        	str.add("B0182L60KM");
        	str.add("B0182L60AM");
        	str.add("B0182L5X9Q");
        	str.add("B06Y2LQY28");
        	str.add("B0182FRHTQ");
        	str.add("B0182FPDCY");
        	str.add("B0182ESYLW");
        	str.add("B0182E16BM");
        	str.add("B01827EGUW");
        	str.add("B06Y1N5BMM");
        	str.add("B017W0PYDI");
        	str.add("B06XY7473P");
        	str.add("B06XY4D6D2");
        	str.add("B06XXFSM3P");
        	str.add("B017VXJU4A");
        	str.add("B017VXEZPO");
        	str.add("B017VXE27A");
        	str.add("B017VXCCWW");
        	str.add("B06XWL5XSC");
        	str.add("B06XVZCCQF");
        	str.add("B06XTXXG75");
        	str.add("B06XTXDPR9");
        	str.add("B06XT6TSBW");
        	str.add("B017S3LX8O");
        	str.add("B017S1HIS0");
        	str.add("B06XSDM1YH");
        	str.add("B017KSCQWY");
        	str.add("B017KSB9QI");
        	str.add("B06XQH1LLD");
        	str.add("B017K6OXLI");
        	str.add("B06XQ3H387");
        	str.add("B017GJFBLA");
        	str.add("B06XPPHV3Z");
        	str.add("B01786QM1Y");
        	str.add("B06XKR2BLW");
        	str.add("B0173SI22O");
        	str.add("B0173JC616");
        	str.add("B0170R6NPG");
        	str.add("B016Z47GTW");
        	str.add("B06XHF1ZV2");
        	str.add("B06XH77T3V");
        	str.add("B06XH45H8L");
        	str.add("B016R6DY6M");
        	str.add("B016QOQ55W");
        	str.add("B06XDCCYXF");
        	str.add("B016MJJFWQ");
        	str.add("B06XCWTVJT");
        	str.add("B016KUA55S");
        	str.add("B016K6ESNC");
        	str.add("B016INRZNW");
        	str.add("B016IMU0M6");
        	str.add("B06XCP1T2Z");
        	str.add("B06XCKGL28");
        	str.add("B06XBNM212");
        	str.add("B016A7SBQQ");
        	str.add("B01678Y3DS");
        	str.add("B06X9FFQBF");
        	str.add("B06X9CX1L9");
        	str.add("B06X3TZDQD");
        	str.add("B06WWP11BF");
        	str.add("B015Z9TFDI");
        	str.add("B06WWNQXPW");
        	str.add("B015YKRBD4");
        	str.add("B015Y1IQQY");
        	str.add("B06WWFC1CX");
        	str.add("B06WRTGW4Y");
        	str.add("B015SPTPRK");
        	str.add("B06WP7FNGP");
        	str.add("B015RYIWWQ");
        	str.add("B06WLPJPDZ");
        	str.add("B015RQQWCG");
        	str.add("B06WGWZHDD");
        	str.add("B015JLJR8U");
        	str.add("B015J4GNIY");
        	str.add("B06W9K7KF5");
        	str.add("B06W2N2DH2");
        	str.add("B01NGTW8TC");
        	str.add("B015CVWCN4");
        	str.add("B0159AZUBO");
        	str.add("B0157VECKA");
        	str.add("B0157VE51Q");
        	str.add("B01540AH9O");
        	str.add("B01NCAO3T7");
        	str.add("B014WMNZ94");
        	str.add("B01NBHZMTF");
        	str.add("B01NBA98RB");
        	str.add("B014SXTB5E");
        	str.add("B014Q28MN4");
        	str.add("B014OAN69S");
        	str.add("B014MI31XS");
        	str.add("B01NAU6MT2");
        	str.add("B01NAU5IIG");
        	str.add("B014IIW124");
        	str.add("B014H5GFY8");
        	str.add("B01N9XNK8Y");
        	str.add("B014BRTFYE");
        	str.add("B014BGMY16");
        	str.add("B0149I8EY2");
        	str.add("B0146IM08G");
        	str.add("B0145OU452");
        	str.add("B01451D6L4");
        	str.add("B01451D0OC");
        	str.add("B01N9BO3SE");
        	str.add("B01451CU6G");
        	str.add("B014518UE2");
        	str.add("B014517JVW");
        	str.add("B0144Q2U8K");
        	str.add("B01N8XVSZM");
        	str.add("B01N8X5A30");
        	str.add("B01N8UK7SF");
        	str.add("B0142XJPMY");
        	str.add("B01N8TPDKN");
        	str.add("B0140A2R4C");
        	str.add("B01N809NG3");
        	str.add("B01N7YVHHM");
        	str.add("B013L1WXWW");
        	str.add("B01N7VWTZF");
        	str.add("B01N7UJV9H");
        	str.add("B013GUG9HS");
        	str.add("B013CDOJ2G");
        	str.add("B0135UMLYE");
        	str.add("B01N7H5PQF");
        	str.add("B012W0RL2U");
        	str.add("B012VLZJ7E");
        	str.add("B012VLDOBW");
        	str.add("B01N6NF3N9");
        	str.add("B01N6HD1HO");
        	str.add("B012GVUNC0");
        	str.add("B01N691002");
        	str.add("B012CZ731E");
        	str.add("B0127SAHBO");
        	str.add("B0127BK4MS");
        	str.add("B01271Z9VE");
        	str.add("B01N5MVFEB");
        	str.add("B01N5KLZOS");
        	str.add("B01N5KL834");
        	str.add("B01N5I0E0X");
        	str.add("B01N59Z49M");
        	str.add("B011X4TOW6");
        	str.add("B01N59NVT5");
        	str.add("B011VRIWI2");
        	str.add("B01N52V1GF");
        	str.add("B01N4V6TJK");
        	str.add("B01N4CXG8Q");
        	str.add("B0114XY1OM");
        	str.add("B01N42RJHQ");
        	str.add("B01N3VVOWT");
        	str.add("B01N3NT5AI");
        	str.add("B010V1EKB2");
        	str.add("B010TSSTAU");
        	str.add("B01N3B3B4M");
        	str.add("B010JKTJ8Y");
        	str.add("B010FACD7W");
        	str.add("B01N308B31");
        	str.add("B01N2UO1CD");
        	str.add("B010BBDUM2");
        	str.add("B010B9QEU4");
        	str.add("B010B0KHEC");
        	str.add("B010B0DS8O");
        	str.add("B010B03VEK");
        	str.add("B01N2HN79I");
        	str.add("B01086FUKA");
        	str.add("B0102UQLAA");
        	str.add("B010046SXI");
        	str.add("B00ZZ02NU0");
        	str.add("B00ZY5ZFXI");
        	str.add("B00ZUDNMIO");
        	str.add("B01N19SRDN");
        	str.add("B01N14NKLQ");
        	str.add("B00ZP60N4W");
        	str.add("B01N0ZLHQV");
        	str.add("B00ZCOP1JE");
        	str.add("B00ZAJVHR6");
        	str.add("B00Z9RULAI");
        	str.add("B00Z71CKBE");
        	str.add("B00Z70OUQS");
        	str.add("B00Z5O579K");
        	str.add("B01N092QAT");
        	str.add("B00YTTLEJY");
        	str.add("B01MZH6XA5");
        	str.add("B00YTJ5V98");
        	str.add("B01MZA9LCQ");
        	str.add("B00YS5YGF8");
        	str.add("B00YJXREOE");
        	str.add("B00YH5MQS8");
        	str.add("B00YE8VLXY");
        	str.add("B00YC9L8N8");
        	str.add("B00YC3UHJU");
        	str.add("B00YB9KPPQ");
        	str.add("B00YB9KALK");
        	str.add("B00YB9GG2M");
        	str.add("B00YB9E5P2");
        	str.add("B00YB9E034");
        	str.add("B00YB9DELS");
        	str.add("B00YB9D5OO");
        	str.add("B00YB9CWWK");
        	str.add("B00YB9CQ4O");
        	str.add("B00YB9CLZI");
        	str.add("B00YB9CBYE");
        	str.add("B00YB9C5N6");
        	str.add("B01MY552XW");
        	str.add("B01MY32QST");
        	str.add("B00YAR86BY");
        	str.add("B01MXOUN1E");
        }

		try {
			testService.checkjp(str, "0216");
		}catch(Exception e) {
			throw new Exception("失败");
		}
	
	}
	
	
	@GetMapping("/check/jp1") 
	public void checkjp1() throws Exception {
		List<String> str = new ArrayList<>();


		try {
			testService.checkjp(str, "322");
		}catch(Exception e) {
			throw new Exception("失败");
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
	
	

	
	
	
	
	
	
	@GetMapping("/choose/us")
	public void chooseUs() throws Exception {

		try {
			testService.chooseUs();
		}catch(Exception e) {
			throw new Exception("失败", e);
		}

	}
	
	@GetMapping("/choose/us/step2")
	public void chooseUsStet2() throws Exception {

		try {
			testService.chooseUsStet2();
		}catch(Exception e) {
			throw new Exception("失败", e);
		}

	}
	
	@GetMapping("/choose/us/step3")
	public void chooseUsStet3() throws Exception {

		try {
			testService.chooseUsStet3();
		}catch(Exception e) {
			throw new Exception("失败", e);
		}

	}
	
	
	
	
	
	
	
}
