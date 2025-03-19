package com.service.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dao.AccountDAO;
import com.dao.DBConnection;

public class SimpleAccountService implements AccountService {

    private static final Logger logger = LogManager.getLogger(SimpleAccountService.class);

    @Override
    public List<String> generateAccounts(String startAccount, int count) {
        logger.info("開始生成帳戶。起始帳戶: {}, 數量: {}", startAccount, count);

        if (!isValidStartAccount(startAccount)) {
            logger.error("無效的起始帳戶號碼: {}", startAccount);
            AccountDAO.logToDatabase(startAccount, count, "FAIL", "Invalid start account number", null);
            throw new IllegalArgumentException("Invalid start account number");
        }
        if (count < 1 || count > 9999) {
            logger.error("無效的帳戶生成數量: {}", count);
            AccountDAO.logToDatabase(startAccount, count, "FAIL", "Count must be between 1 and 9999", null);

            throw new IllegalArgumentException("Count must be between 1 and 9999");
        }

        List<String> accounts = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String currentAccount = String.format("%011d", Long.parseLong(startAccount) + i);
            int checkDigit = calculateCheckDigit(currentAccount);
            String fullAccount = currentAccount + checkDigit;
            accounts.add(fullAccount);
            logger.debug("生成帳戶: {}", fullAccount);
        }

        logger.info("帳戶生成完成。總數量: {}", accounts.size());
        AccountDAO.logToDatabase(startAccount, count, "SUCCESS", null, accounts.toString());

        return accounts;
    }

    private boolean isValidStartAccount(String account) {
        if (account == null || account.length() != 11 || !account.matches("\\d+")) {
            logger.warn("帳戶號碼格式無效: {}", account);
            return false;
        }

        String branchCode = account.substring(0, 3);
        if (!branchCode.matches("0[0-9]{2}") || branchCode.equals("019")) {
            logger.warn("無效的分行代碼: {}", branchCode);
            return false;
        }

        String subjectCode = account.substring(3, 5);
        boolean isValid = subjectCode.matches("10|20|30|40|50|51|66|77|88");
        if (!isValid) {
            logger.warn("無效的科目代碼: {}", subjectCode);
        }

        return isValid;
    }

    private int calculateCheckDigit(String account) {
        logger.debug("開始計算檢查碼，帳戶號: {}", account);
        int sum = 0;
        int[] weights = { 5, 4, 3, 2, 8, 7, 6, 5, 4, 3, 2 };

        for (int i = 0; i < account.length(); i++) {
            sum += Character.getNumericValue(account.charAt(i)) * weights[i];
        }

        int remainder = sum % 11;
        int checkDigit;
        if (remainder == 0) {
            checkDigit = 1;
        } else if (remainder == 1) {
            checkDigit = 0;
        } else {
            checkDigit = 11 - remainder;
        }

        logger.debug("計算的檢查碼: {}", checkDigit);
        return checkDigit;
    }
    
 // 記錄到資料庫的方法
//    private void logToDatabase(String startAccount, int count, String status, String reason, String accounts) {
//        String sql = "INSERT INTO BANK_ACCOUNT_GENERATION (startAccount, count, status, reason, accounts, timestamp) VALUES (?, ?, ?, ?, ?, ?)";
//
//        try (Connection connection = DBConnection.getConnection();
//             PreparedStatement statement = connection.prepareStatement(sql)) {
//
//            statement.setInt(1, Integer.parseInt(startAccount));
//            statement.setInt(2, count);
//            statement.setString(3, status);
//            statement.setString(4, reason);
//            statement.setString(5, accounts);
//            statement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
//
//            statement.executeUpdate();
//            logger.info("帳戶生成記錄已保存到資料庫。");
//        } catch (SQLException e) {
//            logger.error("保存帳戶生成記錄到資料庫時發生錯誤", e);
//        }
//    }
}
