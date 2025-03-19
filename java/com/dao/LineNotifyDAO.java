package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LineNotifyDAO {
	

	private static final Logger logger = LogManager.getLogger(LineNotifyDAO.class);



	public static void logToDatabase(String token, String message, int status, String error) {
        String sql = "INSERT INTO LINE_NOTIFY (token, message, status, error, timestamp) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, token);
            statement.setString(2, message);
            statement.setInt(3, status);
            statement.setString(4, error);
            statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

            statement.executeUpdate();
            logger.info("API請求已經加入資料庫了");
        } catch (SQLException e) {
            logger.error("保存API的錯誤已經存進資料庫中", e);
        }
	}

}
