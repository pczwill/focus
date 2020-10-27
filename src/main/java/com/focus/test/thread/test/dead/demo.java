package com.focus.test.thread.test.dead;

public class demo {

	public static void main(String[] args) {
		Syn syn1= new Syn();
		Syn1 syn2= new Syn1();
		
		T1 t1 = new T1(syn1, syn2);
		Thread th1 = new Thread(t1);
		
		T2 t2 = new T2(syn1, syn2);
		Thread th2 = new Thread(t2);
		th1.start();
		th2.start();
	}

}
