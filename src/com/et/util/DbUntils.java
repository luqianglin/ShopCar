package com.et.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * jdbc封装类
 * @author Administrator
 *
 */
public class DbUntils {
	//用于读取java配置文件
	static Properties p = new Properties();
	static{
		InputStream is = DbUntils.class.getResourceAsStream("jdbc.properties");
		try {
			p.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 获取连接
	 */
	public static Connection getConnection() throws Exception{
		String url = p.getProperty("url");
		String driverClass = p.getProperty("driverClass");
		String uname = p.getProperty("username");
		String password = p.getProperty("password");
		Class.forName(driverClass);
		//登录成功
		Connection conn = DriverManager.getConnection(url,uname,password);
		return conn;
	}
}
