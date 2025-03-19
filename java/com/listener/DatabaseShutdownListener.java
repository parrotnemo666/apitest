package com.listener;

import java.sql.Connection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import com.dao.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebListener
public class DatabaseShutdownListener implements ServletContextListener {
    private static final Logger logger = LogManager.getLogger(DatabaseShutdownListener.class);
	private static final Connection Connection = null;

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("應用程序正在關閉，執行數據庫shutdown操作");
       
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("應用程序正在啟動，執行數據庫初始化操作（如果需要）");
    }
}