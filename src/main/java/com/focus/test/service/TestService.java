package com.focus.test.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.focus.test.dto.ComDto;
import com.focus.test.dto.Commont;
import com.focus.test.entity.HotTitle;
import com.focus.test.entity.Node;
import com.focus.test.entity.Node1;
import com.focus.test.entity.NodeId;
import com.focus.test.entity.Self;
import com.focus.test.entity.Step4;
import com.focus.test.entity.Test;
import com.focus.test.repository.HotTitleRepository;
import com.focus.test.repository.Node1Repository;
import com.focus.test.repository.NodeIdRepository;
import com.focus.test.repository.NodeRepository;
import com.focus.test.repository.SelfRepository;
import com.focus.test.repository.TestRepository;
import com.focus.test.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TestService {
	static List<String> headerList = new ArrayList<String>();
	static List<String> headerNameList = new ArrayList<String>();
	static {
		headerList.add("time");
		headerList.add("level");
		headerList.add("attribute");
		headerList.add("authorName");
		headerList.add("reviewTitle");
		headerList.add("reviewUrl");
		headerList.add("reviewContent");
		headerNameList.add("时间");
		headerNameList.add("概念名称");
		headerNameList.add("股票名称");
		headerNameList.add("股票代码");
		headerNameList.add("驱动事件");
		headerNameList.add("评论URL");
		headerNameList.add("成分股数量");
	}
	/*
	 * 大概意思：在默认的代理模式下，只有目标方法由外部调用， 才能被 Spring 的事务拦截器拦截。在同一个类中的两个方法直接调用，是不会被 Spring
	 * 的事务拦截器拦截， 就像上面的 save 方法直接调用了同一个类中的 method1方法，method1 方法不会被 Spring 的事务拦截器拦截。
	 * 可以使用 AspectJ 取代 Spring AOP 代理来解决这个问题，但是这里暂不讨论。
	 * 
	 * 
	 * 当我们使用@Transaction 时默认为RuntimeException（也就是运行时异常）异常才会回滚。
	 */

	@Autowired
	TestRepository testRepository;

	@Autowired
	HotTitleRepository hotTitleRepository;

	@Autowired
	NodeRepository nodeRepository;

	@Autowired
	Node1Repository node1Repository;
	
	@Autowired
	NodeIdRepository nodeIdRepository;
	
	@Autowired
	SelfRepository selfRepository;

	// @Transactional(rollbackFor = Exception.class)
	@Transactional
	public void tran() throws Exception {
		tran1();
		Test test = Test.builder().name("tran").build();
		testRepository.save(test);

		int i = 1 / 0;
	}

	// @Transactional(propagation = Propagation.REQUIRES_NEW)
	public void tran1() throws Exception {
		Test test = Test.builder().name("tran1").build();
		testRepository.save(test);
	}

	/*
	 * 
	 *      1. 将所有奇数位置（第1、3、5、7、9和11位）上的数字相加。
			6+9+8+0+0+9=32 
			2. 然后，将该数乘以3。
			32*3=96 
			3. 将所有偶数位置（第2、4、6、8和10位）上的数字相加。
			3+3+2+0+3=11 
			4. 然后，将该和与第2步所得的值相加。
			96+11=107 
			5. 保存第4步的值。要创建校验位，需要确定一个值，当将该值与步骤4所得的值相加时，结果为10的倍数。
			107+3=110 
			因此，校验位为3。
	 * 
	 * */
	
	
	public static void main1(String args []) {
		String code = "789" + "5268" + "65231";

		for (int index = 0; index < 10; index++) {
			int ji = 0;
			int ou = 0;
			for (int i = 0; i < code.length(); i++) {
				if (i + 1 == 1 || i + 1 == 3 || i + 1 == 5 || i + 1 == 7 || i + 1 == 9 || i + 1 == 11) {
					ji = ji + Integer.valueOf(code.charAt(i) + "");
				}
				if (i + 1 == 2 || i + 1 == 4 || i + 1 == 6 || i + 1 == 8 || i + 1 == 10) {
					ou = ou + Integer.valueOf(code.charAt(i) + "");
				}
			}
			int valid = (ji * 3 + ou) % 10;
			valid = 10 - valid;
			String newCode = code + valid;
			System.out.println(newCode);
			Long codeL = Long.valueOf(code);
			codeL = codeL + (int) (Math.random() * 10 + 1);
			code = codeL.toString();
		}
	}
	
	
	
	
	/**
	 * @throws InterruptedException
	 * 
	 */
	public void chrome(String commentUrl, HttpServletResponse response) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");

		WebDriver webDriver = new ChromeDriver();

		System.out.println(
				"打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println(
				"页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// 设定网址
		String base = "https://www.amazon.co.jp/product-reviews/%s/ref=cm_cr_arp_d_viewopt_srt?ie=UTF8&showViewpoints=1&sortBy=recent&pageNumber=1";

		webDriver.get(String.format(base, commentUrl));

		// 显示等待控制对象
		WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);
		try {
			List<Commont> comments = new ArrayList<>();

			for (int i = 0; i < 100; i++) {

				List<WebElement> reviews = webDriver.findElements(By.className("review"));
				reviews.forEach(e -> {
					Commont commont = new Commont();
					WebElement user = e.findElement(By.className("a-profile-name"));
					// 用户
					commont.setAuthorName(user.getText());
					System.out.println("author: " + commont.getAuthorName());
					// 标题
					WebElement title = e.findElement(By.className("review-title"));
					commont.setReviewTitle(title.findElement(By.tagName("span")).getText());
					System.out.println("title: " + commont.getReviewTitle());
					// 内容
					WebElement content = e.findElement(By.className("review-text"));
					commont.setReviewContent(content.findElement(By.tagName("span")).getText());
					System.out.println("content: " + commont.getReviewContent());
					// 星级
					WebElement star = e.findElement(By.className("a-link-normal"));
					System.out.println("star: " + star.getAttribute("title"));
					commont.setLevel(star.getAttribute("title").substring(0, 1));

					// 多属性
					WebElement attr = null;
					try {
						attr = e.findElement(By.className("review-data"));
						commont.setAttribute(attr.findElement(By.tagName("a")).getText().replaceAll("サイズ: ", ""));
						System.out.println("attr: " + commont.getAttribute());
						String url = attr.findElement(By.tagName("a")).getAttribute("href");
						int start = url.indexOf("product-reviews") + 16;
						int end = start + 9;
						url = url.substring(start, end);
						commont.setReviewUrl(url);
					} catch (Exception e1) {
						commont.setReviewUrl(commentUrl);
					}
					// 时间
					WebElement date = e.findElement(By.className("review-date"));
					String dateStr = date.getText();
					commont.setTime(dateStr.substring(0, dateStr.indexOf("日") + 1));
					comments.add(commont);
				});
				if (reviews == null || reviews.size() != 10) {
					break;
				}
				WebElement next = null;
				try {
					next = webDriver.findElement(By.className("a-last"));
				} catch (Exception e) {
					break;
				}

				String classs = next.getAttribute("class");
				System.out.println("classs" + classs);

				if (classs.contains("a-disabled")) {
					break;
				} else {
					next.click();
					Thread.sleep(1000 * 5);
				}
			}

			ExcelUtil exUtil = new ExcelUtil();
			try {
				exUtil.exportExcel(commentUrl, headerNameList, headerList, comments, response);
			} catch (Exception e) {
				// break;
			}

			exUtil.exportExcel(commentUrl, headerNameList, headerList, comments);
		} catch (Exception ee) {
			webDriver.close();
		}

		webDriver.close();

	}

	private static int getStart(Element row) {
		int start = 0;
		for (int i = 1; i <= 5; i++) {
			Elements e = row.getElementsByClass("a-star-" + i);
			if (e.size() >= 1) {
				start = i;
				return i;
			}
		}
		return start;
	}

	/**
	 * 下载文件到浏览器
	 * 
	 * @param request
	 * @param response
	 * @param filename 要下载的文件名
	 * @param file     需要下载的文件对象
	 * @throws IOException
	 */
	public static void downFile(HttpServletRequest request, HttpServletResponse response, String filename, File file)
			throws IOException {
		// 文件存在才下载
		if (file.exists()) {
			OutputStream out = null;
			FileInputStream in = null;
			try {
				// 1.读取要下载的内容
				in = new FileInputStream(file);

				// 2. 告诉浏览器下载的方式以及一些设置
				// 解决文件名乱码问题，获取浏览器类型，转换对应文件名编码格式，IE要求文件名必须是utf-8, firefo要求是iso-8859-1编码
				String agent = request.getHeader("user-agent");
				if (agent.contains("FireFox")) {
					filename = new String(filename.getBytes("UTF-8"), "iso-8859-1");
				} else {
					filename = URLEncoder.encode(filename, "UTF-8");
				}
				// 设置下载文件的mineType，告诉浏览器下载文件类型
				String mineType = request.getServletContext().getMimeType(filename);
				response.setContentType(mineType);
				// 设置一个响应头，无论是否被浏览器解析，都下载
				response.setHeader("Content-disposition", "attachment; filename=" + filename);
				// 将要下载的文件内容通过输出流写到浏览器
				out = response.getOutputStream();
				int len = 0;
				byte[] buffer = new byte[1024];
				while ((len = in.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			}
		}
	}

	/*
	 * static List<String> headerList = new ArrayList<String>(); static List<String>
	 * headerNameList = new ArrayList<String>(); static { headerList.add("time");
	 * headerList.add("level"); headerList.add("attribute");
	 * headerList.add("authorName"); headerList.add("reviewTitle");
	 * headerList.add("reviewUrl"); headerList.add("reviewContent");
	 * headerNameList.add("时间"); headerNameList.add("概念名称");
	 * headerNameList.add("股票名称"); headerNameList.add("股票代码");
	 * headerNameList.add("驱动事件"); headerNameList.add("评论URL");
	 * headerNameList.add("成分股数量"); }
	 * 
	 */
	public void search(String code, HttpServletResponse response, String base) {

		// System.setProperty("webdriver.chrome.driver","C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
		// mac
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

		WebDriver webDriver = new ChromeDriver();

		System.out.println(
				"打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println(
				"页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// 设定网址
		// String base =
		// "https://www.amazon.co.jp/s?k=%E9%9B%BB%E5%8B%95%E3%82%AA%E3%83%8A%E3%83%9B%E3%83%BC%E3%83%AB&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss_2";
		// String base =
		// "https://www.amazon.co.jp/s?k=%E3%82%AA%E3%83%8A%E3%83%9B&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss";

		webDriver.get(base + code);

		// 显示等待控制对象
		WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);

		try {
			WebElement cli = webDriver.findElement(By.id("a-autoid-2"));
			if (cli != null) {
				cli.click();
			}
		} catch (Exception e) {
			// log.info(“无确认”);
		}

		WebElement products = webDriver.findElement(By.className("s-main-slot"));
		int[] index = { 1 };
		try {
			List<ComDto> comments = new ArrayList<>();
			for (int i = 0; i < 50; i++) {

				List<WebElement> reviews = products.findElements(By.className("sg-col-4-of-24"));
				String str = (i + 1) + "";
				System.out.print("第" + str + "页");
				reviews.forEach(e -> {
					ComDto commont = new ComDto();
					String asin = e.getAttribute("data-asin");
					commont.setOne(str);
					commont.setTwo(asin);
					try {
						List<WebElement> small = e.findElements(By.className("a-spacing-top-small"));

						// 第一标题
						WebElement title = small.get(0).findElement(By.tagName("span"));
						commont.setThree(title.getText());
						commont.setSeven(index[0] + "");
						index[0] = index[0] + 1;
						// 价钱
						WebElement price = null;
						try {
							price = small.get(1).findElement(By.className("a-price-whole"));
						} catch (Exception ee) {
							log.info("无存");
							throw new Exception("无存" + ee.getMessage());
						}

						String priceStr = price.getText();
						priceStr = priceStr.replaceAll("￥", "");
						priceStr = priceStr.replaceAll(",", "");
						commont.setFour(priceStr);
						List<WebElement> micro = e.findElements(By.className("a-spacing-top-micro"));
						// 如果 有两个表示有平
						// 第二平
						if (micro.size() > 1) {
							WebElement ping = micro.get(0).findElement(By.className("a-size-small"));

							List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
							// 平分
							String startString = pings.get(0).getAttribute("aria-label");
							startString = startString.substring(startString.length() - 3);
							commont.setFive(startString);
							// 数量
							commont.setSix(pings.get(1).getAttribute("aria-label"));
						}
					} catch (Exception ce) {
						log.info("无评价", ce);
					}
					comments.add(commont);
				});
				// if (reviews == null) {
				// if (reviews == null || reviews.size() != 24) {
				// break;
				// }
				WebElement next = null;
				try {
					next = webDriver.findElement(By.className("a-last"));
				} catch (Exception e) {
					Thread.sleep(1000 * 5);
					try {
						next = webDriver.findElement(By.className("a-last"));
					} catch (Exception es) {
						break;
					}

				}

				String classs = next.getAttribute("class");
				System.out.println("classs" + classs);

				if (classs.contains("a-disabled")) {
					break;
				} else {
					next.click();
					Thread.sleep(1000 * 10);
				}
			}

			ExcelUtil exUtil = new ExcelUtil();
			List<String> headerList = new ArrayList<String>();
			List<String> headerNameList = new ArrayList<String>();

			headerList.add("one");
			headerList.add("two");
			headerList.add("three");
			headerList.add("four");
			headerList.add("five");
			headerList.add("six");
			headerList.add("seven");
			headerNameList.add("页码");
			headerNameList.add("ASIN");
			headerNameList.add("标题");
			headerNameList.add("价钱");
			headerNameList.add("评价星级");
			headerNameList.add("评价数量");
			headerNameList.add("顺序");

			try {
				exUtil.exportExcel(code, headerNameList, headerList, comments, response);
			} catch (Exception e) {
				log.error("", e);
			}

			exUtil.exportExcel(code, headerNameList, headerList, comments);
		} catch (Exception ee) {
			log.info("错误" + ee);
			// webDriver.close();
		}

		webDriver.close();

	}

	public void searchCa(String code, HttpServletResponse response, String base) {

		// System.setProperty("webdriver.chrome.driver","C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
		// mac
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

		WebDriver webDriver = new ChromeDriver();

		System.out.println(
				"打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println(
				"页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// 设定网址
		// String base =
		// "https://www.amazon.co.jp/s?k=%E9%9B%BB%E5%8B%95%E3%82%AA%E3%83%8A%E3%83%9B%E3%83%BC%E3%83%AB&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss_2";
		// String base =
		// "https://www.amazon.co.jp/s?k=%E3%82%AA%E3%83%8A%E3%83%9B&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss";

		webDriver.get(base + code);

		// 显示等待控制对象
		WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);

		try {
			WebElement cli = webDriver.findElement(By.id("a-autoid-2"));
			if (cli != null) {
				cli.click();
			}
		} catch (Exception e) {
			// log.info(“无确认”);
		}

		WebElement products = webDriver.findElement(By.className("s-main-slot"));
		int[] index = { 1 };
		try {
			List<ComDto> comments = new ArrayList<>();
			for (int i = 0; i < 50; i++) {

				List<WebElement> reviews = products.findElements(By.className("sg-col-4-of-24"));
				String str = (i + 1) + "";
				System.out.print("第" + str + "页");
				reviews.forEach(e -> {
					ComDto commont = new ComDto();
					String asin = e.getAttribute("data-asin");
					commont.setOne(str);
					commont.setTwo(asin);
					try {
						List<WebElement> small = e.findElements(By.className("a-spacing-top-small"));

						// 第一标题
						WebElement title = small.get(0).findElement(By.className("a-size-base-plus"));
						commont.setThree(title.getText());
						commont.setSeven(index[0] + "");
						index[0] = index[0] + 1;
						// 价钱
						WebElement price = null;
						WebElement price1 = null;
						try {
							price = small.get(1).findElement(By.className("a-price-whole"));
							price1 = small.get(1).findElement(By.className("a-price-fraction"));

						} catch (Exception ee) {
							log.info("无存");
							throw new Exception("无存" + ee.getMessage());
						}
						String priceStr = "";
						String priceStr1 = "";
						priceStr = price.getText();
						priceStr1 = price1.getText();
						priceStr = priceStr.replaceAll("￥", "");
						priceStr = priceStr.replaceAll(",", "");
						commont.setFour(priceStr + "." + priceStr1);
						List<WebElement> micro = e.findElements(By.className("a-spacing-top-micro"));
						// 如果 有两个表示有平
						// 第二平
						if (micro.size() > 1) {
							WebElement ping = micro.get(0).findElement(By.className("a-size-small"));

							List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
							// 平分
							String startString = pings.get(0).getAttribute("aria-label");
							startString = startString.substring(0, 3);
							commont.setFive(startString);
							// 数量
							commont.setSix(pings.get(1).getAttribute("aria-label"));
						}
						// 查询日本是否存在此产品
						// 当前页
						// String home = webDriver.getWindowHandle();
						// Boolean isExist = isJpExist(commont.getTwo(), home, webDriver);
						// webDriver.switchTo().window(home);
						// if(isExist) {
						// log.info(commont.getTwo() + " exist" + " 1");
						// commont.setEight("1");
						// }else {
						// log.info(commont.getTwo() + " exist" + " 0");
						// commont.setEight("0");
						// }
					} catch (Exception ce) {
						log.info("无评价", ce);
					}
					comments.add(commont);
				});
				// if (reviews == null) {
				// if (reviews == null || reviews.size() != 24) {
				// break;
				// }
				WebElement next = null;
				try {
					next = webDriver.findElement(By.className("a-last"));
				} catch (Exception e) {
					Thread.sleep(1000 * 5);
					try {
						next = webDriver.findElement(By.className("a-last"));
					} catch (Exception es) {
						break;
					}

				}

				String classs = next.getAttribute("class");
				System.out.println("classs" + classs);

				if (classs.contains("a-disabled")) {
					break;
				} else {
					next.click();
					Thread.sleep(1000 * 10);
				}
			}

			ExcelUtil exUtil = new ExcelUtil();
			List<String> headerList = new ArrayList<String>();
			List<String> headerNameList = new ArrayList<String>();

			headerList.add("one");
			headerList.add("two");
			headerList.add("three");
			headerList.add("four");
			headerList.add("five");
			headerList.add("six");
			headerList.add("seven");
			// headerList.add("eight");
			headerNameList.add("页码");
			headerNameList.add("ASIN");
			headerNameList.add("标题");
			headerNameList.add("价钱");
			headerNameList.add("评价星级");
			headerNameList.add("评价数量");
			headerNameList.add("顺序");
			// headerNameList.add("jp有此产品");

			try {
				exUtil.exportExcel(code, headerNameList, headerList, comments);
			} catch (Exception e) {
				log.error("", e);
			}

			exUtil.exportExcel(code, headerNameList, headerList, comments);
		} catch (Exception ee) {
			log.info("错误" + ee);
			// webDriver.close();
		}

		webDriver.close();

	}

	public Boolean isJpExist(String keyword, String home, WebDriver webDriver) throws InterruptedException {

		String url = "https://www.amazon.co.jp/s?k=%s&_encoding=UTF8&redirect=true&ref=nb_sb_noss";
		url = String.format(url, keyword);
		log.info("new tab url = " + url);
		((ChromeDriver) webDriver).executeScript("window.open('" + url + "')", "");
		Thread.sleep(1000 * 5);
		Set<String> handle = webDriver.getWindowHandles();
		Boolean result = false;
		// 获取所有页面的句柄，并循环判断不是当前的句柄
		for (String handles : handle) {
			if (handles.equals(home)) {
				continue;
			} else {
				// 打开新窗口

				webDriver.switchTo().window(handles);
				// data-asin="B071CZ8ZS9"
				try {
					// 成人
					try {
						WebElement cli = webDriver.findElement(By.id("a-autoid-2"));
						if (cli != null) {
							cli.click();
							result = true;
						}
					} catch (Exception e) {
						log.error("无确认");
					}
					Thread.sleep(1000);
					if (!result) {
						WebElement ele = webDriver.findElement(By.xpath(".//div[@data-asin='" + keyword + "']"));
						// WebElement ele = webDriver.findElement(By.xpath(".//div[@data-uuid]"));

						if (ele != null) {
							result = true;
						}
					}

				} catch (Exception e) {
					log.error("eee", e);
					result = false;
				}

				webDriver.switchTo().window(handles).close();

			}
		}

		return result;
	}

	public void getCompany(WebDriver webDriver, String home, String url, HotTitle pHotTitle)
			throws InterruptedException {

		/*
		 * String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,Keys.RETURN);
		 * webDriver.findElement(By.linkText("urlLink")).sendKeys(selectLinkOpeninNewTab
		 * ); webDriver.get(url);
		 */
		((ChromeDriver) webDriver).executeScript("window.open('" + url + "')", "");
		Thread.sleep(1000 * 5);
		Set<String> handle = webDriver.getWindowHandles();
		// 获取所有页面的句柄，并循环判断不是当前的句柄
		for (String handles : handle) {
			if (handles.equals(home)) {
				continue;
			} else {
				// 打开新窗口
				webDriver.switchTo().window(handles);
				Integer pageSize = 0;
				try {
					WebElement pageInfoT = webDriver.findElement(By.className("page_info"));
					String pageInfoString = pageInfoT.getText();
					String[] pageArray = pageInfoString.split("\\/");
					pageSize = Integer.valueOf(pageArray[1]) - 1;
				} catch (Exception e1) {
					pageSize = 1;
				}
				List<HotTitle> hotTitles = new ArrayList<>();
				for (int index = 0; index < pageSize; index++) {
					WebElement products = webDriver.findElement(By.className("m-pager-table"));
					WebElement tbody = products.findElement(By.tagName("tbody"));
					List<WebElement> tr = tbody.findElements(By.tagName("tr"));
					for (int i = 0; i < tr.size(); i++) {
						List<WebElement> td = tr.get(i).findElements(By.tagName("td"));
						log.info("title" + webDriver.getTitle());
						log.info("公司");
						log.info(td.get(1).getText());
						log.info(td.get(2).getText());

						HotTitle hotTitle = new HotTitle();
						hotTitle.setCode(td.get(1).getText());
						hotTitle.setParentId(pHotTitle.getId());
						hotTitle.setDisplayName(td.get(2).getText());
						hotTitles.add(hotTitle);
						// 当前页
					}
					if (hotTitles.size() >= 100) {
						break;
					}
					List<WebElement> nexts = webDriver.findElements(By.className("changePage"));
					if (nexts != null && !nexts.isEmpty()) {
						nexts.forEach(e -> {
							if (e.getText().equals("下一页")) {
								e.click();
							}
						});
					}
					Thread.sleep(1000 * 10);
				}
				webDriver.switchTo().window(handles).close();

				hotTitleRepository.saveAll(hotTitles);
			}
		}

	}

	public void hot(HttpServletResponse response) {

		System.setProperty("webdriver.chrome.driver",
				"C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");

		WebDriver webDriver = new ChromeDriver();

		System.out.println(
				"打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println(
				"页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// 设定网址
		String base = "http://q.10jqka.com.cn/gn/";

		webDriver.get(base);

		// 显示等待控制对象
		WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);

		try {
			List<Commont> comments = new ArrayList<>();

			for (int index = 0; index < 28; index++) {
				WebElement products = webDriver.findElement(By.className("m-pager-table"));
				WebElement tbody = products.findElement(By.tagName("tbody"));
				List<WebElement> tr = tbody.findElements(By.tagName("tr"));
				for (int i = 0; i < tr.size(); i++) {
					List<WebElement> td = tr.get(i).findElements(By.tagName("td"));
					String url = td.get(1).findElement(By.tagName("a")).getAttribute("href");
					log.info("分类");
					log.info(td.get(0).getText());
					log.info(td.get(1).getText());
					log.info(url);
					log.info(td.get(2).getText());
					log.info(td.get(3).getText());
					log.info(td.get(4).getText());
					/*
					 * Commont commont = new Commont(); commont.setAttribute("");
					 * commont.setLevel(td.get(1).getText()); commont.setReviewUrl(url);
					 * commont.setReviewContent(td.get(4).getText());
					 * commont.setTime(td.get(0).getText());
					 * commont.setReviewTitle(td.get(2).getText()); comments.add(commont);
					 */
					if (hotTitleRepository.countByTitle(td.get(1).getText()) == 0) {
						HotTitle hotTitle = new HotTitle();
						hotTitle.setTime(td.get(0).getText());
						hotTitle.setTitle(td.get(1).getText());
						hotTitle.setEvent(td.get(2).getText());
						hotTitle.setCompanyCount(td.get(4).getText());
						hotTitle = hotTitleRepository.save(hotTitle);
						// 当前页
						String home = webDriver.getWindowHandle();
						getCompany(webDriver, home, url, hotTitle);
						webDriver.switchTo().window(home);
					}

				}
				try {
					List<WebElement> next = webDriver.findElements(By.className("changePage"));
					WebElement nextElement = next.get(next.size() - 2);
					nextElement.click();
					Thread.sleep(1000 * 10);
				} catch (Exception ex) {
					// TODO: handle exception
				}
			}
			log.info(JSON.toJSONString(comments));

			/*
			 * ExcelUtil exUtil = new ExcelUtil(); try { exUtil.exportExcel("热门概念",
			 * headerNameList, headerList, comments, response); } catch (Exception e) {
			 * log.error("", e); }
			 * 
			 * exUtil.exportExcel("热门概念", headerNameList, headerList, comments);
			 */
		} catch (Exception ee) {
			log.info("错误", ee);
			// webDriver.close();
		}

		// webDriver.close();

	}

	public void chromeUs(String commentUrl, HttpServletResponse response) {

		// windows
		// System.setProperty("webdriver.chrome.driver","C:\\Program Files
		// (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		// mac
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

		WebDriver webDriver = new ChromeDriver();

		System.out.println(
				"打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println(
				"页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// 设定网址
		String base = "https://www.amazon.com/product-reviews/%s/ref=cm_cr_arp_d_viewopt_srt?ie=UTF8&reviewerType=all_reviews&filterByStar=five_star&pageNumber=1&sortBy=recent";

		webDriver.get(String.format(base, commentUrl));

		// 显示等待控制对象
		WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);
		try {
			List<Commont> comments = new ArrayList<>();

			for (int i = 0; i < 100; i++) {

				List<WebElement> reviews = webDriver.findElements(By.className("review"));
				reviews.forEach(e -> {
					Commont commont = new Commont();
					WebElement user = e.findElement(By.className("a-profile-name"));
					// 用户
					commont.setAuthorName(user.getText());
					System.out.println("author: " + commont.getAuthorName());
					// 标题
					WebElement title = e.findElement(By.className("review-title"));
					commont.setReviewTitle(title.findElement(By.tagName("span")).getText());
					System.out.println("title: " + commont.getReviewTitle());
					// 内容
					WebElement content = e.findElement(By.className("review-text"));
					commont.setReviewContent(content.findElement(By.tagName("span")).getText());
					System.out.println("content: " + commont.getReviewContent());
					// 星级
					WebElement star = e.findElement(By.className("a-link-normal"));
					System.out.println("star: " + star.getAttribute("title"));
					commont.setLevel(star.getAttribute("title").substring(0, 1));

					// 多属性
					WebElement attr = null;
					try {
						attr = e.findElement(By.className("review-data"));
						commont.setAttribute(attr.findElement(By.tagName("a")).getText().replaceAll("サイズ: ", ""));
						System.out.println("attr: " + commont.getAttribute());
						String url = attr.findElement(By.tagName("a")).getAttribute("href");
						int start = url.indexOf("product-reviews") + 16;
						int end = start + 9;
						url = url.substring(start, end);
						commont.setReviewUrl(url);
					} catch (Exception e1) {
						commont.setReviewUrl(commentUrl);
					}
					// 时间
					WebElement date = e.findElement(By.className("review-date"));
					String dateStr = date.getText();
					commont.setTime(dateStr.substring(0, dateStr.indexOf("日") + 1));
					comments.add(commont);
				});
				if (reviews == null || reviews.size() != 10) {
					break;
				}
				WebElement next = null;
				try {
					next = webDriver.findElement(By.className("a-last"));
				} catch (Exception e) {
					break;
				}

				String classs = next.getAttribute("class");
				System.out.println("classs" + classs);

				if (classs.contains("a-disabled")) {
					break;
				} else {
					next.click();
					Thread.sleep(1000 * 6);
				}
			}

			ExcelUtil exUtil = new ExcelUtil();
			try {
				exUtil.exportExcel(commentUrl, headerNameList, headerList, comments, response);
			} catch (Exception e) {
				// break;
			}

			exUtil.exportExcel(commentUrl, headerNameList, headerList, comments);
		} catch (Exception ee) {
			webDriver.close();
		}

		webDriver.close();

	}

	/**
	 * @param code
	 * @param response
	 * @param base
	 */
	public void searchUsNode(String code, HttpServletResponse response, String base) {

		// System.setProperty("webdriver.chrome.driver","C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
		// mac
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

		WebDriver webDriver = new ChromeDriver();

		System.out.println(
				"打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println(
				"页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// 设定网址
		// String base =
		// "https://www.amazon.co.jp/s?k=%E9%9B%BB%E5%8B%95%E3%82%AA%E3%83%8A%E3%83%9B%E3%83%BC%E3%83%AB&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss_2";
		// String base =
		// "https://www.amazon.co.jp/s?k=%E3%82%AA%E3%83%8A%E3%83%9B&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss";

		webDriver.get(base);

		// 显示等待控制对象
		WebDriverWait webDriverWait1 = new WebDriverWait(webDriver, 10);

		WebDriverWait webDriverWait = null;

		int[] index = { 1 };
		try {
			List<Node> comments = new ArrayList<>();
			for (int i = 0; i < 50; i++) {
				WebElement products = webDriver.findElement(By.className("s-main-slot"));
				List<WebElement> reviews = products.findElements(By.xpath(".//div[@data-asin!='']"));
				String str = (i + 1) + "";
				System.out.print("第" + str + "页");
				int[] num = { 1 };
				reviews.forEach(e -> {
					Node commont = new Node();
					String asin = e.getAttribute("data-asin");
					commont.setOne(str);
					commont.setTwo(asin);
					String className = e.getAttribute("class");
					// System.out.print(className);
					boolean isSmall = true;
					if (className.contains("sg-col-20-of-24")) {
						isSmall = false;
					}
					if (isSmall) {
						try {
							List<WebElement> small = e.findElements(By.className("a-spacing-top-small"));

							// 第一标题
							WebElement title = small.get(0).findElement(By.className("a-size-base-plus"));
							commont.setThree(title.getText());
							commont.setSeven(index[0] + "");
							index[0] = index[0] + 1;
							num[0] = num[0] + 1;
							// 价钱
							WebElement price = null;
							WebElement price1 = null;
							
							try {
								price = small.get(1).findElement(By.className("a-price-whole"));
								price1 = small.get(1).findElement(By.className("a-price-fraction"));

							} catch (Exception ee) {
								log.info("无存");
								throw new Exception("无存" + ee.getMessage());
							}
							String priceStr = "";
							String priceStr1 = "";
							priceStr = price.getText();
							priceStr1 = price1.getText();
							priceStr = priceStr.replaceAll("￥", "");
							priceStr = priceStr.replaceAll(",", "");
							commont.setFour(priceStr + "." + priceStr1);
							Boolean test = false;
							List<WebElement> micro = e.findElements(By.className("a-spacing-top-micro"));
							// 如果 有两个表示有平
							// 第二平
							if (micro.size() == 2) {
								WebElement ping = micro.get(0).findElement(By.className("a-size-small"));

								List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
								// 平分
								String startString = pings.get(0).getAttribute("aria-label");
								startString = startString.substring(0, 3);
								commont.setFive(startString);
								// 数量
								commont.setSix(pings.get(1).getAttribute("aria-label"));
								
								commont.setFive(startString);
								// 数量
								commont.setSix(pings.get(1).getAttribute("aria-label"));
								if(test) {
									try {
										//WebElement stock = micro.get(1).findElement(By.className("a-size-small"));
										List<WebElement> stockText = micro.get(1).findElements(By.xpath(".//span[@aria-label]"));
										String stockTextString = stockText.get(0).getAttribute("aria-label");
										commont.setEleven(stockTextString);
									}catch(Exception e1) {
										log.info("在库获取错误");
									}
								}
								
							
							}
							if (micro.size() == 3) {
								WebElement ping = micro.get(1).findElement(By.className("a-size-small"));

								List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
								// 平分
								String startString = pings.get(0).getAttribute("aria-label");
								startString = startString.substring(0, 3);
								commont.setFive(startString);
								// 数量
								commont.setSix(pings.get(1).getAttribute("aria-label"));
							}
							// 查询日本是否存在此产品
							// 当前页
							// String home = webDriver.getWindowHandle();
							// Boolean isExist = isJpExist(commont.getTwo(), home, webDriver);
							// webDriver.switchTo().window(home);
							// if(isExist) {
							// log.info(commont.getTwo() + " exist" + " 1");
							// commont.setEight("1");
							// }else {
							// log.info(commont.getTwo() + " exist" + " 0");
							// commont.setEight("0");
							// }
						} catch (Exception ce) {
							log.info("无评价", ce);
						}
					} else {
						try {
							WebElement title = e.findElement(By.className("a-size-medium"));
							commont.setThree(title.getText());
							commont.setSeven(index[0] + "");
							index[0] = index[0] + 1;
							num[0] = num[0] + 1;

							WebElement price = null;
							WebElement price1 = null;
							try {
								WebElement p = e.findElement(By.className("a-price"));
								price = p.findElement(By.className("a-price-whole"));
								price1 = p.findElement(By.className("a-price-fraction"));
								String priceStr = "";
								String priceStr1 = "";
								priceStr = price.getText();
								priceStr1 = price1.getText();
								priceStr = priceStr.replaceAll("￥", "");
								priceStr = priceStr.replaceAll(",", "");
								commont.setFour(priceStr + "." + priceStr1);
							} catch (Exception ee) {
								log.info("无存");
								throw new Exception("无存" + ee.getMessage());
							}

							WebElement ping = e.findElement(By.className("a-size-small"));
							List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
							// 平分
							String startString = pings.get(0).getAttribute("aria-label");
							startString = startString.substring(0, 3);
							commont.setFive(startString);
							// 数量
							commont.setSix(pings.get(1).getAttribute("aria-label"));
						} catch (Exception ex1) {
							log.info("无评价", ex1);
						}
					}
					comments.add(commont);
					log.info("第" + str + "页 第" + num[0] + "个" + JSON.toJSONString(commont));

				if (comments.size() >= 100) {
					nodeRepository.saveAll(comments);
					comments.clear();
				}

			});
			nodeRepository.saveAll(comments);
			// if (reviews == null) {
				WebElement next = null;
				try {
					next = webDriver.findElement(By.className("a-last"));
				} catch (Exception e) {
					Thread.sleep(1000 * 5);
					try {
						next = webDriver.findElement(By.className("a-last"));
					} catch (Exception es) {
						break;
					}

				}

				String classs = next.getAttribute("class");
				System.out.println("classs" + classs);

				if (classs.contains("a-disabled")) {
					break;
				} else {
					next.click();
					Thread.sleep(1000 * 10);
				}
			}

		} catch (Exception ee) {
			log.info("错误" + ee);
			// webDriver.close();
		}

		webDriver.close();

	}
	
	/**
	 * @param code
	 * @param response
	 * @param base
	 */
	public void searchCaNode(String code, HttpServletResponse response, String base) {

		// System.setProperty("webdriver.chrome.driver","C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
		// mac
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

		WebDriver webDriver = new ChromeDriver();

		System.out.println(
				"打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println(
				"页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// 设定网址
		// String base =
		// "https://www.amazon.co.jp/s?k=%E9%9B%BB%E5%8B%95%E3%82%AA%E3%83%8A%E3%83%9B%E3%83%BC%E3%83%AB&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss_2";
		// String base =
		// "https://www.amazon.co.jp/s?k=%E3%82%AA%E3%83%8A%E3%83%9B&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss";

		webDriver.get(base);

		// 显示等待控制对象
		WebDriverWait webDriverWait1 = new WebDriverWait(webDriver, 10);

		WebDriverWait webDriverWait = null;
		try {
			// 选择地区
			WebElement location = webDriver.findElement(By.id("nav-global-location-slot"));
			if (location != null) {
				location.click();
			}

			Thread.sleep(1000);

			WebElement input = webDriver.findElement(By.className("GLUX_Full_Width"));
			if (input != null) {
				// input.sendKeys("10041");
				input.sendKeys("75000");
			}
			Thread.sleep(1000);

			WebElement set = webDriver.findElement(By.xpath(".//span[@data-action='GLUXPostalUpdateAction']"));
			log.info("tt" + (set == null));
			if (set != null) {
				// 设置
				set.click();
				Thread.sleep(2000);
				// webDriver.navigate().to(base + code); //方法二
				// WebElement finish =
				// webDriver.findElement(By.xpath(".//span[@data-action='GLUXConfirmAction']"));
				// JavascriptExecutor executor = (JavascriptExecutor)webDriver;
				// executor.executeScript("arguments[0].click();", finish);
				Thread.sleep(2000);
				webDriver.get(base);

				// 显示等待控制对象
				webDriverWait = new WebDriverWait(webDriver, 10);
				Thread.sleep(2000);
				webDriver.get(base);
				webDriverWait = new WebDriverWait(webDriver, 10);
			}
		} catch (Exception e) {

			log.info("无确认", e);
		}

		int[] index = { 1 };
		try {
			List<ComDto> comments = new ArrayList<>();
			for (int i = 0; i < 50; i++) {
				WebElement products = webDriver.findElement(By.className("s-main-slot"));
				List<WebElement> reviews = products.findElements(By.xpath(".//div[@data-asin!='']"));
				String str = (i + 1) + "";
				System.out.print("第" + str + "页");
				int[] num = { 1 };
				reviews.forEach(e -> {
					ComDto commont = new ComDto();
					String asin = e.getAttribute("data-asin");
					commont.setOne(str);
					commont.setTwo(asin);
					String className = e.getAttribute("class");
					// System.out.print(className);
					boolean isSmall = true;
					if (className.contains("sg-col-20-of-24")) {
						isSmall = false;
					}
					if (isSmall) {
						try {
							List<WebElement> small = e.findElements(By.className("a-spacing-top-small"));

							// 第一标题
							WebElement title = small.get(0).findElement(By.className("a-size-base-plus"));
							commont.setThree(title.getText());
							commont.setSeven(index[0] + "");
							index[0] = index[0] + 1;
							num[0] = num[0] + 1;
							// 价钱
							WebElement price = null;
							WebElement price1 = null;
							try {
								price = small.get(1).findElement(By.className("a-price-whole"));
								price1 = small.get(1).findElement(By.className("a-price-fraction"));

							} catch (Exception ee) {
								log.info("无存");
								throw new Exception("无存" + ee.getMessage());
							}
							String priceStr = "";
							String priceStr1 = "";
							priceStr = price.getText();
							priceStr1 = price1.getText();
							priceStr = priceStr.replaceAll("￥", "");
							priceStr = priceStr.replaceAll(",", "");
							commont.setFour(priceStr + "." + priceStr1);
							List<WebElement> micro = e.findElements(By.className("a-spacing-top-micro"));
							// 如果 有两个表示有平
							// 第二平
							if (micro.size() == 2) {
								WebElement ping = micro.get(0).findElement(By.className("a-size-small"));

								List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
								// 平分
								String startString = pings.get(0).getAttribute("aria-label");
								startString = startString.substring(0, 3);
								commont.setFive(startString);
								// 数量
								commont.setSix(pings.get(1).getAttribute("aria-label"));
							}
							if (micro.size() == 3) {
								WebElement ping = micro.get(1).findElement(By.className("a-size-small"));

								List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
								// 平分
								String startString = pings.get(0).getAttribute("aria-label");
								startString = startString.substring(0, 3);
								commont.setFive(startString);
								// 数量
								commont.setSix(pings.get(1).getAttribute("aria-label"));
							}
							// 查询日本是否存在此产品
							// 当前页
							// String home = webDriver.getWindowHandle();
							// Boolean isExist = isJpExist(commont.getTwo(), home, webDriver);
							// webDriver.switchTo().window(home);
							// if(isExist) {
							// log.info(commont.getTwo() + " exist" + " 1");
							// commont.setEight("1");
							// }else {
							// log.info(commont.getTwo() + " exist" + " 0");
							// commont.setEight("0");
							// }
						} catch (Exception ce) {
							log.info("无评价", ce);
						}
					} else {
						try {
							WebElement title = e.findElement(By.className("a-size-medium"));
							commont.setThree(title.getText());
							commont.setSeven(index[0] + "");
							index[0] = index[0] + 1;
							num[0] = num[0] + 1;

							WebElement price = null;
							WebElement price1 = null;
							try {
								WebElement p = e.findElement(By.className("a-price"));
								price = p.findElement(By.className("a-price-whole"));
								price1 = p.findElement(By.className("a-price-fraction"));
								String priceStr = "";
								String priceStr1 = "";
								priceStr = price.getText();
								priceStr1 = price1.getText();
								priceStr = priceStr.replaceAll("￥", "");
								priceStr = priceStr.replaceAll(",", "");
								commont.setFour(priceStr + "." + priceStr1);
							} catch (Exception ee) {
								log.info("无存");
								throw new Exception("无存" + ee.getMessage());
							}

							WebElement ping = e.findElement(By.className("a-size-small"));
							List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
							// 平分
							String startString = pings.get(0).getAttribute("aria-label");
							startString = startString.substring(0, 3);
							commont.setFive(startString);
							// 数量
							commont.setSix(pings.get(1).getAttribute("aria-label"));
						} catch (Exception ex1) {
							log.info("无评价", ex1);
						}
					}

					comments.add(commont);
					log.info("第" + str + "页 第" + num[0] + "个" + JSON.toJSONString(commont));
				});
				// if (reviews == null) {
				// if (reviews == null || reviews.size() != 24) {
				// break;
				// }
				WebElement next = null;
				try {
					next = webDriver.findElement(By.className("a-last"));
				} catch (Exception e) {
					Thread.sleep(1000 * 5);
					try {
						next = webDriver.findElement(By.className("a-last"));
					} catch (Exception es) {
						break;
					}

				}

				String classs = next.getAttribute("class");
				System.out.println("classs" + classs);

				if (classs.contains("a-disabled")) {
					break;
				} else {
					next.click();
					Thread.sleep(1000 * 10);
				}
			}

			ExcelUtil exUtil = new ExcelUtil();
			List<String> headerList = new ArrayList<String>();
			List<String> headerNameList = new ArrayList<String>();

			headerList.add("one");
			headerList.add("two");
			headerList.add("three");
			headerList.add("four");
			headerList.add("five");
			headerList.add("six");
			headerList.add("seven");
			// headerList.add("eight");
			headerNameList.add("页码");
			headerNameList.add("ASIN");
			headerNameList.add("标题");
			headerNameList.add("价钱");
			headerNameList.add("评价星级");
			headerNameList.add("评价数量");
			headerNameList.add("顺序");
			// headerNameList.add("jp有此产品");

			try {
				exUtil.exportExcel(code, headerNameList, headerList, comments);
			} catch (Exception e) {
				log.error("", e);
			}

			exUtil.exportExcel(code, headerNameList, headerList, comments);
		} catch (Exception ee) {
			log.info("错误" + ee);
			// webDriver.close();
		}

		webDriver.close();

	}

	/**
	 * @param code
	 * @param response
	 * @param base
	 */
	// @Transactional
	public void searchJpNode(List<String> str2, HttpServletResponse response) {

		
		// System.setProperty("webdriver.chrome.driver","C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
		// mac
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

		WebDriver webDriver = new ChromeDriver();

		System.out.println(
				"打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println(
				"页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// 设定网址
		// String base =
		// "https://www.amazon.co.jp/s?k=%E9%9B%BB%E5%8B%95%E3%82%AA%E3%83%8A%E3%83%9B%E3%83%BC%E3%83%AB&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss_2";
		// String base =
		// "https://www.amazon.co.jp/s?k=%E3%82%AA%E3%83%8A%E3%83%9B&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss";

		for (int indexCode = 0; indexCode < str2.size(); indexCode++) {
			//String code = str2.get(indexCode);
			String base = str2.get(indexCode);
			//base = String.format(base, code);

			webDriver.get(base);
			
			
			
			
			webDriver.get(base);

			// 显示等待控制对象
			WebDriverWait webDriverWait1 = new WebDriverWait(webDriver, 10);

			WebDriverWait webDriverWait = null;
			//Thread.sleep(2000);
			JavascriptExecutor executor1 = (JavascriptExecutor) webDriver;
			executor1.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			int[] index = { 1 };
			try {
				List<Node> comments = new ArrayList<>();
				for (int i = 0; i < 500; i++) {
					
					WebElement products = webDriver.findElement(By.className("s-main-slot"));
					List<WebElement> reviews = products.findElements(By.xpath(".//div[@data-asin!='']"));
					String url = webDriver.getCurrentUrl();
					String tt = (i + 1) + "";
					String[] str = { tt };
					System.out.print("第" + str[0] + "页");
					if (url.contains("page=")) {
						int li = url.indexOf("page=");
						str[0] = url.substring(li + 5, li + 10 > url.length() ? url.length() : li + 10);
						str[0] = str[0].replaceAll("&", "");
						str[0] = str[0].replaceAll("_", "");
						str[0] = str[0].replaceAll("e", "");
						str[0] = str[0].replaceAll("n", "");
						log.info("str[0]" + str[0]);
					}

					int[] num = { 1 };
					reviews.forEach(e -> {
						Node commont = new Node();
						String asin = e.getAttribute("data-asin");
						commont.setOne(str[0]);
						commont.setTwo(asin);
						String className = e.getAttribute("class");
						System.out.print("reviews classname" + className);
						boolean isSmall = true;
						//广告
						if (className.contains("a-section")) {
							return;
						}
						if (isSmall) {
							try {
								List<WebElement> small = e.findElements(By.className("a-spacing-top-small"));

								// 第一标题
								WebElement title = small.get(0).findElement(By.className("a-size-mini"));
								commont.setThree(title.getText());
								commont.setSeven(index[0] + "");
								index[0] = index[0] + 1;
								num[0] = num[0] + 1;
								// 价钱
								WebElement price = null;
								WebElement price1 = null;
								Boolean test = false;
								try {
									price = small.get(1).findElement(By.className("a-price-whole"));
									// price1 = small.get(1).findElement(By.className("a-price-fraction"));
									String priceStr = "";
									String priceStr1 = "";
									priceStr = price.getText();
									// priceStr1 = price1.getText();
									priceStr = priceStr.replaceAll("￥", "");
									priceStr = priceStr.replaceAll(",", "");
									commont.setFour(priceStr + "." + priceStr1);
								} catch (Exception ee) {
									test = true;
									log.info("title" + title);
									log.info("无存");
									//throw new Exception("无存" + ee.getMessage());
								}
								
								List<WebElement> micro = e.findElements(By.className("a-spacing-top-micro"));
								// 如果 有两个表示有平
								// 第二平
								if (micro.size() == 2) {
									WebElement ping = micro.get(0).findElement(By.className("a-size-small"));

									List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
									// 平分
									String startString = pings.get(0).getAttribute("aria-label");
									//startString = startString.substring(0, 3);
									commont.setFive(startString);
									// 数量
									commont.setSix(pings.get(1).getAttribute("aria-label"));
									if(test) {
										try {
											//WebElement stock = micro.get(1).findElement(By.className("a-size-small"));
											List<WebElement> stockText = micro.get(1).findElements(By.xpath(".//span[@aria-label]"));
											String stockTextString = stockText.get(0).getAttribute("aria-label");
											commont.setEleven(stockTextString);
										}catch(Exception e1) {
											log.info("在库获取错误");
										}
									}
									
								}
								if (micro.size() == 3) {
									WebElement ping = micro.get(1).findElement(By.className("a-size-small"));

									List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
									// 平分
									String startString = pings.get(0).getAttribute("aria-label");
									startString = startString.substring(0, 3);
									commont.setFive(startString);
									// 数量
									commont.setSix(pings.get(1).getAttribute("aria-label"));
									//在
								}

								
							} catch (Exception ce) {
								log.info("无评价", ce);
							}
						} else {
							try {
								List<WebElement> small = e.findElements(By.className("a-spacing-top-small"));
								WebElement title = small.get(1);
								commont.setThree(title.getText());
								commont.setSeven(index[0] + "");
								index[0] = index[0] + 1;
								num[0] = num[0] + 1;

								WebElement price = null;
								WebElement price1 = null;
								try {
									WebElement p = e.findElement(By.className("a-price"));
									price = p.findElement(By.className("a-price-whole"));
									price1 = p.findElement(By.className("a-price-fraction"));
									String priceStr = "";
									String priceStr1 = "";
									priceStr = price.getText();
									priceStr1 = price1.getText();
									priceStr = priceStr.replaceAll("￥", "");
									priceStr = priceStr.replaceAll(",", "");
									commont.setFour(priceStr + "." + priceStr1);
								} catch (Exception ee) {
									log.info("无存");
									throw new Exception("无存" + ee.getMessage());
								}

								WebElement ping = e.findElement(By.className("a-size-small"));
								List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
								// 平分
								String startString = pings.get(0).getAttribute("aria-label");
								startString = startString.substring(0, 3);
								commont.setFive(startString);
								// 数量
								commont.setSix(pings.get(1).getAttribute("aria-label"));
							} catch (Exception ex1) {
								log.info("无评价", ex1);
							}
						}
						commont.setEight("210108021");
						commont.setNine(base);
						comments.add(commont);
						log.info("第" + str[0] + "页 第" + num[0] + "个" + JSON.toJSONString(commont));

						if (comments.size() >= 100) {
							nodeRepository.saveAll(comments);
							comments.clear();
						}

					});
					nodeRepository.saveAll(comments);
					// if (reviews == null) {
					// if (reviews == null || reviews.size() != 24) {
					// break;
					// }
					
					// 断点
					url = webDriver.getCurrentUrl();
					if (url.contains("page=")) {
						String[] arr = url.split("page=");
						String[] numarr = arr[1].split("&");
						if (numarr.length == 1) {
							url = arr[0] + "page=" + (Integer.valueOf(numarr[0].split("&")[0]) + 1);
						} else {
							url = arr[0] + "page=" + (Integer.valueOf(numarr[0].split("&")[0]) + 1) + "&" + numarr[1];
						}

						System.out.println("url  " + url);
						webDriver.get(url);
						webDriverWait = new WebDriverWait(webDriver, 10);
					} else {
						url = url + "&page=2";
						webDriver.get(url);
						webDriverWait = new WebDriverWait(webDriver, 10);
					}
					
					
					WebElement next = null;
					try {
						System.out.println("点下一页");
						next = webDriver.findElement(By.className("a-last"));
					} catch (Exception e) {
						System.out.println("最后一页");
							break;
						

					}
                    
					String classs = next.getAttribute("class");
					System.out.println("classs" + classs);
                      
					if (classs.contains("a-disabled")) {
						break;
					} else {
						/**
						try {
							System.out.println("再点下一页");
							JavascriptExecutor executor = (JavascriptExecutor) webDriver;
							executor.executeScript("arguments[0].click();", next);
							Thread.sleep(1000 * 10);

							JavascriptExecutor executor2 = (JavascriptExecutor) webDriver;
							executor2.executeScript("window.scrollTo(0,document.body.scrollHeight-500)");
						} catch (ElementClickInterceptedException e4) {
							log.info("下一页错误e4", e4);
							JavascriptExecutor executor = (JavascriptExecutor) webDriver;
							executor.executeScript("arguments[0].click();", next);

							JavascriptExecutor executor2 = (JavascriptExecutor) webDriver;
							executor2.executeScript("window.scrollTo(0,document.body.scrollHeight-500)");
						} catch (Exception e5) {

							log.info("下一页错误", e5);
						}**/
					}
				}

				ExcelUtil exUtil = new ExcelUtil();
				List<String> headerList = new ArrayList<String>();
				List<String> headerNameList = new ArrayList<String>();

				headerList.add("one");
				headerList.add("two");
				headerList.add("three");
				headerList.add("four");
				headerList.add("five");
				headerList.add("six");
				headerList.add("seven");
				// headerList.add("eight");
				headerNameList.add("页码");
				headerNameList.add("ASIN");
				headerNameList.add("标题");
				headerNameList.add("价钱");
				headerNameList.add("评价星级");
				headerNameList.add("评价数量");
				headerNameList.add("顺序");
				// headerNameList.add("jp有此产品");

				try {
					exUtil.exportExcel(base, headerNameList, headerList, comments);
				} catch (Exception e) {
					log.error("", e);
					webDriver.close();
					webDriver.quit();
				}

				exUtil.exportExcel(base, headerNameList, headerList, comments);
			} catch (Exception ee) {
				log.info("错误", ee);
				// webDriver.close();
				// webDriver.quit();
			} finally {
				// webDriver.close();
				// webDriver.quit();
			}
			// webDriver.close();
			// webDriver.quit();
		}

	}
	

	/**
	 * @param code
	 * @param response
	 * @param base
	 */
	// @Transactional
	public void searchFrNode(List<String> str2, HttpServletResponse response) {

		
		// System.setProperty("webdriver.chrome.driver","C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
		// mac
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

		WebDriver webDriver = new ChromeDriver();

		System.out.println(
				"打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println(
				"页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// 设定网址
		// String base =
		// "https://www.amazon.co.jp/s?k=%E9%9B%BB%E5%8B%95%E3%82%AA%E3%83%8A%E3%83%9B%E3%83%BC%E3%83%AB&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss_2";
		// String base =
		// "https://www.amazon.co.jp/s?k=%E3%82%AA%E3%83%8A%E3%83%9B&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss";

		for (int indexCode = 0; indexCode < str2.size(); indexCode++) {
			String code = str2.get(indexCode);
			String base = "https://www.amazon.fr/b?node=%s&ref=dp_bc_3";
			base = String.format(base, code);

			webDriver.get(base);

			// 显示等待控制对象
			WebDriverWait webDriverWait1 = new WebDriverWait(webDriver, 10);

			WebDriverWait webDriverWait = null;
			try {
				// 第一次需要选地区
				if (indexCode == 0) {
					// 选择地区
					WebElement location = webDriver.findElement(By.id("nav-global-location-slot"));
					if (location != null) {
						location.click();
					}

					Thread.sleep(1000);
					
					

					WebElement input = webDriver.findElement(By.className("GLUX_Full_Width"));
					if (input != null) {
						// input.sendKeys("10041");
						input.sendKeys("75000");
					}
					Thread.sleep(1000);
					WebElement set = webDriver.findElement(By.xpath(".//span[@data-action='GLUXPostalUpdateAction']"));
					log.info("tt" + (set == null));
					if (set != null) {
						// 设置
						set.click();
						Thread.sleep(2000);
						// webDriver.navigate().to(base + code); //方法二
						// WebElement finish =
						// webDriver.findElement(By.xpath(".//span[@data-action='GLUXConfirmAction']"));
						// JavascriptExecutor executor = (JavascriptExecutor)webDriver;
						// executor.executeScript("arguments[0].click();", finish);
						Thread.sleep(2000);
						webDriver.get(base);

						// 显示等待控制对象
						webDriverWait = new WebDriverWait(webDriver, 10);
						Thread.sleep(2000);
						webDriver.get(base);
						webDriverWait = new WebDriverWait(webDriver, 10);
						// 筛选 4分以上
						try {
							WebElement commentList = webDriver.findElement(By.id("reviewsRefinements"));
							WebElement star4 = commentList.findElement(By.className("a-star-medium-4"));
							star4.click();
						} catch (ElementClickInterceptedException e4) {
							log.info("有星筛点不了", e4);
							WebElement commentList = webDriver.findElement(By.id("reviewsRefinements"));
							WebElement star4 = commentList.findElement(By.className("a-star-medium-4"));
							JavascriptExecutor executor = (JavascriptExecutor) webDriver;
							executor.executeScript("arguments[0].click();", star4);
						} catch (Exception e5) {
							log.info("无星筛", e5);
						}
						Thread.sleep(3000);
					}

				} else {

					// 筛选 4分以上
					try {
						WebElement commentList = webDriver.findElement(By.id("reviewsRefinements"));
						WebElement star4 = commentList.findElement(By.className("a-star-medium-4"));
						star4.click();
					} catch (ElementClickInterceptedException e4) {
						log.info("有星筛点不了", e4);
						WebElement commentList = webDriver.findElement(By.id("reviewsRefinements"));
						WebElement star4 = commentList.findElement(By.className("a-star-medium-4"));
						JavascriptExecutor executor = (JavascriptExecutor) webDriver;
						executor.executeScript("arguments[0].click();", star4);
					} catch (Exception e5) {
						log.info("无星筛", e5);
					}
					Thread.sleep(3000);

				}
				// 断点
				String url = webDriver.getCurrentUrl();
				if (code.equals("227257031")) {
					if (url.contains("page=1")) {
						url = url.replaceAll("page=1", "page=102");
						webDriver.get(url);
						webDriverWait = new WebDriverWait(webDriver, 10);
					} else {
						url = url + "&page=102";
						webDriver.get(url);
						webDriverWait = new WebDriverWait(webDriver, 10);
					}
				}
				Thread.sleep(2000);
				JavascriptExecutor executor1 = (JavascriptExecutor) webDriver;
				executor1.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			} catch (Exception e) {

				log.info("无确认", e);
			}

			int[] index = { 1 };
			try {
				List<Node> comments = new ArrayList<>();
				for (int i = 0; i < 500; i++) {
					WebElement products = webDriver.findElement(By.className("s-main-slot"));
					List<WebElement> reviews = products.findElements(By.xpath(".//div[@data-asin!='']"));
					String url = webDriver.getCurrentUrl();
					String tt = (i + 1) + "";
					String[] str = { tt };
					System.out.print("第" + str[0] + "页");
					if (url.contains("page=")) {
						int li = url.indexOf("page=");
						str[0] = url.substring(li + 5, li + 10 > url.length() ? url.length() : li + 10);
						str[0] = str[0].replaceAll("&", "");
						str[0] = str[0].replaceAll("_", "");
						str[0] = str[0].replaceAll("e", "");
						str[0] = str[0].replaceAll("n", "");
						log.info("str[0]" + str[0]);
					}

					int[] num = { 1 };
					reviews.forEach(e -> {
						Node commont = new Node();
						String asin = e.getAttribute("data-asin");
						commont.setOne(str[0]);
						commont.setTwo(asin);
						String className = e.getAttribute("class");
						System.out.print("reviews classname" + className);
						boolean isSmall = true;
						//广告
						if (className.contains("a-section")) {
							return;
						}
						if (isSmall) {
							try {
								List<WebElement> small = e.findElements(By.className("a-spacing-top-small"));

								// 第一标题
								WebElement title = small.get(0).findElement(By.className("a-size-mini"));
								commont.setThree(title.getText());
								commont.setSeven(index[0] + "");
								index[0] = index[0] + 1;
								num[0] = num[0] + 1;
								// 价钱
								WebElement price = null;
								WebElement price1 = null;
								try {
									price = small.get(1).findElement(By.className("a-price-whole"));
									// price1 = small.get(1).findElement(By.className("a-price-fraction"));

								} catch (Exception ee) {
									log.info("无存");
									throw new Exception("无存" + ee.getMessage());
								}
								String priceStr = "";
								String priceStr1 = "";
								priceStr = price.getText();
								// priceStr1 = price1.getText();
								priceStr = priceStr.replaceAll("￥", "");
								priceStr = priceStr.replaceAll(",", "");
								commont.setFour(priceStr + "." + priceStr1);
								List<WebElement> micro = e.findElements(By.className("a-spacing-top-micro"));
								// 如果 有两个表示有平
								// 第二平
								if (micro.size() == 2) {
									WebElement ping = micro.get(0).findElement(By.className("a-size-small"));

									List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
									// 平分
									String startString = pings.get(0).getAttribute("aria-label");
									startString = startString.substring(0, 3);
									commont.setFive(startString);
									// 数量
									commont.setSix(pings.get(1).getAttribute("aria-label"));
								}
								if (micro.size() == 3) {
									WebElement ping = micro.get(1).findElement(By.className("a-size-small"));

									List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
									// 平分
									String startString = pings.get(0).getAttribute("aria-label");
									startString = startString.substring(0, 3);
									commont.setFive(startString);
									// 数量
									commont.setSix(pings.get(1).getAttribute("aria-label"));
								}

								
							} catch (Exception ce) {
								log.info("无评价", ce);
							}
						} else {
							try {
								List<WebElement> small = e.findElements(By.className("a-spacing-top-small"));
								WebElement title = small.get(1);
								commont.setThree(title.getText());
								commont.setSeven(index[0] + "");
								index[0] = index[0] + 1;
								num[0] = num[0] + 1;

								WebElement price = null;
								WebElement price1 = null;
								try {
									WebElement p = e.findElement(By.className("a-price"));
									price = p.findElement(By.className("a-price-whole"));
									price1 = p.findElement(By.className("a-price-fraction"));
									String priceStr = "";
									String priceStr1 = "";
									priceStr = price.getText();
									priceStr1 = price1.getText();
									priceStr = priceStr.replaceAll("￥", "");
									priceStr = priceStr.replaceAll(",", "");
									commont.setFour(priceStr + "." + priceStr1);
								} catch (Exception ee) {
									log.info("无存");
									throw new Exception("无存" + ee.getMessage());
								}

								WebElement ping = e.findElement(By.className("a-size-small"));
								List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
								// 平分
								String startString = pings.get(0).getAttribute("aria-label");
								startString = startString.substring(0, 3);
								commont.setFive(startString);
								// 数量
								commont.setSix(pings.get(1).getAttribute("aria-label"));
							} catch (Exception ex1) {
								log.info("无评价", ex1);
							}
						}
						commont.setEight("frLight");
						commont.setNine(code);
						comments.add(commont);
						log.info("第" + str[0] + "页 第" + num[0] + "个" + JSON.toJSONString(commont));

						if (comments.size() >= 100) {
							nodeRepository.saveAll(comments);
							comments.clear();
						}

					});
					nodeRepository.saveAll(comments);
					// if (reviews == null) {
					// if (reviews == null || reviews.size() != 24) {
					// break;
					// }
					
					// 断点
					url = webDriver.getCurrentUrl();
					if (url.contains("page=")) {
						String[] arr = url.split("page=");
						String[] numarr = arr[1].split("&");
						if (numarr.length == 1) {
							url = arr[0] + "page=" + (Integer.valueOf(numarr[0].split("&")[0]) + 1);
						} else {
							url = arr[0] + "page=" + (Integer.valueOf(numarr[0].split("&")[0]) + 1) + "&" + numarr[1];
						}

						System.out.println("url  " + url);
						webDriver.get(url);
						webDriverWait = new WebDriverWait(webDriver, 10);
					} else {
						url = url + "&page=2";
						webDriver.get(url);
						webDriverWait = new WebDriverWait(webDriver, 10);
					}
					
					
					WebElement next = null;
					try {
						System.out.println("点下一页");
						next = webDriver.findElement(By.className("a-last"));
					} catch (Exception e) {
						System.out.println("最后一页");
							break;
						

					}
                    
					String classs = next.getAttribute("class");
					System.out.println("classs" + classs);
                      
					if (classs.contains("a-disabled")) {
						break;
					} else {
						/**
						try {
							System.out.println("再点下一页");
							JavascriptExecutor executor = (JavascriptExecutor) webDriver;
							executor.executeScript("arguments[0].click();", next);
							Thread.sleep(1000 * 10);

							JavascriptExecutor executor2 = (JavascriptExecutor) webDriver;
							executor2.executeScript("window.scrollTo(0,document.body.scrollHeight-500)");
						} catch (ElementClickInterceptedException e4) {
							log.info("下一页错误e4", e4);
							JavascriptExecutor executor = (JavascriptExecutor) webDriver;
							executor.executeScript("arguments[0].click();", next);

							JavascriptExecutor executor2 = (JavascriptExecutor) webDriver;
							executor2.executeScript("window.scrollTo(0,document.body.scrollHeight-500)");
						} catch (Exception e5) {

							log.info("下一页错误", e5);
						}**/
					}
				}

				ExcelUtil exUtil = new ExcelUtil();
				List<String> headerList = new ArrayList<String>();
				List<String> headerNameList = new ArrayList<String>();

				headerList.add("one");
				headerList.add("two");
				headerList.add("three");
				headerList.add("four");
				headerList.add("five");
				headerList.add("six");
				headerList.add("seven");
				// headerList.add("eight");
				headerNameList.add("页码");
				headerNameList.add("ASIN");
				headerNameList.add("标题");
				headerNameList.add("价钱");
				headerNameList.add("评价星级");
				headerNameList.add("评价数量");
				headerNameList.add("顺序");
				// headerNameList.add("jp有此产品");

				try {
					exUtil.exportExcel(code, headerNameList, headerList, comments);
				} catch (Exception e) {
					log.error("", e);
					webDriver.close();
					webDriver.quit();
				}

				exUtil.exportExcel(code, headerNameList, headerList, comments);
			} catch (Exception ee) {
				log.info("错误", ee);
				// webDriver.close();
				// webDriver.quit();
			} finally {
				// webDriver.close();
				// webDriver.quit();
			}
			// webDriver.close();
			// webDriver.quit();
		}

	}
	
	
	
	/**
	 * @param code
	 * @param response
	 * @param base
	 */
	// @Transactional
	public void searchUkNode(List<String> str2, HttpServletResponse response) {

		// System.setProperty("webdriver.chrome.driver","C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
		// mac
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

		WebDriver webDriver = new ChromeDriver();

		System.out.println(
				"打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println(
				"页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// 设定网址
		// String base =
		// "https://www.amazon.co.jp/s?k=%E9%9B%BB%E5%8B%95%E3%82%AA%E3%83%8A%E3%83%9B%E3%83%BC%E3%83%AB&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss_2";
		// String base =
		// "https://www.amazon.co.jp/s?k=%E3%82%AA%E3%83%8A%E3%83%9B&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss";

		for (int indexCode = 0; indexCode < str2.size(); indexCode++) {
			String code = str2.get(indexCode);
			String base = "https://www.amazon.co.uk/b?node=%s&ref=dp_bc_3";
			base = String.format(base, code);

			webDriver.get(base);

			// 显示等待控制对象
			WebDriverWait webDriverWait1 = new WebDriverWait(webDriver, 10);

			WebDriverWait webDriverWait = null;
			try {
				// 第一次需要选地区
				if (indexCode == 0) {
					// 选择地区
					WebElement location = webDriver.findElement(By.id("nav-global-location-slot"));
					if (location != null) {
						location.click();
					}

					Thread.sleep(1000);

					WebElement input = webDriver.findElement(By.className("GLUX_Full_Width"));
					if (input != null) {
						input.sendKeys("MK43 0ZA");
					}
					Thread.sleep(1000);
					WebElement set = webDriver.findElement(By.xpath(".//span[@data-action='GLUXPostalUpdateAction']"));
					log.info("tt" + (set == null));
					if (set != null) {
						// 设置
						set.click();
						Thread.sleep(2000);
						// webDriver.navigate().to(base + code); //方法二
						// WebElement finish =
						// webDriver.findElement(By.xpath(".//span[@data-action='GLUXConfirmAction']"));
						// JavascriptExecutor executor = (JavascriptExecutor)webDriver;
						// executor.executeScript("arguments[0].click();", finish);
						Thread.sleep(2000);
						webDriver.get(base);

						// 显示等待控制对象
						webDriverWait = new WebDriverWait(webDriver, 10);
						Thread.sleep(2000);
						webDriver.get(base);
						webDriverWait = new WebDriverWait(webDriver, 10);
						// 筛选 4分以上
						try {
							WebElement commentList = webDriver.findElement(By.id("reviewsRefinements"));
							WebElement star4 = commentList.findElement(By.className("a-star-medium-4"));
							star4.click();
						} catch (ElementClickInterceptedException e4) {
							log.info("有星筛点不了", e4);
							WebElement commentList = webDriver.findElement(By.id("reviewsRefinements"));
							WebElement star4 = commentList.findElement(By.className("a-star-medium-4"));
							JavascriptExecutor executor = (JavascriptExecutor) webDriver;
							executor.executeScript("arguments[0].click();", star4);
						} catch (Exception e5) {
							log.info("无星筛", e5);
						}
						Thread.sleep(3000);
					}

				} else {

					// 筛选 4分以上
					try {
						WebElement commentList = webDriver.findElement(By.id("reviewsRefinements"));
						WebElement star4 = commentList.findElement(By.className("a-star-medium-4"));
						star4.click();
					} catch (ElementClickInterceptedException e4) {
						log.info("有星筛点不了", e4);
						WebElement commentList = webDriver.findElement(By.id("reviewsRefinements"));
						WebElement star4 = commentList.findElement(By.className("a-star-medium-4"));
						JavascriptExecutor executor = (JavascriptExecutor) webDriver;
						executor.executeScript("arguments[0].click();", star4);
					} catch (Exception e5) {
						log.info("无星筛", e5);
					}
					Thread.sleep(3000);

				}
				// 断点
				String url = webDriver.getCurrentUrl();
				if (code.equals("28262490311")) {
					if (url.contains("page=1")) {
						url = url.replaceAll("page=1", "page=12");
						webDriver.get(url);
						webDriverWait = new WebDriverWait(webDriver, 10);
					} else {
						url = url + "&page=12";
						webDriver.get(url);
						webDriverWait = new WebDriverWait(webDriver, 10);
					}
				}
				Thread.sleep(2000);
				JavascriptExecutor executor1 = (JavascriptExecutor) webDriver;
				executor1.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			} catch (Exception e) {

				log.info("无确认", e);
			}

			int[] index = { 1 };
			try {
				List<Node> comments = new ArrayList<>();
				for (int i = 0; i < 500; i++) {
					WebElement products = webDriver.findElement(By.className("s-main-slot"));
					List<WebElement> reviews = products.findElements(By.xpath(".//div[@data-asin!='']"));
					String url = webDriver.getCurrentUrl();
					String tt = (i + 1) + "";
					String[] str = { tt };
					System.out.print("第" + str[0] + "页");
					if (url.contains("page=")) {
						int li = url.indexOf("page=");
						str[0] = url.substring(li + 5, li + 10 > url.length() ? url.length() : li + 10);
						str[0] = str[0].replaceAll("&", "");
						str[0] = str[0].replaceAll("_", "");
						str[0] = str[0].replaceAll("e", "");
						str[0] = str[0].replaceAll("n", "");
						log.info("str[0]" + str[0]);
					}

					int[] num = { 1 };
					reviews.forEach(e -> {
						Node commont = new Node();
						String asin = e.getAttribute("data-asin");
						commont.setOne(str[0]);
						commont.setTwo(asin);
						String className = e.getAttribute("class");
						//广告
						if(className.contains("a-section")) {
							return;
						}
						
						// System.out.print(className);
						boolean isSmall = true;
						if (className.contains("sg-col-20-of-24")) {
							isSmall = false;
						}
						if (!isSmall) {
							try {
								List<WebElement> small = e.findElements(By.className("a-spacing-top-small"));

								// 第一标题
								WebElement title = small.get(0).findElement(By.className("a-size-mini"));
								commont.setThree(title.getText());
								commont.setSeven(index[0] + "");
								index[0] = index[0] + 1;
								num[0] = num[0] + 1;
								// 价钱
								WebElement price = null;
								WebElement price1 = null;
								try {
									price = small.get(1).findElement(By.className("a-price-whole"));
									// price1 = small.get(1).findElement(By.className("a-price-fraction"));

								} catch (Exception ee) {
									log.info("无存");
									throw new Exception("无存" + ee.getMessage());
								}
								String priceStr = "";
								String priceStr1 = "";
								priceStr = price.getText();
								// priceStr1 = price1.getText();
								priceStr = priceStr.replaceAll("￥", "");
								priceStr = priceStr.replaceAll(",", "");
								commont.setFour(priceStr + "." + priceStr1);
								List<WebElement> micro = e.findElements(By.className("a-spacing-top-micro"));
								// 如果 有两个表示有平
								// 第二平
								if (micro.size() == 2) {
									WebElement ping = micro.get(0).findElement(By.className("a-size-small"));

									List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
									// 平分
									String startString = pings.get(0).getAttribute("aria-label");
									startString = startString.substring(0, 3);
									commont.setFive(startString);
									// 数量
									commont.setSix(pings.get(1).getAttribute("aria-label"));
								}
								if (micro.size() == 3) {
									WebElement ping = micro.get(1).findElement(By.className("a-size-small"));

									List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
									// 平分
									String startString = pings.get(0).getAttribute("aria-label");
									startString = startString.substring(0, 3);
									commont.setFive(startString);
									// 数量
									commont.setSix(pings.get(1).getAttribute("aria-label"));
								}

								// 查询日本是否存在此产品
								// 当前页
								// String home = webDriver.getWindowHandle();
								// Boolean isExist = isJpExist(commont.getTwo(), home, webDriver);
								// webDriver.switchTo().window(home);
								// if(isExist) {
								// log.info(commont.getTwo() + " exist" + " 1");
								// commont.setEight("1");
								// }else {
								// log.info(commont.getTwo() + " exist" + " 0");
								// commont.setEight("0");
								// }
							} catch (Exception ce) {
								log.info("无评价", ce);
							}
						} else {
							try {
								List<WebElement> small = e.findElements(By.className("a-size-mini"));
								WebElement title = small.get(0);
								commont.setThree(title.getText());
								commont.setSeven(index[0] + "");
								index[0] = index[0] + 1;
								num[0] = num[0] + 1;

								WebElement price = null;
								WebElement price1 = null;
								try {
									WebElement p = e.findElement(By.className("a-price"));
									price = p.findElement(By.className("a-price-whole"));
									price1 = p.findElement(By.className("a-price-fraction"));
									String priceStr = "";
									String priceStr1 = "";
									priceStr = price.getText();
									priceStr1 = price1.getText();
									priceStr = priceStr.replaceAll("￥", "");
									priceStr = priceStr.replaceAll(",", "");
									commont.setFour(priceStr + "." + priceStr1);
								} catch (Exception ee) {
									log.info("无存");
									throw new Exception("无存" + ee.getMessage());
								}

								WebElement ping = e.findElement(By.className("a-size-small"));
								List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
								// 平分
								String startString = pings.get(0).getAttribute("aria-label");
								startString = startString.substring(0, 3);
								commont.setFive(startString);
								// 数量
								commont.setSix(pings.get(1).getAttribute("aria-label"));
								
							} catch (Exception ex1) {
								log.info("无评价", ex1);
							}
						}
						commont.setNine(code);
						commont.setEight("uk0323");
						if(commont.getFive() != null) {
							comments.add(commont);
						}
						
						log.info("第" + str[0] + "页 第" + num[0] + "个" + JSON.toJSONString(commont));

						if (comments.size() >= 100) {
							nodeRepository.saveAll(comments);
							comments.clear();
						}

					});
					nodeRepository.saveAll(comments);
					// if (reviews == null) {
					// if (reviews == null || reviews.size() != 24) {
					// break;
					// }
					WebElement next = null;
					try {
						next = webDriver.findElement(By.className("a-last"));
					} catch (Exception e) {
						Thread.sleep(1000 * 5);
						try {
							next = webDriver.findElement(By.className("a-last"));
						} catch (Exception es) {
							break;
						}

					}

					String classs = next.getAttribute("class");
					System.out.println("classs" + classs);

					if (classs.contains("a-disabled")) {
						break;
					} else {
						try {
							next.click();
							Thread.sleep(1000 * 10);

							JavascriptExecutor executor2 = (JavascriptExecutor) webDriver;
							executor2.executeScript("window.scrollTo(0,document.body.scrollHeight-500)");
						} catch (ElementClickInterceptedException e4) {

							JavascriptExecutor executor = (JavascriptExecutor) webDriver;
							executor.executeScript("arguments[0].click();", next);

							JavascriptExecutor executor2 = (JavascriptExecutor) webDriver;
							executor2.executeScript("window.scrollTo(0,document.body.scrollHeight-500)");
						} catch (Exception e5) {

							log.info("下一页错误", e5);
						}
					}
				}

				ExcelUtil exUtil = new ExcelUtil();
				List<String> headerList = new ArrayList<String>();
				List<String> headerNameList = new ArrayList<String>();

				headerList.add("one");
				headerList.add("two");
				headerList.add("three");
				headerList.add("four");
				headerList.add("five");
				headerList.add("six");
				headerList.add("seven");
				// headerList.add("eight");
				headerNameList.add("页码");
				headerNameList.add("ASIN");
				headerNameList.add("标题");
				headerNameList.add("价钱");
				headerNameList.add("评价星级");
				headerNameList.add("评价数量");
				headerNameList.add("顺序");
				// headerNameList.add("jp有此产品");

				try {
					//exUtil.exportExcel(code, headerNameList, headerList, comments);
				} catch (Exception e) {
					log.error("", e);
					webDriver.close();
					webDriver.quit();
				}

				//exUtil.exportExcel(code, headerNameList, headerList, comments);
			} catch (Exception ee) {
				log.info("错误", ee);
				// webDriver.close();
				// webDriver.quit();
			} finally {
				 //webDriver.close();
				// webDriver.quit();
			}
			// webDriver.close();
			 //webDriver.quit();
		}

	}
	/**
	 * 
	 */
	public static void isbn() {

		String prefix = "978";

		String country = "977";

		String str = new Random().nextInt(999999) + "";

		String num = prefix + country + str;
		int sum = 0;
		for (int i = 1; i <= num.length(); i++) {
			if (i % 2 != 0) {
				int tmp = Integer.parseInt(String.valueOf(num.charAt(i - 1)));
				System.out.println(tmp);
				sum = sum + tmp;
			} else {
				int tmp = Integer.parseInt(String.valueOf(num.charAt(i - 1))) * 3;
				System.out.println(tmp);
				sum = sum + tmp;
			}
		}
		System.out.println(sum);
		int less = sum % 10;
		int check = 10 - less;

		num = num + check;
		System.out.println(num);
	}

	public static void main2(String args[]) {
		isbn();
	}

	public void checkjp(List<String> str2, String num) {

		// System.setProperty("webdriver.chrome.driver","C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
		// mac
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

		WebDriver webDriver = new ChromeDriver();

		System.out.println(
				"打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println(
				"页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		int size = str2.size();
		Set<String> exist = new HashSet<>();//node1Repository.findAll().stream().map(e->e.getOne()).collect(Collectors.toSet());
		for (int i = 0; i < size; i++) {

			String asin = str2.get(i);
			if(exist.contains(asin)) {
				continue;
			}
			log.info(i + " " + asin + " less " + (size - i));
			try {
				String base = "https://www.amazon.co.jp/dp/%s";
				base = String.format(base, asin);
				webDriver.get(base);
				// 显示等待控制对象
				WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);
				try {
					WebElement alert = webDriver.findElement(By.className("alert"));
					WebElement center = webDriver.findElement(By.tagName("center"));
					List<WebElement> divs = center.findElements(By.tagName("div"));
					divs.get(1).click();
				} catch (Exception e1) {
					log.info("无alert");
				}

				WebElement pingStar = webDriver.findElement(By.id("acrPopover"));

				WebElement pingNum = webDriver.findElement(By.id("acrCustomerReviewText"));
				String lineInfo = "";
				try {
					WebElement bylineInfo = webDriver.findElement(By.id("bylineInfo"));
					lineInfo = bylineInfo.getText();
				}catch(Exception e2) {
					log.info("无lineInfo");
				}
				
				log.info("as: " + asin + " star: " + pingStar.getAttribute("title") + " num: " + pingNum.getText() + " lineInfo " + lineInfo);
				
				Node1 node = new Node1();
				node.setOne(asin);
				node.setTwo(pingStar.getAttribute("title"));
				node.setThree(pingNum.getText());
				node.setFour(lineInfo);
				node.setFive(num);
				List<Node1> nodes = new ArrayList<>();
				nodes.add(node);
				node1Repository.saveAll(nodes);
			} catch (Exception e) {
				Node1 node = new Node1();
				node.setOne(asin);
				node.setFive(num);
				List<Node1> nodes = new ArrayList<>();
				nodes.add(node);
				node1Repository.saveAll(nodes);
				log.info("无 " + asin, e);
			}

		}

	}
	
	public void checkCa(List<Node> nodes, String num) {

		// System.setProperty("webdriver.chrome.driver","C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
		// mac
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

		WebDriver webDriver = new ChromeDriver();

		System.out.println(
				"打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println(
				"页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		int size = nodes.size();
		for (int i = 0; i < size; i++) {

			Node node = nodes.get(i);
			
			log.info(i + " " + node.getTwo() + " less " + (size - i));
			try {
				String base = "https://www.amazon.ca/dp/%s";
				base = String.format(base, node.getTwo());
				webDriver.get(base);
				// 显示等待控制对象
				WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);
			
				WebElement price = webDriver.findElement(By.className("a-color-price"));
				// 需要记录此种 商品 Currently unavailable.
				String text = price.getText();
				if(!text.contains("unavailable")) {
					continue;
				}
				node.setTen(text);
				//品牌
				String lineInfo = "";
				try {
					WebElement bylineInfo = webDriver.findElement(By.id("bylineInfo"));
					lineInfo = bylineInfo.getText();
				}catch(Exception e2) {
					log.info("无lineInfo");
				}
				WebElement pingStar = webDriver.findElement(By.id("acrPopover"));
				WebElement pingNum = webDriver.findElement(By.id("acrCustomerReviewText"));

				//node.setOne(asin);
				//node.setTwo(pingStar.getAttribute("title"));
				node.setThree(pingNum.getText());
				node.setFour(lineInfo);
				node.setFive(pingStar.getAttribute("title"));
				node.setSix("ca");
				nodeRepository.save(node);
			} catch (Exception e) {}

		}

	}

	/**
	 * 
	 */
	public void chooseUs() {
		List<NodeId> nodeIds = nodeIdRepository.findAll();

		// System.setProperty("webdriver.chrome.driver","C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
		// mac
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

		WebDriver webDriver = new ChromeDriver();

		System.out.println(
				"打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println(
				"页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		// 设定网址
		// String base =
		// "https://www.amazon.co.jp/s?k=%E9%9B%BB%E5%8B%95%E3%82%AA%E3%83%8A%E3%83%9B%E3%83%BC%E3%83%AB&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss_2";
		// String base =
		// "https://www.amazon.co.jp/s?k=%E3%82%AA%E3%83%8A%E3%83%9B&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss";

		for (int indexCode = 0; indexCode < nodeIds.size(); indexCode++) {
			String code = nodeIds.get(indexCode).getOne();
			String base = "https://www.amazon.com/b?node=%s&ref=dp_bc_3";
			base = String.format(base, code);

			webDriver.get(base);

			// 显示等待控制对象
			WebDriverWait webDriverWait1 = new WebDriverWait(webDriver, 10);

			WebDriverWait webDriverWait = null;
			try {
				// 第一次需要选地区
				if (indexCode == 0) {
					// 选择地区
					WebElement location = webDriver.findElement(By.id("nav-global-location-slot"));
					if (location != null) {
						location.click();
					}

					Thread.sleep(1000);

					WebElement input = webDriver.findElement(By.className("GLUX_Full_Width"));
					if (input != null) {
						input.sendKeys("20001");
					}
					Thread.sleep(1000);
					WebElement set = webDriver.findElement(By.xpath(".//span[@data-action='GLUXPostalUpdateAction']"));
					log.info("tt" + (set == null));
					if (set != null) {
						// 设置
						set.click();
						Thread.sleep(2000);
						// webDriver.navigate().to(base + code); //方法二
						// WebElement finish =
						// webDriver.findElement(By.xpath(".//span[@data-action='GLUXConfirmAction']"));
						// JavascriptExecutor executor = (JavascriptExecutor)webDriver;
						// executor.executeScript("arguments[0].click();", finish);
						Thread.sleep(2000);
						webDriver.get(base);

						// 显示等待控制对象
						webDriverWait = new WebDriverWait(webDriver, 10);
						Thread.sleep(2000);
						webDriver.get(base);
						webDriverWait = new WebDriverWait(webDriver, 10);
						// 筛选 4分以上
						try {
							WebElement commentList = webDriver.findElement(By.id("reviewsRefinements"));
							WebElement star4 = commentList.findElement(By.className("a-star-medium-4"));
							star4.click();
						} catch (ElementClickInterceptedException e4) {
							log.info("有星筛点不了", e4);
							WebElement commentList = webDriver.findElement(By.id("reviewsRefinements"));
							WebElement star4 = commentList.findElement(By.className("a-star-medium-4"));
							JavascriptExecutor executor = (JavascriptExecutor) webDriver;
							executor.executeScript("arguments[0].click();", star4);
						} catch (Exception e5) {
							log.info("无星筛", e5);
						}
						Thread.sleep(3000);
					}

				} else {

					// 筛选 4分以上
					try {
						WebElement commentList = webDriver.findElement(By.id("reviewsRefinements"));
						WebElement star4 = commentList.findElement(By.className("a-star-medium-4"));
						star4.click();
					} catch (ElementClickInterceptedException e4) {
						log.info("有星筛点不了", e4);
						WebElement commentList = webDriver.findElement(By.id("reviewsRefinements"));
						WebElement star4 = commentList.findElement(By.className("a-star-medium-4"));
						JavascriptExecutor executor = (JavascriptExecutor) webDriver;
						executor.executeScript("arguments[0].click();", star4);
					} catch (Exception e5) {
						log.info("无星筛", e5);
					}
					Thread.sleep(3000);

				}
				// 断点
				String url = webDriver.getCurrentUrl();
				if (code.equals("18502609011")) {
					if (url.contains("page=1")) {
						url = url.replaceAll("page=1", "page=155");
						webDriver.get(url);
						webDriverWait = new WebDriverWait(webDriver, 10);
					} else {
						url = url + "&page=155";
						webDriver.get(url);
						webDriverWait = new WebDriverWait(webDriver, 10);
					}
				}
				Thread.sleep(2000);
				JavascriptExecutor executor1 = (JavascriptExecutor) webDriver;
				executor1.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			} catch (Exception e) {

				log.info("无确认", e);
			}

			int[] index = { 1 };
			try {
				List<Node> comments = new ArrayList<>();
				for (int i = 0; i < 500; i++) {
					WebElement products = webDriver.findElement(By.className("s-main-slot"));
					List<WebElement> reviews = products.findElements(By.xpath(".//div[@data-asin!='']"));
					String url = webDriver.getCurrentUrl();
					String tt = (i + 1) + "";
					String[] str = { tt };
					System.out.print("第" + str[0] + "页");
					if (url.contains("page=")) {
						int li = url.indexOf("page=");
						str[0] = url.substring(li + 5, li + 10 > url.length() ? url.length() : li + 10);
						str[0] = str[0].replaceAll("&", "");
						str[0] = str[0].replaceAll("_", "");
						str[0] = str[0].replaceAll("e", "");
						str[0] = str[0].replaceAll("n", "");
						log.info("str[0]" + str[0]);
					}

					int[] num = { 1 };
					reviews.forEach(e -> {
						Node commont = new Node();
						String asin = e.getAttribute("data-asin");
						commont.setOne(str[0]);
						commont.setTwo(asin);
						String className = e.getAttribute("class");
						//广告
						if(className.contains("a-section")) {
							return;
						}
						
						// System.out.print(className);
						boolean isSmall = true;
						if (className.contains("sg-col-20-of-24")) {
							isSmall = false;
						}
						if (!isSmall) {
							try {
								List<WebElement> small = e.findElements(By.className("a-spacing-top-small"));

								// 第一标题
								WebElement title = small.get(0).findElement(By.className("a-size-mini"));
								commont.setThree(title.getText());
								commont.setSeven(index[0] + "");
								index[0] = index[0] + 1;
								num[0] = num[0] + 1;
								// 价钱
								WebElement price = null;
								WebElement price1 = null;
								try {
									price = small.get(1).findElement(By.className("a-price-whole"));
									// price1 = small.get(1).findElement(By.className("a-price-fraction"));

								} catch (Exception ee) {
									log.info("无存");
									throw new Exception("无存" + ee.getMessage());
								}
								String priceStr = "";
								String priceStr1 = "";
								priceStr = price.getText();
								// priceStr1 = price1.getText();
								priceStr = priceStr.replaceAll("￥", "");
								priceStr = priceStr.replaceAll(",", "");
								commont.setFour(priceStr + "." + priceStr1);
								List<WebElement> micro = e.findElements(By.className("a-spacing-top-micro"));
								// 如果 有两个表示有平
								// 第二平
								if (micro.size() == 2) {
									WebElement ping = micro.get(0).findElement(By.className("a-size-small"));

									List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
									// 平分
									String startString = pings.get(0).getAttribute("aria-label");
									startString = startString.substring(0, 3);
									commont.setFive(startString);
									// 数量
									commont.setSix(pings.get(1).getAttribute("aria-label"));
								}
								if (micro.size() == 3) {
									WebElement ping = micro.get(1).findElement(By.className("a-size-small"));

									List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
									// 平分
									String startString = pings.get(0).getAttribute("aria-label");
									startString = startString.substring(0, 3);
									commont.setFive(startString);
									// 数量
									commont.setSix(pings.get(1).getAttribute("aria-label"));
								}

								// 查询日本是否存在此产品
								// 当前页
								// String home = webDriver.getWindowHandle();
								// Boolean isExist = isJpExist(commont.getTwo(), home, webDriver);
								// webDriver.switchTo().window(home);
								// if(isExist) {
								// log.info(commont.getTwo() + " exist" + " 1");
								// commont.setEight("1");
								// }else {
								// log.info(commont.getTwo() + " exist" + " 0");
								// commont.setEight("0");
								// }
							} catch (Exception ce) {
								log.info("无评价", ce);
							}
						} else {
							try {
								List<WebElement> small = e.findElements(By.className("a-size-mini"));
								WebElement title = small.get(0);
								commont.setThree(title.getText());
								commont.setSeven(index[0] + "");
								index[0] = index[0] + 1;
								num[0] = num[0] + 1;

								WebElement price = null;
								WebElement price1 = null;
								try {
									WebElement p = e.findElement(By.className("a-price"));
									price = p.findElement(By.className("a-price-whole"));
									price1 = p.findElement(By.className("a-price-fraction"));
									String priceStr = "";
									String priceStr1 = "";
									priceStr = price.getText();
									priceStr1 = price1.getText();
									priceStr = priceStr.replaceAll("￥", "");
									priceStr = priceStr.replaceAll(",", "");
									commont.setFour(priceStr + "." + priceStr1);
								} catch (Exception ee) {
									log.info("无存");
									throw new Exception("无存" + ee.getMessage());
								}

								WebElement ping = e.findElement(By.className("a-size-small"));
								List<WebElement> pings = ping.findElements(By.xpath(".//span[@aria-label]"));
								// 平分
								String startString = pings.get(0).getAttribute("aria-label");
								startString = startString.substring(0, 3);
								commont.setFive(startString);
								// 数量
								commont.setSix(pings.get(1).getAttribute("aria-label"));
								
							} catch (Exception ex1) {
								log.info("无评价", ex1);
							}
						}
						commont.setNine(code);
						commont.setEight("us_health_0629");
						if(commont.getFive() != null) {
							comments.add(commont);
						}
						
						log.info("第" + str[0] + "页 第" + num[0] + "个" + JSON.toJSONString(commont));

						if (comments.size() >= 100) {
							nodeRepository.saveAll(comments);
							comments.clear();
						}

					});
					nodeRepository.saveAll(comments);
					// if (reviews == null) {
					// if (reviews == null || reviews.size() != 24) {
					// break;
					// }
					WebElement next = null;
					try {
						next = webDriver.findElement(By.className("a-last"));
					} catch (Exception e) {
						Thread.sleep(1000 * 5);
						try {
							next = webDriver.findElement(By.className("a-last"));
						} catch (Exception es) {
							break;
						}

					}

					String classs = next.getAttribute("class");
					System.out.println("classs" + classs);

					if (classs.contains("a-disabled")) {
						break;
					} else {
						try {
							next.click();
							Thread.sleep(1000 * 10);

							JavascriptExecutor executor2 = (JavascriptExecutor) webDriver;
							executor2.executeScript("window.scrollTo(0,document.body.scrollHeight-500)");
						} catch (ElementClickInterceptedException e4) {

							JavascriptExecutor executor = (JavascriptExecutor) webDriver;
							executor.executeScript("arguments[0].click();", next);

							JavascriptExecutor executor2 = (JavascriptExecutor) webDriver;
							executor2.executeScript("window.scrollTo(0,document.body.scrollHeight-500)");
						} catch (Exception e5) {

							log.info("下一页错误", e5);
						}
					}
				}

				ExcelUtil exUtil = new ExcelUtil();
				List<String> headerList = new ArrayList<String>();
				List<String> headerNameList = new ArrayList<String>();

				headerList.add("one");
				headerList.add("two");
				headerList.add("three");
				headerList.add("four");
				headerList.add("five");
				headerList.add("six");
				headerList.add("seven");
				// headerList.add("eight");
				headerNameList.add("页码");
				headerNameList.add("ASIN");
				headerNameList.add("标题");
				headerNameList.add("价钱");
				headerNameList.add("评价星级");
				headerNameList.add("评价数量");
				headerNameList.add("顺序");
				// headerNameList.add("jp有此产品");

				try {
					//exUtil.exportExcel(code, headerNameList, headerList, comments);
				} catch (Exception e) {
					log.error("", e);
					webDriver.close();
					webDriver.quit();
				}

				//exUtil.exportExcel(code, headerNameList, headerList, comments);
			} catch (Exception ee) {
				log.info("错误", ee);
				// webDriver.close();
				// webDriver.quit();
			} finally {
				 //webDriver.close();
				// webDriver.quit();
			}
			// webDriver.close();
			 //webDriver.quit();
		}

	}

	/**
	 * 
	 */
	public void chooseUsStet2() {
		List<Node> nodes = nodeRepository.findByEightAndTenIsNull("us_health_0629");
		if(nodes.size() == 0) {
			return;
		}
		
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

		WebDriver webDriver = new ChromeDriver();

		System.out.println(
				"打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println(
				"页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		//https://www.amazon.co.jp/dp/B08NG4YX82
		// 设定网址
		// String base =
		// "https://www.amazon.co.jp/s?k=%E9%9B%BB%E5%8B%95%E3%82%AA%E3%83%8A%E3%83%9B%E3%83%BC%E3%83%AB&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss_2";
		// String base =
		// "https://www.amazon.co.jp/s?k=%E3%82%AA%E3%83%8A%E3%83%9B&i=hpc&__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&ref=nb_sb_noss";

		for (int indexCode = 0; indexCode < nodes.size(); indexCode++) {
			Node node = nodes.get(indexCode);
			String code = node.getTwo();
			String base = "https://www.amazon.co.jp/dp/%s";
			base = String.format(base, code);

			webDriver.get(base);

			// 显示等待控制对象
			WebDriverWait webDriverWait1 = new WebDriverWait(webDriver, 10);

			WebDriverWait webDriverWait = null;
			Self self = new Self();
			try {
				self.setNine(base);
				WebElement wayfinding = webDriver.findElement(By.id("wayfinding-breadcrumbs_feature_div"));
				if (wayfinding == null) {
					break;
				}
				WebElement wayfind = wayfinding.findElement(By.className("a-unordered-list"));
				String sort = wayfind.findElement(By.className("a-link-normal")).getText();
				self.setOne(sort);
				// 标题
				WebElement title = webDriver.findElement(By.id("productTitle"));
				String titleStr = title.getText();
				self.setTwo(titleStr);
				// 品牌
				try {
					WebElement feature = webDriver.findElement(By.id("bylineInfo_feature_div"));
					String featureStr = feature.findElement(By.id("bylineInfo")).getText();
					self.setThree(featureStr);
				} catch (Exception ex) {
					log.info("no brand");
				}
				// 星级
				try {
					WebElement star = webDriver.findElement(By.id("averageCustomerReviews"));
					WebElement starLevel = star.findElement(By.id("acrPopover"));
					String starLevelStr = starLevel.getAttribute("title");
					self.setFour(starLevelStr);
					WebElement startNum = star.findElement(By.id("acrCustomerReviewText"));
					String starNumStr = startNum.getText();
					self.setFive(starNumStr);
				} catch (Exception e) {
					log.info("no star");
				}

				// 送货
				// "sfsb_accordion_head"
				WebElement deliver = webDriver.findElement(By.id("tabular-buybox-container"));
				WebElement sender = deliver.findElement(By.id("tabular-buybox-truncate-0"));
				List<WebElement> delivers = sender.findElements(By.className("tabular-buybox-text"));
				String senderStr = delivers.get(1).getText();
				self.setSix(senderStr);
				List<WebElement> sellers = deliver.findElements(By.id("sellerProfileTriggerId"));
				log.info("seller size " + sellers.size());
				String sellerStr = sellers.get(1).getText();
				self.setSeven(sellerStr);
				log.info("seller " + sellerStr);
				//marketId
				
				WebElement merchantID = webDriver.findElement(By.id("merchantID"));
				String merchantIDStr  =  merchantID.getAttribute("value");
				
				List<WebElement> placeIds = webDriver.findElements(By.xpath(".//div[@data-marketplaceid]"));
				String url = String.format("https://www.amazon.co.jp/s?me=%s&marketplaceID=%s", merchantIDStr,placeIds.get(0).getAttribute("data-marketplaceid"));
				self.setEight(url);
				selfRepository.saveAndFlush(self);
				node.setTen("checked");
				nodeRepository.saveAndFlush(node);
			} catch (ElementClickInterceptedException e4) {
				selfRepository.saveAndFlush(self);
				node.setTen("checked");
				nodeRepository.saveAndFlush(node);
				log.info("有星筛点不了", e4);
			} catch (Exception e5) {
				selfRepository.saveAndFlush(self);
				node.setTen("checked");
				nodeRepository.saveAndFlush(node);
				log.info("无星筛", e5);
			}
			
		}
	}

	//获取具体
	public void chooseUsStet3() {

		List<Self> selfs = selfRepository.findByTenIsNullAndEightNot("");

		// System.setProperty("webdriver.chrome.driver","C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
		// mac
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

		WebDriver webDriver = new ChromeDriver();

		System.out.println(
				"打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println(
				"页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        Map<String, List<Self>> mapSelf = selfs.stream().collect(Collectors.groupingBy(Self::getEight));
		
        mapSelf.forEach((url,list)-> {
        	
         log.info("url" + url);
        
		//for (int indexCode = 0; indexCode < mapSelf.size(); indexCode++) {
			//String base = "https://www.amazon.com/b?node=%s&ref=dp_bc_3";
			//base = String.format(base, code);

			webDriver.get(url);

			// 显示等待控制对象
			WebDriverWait webDriverWait1 = new WebDriverWait(webDriver, 10);

			WebDriverWait webDriverWait = null;
			try {
				// 断点
				if (url.equals("18502609011")) {
					if (url.contains("page=1")) {
						url = url.replaceAll("page=1", "page=155");
						webDriver.get(url);
						webDriverWait = new WebDriverWait(webDriver, 10);
					} else {
						url = url + "&page=155";
						webDriver.get(url);
						webDriverWait = new WebDriverWait(webDriver, 10);
					}
				}
				Thread.sleep(2000);
				JavascriptExecutor executor1 = (JavascriptExecutor) webDriver;
				executor1.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			} catch (Exception e) {

				log.info("无确认", e);
			}

			int[] index = { 1 };
			try {
				List<Step4> comments = new ArrayList<>();
				for (int i = 0; i < 500; i++) {
					WebElement products = webDriver.findElement(By.className("s-main-slot"));
					List<WebElement> reviews = products.findElements(By.xpath(".//div[@data-asin!='']"));
					
					String tt = (i + 1) + "";
					String[] str = { tt };
					System.out.print("第" + str[0] + "页");
					if (url.contains("page=")) {
						int li = url.indexOf("page=");
						str[0] = url.substring(li + 5, li + 10 > url.length() ? url.length() : li + 10);
						str[0] = str[0].replaceAll("&", "");
						str[0] = str[0].replaceAll("_", "");
						str[0] = str[0].replaceAll("e", "");
						str[0] = str[0].replaceAll("n", "");
						log.info("str[0]" + str[0]);
					}

					int[] num = { 1 };
					reviews.forEach(e -> {
						Step4 commont = new Step4();
						String asin = e.getAttribute("data-asin");
						String curUrl = webDriver.getCurrentUrl();
						commont.setOne(str[0]);
						commont.setTwo(asin);
						commont.setNine(curUrl);
						commont.setEight("us_health_0629");
						if (commont.getFive() != null) {
							comments.add(commont);
						}
						log.info("第" + str[0] + "页 第" + num[0] + "个" + JSON.toJSONString(commont));
					});
					
					WebElement next = null;
					try {
						next = webDriver.findElement(By.className("a-last"));
					} catch (Exception e) {
						Thread.sleep(1000 * 5);
						try {
							next = webDriver.findElement(By.className("a-last"));
						} catch (Exception es) {
							break;
						}

					}

					String classs = next.getAttribute("class");
					System.out.println("classs" + classs);

					if (classs.contains("a-disabled")) {
						break;
					} else {
						try {
							next.click();
							Thread.sleep(1000 * 10);

							JavascriptExecutor executor2 = (JavascriptExecutor) webDriver;
							executor2.executeScript("window.scrollTo(0,document.body.scrollHeight-500)");
						} catch (ElementClickInterceptedException e4) {

							JavascriptExecutor executor = (JavascriptExecutor) webDriver;
							executor.executeScript("arguments[0].click();", next);

							JavascriptExecutor executor2 = (JavascriptExecutor) webDriver;
							executor2.executeScript("window.scrollTo(0,document.body.scrollHeight-500)");
						} catch (Exception e5) {

							log.info("下一页错误", e5);
						}
					}
				}

				List<String> headerList = new ArrayList<String>();
				List<String> headerNameList = new ArrayList<String>();

				headerList.add("one");
				headerList.add("two");
				headerList.add("three");
				headerList.add("four");
				headerList.add("five");
				headerList.add("six");
				headerList.add("seven");
				// headerList.add("eight");
				headerNameList.add("页码");
				headerNameList.add("ASIN");
				headerNameList.add("标题");
				headerNameList.add("价钱");
				headerNameList.add("评价星级");
				headerNameList.add("评价数量");
				headerNameList.add("顺序");
				// headerNameList.add("jp有此产品");

				try {
				} catch (Exception e) {
					log.error("", e);
					webDriver.close();
					webDriver.quit();
				}

			} catch (Exception ee) {
				log.info("错误", ee);
			} finally {
			}
			
			list.forEach(e->e.setTen("checked"));
			selfRepository.saveAll(list);
        });

	
	}

	
	  
	
	/**
	 * 
	 *  UPC条形码的最后一位是校验位。扫描器通过校验位判断是否扫描了正确的号码。下面介绍如何用另外的11位数字生成校验码。以63938200039为例说明：
		1.将所有奇数位置（第1,3,5,7,9和11位）上的数字相加。
		6+9+8+0+0+9=32
		2.然后将该数乘以3
		32*3=96
		3.将所有偶数位置（第2,4,6,8,10位）上的数字相加
		3+3+2+0+3=11
		4.然后，将该和与第2步所得的值相加
		96+11=107
		5.保存第4步的值。要创建校验位，需要确定一个值，当将该值与步骤4所得的值相加时，结果为10的倍数。
		107+3=110
		因此，校验位位3。
		以色列729
		
		UPC-A为12位，最后一位是自动生成的校验位
			检查码的算法
			从国别码开始自左至右取数，设UPC-A各码代号如下：
			N1 N2 N3 N4 N5 N6 N7 N8 N9 N10 N11 C
			则检查码之计算步骤如下：
			C1 = N1+N3+N5+N7+N9+N11
			
			C2 = (N2+N4+N6+N8+N10)× 3
			
			CC = (C1+C2) 取个位数
			
			C (检查码) = 10 - CC (若值为10，则取0)
		
	 */
	public void upc() {
		//9934002459515
		//1234567890123
		//9+3+0+2+5+5=24
		//9+4+0+4+9 =26 * 3=78
		// 24+78=102
		// 10-2=8
		
	}
	
	
	public static void main(String args []) {
		Long l = 993400245951L;
		for(int i = 0;i<10000;i++) {
			 int num = (int) (Math.random() * 5 + 1);
			 l = l + num;
			 System.out.println(Ean13Creat(l.toString()));
		}
		
	}
	
	/**
     * 校验输入的是否位12个数字
     *
     * @param Stringtemp  需要生成的12个数字  如“123456789111”
     *
     *返回值：将校验位加上并返回  如“1234567898”
     */
    private static String  Ean13Creat(String Stringtemp)
    {
 
        int[] datas=new int[12];//输入的12位
        for (int i=0;i<12;i++){
            int a=Stringtemp.charAt(i)-48;
            datas[i]=a;
        }
        //奇数位和&&偶数位和
        int p=datas[0]+datas[2]+datas[4]+datas[6]+datas[8]+datas[10];
        int q=datas[1]+datas[3]+datas[5]+datas[7]+datas[9]+datas[11];
        //校验位
        int tes =10-((p+3*q)%10);
        if (tes==10){
            tes=0;
        }
 
        Stringtemp+=tes;
 
        return Stringtemp;
    }
		

}
