package com.service;

import java.util.Set;

public class BankAccountService {
    private static final Set<String> VALID_SUBJECTS = Set.of("10", "20", "30", "40", "50", "51", "66", "77", "88");

    public String validate(String accountNumber) {
        if (accountNumber == null || !accountNumber.matches("\\d{12}")) {
            return "請輸入正確的12位數帳號。";
        }

        String branchCode = accountNumber.substring(0, 3);
        String subjectCode = accountNumber.substring(3, 5);
        String accountNumberSeries = accountNumber.substring(5, 11);

        if (!branchCode.matches("0[0-9][1-9]|0[1-9][0-9]|[1-9][0-9][0-9]") || "019".equals(branchCode)) {
            return "分行別錯誤";
        } else if (!VALID_SUBJECTS.contains(subjectCode)) {
            return "科目別錯誤";
        } 
        else if (!accountNumberSeries.matches("0[1-9][0-9]{3}|[1-9][0-9]{3}")) {
            return "帳號數錯誤";
        } 
        else if (!validateAccountNumber(accountNumber)) {
            return "帳號是無效的。";
        }

        return "帳號是有效的。";
    }

    public boolean validateAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() != 12) {
            return false;
        }

        try {
            int checkDigit = Integer.parseInt(accountNumber.substring(11));
            int expectedCheckDigit = calculateCheckDigit(accountNumber.substring(0, 11));
            return checkDigit == expectedCheckDigit;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int calculateCheckDigit(String accountNumber) {
        int[] multipliers = { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2 };
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Integer.parseInt(accountNumber.substring(i, i + 1)) * multipliers[i];
        }
        int Y = sum % 11;
        int X = 11 - Y;
        int Z;
        if (X == 0) {
            Z = 1;
        } else if (X == 1) {
            Z = 0;
        } else {
            Z = X;
        }
        return Z;
    }
}
