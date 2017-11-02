package com.cetcme.springBootDemo.mybatis.plugin;

import javax.sql.DataSource;

public class DruidDataSourceFactory extends AbstractDataSourceFactory {

	public DruidDataSourceFactory() {
		try {
			Class<?> DruidDataSource = Class.forName("com.alibaba.druid.pool.DruidDataSource");
			this.dataSource = (DataSource) DruidDataSource.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
		}
	}
}