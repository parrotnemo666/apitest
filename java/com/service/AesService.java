package com.service;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class AesService {
	
    private static final Logger logger = LogManager.getLogger(AesService.class); // 初始化 Logger
    //生成密鑰KEY
    public static String generateKey() {
		logger.debug("正在生成 AES 密鑰...");
		KeyGenerator keyGen = null;
		try {
			keyGen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			logger.debug("生成AES密鑰中發生NoSuchAlgorithmException錯誤:{}",e);
			e.printStackTrace();
		}
		keyGen.init(256);
		SecretKey secretKey = keyGen.generateKey();
    	String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    	
    	return encodedKey;  	
    }
    //生成初始化向量
    public static String generateIV() {
    	logger.debug("正在生成向量(IV)");
    	
    	byte[] iv = new byte[16];
        new java.security.SecureRandom().nextBytes(iv); //使用SecureRandom類來填重
        String encodedIv = Base64.getEncoder().encodeToString(iv);
        logger.debug("成功生成 IV: {}", encodedIv); 
        return encodedIv;
    	
    }
    //AES加密
    
    public static String encrypt(String data,String key,String iv) {
    	Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			logger.debug("生成AES密鑰中發生NoSuchAlgorithmException 錯誤:{}",e);
			e.printStackTrace();
			return "AES001";
		} catch (NoSuchPaddingException e) {
			logger.debug("生成AES密鑰中發生NoSuchPaddingException 錯誤:{}",e);
			e.printStackTrace();
			return "AES002";
		}
    	
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
    	IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));
    
    	
    	try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
		} catch (InvalidKeyException e) {
			logger.debug("生成AES密鑰中發生InvalidKeyException 錯誤:{}",e);
			e.printStackTrace();
			return "AES003";
		} catch (InvalidAlgorithmParameterException e) {
			logger.debug("生成AES密鑰中發生InvalidAlgorithmParameterException錯誤:{}",e);
			e.printStackTrace();
			return "AES004";
		}
		byte[] encrypted = null;
		try {
			encrypted = cipher.doFinal(data.getBytes());
		} catch (IllegalBlockSizeException e) {
			logger.debug("生成AES密鑰中發生IllegalBlockSizeException錯誤:{}",e);
			e.printStackTrace();
			return "AES003";
		} catch (BadPaddingException e) {
			logger.debug("生成AES密鑰中發生BadPaddingException錯誤:{}",e);
			e.printStackTrace();
			return "AES004";
		}
		 String encryptedData = Base64.getEncoder().encodeToString(encrypted);
     	
    	return encryptedData;
    }

    //AES解密
    
    public static String decryt(String encryptedData, String key, String iv) {
    	Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			logger.debug("生成AES密鑰中發生 錯誤:{}",e);
			e.printStackTrace();
			return "AES004";
		}
    	
    	 SecretKeySpec secretKeyspec = new SecretKeySpec(Base64.getDecoder().decode(key), "AES");
         IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));
    	
         try {
			cipher.init(Cipher.DECRYPT_MODE, secretKeyspec,ivParameterSpec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			logger.debug("生成AES密鑰中發生 錯誤:{}",e);
			
			e.printStackTrace();
			return "AES004";
		}
         byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
         byte[] decrypted = null;
		try {
			decrypted = cipher.doFinal(decodedBytes);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			logger.debug("生成AES密鑰中發生 錯誤:{}",e);
			
			e.printStackTrace();
			return "AES004";
		}
         String decryptedData = new String(decrypted);
		return decryptedData;
		
    }
    
    
}
