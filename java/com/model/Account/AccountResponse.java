package com.model.Account;


import java.util.List;

public class AccountResponse {
    private String status;
    private String reason;
    private List<String> accounts;

    // Constructors
    public AccountResponse() {
    }

    public AccountResponse(String status, String reason, List<String> accounts) {
        this.status = status;
        this.reason = reason;
        this.accounts = accounts;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }
}

