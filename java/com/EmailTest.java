package com;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

//單元測試 聯邦email可不可以執行
public class EmailTest {
    private static final String SMTP_SERVER = "172.16.5.12"; // 郵件伺服器地址
    private static final String SENDER_EMAIL = "Chun-Yi_Lai@ubot.com.tw"; // 發件人郵箱地址
    private static final String SENDER_PASSWORD = "Ubot@12345"; // 發件人郵箱密碼
    private static final String RECIPIENT_EMAIL = "Pin-Han_Chen@ubot.com.tw"; // 收件人郵箱地址 品涵

    public static void main(String[] args) {
    	
    	
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_SERVER);
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.auth", "true");
        
        
        //建立郵件會話
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD); //提供發送人密碼和帳號
            }
        });
        
//        //如果沒有密碼的話會可以用嗎??
//        Session session = Session.getInstance(properties);

        try {
        	//創建郵件消息
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(RECIPIENT_EMAIL));
            message.setSubject("TO 聯邦飲料大隊長");
            message.setText("是時候啟動飲料外送了吧 =)");
            //可以發送CC(副本抄送 密送) 

            Transport.send(message);
            System.out.println("Email sent successfully");

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email");
        }
    }
}

