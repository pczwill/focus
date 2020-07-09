package com.focus.test.design.single;
//懒汉式(线程安全，调用效率不高，但是能延时加载)：
public class Single2 {

	
	private static Single2 single2 = new Single2();
	
	//构造器私有化
    private Single2(){}
    
	public Single2 getInstance() {
		return single2;
	}


}
