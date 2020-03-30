/**
 * Created by yangbin 2020
 * 
 * 验证测试版本：MySQL5.6 其他版本未验证
 * 
 * 注意：field字段一定要与MySQL数据库表的字段一致 否者会链接数据库失败
 */
package com.Demo.utils.MySQL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Query {
	
	private static final Logger log = LogManager.getLogger(Query.class.getName());
	
	private JDBCUtils JDBCUtils = null;
	
	public Query(String tableName) {
		
		JDBCUtils = new JDBCUtils(tableName);
	}

	/**
	 * 查询数据库字段对应的值；只支持查询一条数据，如果查询多条数据 默认返回最后一条数据的Map对象
	 * 
	 * @author yangbin
	 * 
	 * @param sql：数据库查询语句
	 * @param field：需要查询的字段，String数组
	 * @return 返回map集合 key：数据库字段名称 value：查询字段对应的值
	 */
	public Map<String, String> getValues(String sql, String... field) {

		Connection con = JDBCUtils.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;

		Map<String, String> map = new HashMap<>();

		try {
			statement = con.createStatement();
//            执行查询语句
			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				for (String str : field) {

					map.put(str, resultSet.getString(str));
				}
			}

		} catch (SQLException e) {
			
			log.error("数据库连接失败");

		} finally {

			JDBCUtils.close(resultSet, statement, con);
			log.info("数据库已关闭连接");
		}

		return map;
	}
	/**
	 * 查询单个字段的值，sql语句建议查询单条数据，否者查询到的数据前一条会被后一条覆盖
	 * 
	 * @author yangbin
	 * 
	 * @param sql：数据库查询语句
	 * @param field：需要查询的某一个字段，String类型参数
	 * 
	 * @return 返回数据库字段对应的值（单个字段） 返回数据类型为String
	 * */
	public String getValue(String sql, String field) {

		Connection con = JDBCUtils.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;

		String value = null;

		try {
			statement = con.createStatement();
//            执行查询语句
			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {

				value = resultSet.getString(field);

			}

		} catch (SQLException e) {
			log.error("数据库连接失败");

		} finally {

			JDBCUtils.close(resultSet, statement, con);
			log.info("数据库已关闭连接");
		}

		return value;
	}

	/**
	 * 针对 select count(str) from xxx 这类查询
	 * 
	 * @author yangbin
	 * 
	 * @param sql：数据库查询语句，只支持count聚合查询
	 * @return count数据 int
	 */
	public int getCount(String sql) {

		int count = 0;
		Connection con = JDBCUtils.getConnection();
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			statement = con.createStatement();
//            执行查询语句
			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {

				count = resultSet.getInt(1);
			}

		} catch (SQLException e) {

			log.error("数据库连接失败");

		} finally {

			JDBCUtils.close(resultSet, statement, con);
			log.info("数据库已关闭连接");
		}

		return count;
	}
	
	public static void main(String[] args) {
		
		System.out.println(new Query("yqn_ims").getCount("select count(*) from inquiry"));
		
		System.out.println(new Query("yqn_ims").getValue("select * from inquiry_detail where InquiryId=110004", "InquiryDetailID"));
	}
}
