package com.dao;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import javax.naming.spi.DirStateFactory.Result;

public class RsaSaveKeyPairDao {

	public static boolean saveKeyPairToDatabase(String uuid, KeyPair keyPair) {
		//將鑰匙變成base64的編碼
		String publicKeyString = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
		String privateKeyString = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

		String sql = "INSERT INTO RSA_KEYS (uuid, public_key, private_key) VALUES (?, ?, ?)";

		try (Connection conn = DBConnection.getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, uuid);
			pstmt.setString(2, publicKeyString);
			pstmt.setString(3, privateKeyString);
			
			
 
			//執行插操作並檢查影響的行數 pstmt是一個對象，代表的是欲編譯的SQL語句executeUpdate 代表是執行，affectedRows代表說最後影響的行數
			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//去透過UUID去找金鑰
	public static KeyPair loadKeyPairToDatabase(String uuid, KeyPair keyPair) {

		String sql = "SELECT PUBLIC_KEY,PRIVATE_KEY FROM RSA_KEYS WHERE UUID = ?";

		try (Connection conn = DBConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            
	            pstmt.setString(1, uuid);
	            //pstmt.executeQuery() 開始執行SQL語句，ResultSet去蒐集所有的結果集
	            try (ResultSet rs = pstmt.executeQuery()) {
	            	
	            	//確保只有結果才會繼續採集數據，因為如果沒有or下一行沒有東西部會繼續存取
	                if (rs.next()) {
	                	//如果結果集裡面有數據，在裡面進行搜尋和GET方法獲取byte值
	                    String publicKeyString = rs.getString("public_key");
	                    String privateKeyString = rs.getString("private_key");
	                    
	                    //返回金鑰對，但是金鑰對鑰進行重建(在下面的方法)需要把string轉換成為物件
	                    return reconstructKeyPair(publicKeyString, privateKeyString);
	                }
	            }
	        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	//進行重建金鑰對的方法 公鑰和私鑰有特定的編碼格式
		private static KeyPair reconstructKeyPair(String publicKeyString, String privateKeyString) 
	            throws NoSuchAlgorithmException, InvalidKeySpecException {
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        
	        // Reconstruct public key
	        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
	        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
	        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
	        
	        // Reconstruct private key
	        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
	        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
	        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
	        
	        //返回物件的金鑰(公鑰、私鑰)
	        return new KeyPair(publicKey, privateKey);
	}
		
		
}
