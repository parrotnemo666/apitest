package com.controller;

import com.model.ValidationResult;
import com.service.TaiwanIDService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

// POSTMAN測試
// http://localhost:8087/apitest/jerseyapi/id-validator/validate?id=S124050812

@Path("/id-validator")
public class TaiwanIDResource {

    private static final Logger logger = LogManager.getLogger(TaiwanIDResource.class); // 初始化 Logger
    TaiwanIDService taiwanIDService = new TaiwanIDService();

    @GET
    @Path("/bankAccountService")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateID(@QueryParam("id") String id) {
        logger.info("收到身份證號驗證請求，ID: {}", id); // 日誌：收到的 ID
        
        if (id == null || id.isEmpty()) {
            logger.warn("身份證號驗證請求中 ID 為空"); // 日誌：警告 ID 為空
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"ID cannot be null or empty.\"}")
                    .build();
        }

        boolean isValid = taiwanIDService.isValid(id);
        if (isValid) {
            logger.debug("身份證號驗證通過，ID: {}", id); // 日誌：驗證通過
            return Response.ok(new ValidationResult(isValid, id)).build();
        } else {
            String correctID = taiwanIDService.getCorrectID(id);
            logger.debug("身份證號驗證失敗，建議的正確 ID: {}", correctID); // 日誌：驗證失敗並建議正確 ID
            return Response.ok(new ValidationResult(isValid, correctID)).build();
        }
    }

    
}
