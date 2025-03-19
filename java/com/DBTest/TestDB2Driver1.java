package com.DBTest;

public class TestDB2Driver1 {

	public static void main(String[] args) {
		try {
		    Class.forName("com.ibm.db2.jcc.DB2Driver");
		    System.out.println("DB2 JDBC 驅動程序加載成功！");
		} catch (ClassNotFoundException e) {
		    System.out.println("DB2 JDBC 驅動程序加載失敗。");
		    e.printStackTrace();
		}

	}

}
