package com.cetcme.springBootDemo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cetcme.springBootDemo.domain.IofLog;
import com.cetcme.springBootDemo.utils.Constants;
import com.cetcme.springBootDemo.utils.ConvertUtil;
import com.cetcme.springBootDemo.utils.JdbcUtil;
import com.mysql.jdbc.Statement;

public class IofLogDao {

	Logger logger = LoggerFactory.getLogger(DictDao.class);

	/**
	 * 插入出入港记录
	 */
	public int insertIofLog(IofLog iofLog) {
		int result = Constants.SQL_FAIL;
		if (iofLog == null) {
			return result;
		}

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;
		try {
			String sql = "insert into t_iof_log(iof_flag, device_id, device_no, ship_id, fence_id, "
					+ " iof_time, iof_status, longitude, latitude) values(?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, iofLog.getIofFlag());
			ps.setLong(2, iofLog.getDeviceId());
			ps.setString(3, iofLog.getDeviceNo());
			ps.setLong(4, iofLog.getShipId());
			ps.setLong(5, iofLog.getFenceId());
			ps.setTimestamp(6, ConvertUtil.utilDate2SqlTimestamp(iofLog.getIofTime()));
			ps.setInt(7, iofLog.getIofStatus());
			ps.setDouble(8, iofLog.getLongitude());
			ps.setDouble(9, iofLog.getLatitude());
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			result = rs.getInt(1);
			
			rs.close();
			ps.close();
			logger.info("插入出入港记录(" + iofLog.getDeviceNo() + "," + iofLog.getFenceId() + ")成功");
		} catch (SQLException e) {
			logger.error("SQL(插入出入港记录)执行异常(" + iofLog.getDeviceNo() + "," + iofLog.getFenceId() + "): {}", e.toString());
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
	 * 最后一条出入港记录
	 */
	public int maxIofLog(IofLog iofLog) {
		int result = 0;
		if (iofLog == null || iofLog.getDeviceId() == null || StringUtils.isBlank(iofLog.getDeviceNo())
				|| iofLog.getShipId() == null) {
			return result;
		}

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps = null;
		try {
			String sql_iof = "";
			if(iofLog.getIofFlag() == Constants.IOF_FLAG_IN) {
				sql_iof = "AND IOF_FLAG in (1,3)";
			} else {
				sql_iof = "AND IOF_FLAG = 2";
			}
			String sql = "SELECT IOF_LOG_ID FROM t_iof_log WHERE DEL_FLAG = 0 AND DEVICE_ID = ? AND SHIP_ID = ? "+sql_iof+" ORDER BY UPDATE_TIME DESC LIMIT 0, 1";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, iofLog.getDeviceId());
			ps.setLong(2, iofLog.getShipId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
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
