package com.controller;

import java.util.Base64;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.model.Email.Attachment;
import com.model.Email.EmailRequest;
import com.model.Email.EmailResponse;
import com.model.Email.ErrorResponse;
import com.service.EmailService2;

@Path("/api/v1")
public class EmailController {

    private static final Logger logger = LogManager.getLogger(EmailController.class);

    EmailService2 emailService = new EmailService2();

    @POST
    @Path("/send-email")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendEmail(EmailRequest request) {
        logger.info("收到發送郵件的請求。"); // 日誌：收到發送郵件請求
        

        // 檢查請求是否為空
        if (request == null) {
            logger.warn("請求體為空。"); // 日誌：請求體為空警告
            ErrorResponse error = new ErrorResponse("ERROR", "INVALID_REQUEST", "Request body cannot be null.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        
        // 檢查收件人是否為空
        if (request.getRecipients() == null || request.getRecipients().isEmpty()) {
            logger.warn("收件人列表為空。"); // 日誌：收件人為空警告
            ErrorResponse error = new ErrorResponse("ERROR", "INVALID_RECIPIENTS", "Recipients cannot be null or empty.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
       

        // 檢查主題是否為空
        if (request.getSubject() == null || request.getSubject().isEmpty()) {
            logger.warn("郵件主題為空。"); // 日誌：主題為空警告
            ErrorResponse error = new ErrorResponse("ERROR", "INVALID_SUBJECT", "Subject cannot be null or empty.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        // 檢查文本內容是否為空
        if (request.getTextBody() == null || request.getTextBody().isEmpty()) {
            logger.warn("文本內容為空。"); // 日誌：文本內容為空警告
            ErrorResponse error = new ErrorResponse("ERROR", "INVALID_TEXT_BODY", "Text body cannot be null or empty.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }

        // 檢查附件大小是否超過1MB
        if (request.getAttachments() != null && !request.getAttachments().isEmpty()) {
            for (Attachment attachment : request.getAttachments()) {
                byte[] decodedBytes = Base64.getDecoder().decode(attachment.getContent());
                logger.debug("正在檢查附件大小: {}", attachment.getFilename()); // 日誌：檢查附件大小
                if (decodedBytes.length > 1024 * 1024) {
                    logger.warn("附件 {} 超過1MB大小限制。", attachment.getFilename()); // 日誌：附件過大警告
                    ErrorResponse error = new ErrorResponse("ERROR", "INVALID_ATTACHMENT_SIZE", "Attachment size cannot exceed 1MB.");
                    return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
                }
            }
        }

        
        try {
            EmailResponse response = emailService.sendEmail(request); // 調用 emailService 發送郵件
            logger.info("郵件成功發送給: {}", String.join(",", request.getRecipients())); // 日誌：郵件發送成功
            return Response.ok(response).build();
        } catch (Exception e) {
            logger.error("發送郵件失敗: {}", e.getMessage(), e); // 日誌：處理發送失敗
            ErrorResponse error = new ErrorResponse("ERROR", "INTERNAL_ERROR", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
        }
    }
}
