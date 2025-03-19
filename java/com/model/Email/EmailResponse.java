package com.model.Email;
//EmailResponse.java

import java.time.Instant;

//API的回覆
public class EmailResponse {
 private String status;    //狀態
 private String messageId; //訊息ID
 private String timestamp; //時間戳
 
 // Getters

 public EmailResponse(String status, String messageId) {
     this.status = status;
     this.messageId = messageId;
     this.timestamp = Instant.now().toString();
 }

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

public String getMessageId() {
	return messageId;
}

public void setMessageId(String messageId) {
	this.messageId = messageId;
}

public String getTimestamp() {
	return timestamp;
}

public void setTimestamp(String timestamp) {
	this.timestamp = timestamp;
}


 
 
}
