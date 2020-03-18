package com.focus.test.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.focus.test.dto.Commont;
import com.focus.test.entity.Test;
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
		headerNameList.add("星级");
		headerNameList.add("属性");
		headerNameList.add("作者");
		headerNameList.add("评论标题");
		headerNameList.add("评论URL");
		headerNameList.add("评论内容");
	}
	/*
	 * 大概意思：在默认的代理模式下，只有目标方法由外部调用，
	 * 才能被 Spring 的事务拦截器拦截。在同一个类中的两个方法直接调用，是不会被 Spring 的事务拦截器拦截，
	 * 就像上面的 save 方法直接调用了同一个类中的 method1方法，method1 方法不会被 Spring 的事务拦截器拦截。
	 * 可以使用 AspectJ 取代 Spring AOP 代理来解决这个问题，但是这里暂不讨论。
	 * 
	 * 
	 * 当我们使用@Transaction 时默认为RuntimeException（也就是运行时异常）异常才会回滚。
	 * */
	
	@Autowired
	TestRepository testRepository;

	//@Transactional(rollbackFor = Exception.class)
	@Transactional
	public void tran() throws Exception {
		tran1();
        Test test = Test.builder().name("tran").build();		
		testRepository.save(test);
		
		int i = 1/0;
	}
	
    //@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void tran1() throws Exception {
        Test test = Test.builder().name("tran1").build();		
		testRepository.save(test);
	}
	
	public void upc() {
		//7423738132516
		Long start = 8934935222284L;
		
		for(Long i=8934935222284L;i<8934935224284L;) {
			int j = (int)(10+Math.random()*(20+1));
            
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
		System.setProperty("webdriver.chrome.driver","C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
		
		WebDriver webDriver =new ChromeDriver();

		System.out.println("打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println("页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		//设定网址
        String base = "https://www.amazon.co.jp/product-reviews/%s/ref=cm_cr_arp_d_viewopt_srt?ie=UTF8&showViewpoints=1&sortBy=recent&pageNumber=1";
		
		
		webDriver.get(String.format(base, commentUrl));

		//显示等待控制对象
		WebDriverWait webDriverWait=new WebDriverWait(webDriver,10);
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
					commont.setLevel(Integer.valueOf(star.getAttribute("title").substring(0, 1)));

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
					}catch(Exception e1) {
						commont.setReviewUrl(commentUrl);
					}
					// 时间
					WebElement date = e.findElement(By.className("review-date"));
					String dateStr = date.getText();
					commont.setTime(dateStr.substring(0, dateStr.indexOf("日") + 1));
					comments.add(commont);
				});
				if(reviews == null || reviews.size() != 10) {
					break;
				}
				WebElement next = null;
				try {
					next = webDriver.findElement(By.className("a-last"));
				}catch(Exception e) {
					break;
				}
				
				String classs = next.getAttribute("class");
				System.out.println("classs" + classs);

				if(classs.contains("a-disabled")) {
					break;
				}else {
					next.click();
					Thread.sleep(1000*5);
				}
			}
		
			ExcelUtil exUtil = new ExcelUtil();
			try {
				exUtil.exportExcel(commentUrl, headerNameList, headerList, comments, response);
			}catch(Exception e) {
				//break;
			}
			
			exUtil.exportExcel(commentUrl, headerNameList, headerList, comments);
		}catch(Exception ee) {
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
     * @param request
     * @param response
     * @param filename 要下载的文件名
     * @param file     需要下载的文件对象
     * @throws IOException
     */
    public static void downFile(HttpServletRequest request, HttpServletResponse response, String filename, File file) throws IOException {
        //  文件存在才下载
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

	public void search(String code, HttpServletResponse response) {

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
		String base = "https://www.amazon.co.jp/s/ref=nb_sb_noss_2?__mk_ja_JP=%E3%82%AB%E3%82%BF%E3%82%AB%E3%83%8A&url=search-alias%3Daps&field-keywords=";

		webDriver.get(base + code);

		// 显示等待控制对象
		WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);

		WebElement products = webDriver.findElement(By.className("s-search-results"));

		try {
			List<Commont> comments = new ArrayList<>();

			for (int i = 0; i < 4; i++) {
                
				List<WebElement> reviews = products.findElements(By.className("sg-col-4-of-24"));
				String str = (i+1)+"";
				System.out.print("第"+str+"页");
				reviews.forEach(e -> {
					Commont commont = new Commont();
					String asin = e.getAttribute("data-asin");
					commont.setReviewTitle(asin);
					commont.setLevel(Integer.valueOf(str));
					try {
						List<WebElement> star = e.findElements(By.className("a-spacing-top-micro"));
                        if(star.size()>1) {
                        	//WebElement spans = star.findElement(By.className("a-size-small"));
    						WebElement pans = star.get(0).findElement(By.className("a-size-base"));
    						if(pans != null) {
    							//WebElement countEle = pans.get(1);
        						//String count = countEle.getAttribute("aria-label");
        						commont.setAuthorName(pans.getText());
    						}
    						
                        } 
						
					} catch (Exception ce) {
						log.info("无评价", ce);
					}
					comments.add(commont);
				});
				if (reviews == null || reviews.size() != 48) {
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
				exUtil.exportExcel(code, headerNameList, headerList, comments, response);
			} catch (Exception e) {
				log.error("", e);
			}

			exUtil.exportExcel(code, headerNameList, headerList, comments);
		} catch (Exception ee) {
			log.info("错误" + ee);
			//webDriver.close();
		}

		webDriver.close();

	}
	

}
