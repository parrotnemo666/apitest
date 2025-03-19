package com.exception.dao;

import com.exception.model.User;

public class UserDAOWithErrorCode {
    public static final int SUCCESS = 0;
    public static final int USER_NOT_FOUND = 1;
    public static final int DATABASE_ERROR = 2;

    public int getUser(int id, User[] result) {
        if (id == 1) {
            result[0] = new User(1, "John Doe");
            return SUCCESS;
        } else if (id > 0) {
            return USER_NOT_FOUND;
        } else {
            return DATABASE_ERROR;
        }
    }
}
