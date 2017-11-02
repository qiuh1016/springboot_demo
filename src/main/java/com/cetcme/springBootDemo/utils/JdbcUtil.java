package com.cetcme.springBootDemo.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

public class JdbcUtil {

	private static DataSource dataSource;

	static {
		Properties properties = PropertiesUtil.getResources(Constants.DRUID_FILE_NAME);
		try {
			dataSource = DruidDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			System.out.println("数据源获取失败");
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			System.out.println("连接获取失败");
			e.printStackTrace();
		}
		return null;
	}
}