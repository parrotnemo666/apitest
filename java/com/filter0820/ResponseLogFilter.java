package com.filter0820;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.logging.log4j.Logger;

import filter.ResponseWrapper;

import org.apache.logging.log4j.LogManager;

//@WebFilter("/*") // 讓所有的請求都經過過濾器
public class ResponseLogFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(ResponseLogFilter.class);

  
    
    

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 將 ServletResponse 轉換為 HttpServletResponse，以便使用 HTTP 特定的方法
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        ResponseWrapper wrappedResponse = new ResponseWrapper(httpServletResponse); // 包裝響應以捕獲響應體

        // 紀錄處理的時間
        long startTime = System.currentTimeMillis(); // 記錄開始時間
        chain.doFilter(request, wrappedResponse); // 進行請求的實際處理
        long duration = System.currentTimeMillis() - startTime; // 計算處理時間

        // 獲取並記錄響應體
        String responseBody = wrappedResponse.getCaptureAsString();

        // 記錄請求處理完成的信息，如處理時間
        logger.info("\n 請求處理完畢(Respond)"
                + " - Duration: {} ms " + "\n ResponseBody: {} "
                + "\n------------------------over------------------------",
                duration, responseBody);

        // 完成響應數據的寫入
        if (!response.isCommitted()) {
            byte[] capturedData = wrappedResponse.getCaptureAsBytes();
            ServletOutputStream output = response.getOutputStream();
            output.write(capturedData);
            output.flush();
        }
    }

    @Override
    public void destroy() {
        // 清理邏輯（如果需要）
    }
}
