package com.service.RSA;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.Cipher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RSASignatureService {

    private static final Logger logger = LogManager.getLogger(RSASignatureService.class);

    private static final String RSA = "RSA";  // 定義RSA加密算法
//    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";  // 定義簽名算法
    private static final String KEY_DIR = "D:\\Key\\RSASIGN2"; // 定義密鑰存儲目錄

    // 構造方法：初始化時檢查並創建密鑰存儲目錄
    public RSASignatureService() {
        logger.info("初始化 RSASignatureService2，檢查密鑰存儲目錄是否存在。");
        File dir = new File(KEY_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
            logger.info("密鑰存儲目錄已創建於: {}", KEY_DIR);
        }
    }

    // 生成新的密鑰對，並將其保存到文件中，返回生成的UUID
    public String generateKeyPairAndStore() throws Exception {
        logger.info("進入 generateKeyPairAndStore 方法。");
        try {
            KeyPair keyPair = generateKeyPair();  // 生成密鑰對
            String uuid = UUID.randomUUID().toString(); // 為該密鑰對生成一個UUID
            saveKeyToFile(keyPair.getPublic(), getPublicKeyFilePath(uuid)); // 保存公鑰到文件
            saveKeyToFile(keyPair.getPrivate(), getPrivateKeyFilePath(uuid)); // 保存私鑰到文件
            logger.info("密鑰對已生成並存儲，UUID: {}", uuid);
            return uuid; // 返回UUID
        } catch (Exception e) {
            logger.error("生成和存儲密鑰對時發生錯誤。", e);
            throw e;
        } finally {
            logger.info("退出 generateKeyPairAndStore 方法。");
        }
    }

    // 根據UUID加載公鑰
    private PublicKey loadPublicKey(String uuid) throws Exception {
        logger.info("進入 loadPublicKey 方法，UUID: {}", uuid);
        try {
            return loadPublicKeyFromFile(getPublicKeyFilePath(uuid));  // 從文件中加載公鑰
        } catch (Exception e) {
            logger.error("加載公鑰時發生錯誤，UUID: {}", uuid, e);
            throw e;
        } finally {
            logger.info("退出 loadPublicKey 方法，UUID: {}", uuid);
        }
    }

    // 根據UUID加載私鑰
    private PrivateKey loadPrivateKey(String uuid) throws Exception {
        logger.info("進入 loadPrivateKey 方法，UUID: {}", uuid);
        try {
            return loadPrivateKeyFromFile(getPrivateKeyFilePath(uuid));  // 從文件中加載私鑰
        } catch (Exception e) {
            logger.error("加載私鑰時發生錯誤，UUID: {}", uuid, e);
            throw e;
        } finally {
            logger.info("退出 loadPrivateKey 方法，UUID: {}", uuid);
        }
    }
 // 使用私鑰對明文進行簽名（SHA-256 哈希 + RSA 加密）
    public String sign(String plainText, String uuid) throws Exception {
        logger.info("開始對文本進行簽名，UUID: {}", uuid);
        try {
//        	// 計算 SHA-256 哈希值
//        	MessageDigest digest = MessageDigest.getInstance("SHA-256"); //獲取256實例
//        	byte[] hash = digest.digest(plainText.getBytes("UTF-8")); //明文轉換成為數字節並計算哈希直
//        	logger.info("計算的 SHA-256的哈希值: {}", Base64.getEncoder().encodeToString(hash));
        	
        	// 使用私鑰對哈希值進行 RSA 加密 8787 //這邊本來是SHA256後進行RSA加密，但後面發現說用SHA256withRSA就好
        	Cipher cipher = Cipher.getInstance("SHA256withRSA");
        	cipher.init(Cipher.ENCRYPT_MODE, loadPrivateKey(uuid));
        	byte[] signatureBytes = cipher.doFinal(plainText.getBytes("UTF-8")); //得到簽名
        	
        	String signatureStr = Base64.getEncoder().encodeToString(signatureBytes);
        	logger.info("簽名完成，簽名結果: {}", signatureStr);
        	return signatureStr;
			
		} catch (Exception e) {
        	logger.error("簽名完成，簽名結果: {}", e.getMessage(),e);

			throw new Exception("Failed to sign data.",e);
		}
    }

//    // 使用私鑰對明文進行簽名
//    public String sign(String plainText, String uuid) throws Exception {
//        logger.info("進入 sign 方法，UUID: {}", uuid);
//        try {
//            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM); // 獲取簽名算法
//            signature.initSign(loadPrivateKey(uuid));  // 初始化簽名對象，使用私鑰
//            signature.update(plainText.getBytes());  // 更新簽名內容
//            byte[] signatureBytes = signature.sign();  // 執行簽名操作
//            String result = Base64.getEncoder().encodeToString(signatureBytes);  // 將簽名結果轉為Base64字符串
//            logger.info("簽名成功，UUID: {}", uuid);
//            return result;
//        } catch (Exception e) {
//            logger.error("簽名過程中發生錯誤，UUID: {}", uuid, e);
//            throw e;
//        } finally {
//            logger.info("退出 sign 方法，UUID: {}", uuid);
//        }
//    }

    // 手動驗證簽名（解密簽名 + 比較 SHA-256 哈希值）
    public boolean verify(String plainText, String signatureStr, String uuid) throws Exception {
        logger.info("開始驗證簽名，UUID: {}", uuid);

        // 計算明文的 SHA-256 哈希值
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] calculatedHash = digest.digest(plainText.getBytes("UTF-8"));
    	logger.info("計算的SHA-256哈希值: {}", Base64.getEncoder().encodeToString(calculatedHash));


        // 解密簽名，得到簽名時的哈希值
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, loadPublicKey(uuid)); //公鑰開始進行解密
        byte[] signatureBytes = Base64.getDecoder().decode(signatureStr); //64編碼的簽名字符串變成byte
        byte[] decryptedHash = cipher.doFinal(signatureBytes);
    	logger.debug("解密後的哈希值: {}", Base64.getEncoder().encodeToString(decryptedHash));


        // 比較兩個哈希值是否相等
        boolean isValid = MessageDigest.isEqual(calculatedHash, decryptedHash); //比較兩者的差異
        logger.info("簽名驗證結果: {}", isValid);
        return isValid;
    }
    
    // 使用公鑰驗證簽名是否有效
//    public boolean verify(String plainText, String signatureStr, String uuid) throws Exception {
//        logger.info("進入 verify 方法，UUID: {}", uuid);
//        try {
//            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM); // 獲取簽名算法
//            signature.initVerify(loadPublicKey(uuid));  // 初始化驗證對象，使用公鑰
//            signature.update(plainText.getBytes());  // 更新驗證內容
//            byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);  // 解碼簽名字符串
//            boolean result = signature.verify(signatureBytes);  // 驗證簽名是否有效
//            logger.info("驗證結果，UUID: {}, 驗證通過: {}", uuid, result);
//            return result;
//        } catch (Exception e) {
//            logger.error("驗證簽名過程中發生錯誤，UUID: {}", uuid, e);
//            throw e;
//        } finally {
//            logger.info("退出 verify 方法，UUID: {}", uuid);
//        }
//    }

    // 生成RSA密鑰對的方法
    private KeyPair generateKeyPair() throws Exception {
        logger.info("生成RSA密鑰對。");
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);  // 獲取RSA密鑰生成器
            keyPairGenerator.initialize(2048);  // 初始化密鑰長度
            return keyPairGenerator.generateKeyPair();  // 生成密鑰對
        } catch (Exception e) {
            logger.error("生成RSA密鑰對時發生錯誤。", e);
            throw e;
        }
    }

    // 保存密鑰到文件的方法
    private void saveKeyToFile(Object key, String fileName) throws Exception {
        logger.info("保存密鑰到文件: {}", fileName);
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(key);  // 將密鑰對象寫入文件
            logger.info("密鑰保存成功: {}", fileName);
        } catch (Exception e) {
            logger.error("保存密鑰到文件時發生錯誤: {}", fileName, e);
            throw e;
        }
    }

    // 從文件加載公鑰的方法
    private PublicKey loadPublicKeyFromFile(String fileName) throws Exception {
        logger.info("從文件加載公鑰: {}", fileName);
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (PublicKey) ois.readObject();  // 從文件中讀取並返回公鑰
        } catch (Exception e) {
            logger.error("從文件加載公鑰時發生錯誤: {}", fileName, e);
            throw e;
        }
    }

    // 從文件加載私鑰的方法
    private PrivateKey loadPrivateKeyFromFile(String fileName) throws Exception {
        logger.info("從文件加載私鑰: {}", fileName);
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (PrivateKey) ois.readObject();  // 從文件中讀取並返回私鑰
        } catch (Exception e) {
            logger.error("從文件加載私鑰時發生錯誤: {}", fileName, e);
            throw e;
        }
    }

    // 獲取私鑰文件的路徑
    private String getPrivateKeyFilePath(String uuid) {
        return KEY_DIR + File.separator + uuid + "_private.key";  // 拼接私鑰文件路徑
    }

    // 獲取公鑰文件的路徑
    private String getPublicKeyFilePath(String uuid) {
        return KEY_DIR + File.separator + uuid + "_public.key";  // 拼接公鑰文件路徑
    }
}
