package com.controller;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.model.ErrorRSAKeyResponse;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;



@Path("/generate-keys2")
public class RsaKeyGenernationResource2 {
	
	private static final Logger logger = LogManager.getLogger(RsaKeyGenernationResource2.class);
    // 定義允許的密鑰大小
    private static final int[] ALLOWED_KEY_SIZES = {512, 1024, 2048};

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateKeys(@QueryParam("keySize") int keySize) throws NoSuchAlgorithmException {
        
    	logger.info("已經收到RSA密鑰的請求: {}",keySize);
        // 檢查用戶提供的 keySize 是否在允許的範圍內
        boolean validKeySize = false;
        for (int size : ALLOWED_KEY_SIZES) {
            if (keySize == size) {
                validKeySize = true;
                break;
            }
        }
        
        if (!validKeySize) {
        	logger.error("錯誤的密鑰大小: {}，只允許 512, 1024, 2048",keySize);

            // 返回 HTTP 400 錯誤響應和詳細的錯誤消息
        	ErrorRSAKeyResponse LineNotifyErrorResponse = new ErrorRSAKeyResponse("ERROR", "INVALID_KEY_SIZE", "無效的密鑰大小。允許的值為 512, 1024, 2048。");
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(LineNotifyErrorResponse)
                           .type(MediaType.APPLICATION_JSON) // 指定返回的類型為 JSON
                           .build();
        }

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        
    	logger.info("已經開始產RSA密鑰，密鑰大小為: {}",keySize);

        // 初始化密鑰生成器，大小設置為用戶提供的值
        keyGen.initialize(keySize);
        KeyPair pair = keyGen.generateKeyPair();
        PublicKey publicKey = pair.getPublic();
        PrivateKey privateKey = pair.getPrivate();
        logger.info("已成功產生金鑰對");
        
        // 創建一個MAP來存儲公鑰和私鑰
        Map<String, String> keys = new HashMap<>();
        
        keys.put("publicKey", Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        keys.put("privateKey", Base64.getEncoder().encodeToString(privateKey.getEncoded()));
    	logger.info("已將RSA密鑰放入MAP中");
    	logger.info("已返回RSA密鑰");
        // 返回生成的公鑰和私鑰
        return Response.ok(keys).build();
    }
}
