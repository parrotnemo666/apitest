package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AESDao {
	
	private static final Logger logger = LogManager.getLogger(AESDao.class);

	public static void logToDatabase(String data, String encryptedData, String iv, String key) {
       String sql = "INSERT INTO AES_ENCRYPTION_DECRYPTION (data, dataresult, iv, key, timestamp) VALUES (?, ?, ?, ?, ?)";

       try (Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

           statement.setString(1, data);
           statement.setString(2, encryptedData);
           statement.setString(3, iv);
           statement.setString(4, key);
           statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

           statement.executeUpdate();
           logger.info("加密結果已保存到資料庫。");
       } catch (SQLException e) {
           logger.error("保存加密結果到資料庫時發生錯誤", e);
       }
   }

}
