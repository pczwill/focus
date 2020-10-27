package com.focus.test.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.mysql.cj.log.Log;

public class fileUtil {

	public static String readFileToString(String path) throws IOException {
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		StringBuffer sb = new StringBuffer();
		String str = null;

		while ((str = br.readLine()) != null) {
			sb.append(str + "/n");
		}

		return sb.toString();
	}

	/**
	 * @从制定URL下载文件并保存到指定目录
	 * @param filePath 文件将要保存的目录
	 * @param method   请求方法，包括POST和GET
	 * @param url      请求的路径
	 * @return
	 */

	public static File saveUrlAs(String url, String filePath, String method, String fileName) {
		// System.out.println("fileName---->"+filePath);
		// 创建不同的文件夹目录
		File file = new File(filePath);
		// 判断文件夹是否存在
		if (!file.exists()) {
			// 如果文件夹不存在，则创建新的的文件夹
			file.mkdirs();
		}
		FileOutputStream fileOut = null;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		try {
			// 建立链接
			URL httpUrl = new URL(url);
			conn = (HttpURLConnection) httpUrl.openConnection();
			// 以Post方式提交表单，默认get方式
			conn.setRequestMethod(method);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			// post方式不能使用缓存
			conn.setUseCaches(false);
			// 连接指定的资源
			conn.connect();
			// 获取网络输入流
			inputStream = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(inputStream);
			// 判断文件的保存路径后面是否以/结尾
			if (!filePath.endsWith("/")) {

				filePath += "/";

			}
			// 写入到文件（注意文件保存路径的后面一定要加上文件的名称）
			fileOut = new FileOutputStream(filePath + fileName);
			BufferedOutputStream bos = new BufferedOutputStream(fileOut);

			byte[] buf = new byte[4096];
			int length = bis.read(buf);
			// 保存文件
			while (length != -1) {
				bos.write(buf, 0, length);
				length = bis.read(buf);
			}
			bos.close();
			bis.close();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("抛出异常！！");
		}

		return file;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String fileUrl = "https://img.futuremap.com.cn/FovDfbyc58KImu1P4aZ87xKkulDq?attname=浙江永诚天安云谷住宅地块月报2020.3.doc"; // 文件URL地址
		//String fileUrl = "https://img.futuremap.com.cn/FjS6G5Ec4s5B_5PTJ25WFoLPVdtd?attname=综合单价登记台账-开发与交付中心.xlsx";
		//String fileName = fileUrl.substring(fileUrl.lastIndexOf("/")); // 为下载的文件命名
		//String filePath = "d:\\file\\"; // 保存目录
		String fileUrl = "https://img.futuremap.com.cn/FjS6G5Ec4s5B_5PTJ25WFoLPVdtd?attname=综合单价登记台账-开发与交付中心.xlsx";
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("attname=") + 8); // 为下载的文件命名
		String filePath = fileUrl.substring(fileUrl.lastIndexOf("cn/") + 3, fileUrl.lastIndexOf("?attname="));  // 保存目录
		
		System.out.println(fileName);
		System.out.println(filePath);
		//saveUrlAs(fileUrl, filePath + "FjS6G5Ec4s5B_5PTJ25WFoLPVdtd", "GET", "综合单价登记台账-开发与交付中心.xlsx");
		//File file = saveUrlAs(fileUrl, filePath + "FjS6G5Ec4s5B_5PTJ25WFoLPVdtd", "GET", "综合单价登记台账-开发与交付中心.xlsx");
	}

}
