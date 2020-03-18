package com.focus.test.util;

import com.google.common.collect.Lists;

import io.netty.handler.codec.http.cookie.Cookie;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class RequestWithCookie {

	public static CookieStore cookieStore = null;

	public static void main(String[] args) throws ClassNotFoundException, URISyntaxException, IOException {
		String url = "https://www.amazon.com/product-reviews/B07L5PXMMV/ref=cm_cr_getr_d_paging_btm_prev_1?ie=UTF8&showViewpoints=1&sortBy=recent&pageNumber=1";
		RequestWithCookie request = new RequestWithCookie();
		String content = request.getHtml(url);
		System.out.print(content);
	}

	public String getHtml(String url) throws URISyntaxException, IOException, ClassNotFoundException {

		// 待请求的地址
		// String url =
		// "https://www.amazon.com/product-reviews/B07L5PXMMV/ref=cm_cr_getr_d_paging_btm_prev_1?ie=UTF8&showViewpoints=1&sortBy=recent&pageNumber=1";

		// 请求参数
		List<NameValuePair> loginNV = new ArrayList<>();
		loginNV.add(new BasicNameValuePair("userName", "test"));
		loginNV.add(new BasicNameValuePair("passWord", "test"));

		// 构造请求资源地址
		// URI uri = new URIBuilder(url).addParameters(loginNV).build();
		URI uri = new URIBuilder(url).build();

		// 创建一个HttpContext对象，用来保存Cookie
		HttpClientContext httpClientContext = HttpClientContext.create();

		// 构造自定义Header信息
		List<Header> headerList = Lists.newArrayList();
		headerList.add(new BasicHeader(HttpHeaders.ACCEPT,
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"));
		headerList.add(new BasicHeader(HttpHeaders.USER_AGENT,
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36"));
		headerList.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate, br"));
		headerList.add(new BasicHeader(HttpHeaders.CACHE_CONTROL, "max-age=0"));
		headerList.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
		headerList.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9"));
		headerList.add(new BasicHeader("cookie", "session-id=136-7701851-4073153; session-id-time=2082787201l; i18n-prefs=USD; ubid-main=133-0911486-6586012; x-wl-uid=1ySEirLKuO8ehkA8ck1zg6yvXUw4rX8ewQh7dcePTa/JrDuEA4m/Z4D/RyYdxfPxT3IIZxxq+MMk=; lc-main=en_US; session-token=XsBOm7IigTvooJyal3CYZ9o5h3CT1Oq0Fqlxlj88BnNe1xbCmFUdiHYCE7LBC39naznB+yKXKjDSOZ7ETa3LSgWhK9YbaAvjrZJmWAIuXAmTpvB26kfQCvSkO4VQ7oOydTDt51S3TpPLBS8e8mCkRKWpfdkjcM5RF1ScPf+MCmOqwS+EHOjgaP6QvZTYpLuR; s_fid=7AF2D8D988A91ACB-3091A64241CD4D69; s_vn=1614435596440%26vn%3D1; regStatus=pre-register; c_m=undefinedwww.baidu.comSearch%20Engine; s_cc=true; s_dslv=1582899604716; s_nr=1582899604726-New; csm-hit=tb:s-D604T9J0Q98E2EPNZNPS|1582990537563&t:1582990537971&adb:adblk_no; cdn-session=AK-e7d9d17ba607788c50425db433b0701a"));
		HttpClient httpClient = null;

		// 构造请求对象
		HttpUriRequest httpUriRequest = RequestBuilder.get().setUri(uri).build();
		if (cookieStore == null) {
			// 构造自定义的HttpClient对象
			httpClient = HttpClients.custom().setDefaultHeaders(headerList).build();
			// 从请求结果中获取Cookie，此时的Cookie已经带有登录信息了
			
		    //执行请求，传入HttpContext，将会得到请求结果的信息
		    httpClient.execute(httpUriRequest, httpClientContext);

			// 这个CookieStore保存了我们的登录信息，我们可以先将它保存到某个本地文件，后面直接读取使用
			// saveCookieStore(cookieStore, "cookie");
			// 下面我们将演示如何使用Cookie来请求，首先我们将之前的Cookie读出来
			// cookieStore = readCookieStore("cookie");

			// 从请求结果中获取Cookie，此时的Cookie已经带有登录信息了
			cookieStore = httpClientContext.getCookieStore();
			List<org.apache.http.cookie.Cookie> cookies = cookieStore.getCookies();
			cookies.forEach(e-> System.out.println("cookie:  " + e.getName() + "  " + e.getValue()));
			
			// 构造一个带这个Cookie的HttpClient
			//httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
		} else {
			// 从请求结果中获取Cookie，此时的Cookie已经带有登录信息了
			cookieStore = httpClientContext.getCookieStore();

			// 构造一个带这个Cookie的HttpClient
			httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
		}
		String content = null;
		// 使用这个新的HttpClient请求就可以了。这时候我们的HttpClient已经带有了之前的登录信息，再爬取就不用登录了
		HttpResponse httpResponse = httpClient.execute(httpUriRequest, httpClientContext);
		HttpEntity entity = httpResponse.getEntity();
		if (entity != null) {
			content = EntityUtils.toString(entity, "UTF-8");
			EntityUtils.consume(entity);// 关闭内容流
		}
		// 释放链接
		// httpClient.close();

		return content;
	}

	// 使用序列化的方式保存CookieStore到本地文件，方便后续的读取使用
	private static void saveCookieStore(CookieStore cookieStore, String savePath) throws IOException {

		FileOutputStream fs = new FileOutputStream(savePath);
		ObjectOutputStream os = new ObjectOutputStream(fs);
		os.writeObject(cookieStore);
		os.close();

	}

	// 读取Cookie的序列化文件，读取后可以直接使用
	private static CookieStore readCookieStore(String savePath) throws IOException, ClassNotFoundException {

		FileInputStream fs = new FileInputStream("cookie");// ("foo.ser");
		ObjectInputStream ois = new ObjectInputStream(fs);
		CookieStore cookieStore = (CookieStore) ois.readObject();
		ois.close();
		return cookieStore;

	}

}