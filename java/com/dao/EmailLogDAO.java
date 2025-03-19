package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.model.Email.EmailRequest;
import com.model.Email.EmailResponse;


public class EmailLogDAO {

    private static final String INSERT_LOG_SQL = "INSERT INTO EMAIL_LOG (senderEmail, senderName, recipients, subject, textBody, htmlBody, attachments, contentType, status, messageId, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public void logEmail(EmailRequest request, EmailResponse response) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LOG_SQL)) {
             
            preparedStatement.setString(1, request.getSenderEmail());
//            preparedStatement.setString(2, senderName);
            preparedStatement.setString(2, request.getSenderName());
            preparedStatement.setString(3, String.join(",", request.getRecipients()));
            preparedStatement.setString(4, request.getSubject());
            preparedStatement.setString(5, request.getTextBody());
            preparedStatement.setString(6, request.getHtmlBody());
            preparedStatement.setString(7, String.join(",", request.getAttachmentsFilenames()));
            preparedStatement.setString(8, request.getAttachmentsContentType());
            preparedStatement.setString(9, response.getStatus());
            preparedStatement.setString(10, response.getMessageId());
            preparedStatement.setTimestamp(11, new Timestamp(System.currentTimeMillis()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error while logging email to database.", e);
        }
    }
}
