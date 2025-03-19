package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountDAO {
	

	private static final Logger logger = LogManager.getLogger(AccountDAO.class);



	public static void logToDatabase(String startAccount, int count, String status, String reason, String accounts) {
        String sql = "INSERT INTO BANK_ACCOUNT_GENERATION (startAccount, count, status, reason, accounts, timestamp) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, Integer.parseInt(startAccount));
            statement.setInt(2, count);
            statement.setString(3, status);
            statement.setString(4, reason);
            statement.setString(5, accounts);
            statement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));

            statement.executeUpdate();
            logger.info("帳戶生成記錄已保存到資料庫。");
        } catch (SQLException e) {
            logger.error("保存帳戶生成記錄到資料庫時發生錯誤", e);
        }
    }

}