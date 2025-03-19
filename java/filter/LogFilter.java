package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.UUID;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import com.dao.ApiLogDao;
import com.dao.DBConnection;

import org.apache.logging.log4j.LogManager;
//這邊記得要改回來
@WebFilter("/*") // 讓所有的請求都經過過濾器
public class LogFilter implements Filter {
    private static final Logger logger = LogManager.getLogger(LogFilter.class);
private final ApiLogDao apiLogDao = new ApiLogDao();
   

@Override
    public void init(FilterConfig filterConfig) {
        // 初始化邏輯，如果需要
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // 將 ServletRequest 轉換為 HttpServletRequest，以便使用 HTTP 特定的方法
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //去證明說這個只能接收一次
//        HttpServletRequestWrapper.getBody(); 
       
        RequestWrapper wrappedRequest = new RequestWrapper(httpServletRequest); // 包裝請求以捕獲請求體

        // 將 ServletResponse 轉換為 HttpServletResponse，以便使用 HTTP 特定的方法
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        ResponseWrapper wrappedResponse = new ResponseWrapper(httpServletResponse); // 包裝響應以捕獲響應體

     // 檢查 Content-Type 是否為 application/json  不是就把他全部給踢掉
//        String contentType = wrappedRequest.getContentType();
//        if (contentType == null || !contentType.toLowerCase().contains("application/json")) {
//            httpServletResponse.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
//            httpServletResponse.getWriter().write("不支援的 content/type 只支援application/json 格式");
//            return;
//        }
        
        
        // 生成唯一ID並放入ThreadContext，以便在日誌中使用
        String uniqueID = UUID.randomUUID().toString();
        ThreadContext.put("uniqueID", uniqueID);

        // 獲取請求的相關信息
        String requestURL = wrappedRequest.getRequestURL().toString();
        String queryString = wrappedRequest.getQueryString();
        String method = wrappedRequest.getMethod();
        String remoteAddr = wrappedRequest.getRemoteAddr();
        String body = wrappedRequest.getBody();
        
        // 獲取並記錄請求標頭
        StringBuilder headers = new StringBuilder();
        Enumeration<String> headerNames = wrappedRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = wrappedRequest.getHeader(headerName);
            headers.append(headerName).append(": ").append(headerValue).append(", ");
        }

        // 記錄請求信息，包括請求本身的JSON
        logger.info(" \n------------------------start------------------------\n"
                + "收到請求(Request) - URL: {} ,"
                + "\n Query: {}, Method: {},"
                + "\n IP: {},"
                + "\n Headers: {},"
                + "\n RequestBody: {} "
                + "\n********************************",
                requestURL, queryString, method, remoteAddr, headers.toString(), body);

        //紀錄處理的時間
        try {
            long startTime = System.currentTimeMillis(); // 記錄開始時間
            chain.doFilter(wrappedRequest, wrappedResponse); // 進行請求的實際處理
            long duration = System.currentTimeMillis() - startTime; // 計算處理時間
            
            // 獲取並記錄響應體
            String responseBody = wrappedResponse.getCaptureAsString();
            
            
            // 記錄請求處理完成的信息，如處理時間
            logger.info("\n 請求處理完畢(Respond)"
                    + " - URL: {}, Duration: {} ms " + "\n ResponseBody: {} "
                    + "\n------------------------over------------------------"
                    , requestURL, duration, responseBody);
            //記錄到DB裡面
//            apiLogDao.insertLog( method, requestURL, headers.toString(), body, wrappedResponse.getStatus(), responseBody, "", remoteAddr);   
        } catch (Exception e) {
            // 在發生異常時記錄錯誤日誌，並進行記錄
            logger.error("處理時發生異常 - URL: {}", httpServletRequest.getRequestURL(), e);
            throw new ServletException("處理時發生異常", e);
        } finally {
            // 完成響應數據的寫入
            if (!response.isCommitted()) {
                byte[] capturedData = wrappedResponse.getCaptureAsBytes();
                ServletOutputStream output = response.getOutputStream();
                output.write(capturedData);
                output.flush();
            }
            ThreadContext.remove("uniqueID"); // 清除ThreadContext中的唯一ID
        }
    }

    @Override
    public void destroy() {
        // 清理邏輯，如果需要
    }
}


//+---------------------------------------------------------+
//|                     過濾器初始化                        |
//|        (初始化邏輯，如果需要)                           |
//+---------------------------------------------------------+
//                          |
//                          v
//+---------------------------------------------------------+
//|                    處理請求和響應                        |
//|  - 轉換 ServletRequest -> HttpServletRequest            |
//|  - 轉換 ServletResponse -> HttpServletResponse          |
//|  - 包裝請求以捕獲請求體                                 |
//|  - 包裝響應以捕獲響應體                                 |
//+---------------------------------------------------------+
//                          |
//                          v
//+---------------------------------------------------------+
//|   生成唯一ID並放入 ThreadContext 用於日誌記錄           |
//+---------------------------------------------------------+
//                          |
//                          v
//+---------------------------------------------------------+
//|               獲取請求的相關信息                         |
//|  - 請求 URL、查詢字符串、方法、IP 地址、請求標頭和請求體 |
//+---------------------------------------------------------+
//                          |
//                          v
//+---------------------------------------------------------+
//|                    記錄請求信息                         |
//|        (包括請求本身的 JSON 內容)                       |
//+---------------------------------------------------------+
//                          |
//                          v
//+---------------------------------------------------------+
//|            進行實際請求處理並記錄處理時間                |
//|                (chain.doFilter)                         |
//+---------------------------------------------------------+
//                          |
//                          v
//+---------------------------------------------------------+
//|                獲取並記錄響應體數據                     |
//|          (捕獲響應數據並寫入日誌)                       |
//+---------------------------------------------------------+
//                          |
//                          v
//+---------------------------------------------------------+
//|                    處理異常 (如果發生)                   |
//|        (記錄錯誤日誌並拋出異常)                         |
//+---------------------------------------------------------+
//                          |
//                          v
//+---------------------------------------------------------+
//|                  完成響應數據的寫入                      |
//|        (將捕獲的響應數據寫回到客戶端)                   |
//+---------------------------------------------------------+
//                          |
//                          v
//+---------------------------------------------------------+
//|              清除 ThreadContext 中的唯一ID               |
//+---------------------------------------------------------+
