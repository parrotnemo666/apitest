package com.exception;

import com.exception.controller.UserControllerWithErrorCode;
import com.exception.controller.UserControllerWithException;

public class ErrorHandlingDemo {
    public static void main(String[] args) {
        System.out.println("使用錯誤碼返回方法：");
        UserControllerWithErrorCode controllerWithErrorCode = new UserControllerWithErrorCode();
        controllerWithErrorCode.displayUser(1);  // 成功案例
        controllerWithErrorCode.displayUser(2);  // 用戶未找到
        controllerWithErrorCode.displayUser(-1); // 數據庫錯誤

        System.out.println("\n使用異常處理方法：");
        UserControllerWithException controllerWithException = new UserControllerWithException();
        controllerWithException.displayUser(1);  // 成功案例
        controllerWithException.displayUser(2);  // 用戶未找到
        controllerWithException.displayUser(-1); // 數據庫錯誤
    }
}
