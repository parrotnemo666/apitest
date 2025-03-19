package com.exception.service;

import com.exception.dao.UserDAOWithErrorCode;
import com.exception.model.User;

public class UserServiceWithErrorCode {
    private UserDAOWithErrorCode userDAO = new UserDAOWithErrorCode();

    public int getUser(int id, User[] result) {
        int status = userDAO.getUser(id, result);
        if (status == UserDAOWithErrorCode.SUCCESS) {
            // 可以在這裡添加額外的業務邏輯  
            return UserDAOWithErrorCode.SUCCESS;
        }
        return status;
        
    }
}
