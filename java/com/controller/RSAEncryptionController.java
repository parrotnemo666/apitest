package com.controller;



import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONObject;

import com.model.RSAEncryption.RSAEncryptionRequest;
import com.model.RSAEncryption.RSAEncryptionResult;
import com.service.EmailService2;
import com.service.RSA.RSAEncryption.RSAEncryptionService;

@Path("/encrypt")
public class RSAEncryptionController {
	
	private static final Logger logger = LogManager.getLogger(RSAEncryptionController.class);
    private RSAEncryptionService encryptionService = new RSAEncryptionService();

    // 生成密鑰對並返回UUID
    @POST
    @Path("/generateKey")
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateKeyPair() {
    	logger.info("Received request to generate a new RSA key pair.");
        try {
            String uuid = encryptionService.generateKeyPair();
            logger.debug("生成密鑰的uuid:{}",uuid);
            return Response.ok("{\"uuid\":\"" + uuid + "\"}").build();
        } catch (Exception e) {
        	logger.error("生成RSA時發生錯誤!");
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Key generation error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 加密文本
    @POST
    @Path("/encryptText")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response encryptText(RSAEncryptionRequest request) {
    	logger.info("已收到加密請求");
        if (request == null || request.getUuid() == null || request.getText() == null || request.getText().isEmpty()) {
           logger.info("加密請求無效，UUID或文本為空");
        	return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"UUID and text cannot be null or empty.\"}")
                    .build();
        }

        try {
            String encryptedText = encryptionService.encrypt(request.getUuid(), request.getText());
            logger.info("已成功加入文本，UUID:{}",request.getUuid());
            RSAEncryptionResult result = new RSAEncryptionResult(encryptedText);
            return Response.ok(result).build();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("加密文本發現錯誤，UUID:{}",request.getUuid());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Encryption error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    // 解密文本
    @POST
    @Path("/decryptText")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response decryptText(RSAEncryptionRequest request) {
    	logger.info("解密請求已收到，UUID: {}",request.getUuid());
        if (request == null || request.getUuid() == null || request.getText() == null || request.getText().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"UUID and text cannot be null or empty.\"}")
                    .build();
        }

        try {
            String decryptedText = encryptionService.decrypt(request.getUuid(), request.getText());
            logger.info("已成功解密，UUID:{}",request.getUuid());
            RSAEncryptionResult result = new RSAEncryptionResult(decryptedText);
            
//            JSONObject json= new JSONObject();
//            json.put("text",decryptedText );
//            return Response.ok(json.toString()).build();
            return Response.ok(result).build();
        } catch (Exception e) {
            e.printStackTrace();
            
            System.out.println("error");
            logger.error("加密請求無效，UUID:{}",request.getUuid());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Decryption error: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}