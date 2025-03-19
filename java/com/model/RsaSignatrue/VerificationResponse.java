package com.model.RsaSignatrue;


//定義驗證響應的模型
public class VerificationResponse {
 private boolean valid; // 指示簽名是否有效

 public VerificationResponse() {}

 public VerificationResponse(boolean valid) {
     this.valid = valid;
 }

 public boolean isValid() {
     return valid;
 }

 public void setValid(boolean valid) {
     this.valid = valid;
 }
}
