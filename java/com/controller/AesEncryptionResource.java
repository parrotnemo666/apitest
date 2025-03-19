package com.controller;





import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.model.AES.EncryptionRequest;
import com.model.AES.EncryptionResponse;
import com.service.AesUtils;

@Path("/encryption")
public class AesEncryptionResource {
	
	private static final Logger logger = LogManager.getLogger(AesEncryptionResource.class);

    @POST
    @Path("/encrypt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response encrypt(EncryptionRequest request) {
    	logger.info("收到加密請求。數據: {}",request.getData());
    	
        if (request == null || request.getData() == null || request.getData().isEmpty()) {
        	logger.info("加密請求的數據為空or無效");
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Request data cannot be null or empty.\"}")
                .build();
        }
        
        try {
            // 生成密鑰和IV值
            String key = AesUtils.generateKey();
            String iv = AesUtils.generateIV();
            
            String encryptedData = AesUtils.encrypt(request.getData(), key, iv);
            logger.info("加密成功，加密後的數據: {}",encryptedData);
            return Response.ok(new EncryptionResponse(encryptedData, key, iv)).build();
        }  catch (Exception e) {
            logger.error("加密過程中發生錯誤: {}",e.getMessage(),e);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\":\"An unexpected error occurred: " + e.getMessage() + "\"}")
                .build();
        }
    }

    @POST
    @Path("/decrypt")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response decrypt(EncryptionRequest request) {
        if (request == null || request.getData() == null || request.getKey() == null || request.getIv() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Request data, key, and IV cannot be null.\"}")
                .build();
        }
        
        try {
            String decryptedData = AesUtils.decrypt(request.getData(), request.getKey(), request.getIv());
            logger.info("解密成功，解密後的數據: {}",decryptedData);
            return Response.ok(new EncryptionResponse(decryptedData)).build();
            
        }  catch (Exception e) {
        	logger.error("解密過程中發生錯誤: {}",e.getMessage(),e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\":\"An unexpected error occurred: " + e.getMessage() + "\"}")
                .build();
        }
    }
}
//    POST方法   content-type application/Json
//    http://localhost:8087/apitest/jerseyapi/encryption/encrypt  
//  {
//	  "data": "Hello, World!",
//	  "key": "0123456789abcdef",
//	  "iv": "abcdef0123456789"
//	}
	
