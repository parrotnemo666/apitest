package com.controller;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.service.EmailService2;

@Path("/example")
public class ThreadSleepController {
	
	private static final Logger logger = LogManager.getLogger(ThreadSleepController.class);

    @GET
    @Path("/longRunning")
    @Produces(MediaType.TEXT_PLAIN)
    
    
    public String longRunningOperation() throws InterruptedException {
    	
    	logger.info("start");
        // 模擬長時間處理
        Thread.sleep(10000); // 休眠10秒
        logger.info("over");
        return "Operation completed";
        
    }
}

