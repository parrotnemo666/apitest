package com.service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dao.AESDao;
import com.dao.DBConnection;

public class AesUtils {
    
    private static final Logger logger = LogManager.getLogger(AesUtils.class); // 初始化 Logger
    
    // 定義加密、解密模式、填充方式
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 16;
 
//    public static void main(String[] args) {
//	System.out.println("**************START");
//	String encodedIv = generateIV();
//	//需要了解static差異，可以直接呼叫
//	System.out.println(encodedIv);
//	System.out.println("**************END");
//}
    
    // 生成密鑰key
    public static String generateKey() throws NoSuchAlgorithmException {
        logger.debug("正在生成 AES 密鑰..."); // 日誌：方法開始
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(KEY_SIZE);
        SecretKey secretKey = keyGen.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        logger.debug("成功生成 AES 密鑰: {}", encodedKey); // 日誌：方法結束
        return encodedKey;        
    }
    
    // 生成初始化向量
    public static String generateIV() {
        logger.debug("正在生成初始化向量 (IV)..."); // 日誌：方法開始
        byte[] iv = new byte[IV_SIZE];
        new java.security.SecureRandom().nextBytes(iv); //使用SecureRandom類來進行填充，常用於IV或是密要
        String encodedIv = Base64.getEncoder().encodeToString(iv);
        logger.debug("成功生成 IV: {}", encodedIv); // 日誌：方法結束
        return encodedIv;
    }
    
    
    // 方法 AES加密
    public static String encrypt(String data, String key, String iv) throws Exception {
        logger.debug("開始 AES 加密。數據: {}, 密鑰: {}, IV: {}", data, key, iv); // 日誌：方法開始
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));
        
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        String encryptedData = Base64.getEncoder().encodeToString(encrypted);
        logger.debug("AES 加密完成。加密後數據: {}", encryptedData); // 日誌：方法結束
        
        AESDao.logToDatabase(data, encryptedData, iv, key);
        
        return encryptedData;
    }
    
    // 方法 AES解密
    public static String decrypt(String encryptedData, String key, String iv) throws Exception {
        logger.debug("開始 AES 解密。加密數據: {}, 密鑰: {}, IV: {}", encryptedData, key, iv); // 日誌：方法開始
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));
        //需要多補KEY值
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        String decryptedData = new String(decrypted);
       
        logger.debug("AES 解密完成。解密後數據: {}", decryptedData); // 日誌：方法結束
        AESDao.logToDatabase(decryptedData, encryptedData, iv, key);

        return decryptedData;
    }

}
