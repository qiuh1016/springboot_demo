package com.cetcme.springBootDemo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cetcme.springBootDemo.domain.Alarm;
import com.cetcme.springBootDemo.utils.Constants;
import com.cetcme.springBootDemo.utils.ConvertUtil;
import com.cetcme.springBootDemo.utils.DateUtil;
import com.cetcme.springBootDemo.utils.JdbcUtil;

public class AlarmDao {

	Logger logger = LoggerFactory.getLogger(AlarmDao.class);
	
	public int getAlarm(String alarmNo) {
		int result = 0;

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = "SELECT count(0) FROM t_alarm WHERE DEL_FLAG = 0 AND ALARM_NO = "+alarmNo+" AND SOLVE_FLAG = 0";

			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			logger.error("SQL(获取同一个设备报警ALARM_NO)执行异常: {}", e.toString());
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
	 * 获取同一个设备报警ALARM_NO
	 */
	public String getAlarmNoPageList(Long deviceId, Long shipId, int alarmType, int solveFlag) {
		String result = null;

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = "SELECT ALARM_NO FROM t_alarm t1 WHERE t1.DEL_FLAG = 0 AND t1.ALARM_TYPE = "+alarmType+" AND t1.DEVICE_ID = "+deviceId+" AND t1.SOLVE_FLAG = "+solveFlag+" ORDER BY t1.CREATE_TIME DESC LIMIT 0, 1";

			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getString(1);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			logger.error("SQL(获取同一个设备报警ALARM_NO)执行异常: {}", e.toString());
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
	 * 批量新增报警信息
	 */
	public int addBatch(List<Alarm> alarmList) {
		int result = Constants.SQL_FAIL;
		if (alarmList == null || alarmList.size() == 0) {
			return result;
		}

		Connection conn = JdbcUtil.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;

		try {
			StringBuilder insertSqlSb = new StringBuilder();
			StringBuilder insertSqlSb_notSolve = new StringBuilder();
			boolean needIns = false;
			insertSqlSb
					.append(" insert into t_alarm(alarm_no, device_id, device_no, ship_id, alarm_type, report_time, longitude, latitude, alarm_mark, alarm_user_id) "
							+ " values");

			insertSqlSb_notSolve
					.append(" insert into t_not_solve_alarm(alarm_no, device_id, device_no, ship_id, alarm_type, report_time, longitude, latitude) "
							+ " values");
			ps = conn.prepareStatement("");
			String updateSql = "";
			String deleteSql = "";
			for (Alarm alarm : alarmList) {
				try{
					String success = "成功增加";
					if (!alarm.getSolveFlag()) {
						needIns = true;
						insertSqlSb.append("('");
						insertSqlSb.append(alarm.getAlarmNo() + "',");
						insertSqlSb.append(alarm.getDeviceId() + ",");
						insertSqlSb.append("'" + alarm.getDeviceNo() + "',");
						insertSqlSb.append(alarm.getShipId() + ",");
						insertSqlSb.append(alarm.getAlarmType() + ",");
						insertSqlSb.append("'" + DateUtil.parseDateToString(alarm.getReportTime()) + "',");
						insertSqlSb.append(alarm.getLongitude() + ",");
						insertSqlSb.append(alarm.getLatitude() + ",");
						if(alarm.getAlarmMark() != null){
							insertSqlSb.append("'" + alarm.getAlarmMark() + "',");
						}else{
							insertSqlSb.append("null,");
						}
						insertSqlSb.append(alarm.getAlarmUserId() + "),");

						insertSqlSb_notSolve.append("('");
						insertSqlSb_notSolve.append(alarm.getAlarmNo() + "',");
						insertSqlSb_notSolve.append(alarm.getDeviceId() + ",");
						insertSqlSb_notSolve.append("'" + alarm.getDeviceNo() + "',");
						insertSqlSb_notSolve.append(alarm.getShipId() + ",");
						insertSqlSb_notSolve.append(alarm.getAlarmType() + ",");
						insertSqlSb_notSolve.append("'" + DateUtil.parseDateToString(alarm.getReportTime()) + "',");
						insertSqlSb_notSolve.append(alarm.getLongitude() + ",");
						insertSqlSb_notSolve.append(alarm.getLatitude() + "),");
					} else {
						success = "成功解除";
						if(alarm.getAlarmMark() != null){
							updateSql = " update t_alarm set solve_flag = 1, update_time = now(), solve_time ='"
									+ DateUtil.parseDateToString(alarm.getSolveTime()) + "' where alarm_no = " + alarm.getAlarmNo();
							deleteSql = "delete from t_not_solve_alarm where alarm_no = " + alarm.getAlarmNo();

						}else{
							updateSql = " update t_alarm set solve_flag = 1, update_time = now(), solve_time ='"
									+ DateUtil.parseDateToString(alarm.getSolveTime()) + "' where device_id = "
									+ alarm.getDeviceId() + " and alarm_type = "
									+ alarm.getAlarmType() + " and solve_flag = 0";
							deleteSql = "delete from t_not_solve_alarm where device_id = "
									+ alarm.getDeviceId() + " and alarm_type = "
									+ alarm.getAlarmType() + " and solve_flag = 0";
						}
						
						ps.addBatch(updateSql);
						ps.addBatch(deleteSql);
					}
					logger.info("新增报警信息addBatch(" + alarm.getDeviceNo() + "," + alarm.getAlarmType() + ")"+success);
				}catch(Exception e){
					logger.info("新增报警信息addBatch(" + alarm.getDeviceNo() + "," + alarm.getAlarmType() + ")失败:" + e.getMessage());
					continue;
				}
			}
			String insertSql = StringUtils.left(insertSqlSb.toString(), insertSqlSb.length() - 1);
			String insertSql_notSolve = StringUtils.left(insertSqlSb_notSolve.toString(), insertSqlSb_notSolve.length() - 1);
			if (needIns) {
				ps.addBatch(insertSql);
				ps.addBatch(insertSql_notSolve);
			}
			int[] results = ps.executeBatch();
			conn.commit();
			int tempResult = Constants.SQL_FAIL;
			for (int i : results) {
				tempResult += i;
			}
			if (tempResult > 0) {
				result = Constants.SQL_SUCCESS;
			}

			ps.close();
			logger.info("批量新增报警信息成功");
		} catch (SQLException e) {
			logger.error("SQL(批量新增报警信息)执行异常: {}", e.toString());
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
	 * 获取嘉科信息用户对应权限报警类型
	 */
	public List<Integer> getJkxxAlarmType() {
		List<Integer> result = new ArrayList<Integer>();

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = " select alarm_type from t_alarm_perm t1 where t1.role_id >= (select role_id from t_user where user_name  = 'jkxx') ";

			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result.add(rs.getInt(1));
			}
			rs.close();
			ps.close();
			logger.info("获取嘉科信息用户对应权限报警类型成功");
		} catch (SQLException e) {
			logger.error("SQL(获取嘉科信息用户对应权限报警类型)执行异常: {}", e.toString());
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
	 * 插入报警
	 */
	public int insertAlarm(Alarm alarm) {
		int result = Constants.SQL_FAIL;
		if (alarm == null) {
			return result;
		}

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;
		try {
			String sql = "insert into t_alarm(device_id, alarm_type, report_time) values(?, ?, ?) ";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, alarm.getDeviceId());
			ps.setInt(2, alarm.getAlarmType());
			ps.setTimestamp(3, ConvertUtil.utilDate2SqlTimestamp(alarm.getReportTime()));
			ps.executeUpdate();
			ps.close();
			logger.info("插入报警(" + alarm.getDeviceNo() + "," + alarm.getAlarmType() + ")成功");
		} catch (SQLException e) {
			logger.error("SQL(插入报警)执行异常(" + alarm.getDeviceNo() + "," + alarm.getAlarmType() + "): {}", e.toString());
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
	 * 解除超时报警
	 */
	public int removeOuttimeAlarm(Long deviceId) {
		int result = Constants.SQL_FAIL;

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;
		try {
			String sql = "update t_alarm set solve_flag = 1, solve_time = now() "
					+ "where device_id = ? "
					+ "and alarm_type = 12 and del_flag = 0;";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, deviceId);
			ps.executeUpdate();
			ps.close();
			logger.info("解除超时报警(" + deviceId + ")成功");
		} catch (SQLException e) {
			logger.error("SQL(解除超时报警)执行异常(" + deviceId + "): {}", e.toString());
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
	 * 查询所有进出围栏未解除的设备
	 */
	public int getAlarmCordonOne(String alarmNo, String alarmType) {

		int result = 0;
		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = "SELECT count(0) FROM t_alarm t WHERE t.ALARM_TYPE = "+alarmType+" AND t.SOLVE_TIME IS NULL AND t.ALARM_NO = '"+alarmNo+"'";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			logger.error("SQL(查询所有进出围栏未解除的设备)执行异常: {}", e.toString());
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
