package com.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dao.RSA2LogDAO;
import com.model.RSA2.RSARequest2;
import com.model.RSA2.RSAResponse2;

import com.service.RSA.RsaActionService2;

@Path("/rsa2")
public class RsaAction2 {
    private RsaActionService2 rsaService = new RsaActionService2();
    private RSA2LogDAO logDAO = new RSA2LogDAO();  // 使用 DAO
    private static final Logger logger = LogManager.getLogger(RsaAction2.class);

    @POST
    @Path("/process")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response processRequest(RSARequest2 request) {
        logger.info("收到RSA請求，動作:{},文本:{}", request.getAction(), request.getText());
        String result = null;
        String errorMsg = null;

        try {
            switch (request.getAction().toLowerCase()) {
                case "publickeyencrypt":
                    result = rsaService.encrypt(request.getPublicKey(), request.getText());
                    break;
                case "privatekeydecrypt":
                    result = rsaService.decrypt(request.getPrivateKey(), request.getText());
                    break;
                case "privatekeyencrypt":
                    result = rsaService.encryptWithPrivateKey(request.getPrivateKey(), request.getText());
                    break;
                case "publickeydecrypt":
                    result = rsaService.decryptWithPublicKey(request.getPublicKey(), request.getText());
                    break;
                default:
                    logger.warn("無效的動作:{}", request.getAction());
                    return Response.status(Response.Status.BAD_REQUEST)
                                   .entity(new RSAResponse2("Invalid action specified. Use 'PublicKeyEncrypt', 'PrivateKeyDecrypt', 'PublicKeyDecrypt', or 'PrivateKeyEncrypt'."))
                                   .build();
            }

            logger.info("RSA請求完成，結果:{}", result);

            // 日誌記錄：成功操作
            logDAO.insertLog(request.getAction(), request.toString(), request.getText(), result, result, null);

            return Response.ok(new RSAResponse2(result)).build();

        } catch (Exception e) {
            errorMsg = e.getMessage();
            logger.error("處理RSA發生錯誤:{}", errorMsg, e);

            // 日誌記錄：失敗操作
            logDAO.insertLog(request.getAction(), request.toString(), request.getText(), null, null, errorMsg);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity(new RSAResponse2("An error occurred: " + errorMsg))
                           .build();
        }
    }
}
