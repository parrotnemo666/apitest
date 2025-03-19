package com.DBTest;

public class ParseIntTest {
	
	//簡單的單元測試
	public static void main(String[] args) {
		
//		String startAccountString = "12345678901";
		String startAccountString = "2147483648";
//		String startAccountString = "21474836AA@";

		
		
		try {
			int accountNumber = Integer.parseInt(startAccountString);
			
			System.out.println("The converted account number is: " + accountNumber);
			
		}catch (NumberFormatException e) {
			System.out.println("Invalid start account number: " + e.getMessage());
		}
	}

}
