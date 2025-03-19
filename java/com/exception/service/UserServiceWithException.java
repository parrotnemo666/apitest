package com.exception.service;

import com.exception.dao.UserDAOWithException;
import com.exception.exception.BusinessException;
import com.exception.exception.DataAccessException;
import com.exception.model.User;

public class UserServiceWithException {
    private UserDAOWithException userDAO = new UserDAOWithException();

    public User getUser(int id) throws BusinessException {
        try {
            return userDAO.getUser(id);
        } catch (DataAccessException e) {
            throw new BusinessException("Error processing user: " + e.getMessage());
        }
    }
}
