package com.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Properties properties = new Properties();

    static {
        try {
            // 使用絕對路徑加載配置文件
            String configFilePath = "D:\\Test\\apitest\\src\\main\\resources\\config.properties";
            InputStream input = new FileInputStream(configFilePath);

            properties.load(input);
            
            System.out.println("Config is successfully loaded!");
            
            //把裡面的內容(key, value) 儲存和列印
            properties.forEach((key, value) -> 
            System.out.println("key:"+ key + "  ，value" + value));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
