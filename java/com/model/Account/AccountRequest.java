package com.model.Account;

public class AccountRequest {
    private String startAccount;
    private int count;

    // 無參構造函數
    public AccountRequest() {
    }

    // Getter 和 Setter 方法
    public String getStartAccount() {
        return startAccount;
    }

    public void setStartAccount(String startAccount) {
        this.startAccount = startAccount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
