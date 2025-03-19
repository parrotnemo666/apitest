package com.DBTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB2ConnectionExample2 {

    public static void main(String[] args) {
        // 配置 JDBC URL、用戶名和密碼
        String jdbcURL = "jdbc:db2://172.16.46.181:25000/NEMODB1"; // 使用 localhost 或 IP 地址
        String username = "db2user";
        String password = "ubot@12345";

        Connection connection = null;
        Statement statement = null;

        try {
            // 加載 JDBC 驅動程序
            Class.forName("com.ibm.db2.jcc.DB2Driver");

            // 嘗試連接數據庫
            connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("成功連接到數據庫！");

            // 創建 Statement 對象
            statement = connection.createStatement();

            // 定義創建表的 SQL 語句
//            String createTableSQL = "CREATE TABLE SampleTable (" +
//                                    "ID INT PRIMARY KEY NOT NULL, " +
//                                    "NAME VARCHAR(100) NOT NULL, " +
//                                    "AGE INT NOT NULL" +
//                                    ")";

//            // 執行 SQL 語句
//            statement.executeUpdate(createTableSQL);
//            System.out.println("表格創建成功！");

        } catch (SQLException e) {
            // 處理 SQL 異常
            e.printStackTrace();
            System.out.println("無法創建表格。請檢查 SQL 語句或數據庫連接設置。");
        } catch (ClassNotFoundException e) {
            // 處理類未找到異常
            e.printStackTrace();
            System.out.println("找不到 DB2 JDBC 驅動程序。請檢查驅動是否已添加到類路徑。");
        } finally {
            // 確保連接關閉
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    System.out.println("連接已關閉。");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
 