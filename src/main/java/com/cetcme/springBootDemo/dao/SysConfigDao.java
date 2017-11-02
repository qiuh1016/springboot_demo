package com.cetcme.springBootDemo.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cetcme.springBootDemo.domain.SysConfig;
import com.cetcme.springBootDemo.domain.SysConfigExample;
import com.cetcme.springBootDemo.domain.SysConfigExample.Criteria;
import com.cetcme.springBootDemo.mapper.SysConfigMapper;
import com.cetcme.springBootDemo.utils.MybatisUtil;

public class SysConfigDao {

	Logger logger = LoggerFactory.getLogger(SysConfigDao.class);

	/**
	 * 获取所有有效的系统配置信息
	 */
	public List<SysConfig> getAll() {
		List<SysConfig> result = null;

		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			SysConfigMapper mapper = session.getMapper(SysConfigMapper.class);
			SysConfigExample example = new SysConfigExample();
			Criteria criteria = example.createCriteria();
			criteria.andDelFlagEqualTo(false);
			result = mapper.selectByExample(example);
			session.commit(true);
			logger.info("获取所有有效的系统配置信息成功");
		} catch (Exception e) {
			logger.error("SQL(获取所有有效的系统配置信息: {}", e.toString());
		} finally {
			session.close();
		}
		return result;
	}
}
