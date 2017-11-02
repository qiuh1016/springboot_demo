package com.cetcme.springBootDemo.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;

public class JdbcRuleUtil {

	private static DataSource dataSourceRule;

	static {
		Properties properties = PropertiesUtil.getResources(Constants.RULE_FILE_NAME);
		try {
			dataSourceRule = DruidDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			System.out.println("权限数据源获取失败");
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		try {
			return dataSourceRule.getConnection();
		} catch (SQLException e) {
			System.out.println("权限连接获取失败");
			e.printStackTrace();
		}
		return null;
	}
}