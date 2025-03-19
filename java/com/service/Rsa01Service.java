package com.service;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.encodings.PKCS1Encoding;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.util.PublicKeyFactory;

import com.dao.RsaSaveKeyPairDao;

public class Rsa01Service {

	private static final Map<String, KeyPair> keyPairs = new HashMap<>();

	// 產生一把金鑰
	public String generateKeyPair() {
		// 指定算法
		KeyPairGenerator keyGen = null;
		try {
			keyGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		// 指定密鑰長度
		keyGen.initialize(2048);
		// 生成一把金鑰對
		KeyPair keypair = keyGen.generateKeyPair();
		// 生成一個隨機的UUID
		String uuid = UUID.randomUUID().toString();
		// 把UUID和金鑰對都給塞進一個MAP裡面
		keyPairs.put(uuid, keypair);
		// 把UUID和金鑰對都塞進DB2裡面
		RsaSaveKeyPairDao.saveKeyPairToDatabase(uuid, keypair);

		// 回傳給controller uuid這個東西
		return uuid;

	}

	// 加密方法 裡面有透過UUID找金鑰、
	public static String encrypt(String uuid, String plainText) {

		// 從MAP裡面透過UUID找找看有沒有金鑰對
		KeyPair keypair = keyPairs.get(uuid);

		// 如果金鑰對沒有出現在MAP中 記憶體中
		if (keypair == null) {
			// 透過DAO去資料庫找金鑰對，把東西放進keypair
			keypair = RsaSaveKeyPairDao.loadKeyPairToDatabase(uuid, keypair);

		}

		if (keypair == null) {
			// 金鑰對不存在
			return "8787";
		}

		// 從金鑰對裡面拿取公鑰
		PublicKey publicKey = keypair.getPublic();

		try {
			// 開始進行加密，使用原生算法進行加密，利用算法建立一個cipher對象
			Cipher cipher = Cipher.getInstance("RSA");

			// 初始化設定加密模式，並把公鑰放進去
			cipher.init(cipher.ENCRYPT_MODE, publicKey);

			byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

			return Base64.getEncoder().encodeToString(encryptedBytes);

		} catch (Exception e) {

			return null;
		}

	}

	// RSA解密
	public static String decrypt(String uuid, String encryptedText) {

		// 透過DAO先把金鑰對透過UUID提取出來
		KeyPair keyPair = RsaSaveKeyPairDao.loadKeyPairToDatabase(uuid, null);

		if (keyPair == null) {
			return "金鑰對不存在";

		}

		try {
			PrivateKey privateKey = keyPair.getPrivate();

			// 確認需要的加密模式
			Cipher cipher = Cipher.getInstance("RSA");
			// 進行初始化操作，並設定為解密模式，並把私鑰給塞進去
			cipher.init(cipher.DECRYPT_MODE, privateKey);

			// 把已經base64的encryptedText進行解碼
			byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);

			// 執行解密操作 得到byte
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

			return new String(decryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return "解密發生錯誤" + e.getMessage();

		}

	}

}
