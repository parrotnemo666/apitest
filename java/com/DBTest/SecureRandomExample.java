package com.DBTest;

import java.security.SecureRandom;

import org.apache.http.impl.execchain.MainClientExec;

public class SecureRandomExample {
	
	public static void main(String[] args) {
		SecureRandom secureRandom = new SecureRandom();
		//16byte X8位 一字節 = 128位(也是用來指定為IV大小的)
		byte[] randomBytes = new byte[16];
		
		secureRandom.nextBytes(randomBytes);
		
		System.out.println("隨機生成的數組: ");
		//印最多最少 空
		for(byte b :randomBytes) {		
			System.out.printf("%02x ",b);
		}
	}

}
