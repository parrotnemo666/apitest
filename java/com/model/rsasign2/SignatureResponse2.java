package com.model.rsasign2;

//SignatureResponse.java
public class SignatureResponse2 {
 private String uuid;
 private String signature;

 public SignatureResponse2(String uuid, String signature) {
     this.uuid = uuid;
     this.signature = signature;
 }

 // getters and setters
 public String getUuid() {
     return uuid;
 }

 public void setUuid(String uuid) {
     this.uuid = uuid;
 }

 public String getSignature() {
     return signature;
 }

 public void setSignature(String signature) {
     this.signature = signature;
 }
}