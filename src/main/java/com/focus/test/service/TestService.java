package com.focus.test.service;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.focus.test.entity.Test;
import com.focus.test.repository.TestRepository;

@Service
public class TestService {
	
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

	/**
	 * 
	 */
	public void chrome() {
		System.setProperty("webdriver.chrome.driver","C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		
		WebDriver webDriver =new ChromeDriver();

		System.out.println("打开浏览器--------------------------------------------------------------------------------------------------------------");

		webDriver.manage().window().maximize();

		System.out.println("页面最大化--------------------------------------------------------------------------------------------------------------");
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		//设定网址

		webDriver.get("https://sso.qiniu.com/");

		//显示等待控制对象
		WebDriverWait webDriverWait=new WebDriverWait(webDriver,5);
		
		//另外一种获取元素方法

		WebElement e= webDriver.findElement(By.id("email"));

		e.clear();

		System.out.println("清空密码框内容----------------------------------------------------------------------------------------------------------");

		e.sendKeys("james@futuremap.com.cn");
		
		WebElement e1= webDriver.findElement(By.id("password"));

		e1.clear();

		System.out.println("清空密码框内容----------------------------------------------------------------------------------------------------------");

		e1.sendKeys("fm180417");
		
		WebElement log= webDriver.findElement(By.id("login-button"));

        log.click();

	}
	
	public static void main(String [] args) {
		TestService testService = new TestService();
		testService.chrome();
	}

}
