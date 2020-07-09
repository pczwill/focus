package com.focus.test.thread.test;
/*
 * 
 * 写两个线程，一个线程打印1到52，另一个线程打印A到Z，打印顺序是12A34B...5152Z；
 * 
 * */
public class test2 {

	public static void main(String args[]) throws InterruptedException {
		PrintThread1 pt1 = new PrintThread1(false);
		Thread t1 = new Thread(pt1);
		
		//PrintThread1 pt2 = new PrintThread1(true);
		Thread t2 = new Thread(pt1);
		t1.start();
		Thread.sleep(2000);
		t2.start();
	}

}
