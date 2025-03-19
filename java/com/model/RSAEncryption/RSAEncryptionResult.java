package com.model.RSAEncryption;

public class RSAEncryptionResult {
    private String text; // 加密或解密的結果文本

    public RSAEncryptionResult(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
