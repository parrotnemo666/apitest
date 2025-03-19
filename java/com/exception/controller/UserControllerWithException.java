package com.exception.controller;

import com.exception.exception.BusinessException;
import com.exception.model.User;
import com.exception.service.UserServiceWithException;

public class UserControllerWithException {
    private UserServiceWithException userService = new UserServiceWithException();

    public void displayUser(int id) {
        try {
            User user = userService.getUser(id);
            System.out.println("User found: " + user.getName());
        } catch (BusinessException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}