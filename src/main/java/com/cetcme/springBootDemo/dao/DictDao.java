package com.cetcme.springBootDemo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cetcme.springBootDemo.domain.Dict;
import com.cetcme.springBootDemo.domain.DictExample;
import com.cetcme.springBootDemo.domain.DictExample.Criteria;
import com.cetcme.springBootDemo.mapper.DictMapper;
import com.cetcme.springBootDemo.utils.JdbcUtil;
import com.cetcme.springBootDemo.utils.MybatisUtil;

public class DictDao {

	Logger logger = LoggerFactory.getLogger(DictDao.class);

	/**
	 * 根据字典类别和字典代码获取字典值
	 */
	public String getDictValue(String dictType, String dictCode) {
		String result = "";

		if (dictType == null || dictCode == null) {
			return result;
		}

		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			DictMapper mapper = session.getMapper(DictMapper.class);
			DictExample example = new DictExample();
			Criteria criteria = example.createCriteria();
			criteria.andDictTypeEqualTo(dictType);
			criteria.andDictCodeEqualTo(dictCode);
			criteria.andDelFlagEqualTo(false);
			List<Dict> dicts = mapper.selectByExample(example);
			if (dicts != null && dicts.size() > 0 && dicts.get(0).getDictValue() != null) {
				result = dicts.get(0).getDictValue();
			}

			session.commit(true);
			logger.info("根据字典类别和字典代码获取字典值(" + dictType + "," + dictCode + ")成功");
		} catch (Exception e) {
			logger.error("SQL(根据字典类别和字典代码获取字典值)执行异常(" + dictType + "," + dictCode + "): {}", e.toString());
			session.rollback(true);
		} finally {
			session.close();
		}
		return result;
	}

	/**
	 * 获取所有字典表信息
	 */
	public List<Dict> getAll() {
		List<Dict> result = new ArrayList<Dict>();

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = " select dict_id, dict_type, dict_code, dict_value from t_dict where del_flag = 0 order by dict_type ";

			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Dict dict = new Dict();
				dict.setDictId(rs.getLong(1));
				dict.setDictType(rs.getString(2));
				dict.setDictCode(rs.getString(3));
				dict.setDictValue(rs.getString(4));
				result.add(dict);
			}
			rs.close();
			ps.close();
			logger.info("获取所有字典表信息成功");
		} catch (Exception e) {
			logger.error("SQL(获取所有字典表信息)执行异常: {}", e.toString());
		} finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}
}
