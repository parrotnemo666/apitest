package com.model.bankid;


public class BankAccountResponse {

    private boolean valid;
    private String message;

    // Constructor
    public BankAccountResponse(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    // Getters
    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }
}
