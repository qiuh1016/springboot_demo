package com.cetcme.springBootDemo.utils;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisUtil {
	private static SqlSessionFactory sqlSessionFactory;

	public static synchronized SqlSessionFactory getFactory() {
		if (sqlSessionFactory == null) {

			InputStream reader = MybatisUtil.class.getResourceAsStream("/mybatis-config.xml");
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);

		}
		return sqlSessionFactory;
	}
}
