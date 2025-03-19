package com.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dao.DBConnection;
import com.dao.TaiwanIdDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TaiwanIDService {

    private static final Logger logger = LogManager.getLogger(TaiwanIDService.class); // 初始化 Logger

    public boolean isValid(String id) {
        logger.debug("正在驗證身份證號格式: {}", id); // 日誌：開始驗證格式

        if (!id.matches("[A-Z][12]\\d{8}")) {
            logger.warn("身份證號格式不正確: {}", id); // 日誌：格式不正確
//            logToDatabase(id, false, null); // 記錄到資料庫
            TaiwanIdDAO.createTaiwanId(id, false, null);

            return false;
        }

        int[] lettersValues = {
            10, 11, 12, 13, 14, 15, 16, 17, 34, 18, 19, 20, 21,
            22, 35, 23, 24, 25, 26, 27, 28, 29, 32, 30, 31, 33
        };

        int firstDigitValue = lettersValues[id.charAt(0) - 'A'];

        // 計算檢查碼
        int sum = firstDigitValue / 10 + (firstDigitValue % 10) * 9;
        logger.debug("首字母對應數值為: {}，初始檢查碼為: {}", firstDigitValue, sum); // 日誌：首字母對應的數值和初始檢查碼

        for (int i = 1; i < 9; i++) {
            sum += (id.charAt(i) - '0') * (9 - i);
            logger.debug("累加第{}位數字後檢查碼為: {}", i + 1, sum); // 日誌：每位數字累加後的檢查碼
        }
        sum += id.charAt(9) - '0';
        logger.debug("最終檢查碼為: {}", sum); // 日誌：最終檢查碼

        boolean isValid = sum % 10 == 0;
        logger.info("身份證號 {} 驗證結果: {}", id, isValid ? "有效" : "無效"); // 日誌：驗證結果

        // 記錄到資料庫
        TaiwanIdDAO.createTaiwanId(id, isValid, isValid ? getCorrectID(id) : null);

        return isValid;
    }

    public String getCorrectID(String id) {
        logger.debug("正在計算正確的身份證號: {}", id); // 日誌：開始計算正確的身份證號

        if (!id.matches("[A-Z][12]\\d{8}")) {
            logger.warn("身份證號格式不正確，無法計算正確的 ID: {}", id); // 日誌：格式不正確無法計算
            return null;
        }

        int[] lettersValues = {
            10, 11, 12, 13, 14, 15, 16, 17, 34, 18, 19, 20, 21,
            22, 35, 23, 24, 25, 26, 27, 28, 29, 32, 30, 31, 33
        };

        int firstDigitValue = lettersValues[id.charAt(0) - 'A'];

        // 計算檢查碼
        int sum = firstDigitValue / 10 + (firstDigitValue % 10) * 9;
        logger.debug("首字母對應數值為: {}，初始檢查碼為: {}", firstDigitValue, sum); // 日誌：首字母對應的數值和初始檢查碼

        for (int i = 1; i < 9; i++) {
            sum += (id.charAt(i) - '0') * (9 - i);
            logger.debug("累加第{}位數字後檢查碼為: {}", i + 1, sum); // 日誌：每位數字累加後的檢查碼
        }

        int correctCheckDigit = (10 - (sum % 10)) % 10;
        String correctID = id.substring(0, 9) + correctCheckDigit;
        logger.info("計算出的正確身份證號為: {}", correctID); // 日誌：計算出的正確身份證號

        return correctID;
    }


}
