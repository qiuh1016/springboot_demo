package com.cetcme.springBootDemo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cetcme.springBootDemo.domain.PushLog;
import com.cetcme.springBootDemo.utils.Constants;
import com.cetcme.springBootDemo.utils.ConvertUtil;
import com.cetcme.springBootDemo.utils.JdbcUtil;

public class PushLogDao {

	Logger logger = LoggerFactory.getLogger(PushLogDao.class);

	/**
	 * 插入推送消息
	 */
	public int insertPushLog(PushLog pushLog) {
		int result = Constants.SQL_FAIL;
		if (pushLog == null) {
			return result;
		}

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;
		try {
			String sql = "insert into t_push_log(BOAT_CODE, ALARM_CODE, ALARM_NAME, ALARM_FLAG, ALARM_TIME, PUSH_FLAG) values(?, ?, ?, ?, ?, ?) ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, pushLog.getBoatCode());
			ps.setString(2, pushLog.getAlarmCode());
			ps.setString(3, pushLog.getAlarmName());
			ps.setInt(4, pushLog.getAlarmFlag());
			ps.setTimestamp(5, ConvertUtil.utilDate2SqlTimestamp(pushLog.getAlarmTime()));
			ps.setInt(6, pushLog.getPushFlag());
			ps.executeUpdate();
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
