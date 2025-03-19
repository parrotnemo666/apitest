package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.db2.jcc.am.v;

public class ApiLogDao {
	
	
//	public static void main(String[] args) {
//		
//		String[] ApiLog = {"A","b","c","d","e"};
//        System.out.println(ApiLog);
//        
//        for (int i = 0; i<ApiLog.length ;i++) {
//        	System.out.println(ApiLog[i]);
//			
//		}
//        
//	}
	
	
	


	 private static final Logger logger = LogManager.getLogger(ApiLogDao.class);

	    // 改進的插入方法
	    public void insertLog(String requestMethod, String requestPath, String requestHeaders, String requestBody, int responseStatus, String responseBody, String errorMessage, String logIP) {
	        String sql = "INSERT INTO api_logs (request_method, request_path, request_headers, request_body, response_status, response_body, error_message, timestamp, log_IP) VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
	        
	        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, requestMethod);
	            stmt.setString(2, requestPath);
	            stmt.setString(3, requestHeaders);
	            stmt.setString(4, requestBody);
	            stmt.setInt(5, responseStatus);
	            stmt.setString(6, responseBody);
	            stmt.setString(7, errorMessage);
	            stmt.setString(8, logIP);
	            
//	            String[] ApiLog = {"A","b","c","d","e"};
//	            System.out.println(ApiLog);
	            System.out.println("印出要進DB2:具體的長度");
	            //印出具體的長度，讓我的
	            System.out.println("requestMethod: "+requestMethod.length());
	            System.out.println("requestPath: "+requestPath.length());
	            System.out.println("requestHeaders: "+requestHeaders.length());
	            System.out.println("requestBody: "+requestBody.length());	            
	            System.out.println(responseStatus);
	            System.out.println("responseBody: "+responseBody.length());
	            System.out.println("errorMessage: "+errorMessage.length());
	            System.out.println("logIP: "+logIP.length());

	            stmt.executeUpdate();
	        } catch (SQLException e) {
	            logger.error("Error inserting log entry", e);
	            // 這裡可以選擇拋出自定義異常或進行特定的異常處理
	        }
	    }
}