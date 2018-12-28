package com.everwing.coreservice.common.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MonKong
 * @Description 接口调用工具类
 * @date 2014年12月8日
 */
@SuppressWarnings("unused")
public class HttpUtils {
	public enum HttpMethod {
		GET, POST, PUT, DELETE
	}

	protected static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	public static final String CHARSET = "utf-8";

	public static String doDelete(String requestUrl, String requestBody) {
		return doRequest(requestUrl,HttpMethod.DELETE,requestBody,null,null);
	}
	
	public static String doPut(String requestUrl, String requestBody) {
		return doRequest(requestUrl,HttpMethod.PUT,requestBody,null,null);
	}
	
	public static String doPost(String requestUrl, String requestBody) {
		return doRequest(requestUrl,HttpMethod.POST,requestBody,null,null);
	}
	
	public static String doGet(String requestUrl, Map<String, String> params) {
		return doRequest(requestUrl,HttpMethod.GET,null,params,null);
	}

	/**
	 * HTTP请求的核心方法
	 *
	 * @date 2014年12月12日
	 */
	private static String doRequest(String requestUrl, HttpMethod method, String requestBody,
			Map<String, String> params, Map<String, String> headers)  {
		logger.info("HttpsUtils.doRequest()--开始");
		InputStream in = null;
		try {
			String queryStr = convertToQueryStr(params);

			logger.info("requestUrl + queryStr:  " + requestUrl + queryStr);
			HttpURLConnection conn = (HttpURLConnection) new URL(requestUrl + queryStr)
					.openConnection();
			conn.setReadTimeout(0);// 0秒为不限超时时间
			conn.setDefaultUseCaches(false);
			conn.setUseCaches(false);
			conn.setRequestMethod(method.toString());
			// conn.setRequestProperty("content-type","text/plain;charset=utf-8");
			if(headers!=null && headers.size()!=0){
				for(Entry<String, String> en :headers.entrySet()){
				 conn.setRequestProperty(en.getKey(),en.getValue());
				}
			}
			
			if (!HttpMethod.GET.equals(method)) {// 非GET特有逻辑
				conn.setDoOutput(true);
				OutputStream out = conn.getOutputStream();
				logger.info("requestBody："+requestBody);
				out.write(requestBody.getBytes(CHARSET));// 写入内容到流缓存
				close(out);// 正式发起请求
			}
			in = conn.getInputStream();
			String resultStr = readStreamAsStr(in);
			logger.info("返回内容："+resultStr);
			return resultStr;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally {
			logger.info("HttpsUtils.doRequest()--结束");
			try {
				close(in);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 把Map转为URL的查询字符串
	 *
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @date 2014年12月8日
	 */
	public static String convertToQueryStr(Map<String, String> params) throws UnsupportedEncodingException {
		// 拼凑参数
		StringBuffer queryStr = new StringBuffer("");
		if (params != null) {
			boolean isFirst = true;
			for (Map.Entry<String, String> en : params.entrySet()) {
				String value = en.getValue();
				if (value != null) {// 空值不转换
					String kv = en.getKey() + "=" + value.toString();
					if (isFirst) {
						queryStr.append("?");
						isFirst = false;
					} else {
						queryStr.append("&");
					}
					queryStr.append(kv);
				}
			}
		}
		return queryStr.toString();
	}

	/**
	 * 把流读取为字符串
	 *
	 * @param in
	 * @return
	 * @throws java.io.IOException
	 * @date 2014年12月8日
	 */
	private static String readStreamAsStr(InputStream in) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(in, CHARSET));
		StringBuffer content = new StringBuffer("");
		boolean isFirst = true;
		for (String str; (str = reader.readLine()) != null;) {
			if (isFirst) {// 第一行不需要换行
				content.append(str);
				isFirst = false;
			} else {
				content.append(System.getProperty("line.separator") + str);
			}
		}
		return content.toString();
	}

	/**
	 * 关闭流
	 *
	 * @param stream
	 * @throws java.io.IOException
	 * @date 2014年12月9日
	 */
	private static void close(Closeable stream) throws IOException {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				// logger.error("流关闭失败！", new exception());
				throw e;
			}
		} else {
			// logger.error("流为null");
		}
	}

	/*public static void downloadFile(String url, String postBody, File saveToFile)  {
		if (!saveToFile.getParentFile().exists()) {
			saveToFile.getParentFile().mkdirs();
		}
		URLConnection conn = new URL(url).openConnection();
		conn.setDoOutput(true);
		OutputStream out = conn.getOutputStream();
		IOUtils.write(postBody, out, CHARSET);
		out.close();// 发起请求
		IOUtils.copy(conn.getInputStream(), new FileOutputStream(saveToFile));
	}*/
}
