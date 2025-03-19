package com.filter0820;

import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

/**
 * Servlet Filter implementation class s6filter
 */
//@WebFilter("/*")
public class RequestLogFilter extends HttpFilter implements Filter {

	private static final Logger logger = LogManager.getLogger(RequestLogFilter.class);

	public RequestLogFilter() {
		super();

	}
//這邊為什麼要加?
@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	//將生成的
	HttpServletRequest httpServletRequest = (HttpServletRequest)request;
	
	 // 生成唯一ID並放入ThreadContext，以便在日誌中使用
    String uniqueID = UUID.randomUUID().toString();
    ThreadContext.put("uniqueID", uniqueID);
    
    String requestURL= httpServletRequest.getRequestURI().toString();
    String queryString = httpServletRequest.getQueryString();
    String method = httpServletRequest.getMethod();
    String remoteAddr = httpServletRequest.getRemoteAddr();
    //這邊是不能吃到BODY的
 // 獲取並記錄請求標頭
    StringBuilder headers = new StringBuilder();
    Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
    while (headerNames.hasMoreElements()) {
        String headerName = headerNames.nextElement();
        String headerValue = httpServletRequest.getHeader(headerName);
        headers.append(headerName).append(": ").append(headerValue).append(", ");
    }
    
    // 記錄請求信息，包括請求本身的JSON
    logger.info(" \n------------------------start------------------------\n"
            + "收到請求(Request) - URL: {} ,"
            + "\n Query: {}, Method: {},"
            + "\n IP: {},"
            + "\n Headers: {},"
            + "\n********************************",
            requestURL, queryString, method, remoteAddr, headers.toString());

		chain.doFilter(request, response);
		
		
		
		
	}

	public void init(FilterConfig fConfig) throws ServletException {

	}

}
