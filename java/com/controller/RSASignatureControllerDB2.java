package com.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dao.RSASignatureDaoDB2;
import com.model.RsaSignatrue.VerificationResponse;
import com.model.rsasign2.SignatureRequest2;
import com.model.rsasign2.SignatureResponse2;
import com.model.rsasign2.VerificationRequest2;
import com.service.RSA.RSASignatureService;
import com.service.RSA.RSASignatureServiceDB2;

@Path("/rsadb2")
public class RSASignatureControllerDB2 {

    private static final Logger logger = LogManager.getLogger(RSASignatureControllerDB2.class);
    private RSASignatureServiceDB2 rsaService = new RSASignatureServiceDB2();

    // 處理簽章請求
    @POST
    @Path("/signdb2")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sign(SignatureRequest2 request) {
        logger.info("收到簽章請求。");
        try {
            String uuid = request.getUuid();
            if (uuid == null || uuid.isEmpty()) {
                logger.info("未提供 UUID，生成新的密鑰對。");
                uuid = rsaService.generateKeyPairAndStore();
            }
            logger.info("使用 UUID: {} 進行簽名操作。", uuid);
            String signature = rsaService.sign(request.getText(), uuid);
            SignatureResponse2 response = new SignatureResponse2(uuid, signature);
            logger.info("簽章成功，返回簽名結果。");
            return Response.ok(response).build();
        } catch (Exception e) {
            logger.error("簽章過程中發生錯誤: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Signing error").build();
        }
    }

    // 處理驗章請求
    @POST
    @Path("/verifydb2")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verify(VerificationRequest2 request) {
        logger.info("收到驗章請求。");
        try {
            if (request.getSignature() == null || request.getText() == null || request.getUuid() == null) {
                logger.warn("驗章請求的簽名、文本或 UUID 為空。");
                return Response.status(Response.Status.BAD_REQUEST).entity("Signature, text, or UUID is null").build();
            }
            logger.info("使用 UUID: {} 進行驗證操作。", request.getUuid());
            boolean isValid = rsaService.verify(request.getText(), request.getSignature(), request.getUuid());
            VerificationResponse verificationResponse = new VerificationResponse(isValid);
            logger.info("驗章成功，驗證結果: {}", isValid);
            
            
            
            return Response.ok(verificationResponse).build();
        } catch (Exception e) {
            logger.error("驗章過程中發生錯誤: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Verification error").build();
        }
    }
}
