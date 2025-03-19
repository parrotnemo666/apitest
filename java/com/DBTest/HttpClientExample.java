package com.DBTest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;



public class HttpClientExample {
	
	public static void main(String[] args) {
		try {
			//定義JSON數據
			String json = "{\"username\":\"john\",\"age\":30}";
			
			//創建 HttpClient
			HttpClient client = HttpClient.newHttpClient();
			
			//創建 HttpRequest，設置為POST請求，並將JSON數據變成正文
			HttpRequest request= HttpRequest.newBuilder()
					.uri(URI.create("http://example.com/api/users"))
					.header("Content-Type", "application/json") 
					.POST(HttpRequest.BodyPublishers.ofString(json))//將JSON變成正文可以被請求
					.build();
		  //發送請求並獲取響應
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			
			System.out.println("Response status Code is :" + response.statusCode());
			System.out.println("Response body :"+ response.body());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
