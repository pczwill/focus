package com.focus.test.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import com.focus.test.entity.Test;
import com.focus.test.repository.HotTitleRepository;
import com.focus.test.repository.Node1Repository;
import com.focus.test.repository.NodeRepository;
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

	public void upc() {
		// 7423738132516
		Long start = 8934935222284L;

		for (Long i = 8934935222284L; i < 8934935224284L;) {
			int j = (int) (10 + Math.random() * (20 + 1));

			start = start + j;
			i = start;
			System.out.println(start.toString());
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

	public static void main(String args[]) {
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
	
	public void checkCa(List<String> str2, String num) {

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
		Set<String> exist = node1Repository.findByEight("ca").stream().map(e->e.getOne()).collect(Collectors.toSet());
		for (int i = 0; i < size; i++) {

			String asin = str2.get(i);
			if(exist.contains(asin)) {
				continue;
			}
			log.info(i + " " + asin + " less " + (size - i));
			try {
				String base = "https://www.amazon.ca/dp/%s";
				base = String.format(base, asin);
				webDriver.get(base);
				// 显示等待控制对象
				WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);
				/**
				try {
					WebElement alert = webDriver.findElement(By.className("alert"));
					WebElement center = webDriver.findElement(By.tagName("center"));
					List<WebElement> divs = center.findElements(By.tagName("div"));
					divs.get(1).click();
				} catch (Exception e1) {
					log.info("无alert");
				}
                  **/
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
				node.setEight("ca");
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

}
