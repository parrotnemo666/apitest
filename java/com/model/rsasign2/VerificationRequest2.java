package com.model.rsasign2;

//VerificationRequest.java
public class VerificationRequest2 {
 private String text;
 private String signature;
 private String uuid; // 新增字段

 // getters and setters
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

 public String getUuid() {
     return uuid;
 }

 public void setUuid(String uuid) {
     this.uuid = uuid;
 }
}