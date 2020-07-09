package com.focus.test.arithmetic.sort;

public class fast1 {

	private static int[] array = { 1, 5, 3, 6 };

	public static void main(String args[]) {

		for (int i = 0; i < array.length; i++) {
			for (int j = i + 1; j < array.length; j++) {

				if (array[i] > array[j]) {
					int temp = array[i];
					array[i] = array[j];
					array[j] = temp;
				}
			}
		}

		for (int i = 0; i < array.length; i++) {
			System.out.println(array[i]);
		}

	}

}
