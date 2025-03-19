package com.service.RSA.RSAEncryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.ws.rs.core.Request;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.controller.RSAEncryptionController;
import com.service.Config;


public class RSAEncryptionService {

    private static final String RSA = "RSA"; // 定義RSA算法名稱
    private static final String KEY_DIRECTORY = Config.getProperty("key.directory"); // 從配置文件中獲取密鑰文件存放目錄
    private static final Map<String, KeyPair> keyPairs = new HashMap<>(); // 存儲UUID與密鑰對的映射
    private static final Logger logger = LogManager.getLogger(RSAEncryptionService.class);
    // 靜態初始化區塊，確保密鑰存放的目錄存在
    static {
        try {
            File keyDir = new File(KEY_DIRECTORY);
            if (!keyDir.exists()) {
                keyDir.mkdirs(); // 如果目錄不存在，則創建
                logger.info("已創建目錄:{}",KEY_DIRECTORY);
            }
        } catch (Exception e) {
        
            logger.error("初始化密鑰目錄時發生錯誤。",e);
        }
    }

    //第一支API用 生成新的密鑰對並返回UUID
    public String generateKeyPair() throws Exception {
    	logger.info("已經開始生成密鑰對!!");
        KeyPair keyPair = generateRSAKeyPair();
        String uuid = UUID.randomUUID().toString();
        keyPairs.put(uuid, keyPair);
        saveKeyPair(uuid, keyPair);
        logger.info("已生成密鑰對UUID:{}",uuid);
        return uuid;
    }

    //方法 生成RSA密鑰對
    private KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    //方法 保存密鑰對到文件
    private void saveKeyPair(String uuid, KeyPair keyPair) throws Exception {
        saveKey(KEY_DIRECTORY + File.separator + uuid + "_publicKey.ser", keyPair.getPublic());
        saveKey(KEY_DIRECTORY + File.separator + uuid + "_privateKey.ser", keyPair.getPrivate());
        logger.info("密鑰已保存到文件系統裡面 UUID:{}",uuid);
    }

    //方法 保存密鑰到文件
    private void saveKey(String fileName, Object key) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(key);
            logger.debug("密鑰已保存到文件系統裡面 檔案名:{}",fileName);

            //為什麼要用這個
        }
    }
    
    // 使用公鑰加密明文
    public String encrypt(String uuid, String plainText) throws Exception {
        logger.debug("已經開始加密 UUID:{}",uuid);

    	//拿UUID去找之前產生的KEY
    	KeyPair keyPair = getKeyPair(uuid);

        // 使用公鑰進行加密
        PublicKey publicKey = keyPair.getPublic();
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        logger.info("加密成功,UUID:{}",uuid);

        // 返回Base64編碼的加密結果
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 使用私鑰解密密文
    public String decrypt(String uuid, String encryptedText) throws Exception {
    	logger.debug("已經開始解密 UUID:{}",uuid);
    	KeyPair keyPair = getKeyPair(uuid);

        // 使用私鑰進行解密
        PrivateKey privateKey = keyPair.getPrivate();
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        logger.info("解密成功,UUID:{}",uuid);
        // 返回解密後的明文
        return new String(decryptedBytes);
    }

    //方法 根據UUID從Map中獲取密鑰對
    private KeyPair getKeyPair(String uuid) {
        //去MAP裡面去找key值
    	logger.info("正在獲取加密密鑰對,UUID:{}",uuid);
    	KeyPair keyPair = keyPairs.get(uuid);

        // 檢查密鑰對是否存在
        if (keyPair == null) {
            keyPair = loadKeyPair(uuid);
            keyPairs.put(uuid, keyPair);
        }

        if (keyPair == null) {
        	logger.error("密鑰對不存在，請確認 UUID 是否正確,UUID:{}",uuid);
            throw new IllegalArgumentException("密鑰對不存在，請確認 UUID 是否正確");
        }
        logger.info("已成功出現密鑰對,UUID:{}",uuid);
        return keyPair;
    }


    //方法 從文件系統中加載密鑰對
    private KeyPair loadKeyPair(String uuid) {
        try {
            PublicKey publicKey = loadKey(KEY_DIRECTORY + File.separator + uuid + "_publicKey.ser", PublicKey.class);
            PrivateKey privateKey = loadKey(KEY_DIRECTORY + File.separator + uuid + "_privateKey.ser", PrivateKey.class);
            logger.debug("已從本地文件中獲取密鑰對,UUID:{}",uuid);
            return new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
        	logger.info("文件加載密鑰時發生錯誤,UUID:{}",uuid,e);
            e.printStackTrace();
            return null;
        }
    }

    // 從文件中加載指定類型的密鑰
    //loadKey(加載的檔案, 指定的類型);
    private <KeyClass> KeyClass loadKey(String fileName, Class<KeyClass> keyClass) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            //把讀出來的object給讀出來，並用KEY.cast方法來完成轉換成我需要的類型(PublicKey or PrivateKey)
        	logger.info("已成功從文件中拿去到公鑰私鑰,UUID:{}",fileName);
        	return keyClass.cast(ois.readObject());
        }
    }

    
}



//// 使用公鑰加密明文
//public String encrypt(String uuid, String plainText) throws Exception {
//    // 根據UUID從Map中獲取密鑰對，如果Map中不存在則使用loadKeyPair方法從文件中加載密鑰對並放入Map中
//    KeyPair keyPair = keyPairs.computeIfAbsent(uuid, this::loadKeyPair);
//
//    // 檢查是否成功獲取密鑰對，如果為null，則拋出IllegalArgumentException
//    if (keyPair == null) {
//        throw new IllegalArgumentException("密鑰對不存在，請確認 UUID 是否正確");
//    }
//
//    // 從密鑰對中獲取公鑰
//    PublicKey publicKey = keyPair.getPublic();
//
//    // 創建Cipher對象，並指定加密演算法為RSA
//    Cipher cipher = Cipher.getInstance("RSA");
//
//    // 初始化Cipher對象，設置為加密模式，並將公鑰作為參數傳入
//    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//
//    // 將明文字串轉換為字節數組，並使用Cipher對象進行加密
//    byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
//
//    // 將加密後的字節數組轉換為Base64編碼的字串，並返回該字串
//    return Base64.getEncoder().encodeToString(encryptedBytes);
//}
//
//// 使用私鑰解密密文
//public String decrypt(String uuid, String encryptedText) throws Exception {
//    // 根據UUID從Map中獲取密鑰對，如果Map中不存在則使用loadKeyPair方法從文件中加載密鑰對並放入Map中
//    KeyPair keyPair = keyPairs.computeIfAbsent(uuid, this::loadKeyPair);
//
//    // 檢查是否成功獲取密鑰對，如果為null，則拋出IllegalArgumentException
//    if (keyPair == null) {
//        throw new IllegalArgumentException("密鑰對不存在，請確認 UUID 是否正確");
//    }
//
//    // 從密鑰對中獲取私鑰
//    PrivateKey privateKey = keyPair.getPrivate();
//
//    // 創建Cipher對象，並指定加密演算法為RSA
//    Cipher cipher = Cipher.getInstance("RSA");
//
//    // 初始化Cipher對象，設置為解密模式，並將私鑰作為參數傳入
//    cipher.init(Cipher.DECRYPT_MODE, privateKey);
//
//    // 將Base64編碼的密文解碼為字節數組，並使用Cipher對象進行解密
//    byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
//
//    // 將解密後的字節數組轉換為字串，並返回該字串
//    return new String(decryptedBytes);
//}
