package com.DBTest;

import java.sql.Timestamp;

public class TestCurrentTimestamp {

	public static void main(String[] args) {
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		System.out.println("Current Timestamp: " + now);

	}

}
