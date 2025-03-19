package com.controller;

import javax.swing.text.html.parser.Entity;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.model.RSAEncryption.RSAEncryptionRequest;
import com.model.RSAEncryption.RSAEncryptionResult;
import com.service.Rsa01Service;

@Path("/rsa01")
public class Rsa01EncryptionController {

	private static final Logger logger = LogManager.getLogger(Rsa01EncryptionController.class);
	private Rsa01Service rsa01Service = new Rsa01Service();

	@POST
	@Path("/generateKey")
	@Produces(MediaType.APPLICATION_JSON)

	public Response generateKeyPair() {

		try {
			// 產生一把新的金鑰
			Rsa01Service rsa01Service = new Rsa01Service();
			String uuid = rsa01Service.generateKeyPair();
//			String uuid = new Rsa01Service().generateKeyPair();
			return Response.ok("{\"uuid\":\"" + uuid + "\"}").build();

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{\"error\":\"Key generation error: " + e.getMessage() + "\"}").build();
		}

	}

	// RSA加密
	@POST
	@Path("/rsaEncrypt")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response rsaEncrypt(RSAEncryptionRequest request) {
		logger.info("已收到加密請求");

		if (request == null || request.getUuid() == null || request.getText() == null) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("{\"error\":\"UUID and text cannot be null or empty.\"}").build();
		}

		try {
			// 執行Rsa01Service裡面的加密方法
			String encryptedText = Rsa01Service.encrypt(request.getUuid(), request.getText());
			// 執行的結果放到我的RSAEncryptionResult model中返回回去
			RSAEncryptionResult result = new RSAEncryptionResult(encryptedText);
			return Response.ok(result).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{\"error\":\"Encryption error: " + e.getMessage() + "\"}").build();
		}
	}

	// RSA解密
	@POST
	@Path("/rsaDecrypt")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response rsaDecrypt(RSAEncryptionRequest request) {
		logger.info("已收到解密請求");
		if (request == null || request.getUuid() == null || request.getText() == null || request.getText().isEmpty()) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity("{\"error\":\"UUID and text cannot be null or empty.\"}").build();
		}

		try {
			// 進行加密請求
			String decryptedTextString = Rsa01Service.decrypt(request.getUuid(), request.getText());
			// 放到model裡面
			RSAEncryptionResult result = new RSAEncryptionResult(decryptedTextString);

			return Response.ok(result).build();
		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("{\"error\":\"Decryption error: " + e.getMessage() + "\"}").build();
		}

	}

}
