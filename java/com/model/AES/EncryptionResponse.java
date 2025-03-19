package com.model.AES;

public class EncryptionResponse {
    private String encryptedData;
    private String key;
    private String iv;

    // 无参构造函数
    public EncryptionResponse() {}

    // 带参构造函数
    public EncryptionResponse(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    // 带参构造函数
    public EncryptionResponse(String encryptedData, String key, String iv) {
        this.encryptedData = encryptedData;
        this.key = key;
        this.iv = iv;
    }

    // Getter 和 Setter 方法
    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
