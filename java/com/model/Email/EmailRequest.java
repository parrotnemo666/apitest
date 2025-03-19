package com.model.Email;
//EmailRequest.java

import java.util.List;
import java.util.stream.Collectors;

public class EmailRequest {
	private String senderEmail; // 發件人email
	private String senderName; // 發件人名
	private List<String> recipients; // 收件者列表

	private String subject; // 郵件主題
	private String textBody; // 純文字內容
	private String htmlBody; // HTML內容
	private List<Attachment> attachments; // 附件列表

	// Getters and setters
	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public List<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<String> recipients) {
		this.recipients = recipients;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTextBody() {
		return textBody;
	}

	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}

	public String getHtmlBody() {
		return htmlBody;
	}

	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

   //提取附件的名
	public List<String> getAttachmentsFilenames() {
		return this.attachments.stream().map(Attachment::getFilename).collect(Collectors.toList());
	}

	// 提取附件的檔案類型
	public String getAttachmentsContentType() {
		return this.attachments.isEmpty() ? null : this.attachments.get(0).getContentType();
	}

}