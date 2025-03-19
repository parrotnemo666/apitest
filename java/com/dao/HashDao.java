package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HashDao {
	
	
	private static final Logger logger = LogManager.getLogger(HashDao.class);



	public static void saveHashToDB(String input, String algorithm, String hash) throws SQLException {

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
