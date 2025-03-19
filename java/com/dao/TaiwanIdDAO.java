package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.service.LineNotifyService;



public class TaiwanIdDAO {

	private static final Logger logger = LogManager.getLogger(TaiwanIdDAO.class);

	public static void createTaiwanId(String id, boolean isValid, String correctID) {

		String sql = "INSERT INTO IDENTITY_VERIFICATION_CODE (id, valid, correctID, timestamp) VALUES (?, ?, ?, ?)";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, id);
			statement.setBoolean(2, isValid);
			statement.setString(3, correctID);
			statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

			statement.executeUpdate();
			logger.info("身份證驗證記錄已保存到資料庫。");
		} catch (SQLException e) {
			logger.error("保存身份證驗證記錄到資料庫時發生錯誤", e);
		}
	}
}
