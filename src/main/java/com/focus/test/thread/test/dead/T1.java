package com.focus.test.thread.test.dead;

public class T1 implements Runnable {

	
	Syn syn1;
	
	Syn1 syn2;
	
	T1(Syn syn1, Syn1 syn2) {
		this.syn1 = syn1;
		this.syn2 = syn2;
	}
	
	
	public void run () {
		synchronized (syn1) {
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.print(Thread.currentThread().getName() + "等待syn2");
			synchronized (syn2) {
				
			}
			
			
			
		}
		
		
	}
}
