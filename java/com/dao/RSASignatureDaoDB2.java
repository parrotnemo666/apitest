package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class RSASignatureDaoDB2 {
private static final Logger logger =LogManager.getLogger(RSASignatureDaoDB2.class);
    // 保存密鑰對到資料庫
    public void saveKeyPair(String uuid, byte[] publicKey, byte[] privateKey) throws Exception {
        String sql = "INSERT INTO rsa_keys (uuid, public_key, private_key) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid);
            pstmt.setString(2, Base64.getEncoder().encodeToString(publicKey));
            pstmt.setString(3, Base64.getEncoder().encodeToString(privateKey));
            pstmt.executeUpdate();
        }
    }

    // 根據UUID加載公鑰
    public byte[] loadPublicKey(String uuid) throws Exception {
        String sql = "SELECT public_key FROM rsa_keys WHERE uuid = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Base64.getDecoder().decode(rs.getString("public_key"));
            } else {
                throw new Exception("公鑰不存在，UUID: " + uuid);
            }
        }
    }

    // 根據UUID加載私鑰
    public byte[] loadPrivateKey(String uuid) throws Exception {
        String sql = "SELECT private_key FROM rsa_keys WHERE uuid = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Base64.getDecoder().decode(rs.getString("private_key"));
            } else {
                throw new Exception("私鑰不存在，UUID: " + uuid);
            }
        }
    }
    
    //驗證時插入LOG軌跡進去DB裡面
    public void insertVerificationLog(String uuid, String text, String signature, boolean isValid) {
        String sql = "INSERT INTO RSASIGNlog (UUID, TEXT, SIGNATURE, IS_VALID, TIMESTAMP) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid);
            pstmt.setString(2, text);
            pstmt.setString(3, signature);
            pstmt.setBoolean(4, isValid);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("驗證日誌插入成功，UUID: {}, 驗證結果: {}", uuid, isValid);
            } else {
                logger.warn("驗證日誌插入失敗，UUID: {}", uuid);
            }
        } catch (Exception e) {
            logger.error("插入驗證日誌時發生錯誤，UUID: {}", uuid, e);
        }
    }
    
    //簽章的時候寫入軌跡進DB裡面
    public void insertSignatureLog(String uuid, String text, String signature) {
        String sql = "INSERT INTO RSASIGNlog (UUID, TEXT, SIGNATURE, TIMESTAMP) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid);
            pstmt.setString(2, text);
            pstmt.setString(3, signature);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("簽名日誌插入成功，UUID: {}", uuid);
            } else {
                logger.warn("簽名日誌插入失敗，UUID: {}", uuid);
            }
        } catch (Exception e) {
            logger.error("插入簽名日誌時發生錯誤，UUID: {}", uuid, e);
        }
    }
    
    
}
