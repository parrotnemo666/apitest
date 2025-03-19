package com.service.RSA;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dao.DBConnection;
import com.dao.RSASignatureDaoDB2;



public class RSASignatureServiceDB2 {

    private static final Logger logger = LogManager.getLogger(RSASignatureServiceDB2.class);
    private static final String RSA = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private RSASignatureDaoDB2 rsaKeyDAO = new RSASignatureDaoDB2();

    public String generateKeyPairAndStore() throws Exception {
        logger.info("進入 generateKeyPairAndStore 方法。");
        try {
            KeyPair keyPair = generateKeyPair();
            String uuid = UUID.randomUUID().toString();

            rsaKeyDAO.saveKeyPair(uuid, keyPair.getPublic().getEncoded(), keyPair.getPrivate().getEncoded());

            logger.info("密鑰對已生成並存儲，UUID: {}", uuid);
            return uuid;
        } catch (Exception e) {
            logger.error("生成和存儲密鑰對時發生錯誤。", e);
            throw e;
        } finally {
            logger.info("退出 generateKeyPairAndStore 方法。");
        }
    }

    private PublicKey loadPublicKey(String uuid) throws Exception {
        logger.info("進入 loadPublicKey 方法，UUID: {}", uuid);
        try {
            byte[] publicKeyBytes = rsaKeyDAO.loadPublicKey(uuid);
            return KeyFactory.getInstance(RSA).generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        } catch (Exception e) {
            logger.error("加載公鑰時發生錯誤，UUID: {}", uuid, e);
            throw e;
        }
    }

    private PrivateKey loadPrivateKey(String uuid) throws Exception {
        logger.info("進入 loadPrivateKey 方法，UUID: {}", uuid);
        try {
            byte[] privateKeyBytes = rsaKeyDAO.loadPrivateKey(uuid);
            return KeyFactory.getInstance(RSA).generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        } catch (Exception e) {
            logger.error("加載私鑰時發生錯誤，UUID: {}", uuid, e);
            throw e;
        }
    }

    public String sign(String plainText, String uuid) throws Exception {
        logger.info("進入 sign 方法，UUID: {}", uuid);
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(loadPrivateKey(uuid));
            //明文轉換成為bytes 並更新到 signature
            signature.update(plainText.getBytes());
            //執行簽名操作
            byte[] signatureBytes = signature.sign();
            String result = Base64.getEncoder().encodeToString(signatureBytes);
            logger.info("簽名成功，UUID: {}", uuid);
            
            //
            rsaKeyDAO.insertSignatureLog(uuid, plainText, result);
            
            return result;
        } catch (Exception e) {
            logger.error("簽名過程中發生錯誤，UUID: {}", uuid, e);
            throw e;
        }
    }

    public boolean verify(String plainText, String signatureStr, String uuid) throws Exception {
        logger.info("進入 verify 方法，UUID: {}", uuid);
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(loadPublicKey(uuid));
            signature.update(plainText.getBytes());
            byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);
            boolean result = signature.verify(signatureBytes);
            logger.info("驗證結果，UUID: {}, 驗證通過: {}", uuid, result);
            
            //
            rsaKeyDAO.insertVerificationLog(uuid, plainText, signatureStr, result);
            
            return result;
        } catch (Exception e) {
            logger.error("驗證簽名過程中發生錯誤，UUID: {}", uuid, e);
            throw e;
        }
    }

    private KeyPair generateKeyPair() throws Exception {
        logger.info("生成RSA密鑰對。");
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            logger.error("生成RSA密鑰對時發生錯誤。", e);
            throw e;
        }
    }
    
    
}
