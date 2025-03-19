package com.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dao.DBConnection;

public class HashService {
	
	
	//可以直接使用MAIN方法進行測試的東西
//	public static void main(String[] args) throws NoSuchAlgorithmException {
//    	System.out.println("**************START");
//    	String returnHttpCode = new HashService().generateHash("8787","SHA-512");
//    	System.out.println("returnHttpCode is :" +  returnHttpCode);
//
//    	System.out.println("**************END");
//    }

	private static final Logger logger = LogManager.getLogger(HashService.class.getName()); // 初始化 Logger

	// 方法 產生雜湊值，可以選擇想要的加密算法
	public String generateHash(String input, String algorithm) throws NoSuchAlgorithmException, SQLException {
		logger.debug("正在使用算法 {} 生成輸入 {} 的雜湊值", algorithm, input); // 日誌：開始生成雜湊

		MessageDigest md = MessageDigest.getInstance(algorithm);
		byte[] hashInBytes = md.digest(input.getBytes());
		logger.info("已經完成加密算法，加密hashInBytes:{}",hashInBytes);
		String hash = Base64.getEncoder().encodeToString(hashInBytes);
		logger.info("已經完成加密算法，加密出來的文本:{}",hash);
		
		//將生成的雜湊保存到資料庫
		saveHashToDB(input, algorithm, hash);
		

//        // 字節轉16進制字符串的方法  HEX的方法
//        StringBuilder sb = new StringBuilder();
//        for (byte b : hashInBytes) {
//            // 將字節轉換為16進制表示b & 0xFF代表轉換成0~255 toHexString 數字轉換成為16進制的字符串 15變f 255變ff
//        	
//            String hex = Integer.toHexString(b & 0xFF);
//            
//            // 如果16進制字符串只有一位，則補 '0'
//            if (hex.length() == 1) {
//                sb.append('0');
//            }
//            
//            sb.append(hex); // 將16進制字符串附加到 StringBuilder
//        }
//
//        String hash = sb.toString();
//        logger.debug("成功生成雜湊值: {}", hash); // 日誌：雜湊值生成成功

		return hash;
	}
	//方法 塞入DB的方法
	  public void saveHashToDB(String input, String algorithm, String hash) throws SQLException {
	        String sql = "INSERT INTO HASH_MD5_SHA (input, algorithm, hash, timestamp) VALUES (?, ?, ?, ?)";

	        try (Connection connection = DBConnection.getConnection();
	             PreparedStatement statement = connection.prepareStatement(sql)) {

	            statement.setString(1, input);
	            statement.setString(2, algorithm);
	            statement.setString(3, hash);
	            //註解
	            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

	            int rowsInserted = statement.executeUpdate();
	            if (rowsInserted > 0) {
	                logger.info("數據成功插入資料庫");
	            }
	        } catch (SQLException e) {
	            logger.error("插入數據到資料庫時發生錯誤", e);
	            throw e;
	        }
	    }
}
