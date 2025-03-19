package com.DBTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;




public class DB2ConnectionExample {

	//目前無法進行測試，因為權限需要一直開啟，所以現在不能用
	 public static void main(String[] args) {
	        // 配置 JDBC URL、用戶名和密碼
		
	        String jdbcURL = "jdbc:db2://172.16.46.181:25000/NEMODB"; // 使用 localhost 或 IP 地址
	        String username = "1130871";
	        String password = "ubot@12345";

	        Connection connection = null;

	        try {
	            // 加載 JDBC 驅動程序
	            Class.forName("com.ibm.db2.jcc.DB2Driver");

	            // 嘗試連接數據庫
	            connection = DriverManager.getConnection(jdbcURL, username, password);
	            System.out.println("成功連接到數據庫！");
	        } 
	        
	        catch (SQLException e) {
	            // 處理 SQL 異常
	            e.printStackTrace();
	            System.out.println("無法連接到數據庫。請檢查 JDBC URL、用戶名或密碼是否正確。");
	        }
	        catch (ClassNotFoundException e) {
	            // 處理類未找到異常
	            e.printStackTrace();
	            System.out.println("找不到 DB2 JDBC 驅動程序。請檢查驅動是否已添加到類路徑。");
	        } finally {
	            // 確保連接關閉
	            try {
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
