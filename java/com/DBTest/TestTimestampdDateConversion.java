package com.DBTest;

import java.sql.Date;
import java.sql.Timestamp;

public class TestTimestampdDateConversion {
	
	public static void main(String[] args) {
		//現在的時間戳
		Timestamp nowTimestamp = new Timestamp(System.currentTimeMillis());
		System.out.println("Current Timstamp: "+ nowTimestamp);
		//把時間戳轉換成date
		Date date = new Date(nowTimestamp.getTime());
		System.out.println("Converted Date:" + date);
		//Date 轉換成為 Timestamp
		Timestamp newTimestamp = new Timestamp(date.getTime());
		System.out.println("converted times" + newTimestamp);
		
	}

}
