package com.focus.test.arithmetic.sort;

import org.modelmapper.internal.asm.tree.IntInsnNode;

public class fast {

	private static int[] array = { 6, 1, 2, 7, 9, 3, 4, 5, 10, 8 };

	public static void main(String args[]) {
		for (int c = 0; c < array.length; c++) {
			System.out.print(array[c]+",");
		}
		System.out.println("");
		int i = 0;
		int j = array.length - 1;
		quickSort(array, i, j);
		for (int c = 0; c < array.length; c++) {
			System.out.print(array[c]+",");
		}

	}

	public static void quickSort(int[] array, int i, int j) {
		
		int end = j;
		int start = i;
		int temp;
		while (j > i) {
			System.out.println("start= " + i + " end= " + j);

			int base = array[i];
			for (int c = 0; c < array.length; c++) {
				System.out.print(array[c]+",");
			}
			System.out.println("");
			for (; j > i; j--) {
				if (array[j] < base) {
					break;
				}
			}

			for (; i < j; i++) {
				if (array[i] > base) {
					break;
				}
			}
			temp = array[j];
			array[j] = array[i];
			array[i] = temp;
			if (i == j) {
				temp = array[start];
				array[start] = array[i];
				array[i] = temp;
				quickSort(array, start, j-1);
				quickSort(array, j+1, end);
			}
		}

	}

}
