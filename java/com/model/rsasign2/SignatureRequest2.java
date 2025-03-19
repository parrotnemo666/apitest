package com.model.rsasign2;
//SignatureRequest.java
public class SignatureRequest2 {
 private String text;
 private String uuid; // 新增字段

 // getters and setters
 public String getText() {
     return text;
 }

 public void setText(String text) {
     this.text = text;
 }

 public String getUuid() {
     return uuid;
 }

 public void setUuid(String uuid) {
     this.uuid = uuid;
 }
}
