package com.service;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Properties;
import java.util.UUID;
import java.util.Base64;
import java.io.ByteArrayInputStream;
import java.sql.SQLException;

import com.model.Email.EmailRequest;
import com.model.Email.EmailResponse;
import com.dao.EmailLogDAO;
import com.model.Email.Attachment;
import com.sun.mail.smtp.SMTPTransport;




public class EmailService2 {
	
//	public static void main(String[] args) {
//    	System.out.println("**************START");
//    	int returnHttoCode = new EmailService2().sendEmail(null)
//    	System.out.println(returnHttoCode);
//
//    	System.out.println("**************END");
//    }
//	
	private static final Logger logger = LogManager.getLogger(EmailService2.class);

    // 設定郵件伺服器的IP地址
//    private static final String SMTP_SERVER = "172.16.5.12";
    private static final String SMTP_SERVER = Config.getProperty("smtp.server");
    // 設定發件人的郵件地址
//    private static final String SENDER_EMAIL = "Chun-Yi_Lai@ubot.com.tw";
    private static final String SENDER_EMAIL = Config.getProperty("sender.email");
    // 設定發件人的郵件密碼
//    private static final String SENDER_PASSWORD = "Ubot@12345";
    private static final String SENDER_PASSWORD = Config.getProperty("sender.password");
    /**
     * 發送電子郵件的方法
     * @param request 包含電子郵件信息的請求物件
     * @return 包含發送結果的EmailResponse物件
     * @throws Exception 當發送郵件失敗時拋出異常
     */
    private EmailLogDAO emailLogDAO = new EmailLogDAO();
    
    public EmailResponse sendEmail(EmailRequest request) throws Exception {
        // 設定郵件伺服器的屬性
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_SERVER);
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.debug", "true"); // 啟用調試模式
        
        
        logger.info("Starting to send email to :{}",String.join(",",request.getRecipients()));
        // 建立郵件會話，並進行身份驗證
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
            	logger.info("已經把寄件者資料送進session裡面，完成驗證",SENDER_EMAIL, SENDER_PASSWORD);
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });
        
        SMTPTransport transport = null;
        EmailResponse response = null;
//        String senderName = "Chun-Yi Lai"; // 可以从配置或其他地方获取
        
        

        try {
            // 建立郵件消息物件
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, 
                InternetAddress.parse(String.join(",", request.getRecipients())));
            message.setSubject(request.getSubject());

            // 建立多部分郵件內容
            Multipart multipart = new MimeMultipart();

            // 添加純文本部分
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(request.getTextBody(), "utf-8");
            multipart.addBodyPart(textPart);

            // 如果有 HTML 內容，則添加 HTML 部分
            if (request.getHtmlBody() != null && !request.getHtmlBody().isEmpty()) {
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(request.getHtmlBody(), "text/html; charset=utf-8");
                multipart.addBodyPart(htmlPart);
            }

            // 添加附件部分
            if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
                for (Attachment attachment : request.getAttachments()) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.setFileName(attachment.getFilename());
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(attachment.getContent()));
                    DataSource dataSource = new ByteArrayDataSource(inputStream, attachment.getContentType());
                    attachmentPart.setDataHandler(new DataHandler(dataSource));
                    multipart.addBodyPart(attachmentPart);
                }
            }

            // 設定郵件內容
            message.setContent(multipart);

            // 使用 SMTPTransport 發送郵件
            transport = (SMTPTransport) session.getTransport("smtp");
            transport.connect(SMTP_SERVER, SENDER_EMAIL, SENDER_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());

            logger.info("Email sent successfully.");

            // 獲取伺服器回應碼和消息
            int responseCode = transport.getLastReturnCode();
            String responseMessage = transport.getLastServerResponse();
            logger.info("Response Code: {}", responseCode);
            logger.info("Response Message: {}", responseMessage);

            transport.close();

            // 生成唯一的 messageId
            String messageId = UUID.randomUUID().toString();
            logger.info("Generated messageId: {}", messageId);

            // 返回發送結果
            response = new EmailResponse("SUCCESS", messageId);

            // 記錄成功的郵件發送到資料庫
            emailLogDAO.logEmail(request, response);

        } catch (MessagingException e) {
            logger.error("Failed to send email: {}", e.getMessage(), e);

            // 生成失敗的回應
            response = new EmailResponse("FAILED", null);

            // 如果發送失敗，也記錄日志
            try {
                emailLogDAO.logEmail(request, response);
            } catch (SQLException logException) {
                logger.error("Failed to log email to database: {}", logException.getMessage(), logException);
            }

            // 重新拋出異常
            throw e;

        } finally {
            if (transport != null && transport.isConnected()) {
                try {
                    transport.close();
                    logger.debug("SMTP connection closed.");
                } catch (MessagingException e) {
                    logger.error("Failed to close SMTP connection: {}", e.getMessage(), e);
                }
            }
        }

        return response;
    }
}
        
//
//        SMTPTransport transport = null;
//        try {
//            // 建立一個郵件Message消息物件
//            Message message = new MimeMessage(session);
//            // 設定發件人地址
//            message.setFrom(new InternetAddress(SENDER_EMAIL));
//            // 設定收件人地址
//             message.setRecipients(Message.RecipientType.TO, 
//            InternetAddress.parse(String.join(",", request.getRecipients())));
//            // 設定郵件主題
//            message.setSubject(request.getSubject());
//            
//            logger.info("已完成郵件主題、收件人地址、發件人地址");
//
//            // 建立多部分郵件內容
//            Multipart multipart = new MimeMultipart();
//
//            // 添加純文本部分
//            MimeBodyPart textPart = new MimeBodyPart();
//            textPart.setText(request.getTextBody(), "utf-8");
//            multipart.addBodyPart(textPart);
//            logger.info("已完成郵件添加純文本");
//
//            // 如果有 HTML 內容，則添加 HTML 部分
//            if (request.getHtmlBody() != null && !request.getHtmlBody().isEmpty()) {
//                MimeBodyPart htmlPart = new MimeBodyPart();
//                htmlPart.setContent(request.getHtmlBody(), "text/html; charset=utf-8");
//                multipart.addBodyPart(htmlPart);
//                logger.info("已完成郵件添加HTML");
//            }
//
//            // 添加附件部分
//            if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
//                for (Attachment attachment : request.getAttachments()) {
//                	logger.info("正在處理附件:{}", attachment.getFilename());
//                    MimeBodyPart attachmentPart = new MimeBodyPart();
//                    attachmentPart.setFileName(attachment.getFilename());
//                    ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(attachment.getContent()));
//                    DataSource dataSource = new ByteArrayDataSource(inputStream, attachment.getContentType());
//                    attachmentPart.setDataHandler(new DataHandler(dataSource));
//                    multipart.addBodyPart(attachmentPart);
//                }
//                //太少log的東西去去進行分別，請用剪法的發誓進行添加
//            }
//
//            // 設定郵件內容
//            message.setContent(multipart);
//
//            // 使用 SMTPTransport 發送郵件
//            transport = (SMTPTransport) session.getTransport("smtp");
//            transport.connect(SMTP_SERVER, SENDER_EMAIL, SENDER_PASSWORD);
//            transport.sendMessage(message, message.getAllRecipients());
//            logger.info("已完成 SMTPTransport 發送郵件 ");
//
//            // 獲取伺服器回應碼和消息 並且印出來
//            int responseCode = transport.getLastReturnCode();
//            String responseMessage = transport.getLastServerResponse();
//            System.out.println("Response Code: " + responseCode);
//            System.out.println("Response Message: " + responseMessage);
//            logger.info("Response Code: " + responseCode);
//            logger.info("Response Message: " + responseMessage);
//            
//            transport.close();
//
//            // 生成唯一的 messageId
//            String messageId = UUID.randomUUID().toString();
//            //要加LOG
//            logger.info("Generated messageId: {}",messageId);
//            
//
//   
////            // 返回發送結果
//            return new EmailResponse("SUCCESS", messageId);
//        } catch (MessagingException e) {
//                logger.error("消息異常發生: {}", e.getMessage(), e); // 日誌：處理消息異常
//                if (e.getCause() instanceof java.net.ConnectException) {
//                    throw new Exception("無法連接到SMTP伺服器。請檢查伺服器地址和端口。", e);
//                } else if (e instanceof AddressException) {
//                    throw new Exception("由於電子郵件地址格式無效，發送郵件失敗。", e);
//                } else {
//                    throw new Exception("由於意外錯誤，發送郵件失敗。", e);
//                }
//            } finally {
//                if (transport != null && transport.isConnected()) {
//                    try {
//                        transport.close();
//                        logger.debug("SMTP 連接已成功關閉。"); // 日誌：關閉SMTP連接
//                    } catch (MessagingException e) {
//                        logger.error("無法關閉SMTP連接: {}", e.getMessage(), e); // 日誌：處理SMTP連接關閉失敗
//                    }
//                }
//            }
//        }
//    }