package com.cetcme.springBootDemo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cetcme.springBootDemo.domain.AcqTblIndex;
import com.cetcme.springBootDemo.utils.JdbcUtil;

public class AcqTblIndexDao {

	Logger logger = LoggerFactory.getLogger(AcqTblIndexDao.class);

	/**
	 * 获取所有历史数据表索引信息
	 */
	public List<AcqTblIndex> getAll() {
		List<AcqTblIndex> result = new ArrayList<AcqTblIndex>();

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = " select device_no, table_suff from t_acqtbl_index ";

			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				AcqTblIndex acqTblIndex = new AcqTblIndex();
				acqTblIndex.setDeviceNo(rs.getString(1));
				acqTblIndex.setTableSuff(rs.getString(2));
				result.add(acqTblIndex);
			}
			rs.close();
			ps.close();
			logger.info("获取所有历史数据表索引信息成功");
		} catch (SQLException e) {
			logger.error("SQL(获取所有历史数据表索引信息)执行异常: {}", e.toString());
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
