package com.focus.test.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class fileUtil {

	public static String readFileToString(String path) throws IOException {
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
        StringBuffer sb = new StringBuffer();
		String str = null;

		while ((str = br.readLine()) != null) {
			sb.append(str + "/n");
			//System.out.println(str);
		}

		return sb.toString();
	}

}
