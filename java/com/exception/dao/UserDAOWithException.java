package com.exception.dao;

import com.exception.exception.DataAccessException;
import com.exception.model.User;

public class UserDAOWithException {
    public User getUser(int id) throws DataAccessException {
        if (id == 1) {
            return new User(1, "John Doe");
        } else if (id > 0) {
            throw new DataAccessException("User not found");
        } else {
            throw new DataAccessException("Database error");
        }
    }
}

