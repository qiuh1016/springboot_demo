package com.cetcme.springBootDemo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cetcme.springBootDemo.domain.IofLog;
import com.cetcme.springBootDemo.domain.IofSailor;
import com.cetcme.springBootDemo.utils.Constants;
import com.cetcme.springBootDemo.utils.JdbcUtil;

public class IofSailorDao {

	Logger logger = LoggerFactory.getLogger(DictDao.class);

	/**
	 * APP、短信已提交的进出港人员List
	 */
	public List<IofSailor> getSubmitIofSailorList(IofLog iofLog) {
		List<IofSailor> iofSailorList = new ArrayList<IofSailor>();
		if (iofLog == null || iofLog.getDeviceId() == null || StringUtils.isBlank(iofLog.getDeviceNo())
				|| iofLog.getShipId() == null) {
			return iofSailorList;
		}

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select t1.SAILOR_NAME from t_iof_sailor t1 where t1.iof_log_id is null ");
			sqlSb.append(" and t1.del_flag = 0 and t1.device_id = " + iofLog.getDeviceId());
			sqlSb.append(" and t1.device_no = '" + iofLog.getDeviceNo() + "'");
			sqlSb.append(" and t1.ship_id = " + iofLog.getShipId() + " and t1.data_type <> 2");
			if (iofLog.getIofFlag() != null) {
				int iofFlag = iofLog.getIofFlag();
				if (iofLog.getIofFlag() == Constants.IOF_FLAG_CFS_OUT) {
					iofFlag = Constants.IOF_FLAG_OUT;
				}
				sqlSb.append(" and t1.iof_flag = " + iofFlag);
			}
			ps = conn.prepareStatement(sqlSb.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				IofSailor iofSailor = new IofSailor();
				iofSailor.setSailorName(rs.getString(1));
				iofSailorList.add(iofSailor);
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
		
		return iofSailorList;
	}
	
	/**
	 * APP、短信已提交的进出港人员数量
	 */
	public int getSubmitIofSailorCnt(IofLog iofLog) {
		int result = 0;
		if (iofLog == null || iofLog.getDeviceId() == null || StringUtils.isBlank(iofLog.getDeviceNo())
				|| iofLog.getShipId() == null) {
			return result;
		}

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select count(1) from t_iof_sailor t1 where t1.iof_log_id is null ");
			sqlSb.append(" and t1.del_flag = 0 and t1.device_id = " + iofLog.getDeviceId());
			sqlSb.append(" and t1.device_no = '" + iofLog.getDeviceNo() + "'");
			sqlSb.append(" and t1.ship_id = " + iofLog.getShipId());
			if (iofLog.getIofFlag() != null) {
				int iofFlag = iofLog.getIofFlag();
				if (iofLog.getIofFlag() == Constants.IOF_FLAG_CFS_OUT) {
					iofFlag = Constants.IOF_FLAG_OUT;
				}
				sqlSb.append(" and t1.iof_flag = " + iofFlag);
			}
			ps = conn.prepareStatement(sqlSb.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
			rs.close();
			ps.close();
			logger.info("APP、短信已提交的进出港人员数量(" + iofLog.getDeviceNo() + "," + iofLog.getFenceId() + ")成功");
		} catch (SQLException e) {
			logger.error("SQL(APP、短信已提交的进出港人员数量(" + iofLog.getDeviceNo() + "," + iofLog.getFenceId() + "): {}", e.toString());
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
	 * 更新出入港人员的出入港标识
	 */
	public int update(IofLog iofLog) {
		int result = Constants.SQL_FAIL;
		if (iofLog == null || iofLog.getDeviceId() == null || iofLog.getShipId() == null
				|| iofLog.getIofFlag() == null) {
			return result;
		}

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;
		try {

			String sql = " update t_iof_sailor t1 inner join  (select * from t_iof_log temp "
					+ " where temp.del_flag = 0 and temp.device_id = ? "
					+ " and temp.ship_id = ? and temp.iof_flag = ? "
					+ " order by temp.update_time desc limit 1) t2 on 1 = 1 "
					+ " set t1.iof_log_id = t2.iof_log_id, t1.iof_time = t2.iof_time "
					+ " where t1.iof_log_id is null  and t1.del_flag = 0 and t1.device_id = ? "
					+ " and t1.ship_id = ? and t1.iof_flag = ? and t1.data_type <> 2";
			ps = conn.prepareStatement(sql);
			ps.setLong(1, iofLog.getDeviceId());
			ps.setLong(2, iofLog.getShipId());
			ps.setInt(3, iofLog.getIofFlag());
			ps.setLong(4, iofLog.getDeviceId());
			ps.setLong(5, iofLog.getShipId());
			ps.setInt(6,
					iofLog.getIofFlag() == Constants.IOF_FLAG_CFS_OUT ? Constants.IOF_FLAG_OUT : iofLog.getIofFlag());
			ps.executeUpdate();
			ps.close();
			logger.info("更新出入港人员的出入港标识(" + iofLog.getDeviceNo() + "," + iofLog.getFenceId() + ")成功");
		} catch (SQLException e) {
			logger.error("SQL(更新出入港人员的出入港标识(" + iofLog.getDeviceNo() + "," + iofLog.getFenceId() + "): {}", e.toString());
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
	 * 提交打卡人员
	 */
	public int addIofSailor(List<IofSailor> iofSailorList, int iofLogId, int iofFlag) {
		int result = Constants.SQL_FAIL;
		if(iofSailorList == null || iofSailorList.size() == 0){
			return result;
		}

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;
		try {
			String sql = "insert into t_iof_sailor(iof_log_id, iof_flag, device_id, device_no, ship_id,"
					+ " sailor_id_no, sailor_name, punch_time, data_type, iof_time, update_time, del_flag) "
					+ "values (?, ?, ?, ?, ?, ?, ?, now(), 1, now(), now(), 0)";

			ps = conn.prepareStatement(sql);
			for(IofSailor iofSailor : iofSailorList){
				ps.setInt(1, iofLogId);
				ps.setInt(2, iofFlag == Constants.IOF_FLAG_CFS_OUT ? Constants.IOF_FLAG_OUT : iofFlag);
				ps.setLong(3, iofSailor.getDeviceId());
				ps.setString(4, iofSailor.getDeviceNo());
				ps.setLong(5, iofSailor.getShipId());
				ps.setString(6, iofSailor.getSailorIdNo());
				ps.setString(7, iofSailor.getSailorName());
				
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
			ps.close();
			
			result = Constants.SQL_SUCCESS;
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

	/**
	 * 提交打卡人员
	 */
	public int addIofSailor(IofLog iofLog) {
		int result = Constants.SQL_FAIL;
		if (iofLog == null || iofLog.getDeviceId() == null || iofLog.getShipId() == null
				|| iofLog.getIofFlag() == null) {
			return result;
		}

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;
		try {

			String sql = "insert into t_iof_sailor(iof_log_id, iof_flag, device_id, device_no, "
					+ " ship_id, sailor_id_no, sailor_name, punch_time, data_type, iof_time) "
					+ " select t2.iof_log_id, ?, device_id, device_no, ship_id, sailor_id_no, sailor_name, punch_time, 0, "
					+ " t2.iof_time from ( select * from (select * from t_punch where device_id = ? and ship_id = ? and status = 0 "
					+ " and del_flag = 0 order by punch_time desc) tmp group by sailor_id_no ) "
					+ " t1 left join (select iof_log_id, iof_flag, iof_time "
					+ " from t_iof_log where device_id = ? and ship_id = ? and iof_flag = ? order by iof_time desc "
					+ " limit 1) t2 on 1 = 1 ";

			ps = conn.prepareStatement(sql);
			ps.setInt(1,
					iofLog.getIofFlag() == Constants.IOF_FLAG_CFS_OUT ? Constants.IOF_FLAG_OUT : iofLog.getIofFlag());
			ps.setLong(2, iofLog.getDeviceId());
			ps.setLong(3, iofLog.getShipId());
			ps.setLong(4, iofLog.getDeviceId());
			ps.setLong(5, iofLog.getShipId());
			ps.setInt(6, iofLog.getIofFlag());
			ps.executeUpdate();
			ps.close();
			logger.info("提交打卡人员(" + iofLog.getDeviceNo() + "," + iofLog.getFenceId() + ")成功");
		} catch (SQLException e) {
			logger.error("SQL(提交打卡人员(" + iofLog.getDeviceNo() + "," + iofLog.getFenceId() + "): {}", e.toString());
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
	 * 查询上次出港人员list
	 */
	public List<IofSailor> prevFenceOutList(int iofLogId) {
		List<IofSailor> iofSailorList  = new ArrayList<IofSailor>();
		if(iofLogId > 0){
			Connection conn = JdbcUtil.getConnection();
			PreparedStatement ps;
			try {
				String sql = "SELECT DEVICE_ID,DEVICE_NO,SHIP_ID,SAILOR_ID_NO,SAILOR_NAME FROM t_iof_sailor where IOF_LOG_ID = ? and DATA_TYPE <> 2 and DEL_FLAG = 0";
				ps = conn.prepareStatement(sql);
				ps.setInt(1, iofLogId);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					IofSailor iofSailor = new IofSailor();
					iofSailor.setDeviceId(rs.getLong(1));
					iofSailor.setDeviceNo(rs.getString(2));
					iofSailor.setShipId(rs.getLong(3));
					iofSailor.setSailorIdNo(rs.getString(4));
					iofSailor.setSailorName(rs.getString(5));
					iofSailorList.add(iofSailor);
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
		}
		
		return iofSailorList;
	}
}
