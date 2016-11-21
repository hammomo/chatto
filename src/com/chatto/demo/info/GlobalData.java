package com.chatto.demo.info;

public class GlobalData {
	public static final String ADDRESS = "127.0.0.1"; 		// set ip address, eg: localhost
	public static final int REGISTER_PORT = 8888;					// set port number, eg: 8888
	public static final int LOGIN_PORT = 8889;
	
	// 数据库信息
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	public static final String DB_URL = "jdbc:mysql://localhost:3306/chatto_demo?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	public static final String MYSQL_USER = "root";
	public static final String MYSQL_PASSWORD = "2521";
}
