package com;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLServerConnectionExample {
    public static void main(String[] args) {
        // 数据库连接信息
    	String className = "com.microsoft.sqlserver.jdbc.SQLServerDriver" ;
        String url = "jdbc:sqlserver://172.16.45.213:1433;databaseName=TEST;encrypt=true;trustServerCertificate=true;";
        String user = "sqlap";
        String password = "Ubot@1234";

        // 加載 JDBC 驅動
        try {
            Class.forName(className);
            System.out.println("驅動加载成功！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // 連接數據庫
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            System.out.println("連接數據庫成功！");
            
            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT USERID, USERNAME, NUMBER, ADDRESS, MEMO FROM USERLIST"; // 使用实际的表名和列名
                ResultSet resultSet = statement.executeQuery(sql);

                // 处理查询结果（ResultSet）
                while (resultSet.next()) {
                    // 根据数据表结构获取和处理数据
                    String userId = resultSet.getString("USERID");
                    String userName = resultSet.getString("USERNAME");
                    int number = resultSet.getInt("NUMBER");
                    String address = resultSet.getString("ADDRESS");
                    String memo = resultSet.getString("MEMO");
                    
                    // 输出获取到的数据，实际应用中可以进行进一步处理
                    System.out.println("UserID: " + userId + ", UserName: " + userName + ", Number: " + number + ", Address: " + address + ", Memo: " + memo);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            
            
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

