package com.model.bankid;


import javax.validation.constraints.NotEmpty;

public class BankAccountRequest {

    @NotEmpty
    private String accountNumber;

    // Getters and Setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}

