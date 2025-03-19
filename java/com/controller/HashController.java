package com.controller;


import com.model.hash.HashRequestModel;
import com.model.hash.HashResponseModel;
import com.service.HashService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/api/hash")
public class HashController {

    private static final Logger logger = LogManager.getLogger(HashController.class); // 初始化 Logger
    private HashService hashService = new HashService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateHash(HashRequestModel request) {
        logger.info("收到雜湊生成請求: {}", request); // 日誌：收到請求

        if (request == null) {
            logger.warn("請求體為空"); // 日誌：請求體為空
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Request body cannot be null.\"}")
                    .build();
        }

        if (request.getInput() == null || request.getInput().isEmpty()) {
            logger.warn("輸入值為空"); // 日誌：輸入值為空
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Input cannot be null or empty.\"}")
                    .build();
        }

        if (request.getAlgorithm() == null || request.getAlgorithm().isEmpty()) {
            logger.warn("算法為空"); // 日誌：算法為空
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Algorithm cannot be null or empty.\"}")
                    .build();
        }

        try {
            logger.debug("正在生成雜湊，輸入: {}, 算法: {}", request.getInput(), request.getAlgorithm()); // 日誌：開始生成雜湊
            String hash = hashService.generateHash(request.getInput(), request.getAlgorithm());
            HashResponseModel hashResponse = new HashResponseModel(request.getInput(), request.getAlgorithm(), hash);
            logger.info("雜湊生成成功，輸出: {}", hash); // 日誌：生成成功
            return Response.ok(hashResponse).build();
        } catch (Exception e) {
            logger.error("生成雜湊時發生錯誤", e); // 日誌：捕獲異常
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"An unexpected error occurred: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}

//http://localhost:8087/apitest/jerseyapi/api/hash
//content-type: application/json
//{
//
//"input":"Hello,world!",
//
//"algorithm":"SHA"
//
//}

//{
//
//     "algorithm": "SHA",
//
//     "hash": "fbb7ff0741faad264c45daa6a5d623c0581def9b",
//
//     "input": "Hello,world!"
//
//}
