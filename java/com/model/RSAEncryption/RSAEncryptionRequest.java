package com.model.RSAEncryption;



public class RSAEncryptionRequest {
    private String uuid; // 用於標識密鑰對的UUID
    private String text; // 要加密或解密的文本

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
