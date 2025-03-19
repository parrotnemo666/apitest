package com.exception.controller;

import com.exception.dao.UserDAOWithErrorCode;
import com.exception.model.User;
import com.exception.service.UserServiceWithErrorCode;

public class UserControllerWithErrorCode {
    private UserServiceWithErrorCode userService = new UserServiceWithErrorCode();

    public void displayUser(int id) {
        User[] result = new User[1];
        int status = userService.getUser(id, result);
        switch (status) {
            case UserDAOWithErrorCode.SUCCESS:
                System.out.println("User found: " + result[0].getName());
                break;
            case UserDAOWithErrorCode.USER_NOT_FOUND:
                System.out.println("User not found");
                break;
            case UserDAOWithErrorCode.DATABASE_ERROR:
                System.out.println("Database error occurred");
                break;
            default:
                System.out.println("Unknown error occurred");
        }
    }
}