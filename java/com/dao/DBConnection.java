package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:db2://172.16.46.181:25000/NEMODB1";
    private static final String USER = "db2user";
    private static final String PASSWORD = "ubot@12345";

    static {
        try {
            // 載入DB2 JDBC驅動
            Class.forName("com.ibm.db2.jcc.DB2Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  // 這裡可以改為使用Logger記錄，後面處理
            throw new RuntimeException("未找到DB2 JDBC驅動", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        // 創建並返回資料庫連接
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("連接已關閉。");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("關閉資料庫連接時發生錯誤。");
            }
        }
    }
}
