package com.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class RSA2LogDAO {
    private static final String INSERT_LOG_SQL = "INSERT INTO rsa_log (action, request_body, text_content, response_body, result, error_message, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?)";

    public void insertLog(String action, String requestBody, String textContent, String responseBody, String result, String errorMessage) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_LOG_SQL)) {

            stmt.setString(1, action);
            stmt.setString(2, requestBody);
            stmt.setString(3, textContent);
            stmt.setString(4, responseBody);
            stmt.setString(5, result);
            stmt.setString(6, errorMessage);
            stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
          
        }
    }

  
}
