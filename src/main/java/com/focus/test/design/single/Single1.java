package com.focus.test.design.single;

//饿汉式(线程安全，调用效率高，但是不能延时加载)：
public class Single1 {
	
	private static Single1 single1 = null;
	
	//构造器私有化
	private Single1() {};
	
	public Single1 getInstance() {
		if(single1 == null) {
			single1 = new Single1();
		}
		return single1;
	}

}
