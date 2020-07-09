package com.focus.test.design.single;
//静态内部类实现模式（线程安全，调用效率高，可以延时加载）
public class Single3 {

	private static class singleClassInstance {
		private static final Single3 instance = new Single3();
	}

	private void single3() {};

	public Single3 getInstance() {
		return singleClassInstance.instance;
	}
}
