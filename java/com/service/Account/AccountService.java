package com.service.Account;

import java.util.List;

public interface AccountService {
    List<String> generateAccounts(String startAccount, int count);
}
