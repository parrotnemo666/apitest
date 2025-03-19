package com.model;

import javax.validation.constraints.NotEmpty;

public class LineNotifyModel {

    @NotEmpty
    private String token;

    @NotEmpty
    private String message;

    
    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
