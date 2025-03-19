package com.model.Email;
//Attachment.java

public class Attachment {
 private String filename; //附件文件名
 private String content;  //附件內容
 private String contentType; //附件內容類型
 
 
 // Getters and setters
public String getFilename() {
	return filename;
}
public void setFilename(String filename) {
	this.filename = filename;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public String getContentType() {
	return contentType;
}
public void setContentType(String contentType) {
	this.contentType = contentType;
}

}
