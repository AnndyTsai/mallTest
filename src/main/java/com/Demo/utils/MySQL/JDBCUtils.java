package com.Demo.utils.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.Demo.utils.FileSystem.readProperties;


public class JDBCUtils {
	//url
	private static String url = null;
	//user
	private static String user = null;
	//password
	private static String password = null;
	
	private static final Logger log = LogManager.getLogger(JDBCUtils.class.getName());
	/**
	 * 构造方法 初始化对象
	 */
	public JDBCUtils(String DBName){
		
		//注册驱动程序
		try {
			
			/**
			 * 读取jdbc.properties文件
			 */
			readProperties Properties = new readProperties("./JDBC.properties");
			//3)读取文件内容 jdbc:mysql://49.234.62.90:3306/goods
			
			//替换占位符
			url = Parser.parse0(Properties.getValue("JDBC.url"), DBName);			
			user = Properties.getValue("JDBC.user");
			password = Properties.getValue("JDBC.password");
			Class.forName(Properties.getValue("JDBC.driver"));
			//Class.forName("com.mysql.jdbc.Driver");
			log.info("JDBC.url = "+url);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取连接方法
	 */
	public Connection getConnection(){
		try {
			Connection conn = DriverManager.getConnection(url, user, password);
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 释放资源的方法
	 */
	public void close(Statement stmt,Connection conn){
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * 释放资源的方法
	 */
	public void close(ResultSet rs,Statement stmt,Connection conn){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		if(stmt!=null){
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
}
