package com.focus.test.transaction;

import java.io.FileReader;
import java.math.BigDecimal;


public class test1 {
	public static test1 t1 = new test1();
	public static test1 t2 = new test1();
	{
		System.out.println("构造块");
	}
	static {
		System.out.println("静态块");
	}

	public static void main(String[] args) {
		test1 t = new test1();
	}
}

