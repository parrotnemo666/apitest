package com.model.Email;

//LineNotifyErrorResponse.java


import java.time.Instant;

public class ErrorResponse {
 private String status;  //狀態
 private String errorReason; //
 private String errorMessage; //錯誤跳出的訊息
 private String timestamp; //時間戳

 
 //錯誤訊息的建構子
 public ErrorResponse(String status, String error, String errorMessage) {
     this.status = status;
     this.errorMessage = errorMessage;
     this.timestamp = Instant.now().toString();
 }


public String getStatus() {
	return status;
}


public void setStatus(String status) {
	this.status = status;
}


public String getErrorReason() {
	return errorReason;
}


public void setErrorReason(String errorReason) {
	this.errorReason = errorReason;
}


public String getErrorMessage() {
	return errorMessage;
}


public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
}


public String getTimestamp() {
	return timestamp;
}


public void setTimestamp(String timestamp) {
	this.timestamp = timestamp;
}

 
}
