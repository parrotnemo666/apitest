package com.controller;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.model.LineNotifyModel;
import com.service.LineNotifyService;

@Path("/line-notify")
public class LineNotifyController {

    private static final Logger logger = LogManager.getLogger(LineNotifyController.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public  Response sendNotification(LineNotifyModel request) {
        try {
            logger.info("收到LINE通知請求。");

            // 檢查請求中的 token 和 message 是否為空
            if (request.getToken() == null || request.getToken().isEmpty()) {
                logger.warn("Token 為空或無效。");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Token cannot be null or empty\"}")
                        .build();
            }
            if (request.getMessage() == null || request.getMessage().isEmpty()) {
                logger.warn("Message 為空或無效。");
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Message cannot be null or empty\"}")
                        .build();
            }

            // 設定代理和端口
            String proxyHost = "proxy2.xu06p.com.tw";
            int proxyPort = 80;

            logger.info("準備發送LINE通知，代理: {}，端口: {}。", proxyHost, proxyPort);

            // 發送LINE通知
            int statusCode = LineNotifyService.sendLineNotify1(request.getToken(), request.getMessage(), proxyHost, proxyPort);

            logger.info("LINE通知發送完成，狀態碼: {}", statusCode);

            // 根據狀態碼返回不同的響應
            if (statusCode >= 200 && statusCode < 300) {
                return Response.ok("{\"status\":\"success\"}").build();
            } else if (statusCode == 401) {
                logger.error("無效的訪問令牌。");
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"Invalid access token\"}")
                        .build();
            } else {
                logger.error("發送消息失敗，狀態碼: {}", statusCode);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Failed to send message, status code: " + statusCode + "\"}")
                        .build();
            }

        } catch (Exception e) {
            logger.error("發送LINE通知過程中發生異常: {}", e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"An unexpected error occurred: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
