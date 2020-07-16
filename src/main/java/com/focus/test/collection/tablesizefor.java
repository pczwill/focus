package com.focus.test.collection;

public class tablesizefor {
    /**
     * https://blog.csdn.net/fan2012huan/article/details/51097331
     * 
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

	public static void main(String args[]) {
		System.out.println(addBit(2,2));
		System.out.println(subBit(3,2));
	}

	public static int tableSizeFor(int cap) {
		int n = cap - 1;
		n |= n >>> 1;
		n |= n >>> 2;
		n |= n >>> 4;
		n |= n >>> 8;
		n |= n >>> 16;
		return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
	}
	
	public static int addBit(int a, int b) {
		
		//0001
		//0010
		return a|b;
	}

	public static int subBit(int a, int b) {
		// 0011
		// 0010 1101
		return a & b;
	}
}
