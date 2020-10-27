package com.focus.test.thread.test.ThreadPool;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Demo {

	public static void main(String[] args) {
		// System.out.print(Runtime.getRuntime().availableProcessors());
		int num = Runtime.getRuntime().availableProcessors();
		Executor executor = Executors.newFixedThreadPool(num);
		for (int i = 0; i < num; i++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName() + "我是一个子线程!!");
				}
			});
		}
	}

}
