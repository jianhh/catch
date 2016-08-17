package com.framework.httpsend;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.util.StringUtils;

/**
 * Http接口连接方法
 * 
 * @author tangbiao
 * 
 */
public class HttpSend {
	private static JLogger logger = LoggerFactory.getLogger(HttpSend.class);
	
	private static URLConnection reload(URLConnection uc) throws Exception {

        HttpURLConnection huc = (HttpURLConnection) uc;
        
        if (huc.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP 
        		|| huc.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM)// 302, 301
        	return reload(new URL(huc.getHeaderField("location")).openConnection());
        
        return uc;
	}

	/**
	 * 发送方法
	 * 
	 * @param sendUrl
	 *            请求地址
	 * @param reqString
	 *            请求Post内容
	 * @param httpVo
	 *            http请求参数配置
	 * @return
	 */
	public final static HttpReturnVo senderReq(String reqString,
			String sendUrl, HttpParsVo httpVo) {

		HttpReturnVo retVo = new HttpReturnVo();
		long begin = java.lang.System.currentTimeMillis(); // 开始时间
		long end = 0; // 结束时间
		String reqHeadStr = ""; // 请求头参数

		String respString = "";
		HttpURLConnection connection = null;
		InputStream in = null;
		String proxyip = "非代理请求";// 记录代理ip
		try {
			Proxy proxy = null;
			if (StringUtils.isNotEmpty(httpVo.getProxyHost())) {
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(httpVo
						.getProxyHost(), httpVo.getProxyPort()));
				proxyip = "代理ip " + httpVo.getProxyHost() + ":"
						+ httpVo.getProxyPort();
			}
			// 创建连接
			URL url = new URL(sendUrl);
			String readTimeout = "300000";
			String connectTimeout = "300000";
			if (StringUtils.isNotEmpty(httpVo.getProxyHost())) {
				connection = (HttpURLConnection) url.openConnection(proxy);
			} else {
				connection = (HttpURLConnection) url.openConnection();
			}
			connection.setConnectTimeout(Integer.parseInt(connectTimeout));
			connection.setReadTimeout(Integer.parseInt(readTimeout));
			// 根据httpVo中的参数初始化connection
			getConnectionByParms(connection, httpVo);

			if (httpVo.isPost()) {
				// POST请求
				DataOutputStream out = new DataOutputStream(connection
						.getOutputStream());
				if (null != reqString) {
					out.write(reqString.getBytes("utf-8"));
				}
				out.flush();
				out.close();
			} else {
				connection.connect();
			}
			
			connection = (HttpURLConnection) reload(connection);

			if (httpVo.isPost()) {
				reqHeadStr = reqString;
			} else {
				reqHeadStr = getHeadString(httpVo);
			}

			/* 获取服务器端返回信息 */
			in = connection.getInputStream();
			end = java.lang.System.currentTimeMillis();
			Map headers = connection.getHeaderFields();
			Map header = new HashMap();
			if (headers != null) {
				Set<String> keys = headers.keySet();
				for (String key : keys) {
					if (key != null && connection.getHeaderField(key) != null
							&& !"".equals(connection.getHeaderField(key))) {
						String val = connection.getHeaderField(key);
						header.put(key, val);
					}
				}
			}
			byte[] tempb = InputStreamToByte(in);
			retVo.setHeaders(header);
			retVo.setTempb(tempb);
			respString = new String(tempb, httpVo.getCharset());
			retVo.setRespString(respString);

		} catch (Exception e) {
			logger.error(proxyip + " 接口发送失败：" + sendUrl, e);
			// 发送邮件
			// SimpleMailSender sms = MailSenderFactory.getSender();
			// sms.send(CommodityContent.getRecipients(), proxyip + " 接口发送失败："
			// + sendUrl, LogUtil.getExceptionError(e));
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return retVo;
	}

	private static void getConnectionByParms(HttpURLConnection connection,
			HttpParsVo httpVo) throws Exception {

		if (null == httpVo) {
			httpVo = new HttpParsVo();
		}

		connection.setDoOutput(httpVo.isDoOutput());
		connection.setDoInput(httpVo.isDoInput());
		if (httpVo.isPost()) {
			connection.setRequestMethod("POST");
		} else {
			connection.setRequestMethod("GET");
		}
		connection.setUseCaches(httpVo.isUseCaches());
		connection.setInstanceFollowRedirects(httpVo
				.isInstanceFollowRedirects());
		connection.setReadTimeout(httpVo.getReadTimeout());
		connection.setConnectTimeout(httpVo.getConnectTimeout());

		Map<String, String> headMap = httpVo.getHeadMap();
		if (headMap != null && !headMap.isEmpty()) {
			// 设置head参数
			for (String key : headMap.keySet()) {
				String value = headMap.get(key);
				connection.setRequestProperty(key, value);
			}
		}
	}

	private static String getHeadString(HttpParsVo httpVo) throws Exception {

		if (null == httpVo) {
			httpVo = new HttpParsVo();
		}
		StringBuffer str = new StringBuffer(); // get请求头参数记录
		Map<String, String> headMap = httpVo.getHeadMap();
		if (headMap != null && !headMap.isEmpty()) {
			// 设置head参数
			for (String key : headMap.keySet()) {
				String value = headMap.get(key);
				str
						.append(key + "：" + URLEncoder.encode(value, "utf-8")
								+ "\n");
			}
		}
		return str.toString();
	}

	public static byte[] InputStreamToByte(InputStream iStrm) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedInputStream bis = new BufferedInputStream(iStrm);
		int b = 0;
		try {
			while ((b = bis.read()) != -1)
				baos.write(b);
		} catch (IOException e) {
		}
		return baos.toByteArray();
	}

}
