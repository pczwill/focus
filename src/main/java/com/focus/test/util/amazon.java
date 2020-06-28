package com.focus.test.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.focus.test.dto.Commont;

import okhttp3.Cookie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class amazon {
	// static String index =
	// "https://www.amazon.com/Desirelove-Adhesive-Strapless-Push-up-Silicone/product-reviews/B07FVR9RR2/ref=cm_cr_arp_d_paging_btm_2?ie=UTF8&pageNumber=1&reviewerType=all_reviews&sortBy=recent";
	 static String index = "https://www.amazon.com/product-reviews/B07L5PXMMV/ref=cm_cr_getr_d_paging_btm_prev_1?ie=UTF8&showViewpoints=1&sortBy=recent&pageNumber=1";
	//static String index =  "https://www.amazon.com/Romabra-Push-up-Padded-Underwire-T-Shirt/product-reviews/B0788BVQSJ/ref=cm_cr_arp_d_paging_btm_next_2?ie=UTF8&reviewerType=all_reviews&pageNumber=1&sortBy=recent";
	//static String index = "https://www.amazon.co.jp/Lyork-%E3%83%8F%E3%83%B3%E3%83%87%E3%82%A3%E3%83%9E%E3%83%83%E3%82%B5%E3%83%BC%E3%82%B8%E3%83%A3%E3%83%BC-20%E7%A8%AE%E6%8C%AF%E5%8B%95%E3%83%A2%E3%83%BC%E3%83%89-%E3%82%B3%E3%83%BC%E3%83%89%E3%83%AC%E3%82%B9-USB%E5%85%85%E9%9B%BB%E5%BC%8F/product-reviews/B07D5SC6C7/ref=cm_cr_arp_d_viewopt_srt?ie=UTF8&reviewerType=all_reviews&filterByStar=positive&sortBy=recent&pageNumber=1";
	// "https://www.amazon.com/Desirelove-Seamless-Nursing-Maternity-Breastfeeding/product-reviews/B079M87VG4/ref=cm_cr_arp_d_viewopt_srt?ie=UTF8&reviewerType=all_reviews&pageNumber=1&sortBy=recent";
	// "https://www.amazon.com/Desirelove-Sports-Padded-Seamless-Activewear/product-reviews/B077X738N2/ref=cm_cr_arp_d_paging_btm_next_2?ie=UTF8&reviewerType=all_reviews&pageNumber=1";
	static List<String> headerList = new ArrayList<String>();
	static List<String> headerNameList = new ArrayList<String>();
	static {
		headerList.add("time");
		headerList.add("level");
		headerList.add("attribute");
		headerList.add("authorName");
		headerList.add("reviewTitle");
		headerList.add("reviewUrl");
		headerList.add("reviewContent");
		headerNameList.add("时间");
		headerNameList.add("星级");
		headerNameList.add("属性");
		headerNameList.add("作者");
		headerNameList.add("评论标题");
		headerNameList.add("评论URL");
		headerNameList.add("评论内容");
	}
	
	public static String  okHttp() throws IOException {
		OkHttpClient client = new OkHttpClient();
	    Request request = new Request.Builder().url(index).build();
	    Response response = client.newCall(request).execute();
	    if (response.isSuccessful()) {
	        return response.body().string();
	    } else {
	        throw new IOException("Unexpected code " + response);
	    }
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		//System.out.println(okHttp());
		runJsonp();
	}
	public static void runJsonp() throws IOException, InterruptedException {

		// Document docTemp = Jsoup.connect(index).get();
		
      //获取整个页面文件
     
        
        
		//Document docTemp = Jsoup.connect(new String(index.getBytes(), "utf-8")).data("query", "Java").userAgent("Mozilla").cookie("auth", "token").header("content-type", "text/html;charset=UTF-8").timeout(30000).get();
		// 获取页面数量等 信息
		//123 Element allRows = (Element) docTemp.select("[data-hook=\"total-review-count\"]").get(0);
		//123 System.out.println(allRows.text());
		//123 int totalRols = Integer.parseInt(allRows.text().replace(",", ""));
		int totalRols = 158;
		int page = totalRols % 10 == 0 ? totalRols / 10 : (totalRols / 10) + 1;
		System.out.println("allpages: " + page);
		String[] allReviewUrl = getAllReviewUrl(index, page);
		List<Commont> reviewList = new ArrayList<Commont>();
		for (int j = 0; j < allReviewUrl.length; j++) {

			Thread.sleep(5000);
			Document doc = null;
			try {
				// doc = Jsoup.connect(allReviewUrl[j]).data("query",
				// "Java").userAgent("Mozilla").cookie("auth", "token").timeout(30000).get();
				// 获取整个页面文件
				Connection connect = Jsoup.connect(allReviewUrl[j]);
		        //doc = data.timeout(30000).get();
			} catch (Exception eTimeOut) {
				System.out.println("超时15秒后重试！！" + allReviewUrl[j]);
				Thread.sleep(15000);
				try {
					doc = Jsoup.connect(allReviewUrl[j]).data("query", "Java").userAgent("Mozilla")
							.cookie("auth", "token").timeout(30000).get();
				} catch (Exception eTimeOut1) {
					System.out.println("超时30秒后重试！！" + allReviewUrl[j]);
					Thread.sleep(30000);
					try {
						doc = Jsoup.connect(allReviewUrl[j]).data("query", "Java").userAgent("Mozilla")
								.cookie("auth", "token").timeout(30000).get();
					} catch (Exception ee) {
						System.out.println("重试两次失败,重跑此页面： " + allReviewUrl[j]);
						j = j - 1;
						continue;
					}

				}
			}

			Elements e = doc.select("[data-hook=\"review\"]");
			for (int i = 0; i < e.size(); i++) {
				Commont reviewInfo = new Commont();
				Element ele = (Element) e.get(i);
				// 每个评论外部DIV
				getReviewInfo(ele, reviewInfo);
				System.out.println(allReviewUrl[j]);
				System.out.println("page" + (j + 1) + " reviewInfo" + (i + 1) + "： " + reviewInfo);
				reviewList.add(reviewInfo);

			}
		}

		ExcelUtil exUtil = new ExcelUtil();
		// System.out.println("headerList： " + headerList);
		// System.out.println("统计： " + reviewList);

		//exUtil.exportExcel("reviewInfo", headerNameList, headerList, sortResult(reviewList));

	}

	private static void getReviewInfo(Element ele, Commont reviewInfo) {

		setReviewInfoByNewPage(ele, reviewInfo);

		Elements rows = ele.getElementsByClass("a-row");
		// 第一行 作者时间 相关信息
		Element row1 = (Element) rows.get(0);
		try {
			Element authorEle = (Element) row1.select(".a-profile-name").get(0);
			setReviewInfoByNewPage(ele, reviewInfo);
		} catch (Exception Auth) {
			setReviewInfoByOldPage(ele, reviewInfo);
		}

	}

	private static void setReviewInfoByOldPage(Element ele, Commont reviewInfo) {
		// 每个评论外部DIV
		Elements rows = ele.getElementsByClass("a-row");
		// 第一行 星级信息
		Element row1 = (Element) rows.get(0);
		int starLevel = getStart(row1);
		reviewInfo.setLevel("");
		// 第一行 评价标题 评价链接
		Element url = (Element) row1.select(".a-size-base").get(0);
		String urlStr = url.attr("href");
		reviewInfo.setReviewTitle(url.text());
		reviewInfo.setReviewUrl(urlStr);
		
		
		
		
		

		// 第三行 多属性相关信息
		Element row3 = (Element) rows.get(2);
		try {
			Element attrEle = (Element) row3.select("[data-hook='format-strip']").get(0);
			reviewInfo.setAttribute(attrEle.text());
		} catch (Exception eAttr) {
			System.out.println("解析多属性失败");
			reviewInfo.setAttribute("");
		}

		// 第四行 评论
		Element row4 = (Element) rows.get(3);
		Element contentEle = (Element) row4.select("[data-hook='review-body']").get(0);
		reviewInfo.setReviewContent(contentEle.text());

	}

	/**
	 * 新版页面
	 * 
	 * @param ele
	 * @param reviewInfo
	 */
	private static void setReviewInfoByNewPage(Element ele, Commont reviewInfo) {
		// 每个评论外部DIV
		Elements rows = ele.getElementsByClass("a-row");
		// 第一行 作者时间 相关信息
		Element row1 = (Element) rows.get(0);
		// Element dateEle = (Element)
		// row1.select("[data-hook=\"genome-widget\"]").get(0);
		// reviewInfo.setTime(DateUtil.formatAmaZonDate(dateEle.text()));
		try {
			Element authorEle = (Element) row1.select(".a-profile-name").get(0);
			String authorName = authorEle.text();
			System.out.println("作者"+ authorName);
			reviewInfo.setAuthorName(authorName);
		} catch (Exception Auth) {
			System.out.println("解析作者失败");
			reviewInfo.setAuthorName("");
		}

		// 第二行 星级信息
		Element row2 = (Element) rows.get(1);
		int starLevel = getStart(row2);
		reviewInfo.setLevel("");
		// 第二行 评价标题 评价链接
		Element url = (Element) row2.select(".a-size-base").get(0);
		//123 String urlStr = url.attr("href");
		reviewInfo.setReviewTitle(url.text());
		reviewInfo.setReviewUrl("");

		// 第三行 时间 信息
		Element row3 = (Element) ele.select("[data-hook=\"review-date\"]").get(0);
		reviewInfo.setTime(DateUtil.formatAmaZonDate(row3.text()));

		// 第四行 多属性相关信息
		Element row4 = (Element) rows.get(2);
		try {
			Element attrEle = (Element) row4.select("[data-hook='format-strip']").get(0);
			reviewInfo.setAttribute(attrEle.text());
		} catch (Exception eAttr) {
			System.out.println("解析多属性失败");
			reviewInfo.setAttribute("");
		}

		// 第五行 评论
		Element row5 = (Element) rows.get(3);
		Element contentEle = (Element) row5.select("[data-hook='review-body']").get(0);
		reviewInfo.setReviewContent(contentEle.text());
		// System.out.println(allReviewUrl[j]);
	}

	private static List<Commont> sortResult(List<Commont> reviewList) {
		// System.out.println("排序前----");
		for (Commont c : reviewList) {
			// System.out.println(c.getTime() + "--" + c.getAuthorName());
		}
		System.out.println("----");
		Collections.sort(reviewList, new Comparator<Commont>() {
			/*
			 * int compare(Person p1, Person p2) 返回一个基本类型的整型， 返回负数表示：p1 小于p2， 返回0
			 * 表示：p1和p2相等， 返回正数表示：p1大于p2
			 */
			public int compare(Commont p1, Commont p2) {
				// 按照Person的年龄进行升序排列
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				Date date1;
				try {
					date1 = sf.parse(p1.getTime());
					// System.out.println("date1 " + sf.format(date1));
				} catch (ParseException e1) {
					return 0;
				}
				Date date2;
				try {
					date2 = sf.parse(p2.getTime());
					// System.out.println("date2 " + sf.format(date2));
				} catch (ParseException e) {
					return 0;
				}
				// System.out.println(date1.compareTo(date2));
				/*
				 * if(date1.after(date2)) { System.out.println("1"); return 1; }else
				 * if(date2.after(date1)) { System.out.println("-1"); return -1; }else {
				 * System.out.println("0"); return 0; }
				 */
				if (date1.getTime() > date2.getTime()) {
					// System.out.println("dt1 在dt2前");
					return -1;
				} else if (date1.getTime() < date2.getTime()) {
					// System.out.println("在dt2后");
					return 1;
				} else {
					return 0;
				}
			}
		});
		System.out.println("排序后----");
		for (Commont c : reviewList) {
			// System.out.println(c.getTime() + "--" + c.getAuthorName());
		}
		System.out.println("----");
		return reviewList;
	}

	private static int getStart(Element row) {
		int start = 0;
		for (int i = 1; i <= 5; i++) {
			Elements e = row.getElementsByClass("a-star-" + i);
			if (e.size() >= 1) {
				start = i;
				return i;
			}
		}
		return start;
	}

	private static String getUrlContent(String firstIndexUrl) {
		String strContent = "";
		String str = "";
		try {
			// String index =
			// "https://www.amazon.com/Lansinoh-Nursing-Packs-Disposable-Breast/product-reviews/B06XPHR7DF/ref=cm_cr_arp_d_paging_btm_2?ie=UTF8&reviewerType=all_reviews&pageNumber=1";
			URL url = new URL(firstIndexUrl);
			InputStream in = url.openStream();
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader bufr = new BufferedReader(isr);

			while ((str = bufr.readLine()) != null) {
				strContent = strContent + str;
			}
			bufr.close();
			isr.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(strContent);
		return str;
	}

	private static String[] getAllReviewUrl(String url, int Allpages) {
		// 58 - 36 = 22
		/*Allpages = 22;
		String[] urlArray = new String[Allpages];
		url = url.replace("&pageNumber=1", "");
		for (int i = 0; i < Allpages; i++) {
			urlArray[i] = url + "&pageNumber=" + (i + 37);
			System.out.println(urlArray[i]);
		}
		return urlArray;*/
		//Allpages = 37;
		String[] urlArray = new String[Allpages];
		url = url.replace("&pageNumber=1", "");
		for (int i = 0; i < Allpages; i++) {
			urlArray[i] = url + "&pageNumber=" + (i + 1);
			System.out.println(urlArray[i]);
		}
		return urlArray;

	}

}
