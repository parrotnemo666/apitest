package com.model.RsaSignatrue;
// 驗證請求專用
public class SignatureRequest {
    private String text;

    public SignatureRequest() {}

    public SignatureRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
