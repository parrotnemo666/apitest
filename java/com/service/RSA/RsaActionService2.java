package com.service.RSA;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RsaActionService2 {

    private static final Logger logger = LogManager.getLogger(RsaActionService2.class); // 初始化 Logger
    private static final String RSA = "RSA"; // 指定加密算法為 RSA

    // 使用公鑰加密文本
    public String encrypt(String publicKeyStr, String text)  {
        logger.info("開始使用公鑰加密文本。公鑰: {}, 文本: {}", publicKeyStr, text); // 日誌：方法開始
       
        byte[] encryptedBytes = null;
		try {
			PublicKey publicKey = getPublicKeyFromBase64(publicKeyStr);
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			encryptedBytes = cipher.doFinal(text.getBytes());
		} catch (InvalidKeyException e) {
			logger.error("",e);//堆疊 會把追蹤的錯誤印出來
			logger.error(""+ e.toString()); //字串+ 異常物件(InvalidKeyException)
			return "";
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
        logger.info("公鑰加密完成。加密後文本: {}", encryptedText); // 日誌：方法結束
        return encryptedText;
    }

    // 使用私鑰加密文本
    public String encryptWithPrivateKey(String privateKeyStr, String text) throws Exception {
        logger.info("開始使用私鑰加密文本。私鑰: {}, 文本: {}", privateKeyStr, text); // 日誌：方法開始
        PrivateKey privateKey = getPrivateKeyFromBase64(privateKeyStr);
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encryptedBytes = cipher.doFinal(text.getBytes());
        String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
        logger.info("私鑰加密完成。加密後文本: {}", encryptedText); // 日誌：方法結束
        return encryptedText;
    }

    // 使用私鑰解密文本
    public String decrypt(String privateKeyStr, String encryptedText) throws Exception {
        logger.info("開始使用私鑰解密文本。私鑰: {}, 加密文本: {}", privateKeyStr, encryptedText); // 日誌：方法開始
        PrivateKey privateKey = getPrivateKeyFromBase64(privateKeyStr);
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        String decryptedText = new String(decryptedBytes);
        logger.info("私鑰解密完成。解密後文本: {}", decryptedText); // 日誌：方法結束
        return decryptedText;
    }

    // 使用公鑰解密文本
    public String decryptWithPublicKey(String publicKeyStr, String encryptedText) throws Exception {
        logger.info("開始使用公鑰解密文本。公鑰: {}, 加密文本: {}", publicKeyStr, encryptedText); // 日誌：方法開始
        PublicKey publicKey = getPublicKeyFromBase64(publicKeyStr);
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        String decryptedText = new String(decryptedBytes);
        logger.info("公鑰解密完成。解密後文本: {}", decryptedText); // 日誌：方法結束
        return decryptedText;
    }

    // 從 Base64 編碼的字符串中獲取公鑰
    private PublicKey getPublicKeyFromBase64(String publicKeyStr) throws Exception {
        logger.debug("從 Base64 字符串中獲取公鑰。公鑰字符串: {}", publicKeyStr); // 日誌：方法開始
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey publicKey = keyFactory.generatePublic(spec);
        logger.debug("公鑰生成成功。"); // 日誌：方法結束
        return publicKey;
    }

    // 從 Base64 編碼的字符串中獲取私鑰
    private PrivateKey getPrivateKeyFromBase64(String privateKeyStr) throws Exception {
        logger.debug("從 Base64 字符串中獲取私鑰。私鑰字符串: {}", privateKeyStr); // 日誌：方法開始
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyStr);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey privateKey = keyFactory.generatePrivate(spec);
        logger.debug("私鑰生成成功。"); // 日誌：方法結束
        return privateKey;
    }
}
