package com.model.RsaSignatrue;


//定義驗證請求的模型
public class VerificationRequest {
 private String text;
 private String signature;

 public VerificationRequest() {}

 
 public VerificationRequest(String text, String signature) {
     this.text = text;
     this.signature = signature;
 }

 public String getText() {
     return text;
 }

 public void setText(String text) {
     this.text = text;
 }

 public String getSignature() {
     return signature;
 }

 public void setSignature(String signature) {
     this.signature = signature;
 }
}
