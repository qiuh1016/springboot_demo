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

import com.cetcme.springBootDemo.domain.Punch;
import com.cetcme.springBootDemo.domain.PunchExample;
import com.cetcme.springBootDemo.domain.PunchExample.Criteria;
import com.cetcme.springBootDemo.mapper.PunchMapper;
import com.cetcme.springBootDemo.utils.Constants;
import com.cetcme.springBootDemo.utils.JdbcUtil;
import com.cetcme.springBootDemo.utils.MybatisUtil;

public class PunchDao {

	Logger logger = LoggerFactory.getLogger(PunchDao.class);

	/**
	 * 新增打卡数据
	 */
	public int add(Punch punch) {
		int result = Constants.SQL_FAIL;
		if (punch == null || punch.getDeviceId() == null || punch.getDeviceId() <= 0) {
			return result;
		}

		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			PunchMapper mapper = session.getMapper(PunchMapper.class);

			// 判断是否已经存在相同的打卡数据
			if (!isDuplicate(punch, mapper)) {
				result = mapper.insertSelective(punch);
			}
			session.commit(true);
			logger.info("新增打卡数据(" + punch.getDeviceNo() + "," + punch.getSailorIdNo() + ")成功");
		} catch (Exception e) {
			logger.error("SQL(新增打卡数据(" + punch.getDeviceNo() + "," + punch.getSailorIdNo() + "): {}", e.toString());
			session.rollback(true);
		} finally {
			session.close();
		}
		return result;
	}

	/**
	 * 根据身份证号判断是否已经存在重复打卡数据
	 */
	private boolean isDuplicate(Punch punch, PunchMapper mapper) {
		int result = -1;
		try {
			PunchExample example = new PunchExample();
			Criteria criteria = example.createCriteria();
			criteria.andDeviceIdEqualTo(punch.getDeviceId());
			if (punch.getShipId() != null) {
				criteria.andShipIdEqualTo(punch.getShipId());
			}
			criteria.andSailorIdNoEqualTo(punch.getSailorIdNo());
			criteria.andPunchTimeEqualTo(punch.getPunchTime());
			criteria.andDelFlagEqualTo(false);
			result = mapper.countByExample(example);
			logger.info("根据身份证号判断是否已经存在重复打卡数据(" + punch.getDeviceNo() + "," + punch.getSailorIdNo() + ")成功");
		} catch (Exception e) {
			logger.error("SQL(根据身份证号判断是否已经存在重复打卡数据(" + punch.getDeviceNo() + "," + punch.getSailorIdNo() + "): {}", e.toString());
			result = -1;
		} finally {
		}
		return result > 0;
	}
	
	/**
	 * 打卡表中未使用的打卡记录List
	 */
	public List<Punch> getUnUserdPunchRecordList(Long deviceId, Long shipId) {
		List<Punch> punchList = new ArrayList<Punch>();
		if (deviceId == null || shipId == null) {
			return punchList;
		}

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;
		try {
			String sql = "select SAILOR_NAME from (select * from t_punch where"
					+ " device_id = "+deviceId+" and ship_id = "+shipId+" and status = 0 "
					+ " and del_flag = 0 order by punch_time desc) tmp group by sailor_id_no";
			
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Punch punch = new Punch();
				punch.setSailorName(rs.getString(1));
				punchList.add(punch);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("回滚失败: {}", e1.toString());
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (SQLException e) {}
			}
		}
		
		return punchList;
	}

	/**
	 * 打卡表中未使用的打卡记录
	 */
	public int getUnUserdPunchRecord(Long deviceId, Long shipId) {
		int result = 0;
		if (deviceId == null || shipId == null) {
			return result;
		}

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select count(1) from t_punch t1 where t1.del_flag = 0 ");
			sqlSb.append(" and t1.device_id = " + deviceId);
			sqlSb.append(" and t1.ship_id = " + shipId);
			sqlSb.append(" and t1.status = 0 ");
			ps = conn.prepareStatement(sqlSb.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();
			ps.close();
			logger.info("打卡表中未使用的打卡记录(" + deviceId + "," + shipId + ")成功");
		} catch (Exception e) {
			logger.error("SQL(打卡表中未使用的打卡记录(" + deviceId + "," + shipId + "): {}", e.toString());
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("回滚失败: {}", e1.toString());
			}
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

	/**
	 * 更新打卡信息状态
	 */
	public int updateStatus(Long deviceId, Long shipId) {
		int result = Constants.SQL_FAIL;
		if (deviceId == null || shipId == null) {
			return result;
		}

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;
		try {
			String sql = " update t_punch set status = 1 "
					+ " where device_id = ? and ship_id = ? and status = 0 and del_flag = 0 ";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, deviceId);
			ps.setLong(2, shipId);
			ps.executeUpdate();
			ps.close();
			logger.info("更新打卡信息状态(" + deviceId + "," + shipId + ")成功");
		} catch (SQLException e) {
			logger.error("SQL(更新打卡信息状态(" + deviceId + "," + shipId + "): {}", e.toString());
			try {
				conn.rollback();
			} catch (SQLException e1) {
				logger.error("回滚失败: {}", e1.toString());
			}
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
