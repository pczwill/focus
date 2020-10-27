package com.focus.test.arithmetic;
/*
 * [70, 80, 110, 50, 80, 130, 60, 110, 150, 50, 80]
这个数组相当于股票每个时间点的价格, 算出哪个点买进哪个点卖出可以赚最多钱或者亏最少

要求是: 只能用一次循环
 * 
 * */
public class test1 {

	public static void main(String[] args) {
		int[] tt = { 70, 80, 110, 50, 80, 130, 60, 110, 150, 50, 80 };

		int buy = 0;
		int buyValue = tt[buy];
		
		int sell = tt.length - 1;
		int sellValue = tt[sell];

		for (int i = 0; i < tt.length; i++) {
			
		}

		
	}

}
