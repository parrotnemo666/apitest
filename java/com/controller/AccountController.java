package com.controller;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.model.Account.AccountRequest;
import com.model.Account.AccountResponse;
import com.service.Account.AccountService;
import com.service.Account.SimpleAccountService;

@Path("/accounts")
public class AccountController {

    private static final Logger logger = LogManager.getLogger(AccountController.class);

    private AccountService accountService = new SimpleAccountService();

    @POST
    @Path("/bank")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateAccounts(AccountRequest request) {
        logger.info("收到生成帳戶請求。");

        if (request == null) {
            logger.warn("請求體為空。");
            AccountResponse response = new AccountResponse("error", "Request body cannot be null.", null);
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
        if (request.getStartAccount() == null || request.getStartAccount().isEmpty()) {
            logger.warn("起始帳戶為空或無效。");
            AccountResponse response = new AccountResponse("error", "Start account cannot be null or empty.", null);
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
        if (request.getCount() < 1 || request.getCount() > 9999) {
            logger.warn("帳戶生成數量無效: {}", request.getCount());
            AccountResponse response = new AccountResponse("error", "Count must be between 1 and 9999", null);
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }

        try {
            logger.info("開始生成帳戶，起始帳戶: {}, 數量: {}", request.getStartAccount(), request.getCount());
            List<String> accounts = accountService.generateAccounts(request.getStartAccount(), request.getCount());
            AccountResponse response = new AccountResponse("success", "Account generated successfully", accounts);
            logger.info("帳戶生成成功。");
            return Response.ok(response).build();

        } catch (IllegalArgumentException e) {
            logger.error("非法參數異常: {}", e.getMessage());
            AccountResponse response = new AccountResponse("error", e.getMessage(), null);
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();

        } catch (Exception e) {
            logger.error("生成帳戶過程中發生未預期的錯誤: {}", e.getMessage(), e);
            AccountResponse response = new AccountResponse("error", "An unexpected error occurred", null);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
        }
    }
}
