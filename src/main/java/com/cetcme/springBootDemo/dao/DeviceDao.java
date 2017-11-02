package com.cetcme.springBootDemo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cetcme.springBootDemo.domain.Device;
import com.cetcme.springBootDemo.domain.DeviceExample;
import com.cetcme.springBootDemo.domain.DeviceExample.Criteria;
import com.cetcme.springBootDemo.domain.DeviceExtend;
import com.cetcme.springBootDemo.mapper.DeviceMapper;
import com.cetcme.springBootDemo.utils.Constants;
import com.cetcme.springBootDemo.utils.JdbcRuleUtil;
import com.cetcme.springBootDemo.utils.JdbcUtil;
import com.cetcme.springBootDemo.utils.MybatisUtil;

public class DeviceDao {

	Logger logger = LoggerFactory.getLogger(DeviceDao.class);

	/**
	 * 更新设备次要状态(设备编码不存在或者已删除时不更新)
	 */
	public synchronized int update(Device device) {
		int result = Constants.SQL_FAIL;
		if (device == null || StringUtils.isBlank(device.getDeviceNo())) {
			return result;
		}

		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			DeviceMapper mapper = session.getMapper(DeviceMapper.class);
			DeviceExample example = new DeviceExample();
			Criteria criteria = example.createCriteria();
			criteria.andDeviceNoEqualTo(device.getDeviceNo());
			criteria.andDelFlagEqualTo(false);
			result = mapper.updateByExampleSelective(device, example);
			session.commit(true);
//			logger.info("更新设备表成功");
		} catch (Exception e) {
			logger.error("SQL(update更新设备表)执行异常(" + device.getDeviceNo() + "): {}", e.toString());
			session.rollback(true);
		} finally {
			session.close();
		}
		return result;
	}

	/**
	 * 根据设备编码获取设备信息
	 */
	public Device getByDeviceNo(String deviceNo) {
		Device result = null;

		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			DeviceMapper mapper = session.getMapper(DeviceMapper.class);
			DeviceExample example = new DeviceExample();
			Criteria criteria = example.createCriteria();
			criteria.andDeviceNoEqualTo(deviceNo);
			criteria.andDelFlagEqualTo(false);
			List<Device> deviceList = mapper.selectByExample(example);
			if (deviceList != null && deviceList.size() > 0) {
				result = deviceList.get(0);
			}
			session.commit(true);
//			logger.info("根据设备编码获取设备信息成功");
		} catch (Exception e) {
			logger.error("SQL(getByDeviceNo根据设备编码获取设备信息)执行异常(" + deviceNo + "): {}", e.toString());
		} finally {
			session.close();
		}
		return result;
	}

	/**
	 * 设置设备为不在线
	 */
	public int setDeviceOffline(String deviceNo) {
		return setDeviceOffline(deviceNo, true);
	}

	public int setDeviceOnline(String deviceNo) {
		return setDeviceOffline(deviceNo, false);
	}

	public int setDeviceOffline(String deviceNo, boolean offlineFlag) {
		int result = 0;

		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			DeviceMapper mapper = session.getMapper(DeviceMapper.class);
			DeviceExample example = new DeviceExample();
			Criteria criteria = example.createCriteria();
			criteria.andDeviceNoEqualTo(deviceNo);
			criteria.andDelFlagEqualTo(false);
			Device device = new Device();
			/*if (!offlineFlag) {
				device.setOuttimeFlag(false);
			}*/
			device.setOfflineFlag(offlineFlag);
			device.setUpdateTime(new Date());
			result = mapper.updateByExampleSelective(device, example);
			session.commit(true);
			logger.info("更新数据库设备(" + deviceNo + ")离线状态" + Boolean.toString(offlineFlag) + "成功");
		} catch (Exception e) {
			logger.error("SQL(setDeviceOffline)执行异常(" + deviceNo + "): {}", e.toString());
		} finally {
			session.close();
		}
		return result;
	}

	/**
	 * 设置设备为超时
	 */
	public int setDeviceOuttime(String deviceNo) {
		return setDeviceOuttime(deviceNo, true);
	}

	public int setDeviceOuttime(String deviceNo, boolean outtimeFlag) {
		int result = 0;

		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			DeviceMapper mapper = session.getMapper(DeviceMapper.class);
			DeviceExample example = new DeviceExample();
			Criteria criteria = example.createCriteria();
			criteria.andDeviceNoEqualTo(deviceNo);
			criteria.andDelFlagEqualTo(false);
			Device device = new Device();
			device.setOuttimeFlag(outtimeFlag);
			result = mapper.updateByExampleSelective(device, example);
			session.commit(true);
			logger.info("更新数据库设备(" + deviceNo + ")超时状态成功");
		} catch (Exception e) {
			logger.error("SQL(setDeviceOuttime)执行异常(" + deviceNo + "): {}", e.toString());
		} finally {
			session.close();
		}
		return result;
	}

	/**
	 * 获取所有设备渔船信息
	 */
	public List<DeviceExtend> getAll() {
		List<DeviceExtend> result = new ArrayList<DeviceExtend>();

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = " select t1.device_id,t1.device_no, t1.idcardreader_no, t1.pair_flag , t1.offline_flag, t1.outtime_flag, t1.fence_id "
					+ " , t2.ship_id, t2.ship_no, t2.pic_name, t2.pic_tel_no, t2.cfs_start_date, t2.cfs_end_date from t_device t1 "
					+ " left join t_ship t2 on t1.device_id = t2.device_id and t2.del_flag = 0 where t1.del_flag = 0 ";

			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				DeviceExtend deviceExtend = new DeviceExtend();
				deviceExtend.setDeviceId(rs.getLong(1));
				deviceExtend.setDeviceNo(rs.getString(2));
				deviceExtend.setIdcardreaderNo(rs.getString(3));
				deviceExtend.setPairFlag(rs.getBoolean(4));
				deviceExtend.setOfflineFlag(rs.getBoolean(5));
				deviceExtend.setOuttimeFlag(rs.getBoolean(6));
				deviceExtend.setFenceId(rs.getLong(7));
				deviceExtend.setShipId(rs.getLong(8));
				deviceExtend.setShipNo(rs.getString(9));
				deviceExtend.setPicName(rs.getString(10));
				deviceExtend.setPicTelNo(rs.getString(11));
				deviceExtend.setCfsStartDate(rs.getDate(12));
				deviceExtend.setCfsEndDate(rs.getDate(13));
				//deviceExtend.setOfflineFlag(rs.getBoolean(14));
				//deviceExtend.setOuttimeFlag(rs.getBoolean(15));
				result.add(deviceExtend);
			}
			rs.close();
			ps.close();
//			logger.info("获取数据库所有设备渔船信息成功");
		} catch (SQLException e) {
			logger.error("SQL(getAll获取所有设备渔船信息)执行异常: {}", e.toString());
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
	 * 获取所有设备渔船信息
	 */
	public List<DeviceExtend> getfindAll() {
		List<DeviceExtend> result = new ArrayList<DeviceExtend>();

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = " select t1.device_id, t1.device_no, t1.offline_flag, t2.SIGNAL_STRENGTH from t_device t1 "
					+ "left join t_acq_data t2 on t1.device_id = t2.device_id where t1.del_flag = 0 AND t2.ACQ_DATA_ID IS NOT NULL";

			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				DeviceExtend deviceExtend = new DeviceExtend();
				deviceExtend.setDeviceId(rs.getLong(1));
				deviceExtend.setDeviceNo(rs.getString(2));
				deviceExtend.setOfflineFlag(rs.getBoolean(3));
				result.add(deviceExtend);
			}
			rs.close();
			ps.close();
//			logger.info("获取数据库所有设备渔船信息成功");
		} catch (SQLException e) {
			logger.error("SQL(getAll获取所有设备渔船信息)执行异常: {}", e.toString());
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
	 * 更新港口标识
	 */
	public int updateFenceId(Long deviceId, Long fenceId) {
		int result = Constants.SQL_FAIL;
		if (fenceId == null || deviceId == null) {
			return result;
		}

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;
		try {

			String sql = " update t_device set fence_id = ? where device_id = ? and del_flag = 0 ";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, fenceId);
			ps.setLong(2, deviceId);
			ps.executeUpdate();
			ps.close();
			logger.info("更新港口标识(deviceId:" + deviceId + ";fenceId:" + fenceId +")离线状态成功");
		} catch (Exception e) {
			logger.error("SQL(updateFenceId更新港口标识(deviceId:" + deviceId + ";fenceId:" + "执行异常: {}", e.toString());
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
	 * 获取所有设备权限用户Id
	 */
	public Map<String, String> deviceRuleUserId() {
		Map<String, String> result = new HashMap<String, String>();

		Connection conn = JdbcRuleUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = "SELECT t.Device_No, GROUP_CONCAT(t.User_Id SEPARATOR ',') User_Ids FROM userdevices t GROUP BY t.Device_No";
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String deviceId = rs.getString(1);
				String userIds = rs.getString(2);
				result.put(deviceId, userIds);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			logger.error("SQL(获取所有设备权限用户Id)执行异常: {}", e.toString());
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
