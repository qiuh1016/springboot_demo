package com.cetcme.springBootDemo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cetcme.springBootDemo.domain.GpsPosition;
import com.cetcme.springBootDemo.utils.JdbcUtil;

public class CordonDao {

	Logger logger = LoggerFactory.getLogger(CordonDao.class);
	
	/**
	 * 获取所有围栏的坐标位置
	 */
	public Map<String, List<GpsPosition>> getCordonList() {
		Map<String, List<GpsPosition>> result = new HashMap<String, List<GpsPosition>>();
		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = "SELECT c.CORDON_ID, cp.LONGITUDE, cp.LATITUDE FROM t_cordon c "+
					"LEFT JOIN t_cordon_polygon cp ON c.CORDON_ID = cp.CORDON_ID "+
					"WHERE c.DEL_FLAG = 0 ORDER BY cp.POLYGON_ID";

			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int cordonId = 0;
			while (rs.next()) {
				if(cordonId == 0 || cordonId != rs.getInt(1)){
					cordonId = rs.getInt(1);
					List<GpsPosition> gpsList = new ArrayList<GpsPosition>();
					GpsPosition curPosition = new GpsPosition(rs.getDouble(2), rs.getDouble(3));
					gpsList.add(curPosition);
					result.put(cordonId+"", gpsList);
				}else{
					cordonId = rs.getInt(1);
					List<GpsPosition> gpsList = result.get(cordonId+"");
					GpsPosition curPosition = new GpsPosition(rs.getDouble(2), rs.getDouble(3));
					gpsList.add(curPosition);
					result.put(cordonId+"", gpsList);
				}
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SQL(获取所有围栏的坐标位置)执行异常: {}", e.toString());
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
	 * 获取所有围栏的所有人关系
	 */
	public Map<String, String> getCordonUserList() {
		Map<String, String> result = new HashMap<String, String>();
		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = "SELECT t.CREATE_USER_ID, GROUP_CONCAT(CONCAT(t.CORDON_ID,'_',t.CORDON_FLAG,'_',t.CORDON_ALL,'_',t.CORDON_NAME) SEPARATOR ',') CORDON_IDS FROM t_cordon t WHERE t.DEL_FLAG = 0 GROUP BY t.CREATE_USER_ID";

			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result.put(rs.getString(1), rs.getString(2));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			logger.error("SQL(获取所有围栏的所有人关系)执行异常: {}", e.toString());
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
	 * 获取围栏与设备关系
	 */
	public Map<String, String> getCordonDeviceList() {
		Map<String, String> result = new HashMap<String, String>();
		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = "SELECT t.CORDON_ID, GROUP_CONCAT(t.DEVICE_ID SEPARATOR ',') DEVICE_IDS FROM t_cordon_device t GROUP BY t.CORDON_ID";

			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result.put(rs.getString(1), rs.getString(2));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			logger.error("SQL(获取围栏与设备关系)执行异常: {}", e.toString());
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
	 * 获取进出围栏报警未解除部分
	 */
	public Map<String, String> getCordonIN() {
		Map<String, String> result = new HashMap<String, String>();
		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = "SELECT a.ALARM_NO, a.ALARM_TYPE FROM t_alarm a WHERE a.ALARM_TYPE = 95 AND a.DEL_FLAG = 0 AND a.SOLVE_FLAG = 0 GROUP BY a.DEVICE_NO";

			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result.put(rs.getString(1), rs.getString(2));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			logger.error("SQL(获取围栏与设备关系)执行异常: {}", e.toString());
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
	 * 获取进出围栏报警未解除部分
	 */
	public Map<String, String> getCordonOUT() {
		Map<String, String> result = new HashMap<String, String>();
		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = "SELECT a.ALARM_NO, a.ALARM_TYPE FROM t_alarm a WHERE a.ALARM_TYPE = 96 AND a.DEL_FLAG = 0 AND a.SOLVE_FLAG = 0 GROUP BY a.DEVICE_NO";

			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result.put(rs.getString(1), rs.getString(2));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			logger.error("SQL(获取围栏与设备关系)执行异常: {}", e.toString());
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
