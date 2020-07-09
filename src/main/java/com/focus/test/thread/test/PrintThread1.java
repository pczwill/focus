package com.focus.test.thread.test;

public class PrintThread1 implements Runnable {
	Boolean isWord  = false;
	
	int indexNum = 1;
	int indexWord = (int)'A';
	
	PrintThread1(Boolean isWord) {
		this.isWord = isWord;
	}
	@Override
	public void run() {
		synchronized (this) {
			if (isWord) {
				for (int i = indexWord; i <= (int) 'A' + 26; i++) {
					System.out.print((char) indexWord);
					indexWord++;
					notifyAll();
					try {
						isWord = false;
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
			} else {
				for (int i = indexNum; i <= 52; i++) {
					System.out.print(indexNum);
					
					if (indexNum % 2 == 0) {
						isWord = true;
						notifyAll();
						try {
							wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					indexNum++;

				}
			}
		}
		
	}

}
