package com.model.RsaSignatrue;


// 定義簽章響應的模型
public class SignatureResponse {
    private String signature;

    public SignatureResponse() {}

    public SignatureResponse(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
