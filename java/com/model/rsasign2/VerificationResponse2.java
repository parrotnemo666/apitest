package com.model.rsasign2;


//定義驗證響應的模型
public class VerificationResponse2 {
 private boolean valid; // 指示簽名是否有效

 public VerificationResponse2() {}

 public VerificationResponse2(boolean valid) {
     this.valid = valid;
 }

 public boolean isValid() {
     return valid;
 }

 public void setValid(boolean valid) {
     this.valid = valid;
 }
}
