package com.everwing.coreservice.wy.core.request;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;


public class HttpsRequest {

	public interface ResultListener {

		public void onConnectionPoolTimeoutError();

	}

	private static final Logger logger = LoggerFactory.getLogger(HttpsRequest.class);
	// 表示请求器是否已经做了初始化工作
	private boolean hasInit = false;

	// 连接超时时间，默认10秒
	private int socketTimeout = 10000;

	// 传输超时时间，默认30秒
	private int connectTimeout = 30000;

	// 请求器的配置
	private RequestConfig requestConfig;

	// HTTP请求器
	private CloseableHttpClient httpClient;

	public HttpsRequest() throws UnrecoverableKeyException, KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException, IOException {
		init();
	}

	private void init() throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException,
			KeyManagementException {
		hasInit = true;
		httpClient = HttpClients.custom().build();
	}

	/**
	 * 通过Https往API post xml数据
	 *
	 * @param url
	 *            API地址
	 * @param xmlObj
	 *            要提交的XML数据对象
	 * @return API回包的实际数据
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */

	public ScanPayResData savePost(String url, Object xmlObj) throws IOException, KeyStoreException,
			UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {

		if (!hasInit) {
			init();
		}

		String result = null;

		HttpPost httpPost = new HttpPost(url);

		// 解决XStream对出现双下划线的bug
		// XStream xStreamForRequestPostData = new XStream(new
		// DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		// 将要提交给API的数据对象转换成XML格式数据Post给API
		// String postDataXML = xStreamForRequestPostData.toXML(xmlObj);
		String postDataXML = toXml(xmlObj);
		// 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
		StringEntity postEntity = new StringEntity(postDataXML,"UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.setEntity(postEntity);

		// 设置请求器的配置
		httpPost.setConfig(requestConfig);

		try {
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity entity = response.getEntity();

			result = EntityUtils.toString(entity, "UTF-8");

		} catch (ConnectionPoolTimeoutException e) {
			logger.error("http get throw ConnectionPoolTimeoutException(wait time out)");

		} catch (ConnectTimeoutException e) {
			logger.error("http get throw ConnectTimeoutException");

		} catch (SocketTimeoutException e) {
			logger.error("http get throw SocketTimeoutException");

		} catch (Exception e) {
			logger.error("http get throw exception");

		} finally {
			httpPost.abort();
		}
		ScanPayResData scanPayResData = formXml(result, ScanPayResData.class);
		return scanPayResData;
	}

	/**
	 * 对象解析成xml格式
	 * 
	 * @param obj
	 * @return
	 */
	public static String toXml(Object obj) {
		// 解决xstream将对象转换成xml 时出现2个_
		XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
		xStream.autodetectAnnotations(true);
		return xStream.toXML(obj);
	}

	/**
	 * 解析xml
	 * 
	 * @param xml
	 * @param clazz
	 * @return
	 */
	public static <T> T formXml(String xml, Class<T> clazz) {
		XStream xStream = new XStream();
		xStream.processAnnotations(clazz);
		return clazz.cast(xStream.fromXML(xml));
	}

	/**
	 * 设置连接超时时间
	 *
	 * @param socketTimeout
	 *            连接时长，默认10秒
	 */
	public void setSocketTimeout(int socketTimeout) {
		socketTimeout = socketTimeout;
		resetRequestConfig();
	}

	/**
	 * 设置传输超时时间
	 *
	 * @param connectTimeout
	 *            传输时长，默认30秒
	 */
	public void setConnectTimeout(int connectTimeout) {
		connectTimeout = connectTimeout;
		resetRequestConfig();
	}

	private void resetRequestConfig() {
		requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout)
				.build();
	}

	/**
	 * 允许商户自己做更高级更复杂的请求器配置
	 *
	 * @param requestConfig
	 *            设置HttpsRequest的请求器配置
	 */
	public void setRequestConfig(RequestConfig requestConfig) {
		requestConfig = requestConfig;
	}
}
