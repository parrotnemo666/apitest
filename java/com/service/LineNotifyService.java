package com.service;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dao.DBConnection;
import com.dao.LineNotifyDAO;

public class LineNotifyService {

	private static final Logger logger = LogManager.getLogger(LineNotifyService.class);

//    public static void main(String[] args) {
//    	System.out.println("**************START");
//    	int returnHttoCode = new LineNotifyService().sendLineNotify1
//      ("Kx8mOcL0Vm8qFgXGFG15vTrHonBqtP7lMs5BK4WLkUf", "透過APPCATION測試，可以不用重啟SERVER", "172.16.20.114", 80);
//    	System.out.println(returnHttoCode);
////    	BodyPublishers bps = (BodyPublishers) HttpRequest.BodyPublishers.ofString("message=" + URLEncoder.encode("ABCDEFG", StandardCharsets.UTF_8));
////    	System.out.println(bps.toString());
//    	System.out.println("**************END");
//    }

	// 傳入token、msg 發送LINE通知
	public static int sendLineNotify1(String token, String message, String proxyHost, int proxyPort) {
		logger.info("開始發送LINE通知。Token: {}, Message: {}", token, message);
		try {
			// 獲取信任所有證書的sslContext
			SSLContext sslContext = getTrustAllSSLContext();

			// 建立HttpClient，設置SSL上下文
			HttpClient.Builder clientBuilder = HttpClient.newBuilder().sslContext(sslContext);

			// 設定代理
			if (proxyHost != null && !proxyHost.isEmpty() && proxyPort > 0) {
				clientBuilder.proxy(ProxySelector.of(new InetSocketAddress(proxyHost, proxyPort)));
				logger.info("使用代理發送請求，代理: {}，端口: {}", proxyHost, proxyPort);
			}
            //出發
			HttpClient client = clientBuilder.build();
   
			//LINENOTIFY的請求
			String url = "https://notify-api.line.me/api/notify";
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
					.header("Authorization", "Bearer " + token)
					.header("Content-Type", "application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers
							.ofString("message=" + URLEncoder.encode(message, StandardCharsets.UTF_8)))
					.build();
			logger.info("message ofstring: {}", HttpRequest.BodyPublishers
					.ofString("message=" + URLEncoder.encode(message, StandardCharsets.UTF_8)));

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			logger.info("LINE通知請求已發送。響應狀態碼: {}", response.statusCode());
			logger.debug("響應正文: {}", response.body());

			return response.statusCode();

		} catch (Exception e) {
			logger.error("發送LINE通知過程中發生錯誤: {}", e.getMessage(), e);
			return -1;
		} finally {
			LineNotifyDAO.logToDatabase(token, message, proxyPort, proxyHost);

		}

	}

	private static SSLContext getTrustAllSSLContext() throws Exception {
		logger.debug("設置 SSLContext 信任所有證書...");
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, trustAllCerts, new SecureRandom());
		logger.debug("SSLContext 設置完成。");
		return sslContext;
	}
}
