package com.cetcme.springBootDemo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.ibatis.session.SqlSession;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cetcme.springBootDemo.domain.AcqData;
import com.cetcme.springBootDemo.domain.AcqDataExample;
import com.cetcme.springBootDemo.domain.AcqDataExample.Criteria;
import com.cetcme.springBootDemo.mapper.AcqDataMapper;
import com.cetcme.springBootDemo.utils.CacheUtil;
import com.cetcme.springBootDemo.utils.Constants;
import com.cetcme.springBootDemo.utils.Constants.CacheType;
import com.cetcme.springBootDemo.utils.DateUtil;
import com.cetcme.springBootDemo.utils.JdbcUtil;
import com.cetcme.springBootDemo.utils.MongodbUtil;
import com.cetcme.springBootDemo.utils.MybatisUtil;
import com.mongodb.client.MongoCollection;

public class AcqDataDao {

	Logger logger = LoggerFactory.getLogger(AcqDataDao.class);

	/**
	 * 批量更新采集数据
	 */
	public int updateBatch(List<AcqData> acqDataList) {
		int result = Constants.SQL_FAIL;
		if (acqDataList == null || acqDataList.size() == 0) {
			return result;
		}

		Connection conn = JdbcUtil.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e1) {
			logger.error("数据库异常: {}", e1.toString());
		}
		PreparedStatement delPs;
		PreparedStatement insPs;

		StringBuilder delSqlSb = new StringBuilder();
		delSqlSb.append(" delete from t_acq_data where device_no in (");

		// 先批量删除上传数据的设备，再批量新增，这样比一个一个更新效率高
		try {
			insPs = conn.prepareStatement("");
			StringBuilder insSqlSb = new StringBuilder();
			insSqlSb.append(
					" insert into t_acq_data(device_id, device_no, ship_id, iof_flag, acq_time, day_total_mileage, total_mileage, alarm_status, "
							+ " longitude, latitude, signal_type, speed, tack, signal_strength, cpu_tempreture, libv, pvbv, data_type) "
							+ " values ");
			for (int i = 0; i < acqDataList.size(); i++) {
				AcqData acqData = acqDataList.get(i);
				try {
					delSqlSb.append(acqData.getDeviceNo());

					insSqlSb.append("(");
					insSqlSb.append(acqData.getDeviceId() + ",");
					insSqlSb.append("'" + acqData.getDeviceNo() + "',");
					insSqlSb.append(acqData.getShipId() + ",");
					insSqlSb.append(acqData.getIofFlag() + ",");
					insSqlSb.append("'" + DateUtil.parseDateToString(acqData.getAcqTime()) + "',");
					insSqlSb.append(acqData.getDayTotalMileage() + ",");
					insSqlSb.append(acqData.getTotalMileage() + ",'");
					insSqlSb.append(acqData.getAlarmStatus() + "',");
					insSqlSb.append(acqData.getLongitude() + ",");
					insSqlSb.append(acqData.getLatitude() + ",");
					insSqlSb.append(acqData.getSignalType() + ",");
					insSqlSb.append(acqData.getSpeed() + ",");
					insSqlSb.append(acqData.getTack() + ",");
					insSqlSb.append(acqData.getSignalStrength() + ",");
					insSqlSb.append(acqData.getCpuTempreture() + ",");
					insSqlSb.append(acqData.getLibv() + ",");
					insSqlSb.append(acqData.getPvbv() + ",");
					insSqlSb.append(acqData.getDataType() + ")");

					if (i < acqDataList.size() - 1) {
						delSqlSb.append(",");
						insSqlSb.append(",");
					}
					// logger.info("更新采集数据addBatch(" + acqData.getDeviceNo() +
					// ")成功");
				} catch (Exception e) {
					logger.info("更新采集数据addBatch(" + acqData.getDeviceNo() + e.getMessage());

				}
			}

			delSqlSb.append(" )");
			delPs = conn.prepareStatement(delSqlSb.toString());
			delPs.execute();

			insPs.addBatch(insSqlSb.toString());
			int[] results = insPs.executeBatch();
			conn.commit();
			int tempResult2 = Constants.SQL_FAIL;
			for (int i : results) {
				tempResult2 += i;
			}
			if (tempResult2 > 0) {
				result = Constants.SQL_SUCCESS;
			}
			delPs.close();
			insPs.close();
			// logger.info("更新数据库实时数据成功");
		} catch (SQLException e) {
			logger.error("SQL(更新数据库实时数据)执行异常: {}", e.toString());
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
					logger.error("conn关闭异常: {}", e.toString());
				}
			}
		}
		return result;
	}

	/**
	 * 新增采集数据
	 */
	public int add(AcqData acqData) {
		int result = Constants.SQL_FAIL;
		if (acqData == null || acqData.getDeviceId() == null || acqData.getDeviceId() <= 0) {
			return result;
		}

		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			AcqDataMapper mapper = session.getMapper(AcqDataMapper.class);
			result = mapper.insertSelective(acqData);
			session.commit(true);
			logger.info("新增采集数据成功");
		} catch (Exception e) {
			logger.error("SQL(新增采集数据)执行异常(" + acqData.getDeviceNo() + "): {}", e.toString());
			session.rollback(true);
		} finally {
			session.close();
		}
		return result;
	}

	/**
	 * 查询某一设备当日最近的采集数据
	 */
	public AcqData getLatest(Long deviceId, Date startDate, Date endDate) {

		// 如果设备标识为空，则返回null
		if (deviceId == null || deviceId <= 0) {
			return null;
		}

		AcqData result = null;
		SqlSession session = MybatisUtil.getFactory().openSession();
		List<AcqData> acqDataList = new ArrayList<AcqData>();
		try {
			AcqDataMapper mapper = session.getMapper(AcqDataMapper.class);
			AcqDataExample example = new AcqDataExample();
			Criteria criteria = example.createCriteria();
			criteria.andDelFlagEqualTo(false);
			criteria.andDeviceIdEqualTo(deviceId);
			criteria.andAcqTimeBetween(startDate, endDate);
			example.setOrderByClause("acq_time desc");
			acqDataList = mapper.selectByExample(example);
			session.commit(true);
			logger.info("查询某一设备当日最近的采集数据成功");
		} catch (Exception e) {
			logger.error("SQL(查询某一设备当日最近的采集数据)执行异常(" + deviceId + "," + startDate.toLocaleString() + ","
					+ endDate.toLocaleString() + "): {}", e.toString());
		} finally {
			session.close();
		}
		if (acqDataList != null && !acqDataList.isEmpty()) {
			result = acqDataList.get(0);
		}
		return result;
	}

	/**
	 * 查询所有设备最近的采集数据
	 */
	public List<AcqData> getLatestAlarmStatus() {

		List<AcqData> result = new ArrayList<AcqData>();

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = " select device_no, alarm_status from (select * from t_acq_data where del_flag = 0 order by acq_time desc) t1 "
					+ " group by device_no order by device_no ";

			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				AcqData acqData = new AcqData();
				acqData.setDeviceNo(rs.getString(1));
				acqData.setAlarmStatus(rs.getString(2));
			}
			rs.close();
			ps.close();
			logger.info("查询所有设备最近的采集数据成功");
		} catch (SQLException e) {
			logger.error("SQL(查询所有设备最近的采集数据)执行异常: {}", e.toString());
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
	 * 查询某一设备最近的采集数据
	 */
	public AcqData getLatestByDeviceId(Long deviceId) {

		// 如果设备标识为空，则返回null
		if (deviceId == null || deviceId <= 0) {
			return null;
		}

		AcqData result = null;
		SqlSession session = MybatisUtil.getFactory().openSession();
		List<AcqData> acqDataList = new ArrayList<AcqData>();
		try {
			AcqDataMapper mapper = session.getMapper(AcqDataMapper.class);
			AcqDataExample example = new AcqDataExample();
			Criteria criteria = example.createCriteria();
			criteria.andDelFlagEqualTo(false);
			criteria.andDeviceIdEqualTo(deviceId);
			example.setOrderByClause("acq_time desc");
			acqDataList = mapper.selectByExample(example);
			session.commit(true);
			logger.info("查询某一设备最近的采集数据成功");
		} catch (Exception e) {
			logger.error("SQL(查询某一设备最近的采集数据)执行异常: {}", e.toString());
		} finally {
			session.close();
		}
		if (acqDataList != null && !acqDataList.isEmpty()) {
			result = acqDataList.get(0);
		}
		return result;
	}

	/**
	 * 查询实时采集表中的所有数据
	 */
	public List<AcqData> getAll() {

		List<AcqData> result = new ArrayList<AcqData>();

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			String sql = " select device_id, device_no, ship_id, iof_flag, acq_time, day_total_mileage, total_mileage, "
					+ " alarm_status, longitude, latitude, signal_type, speed, tack, signal_strength, cpu_tempreture, "
					+ " libv, pvbv, update_time from (select * from t_acq_data where del_flag = 0 order by acq_time desc) t1 "
					+ " group by device_no order by device_no ";

			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				AcqData acqData = new AcqData();
				acqData.setDeviceId(rs.getLong(1));
				acqData.setDeviceNo(rs.getString(2));
				acqData.setShipId(rs.getLong(3));
				acqData.setIofFlag(rs.getInt(4));
				acqData.setAcqTime(rs.getTimestamp(5));
				acqData.setDayTotalMileage(rs.getDouble(6));
				acqData.setTotalMileage(rs.getDouble(7));
				acqData.setAlarmStatus(rs.getString(8));
				acqData.setLongitude(rs.getDouble(9));
				acqData.setLatitude(rs.getDouble(10));
				acqData.setSignalType(rs.getInt(11));
				acqData.setSpeed(rs.getFloat(12));
				acqData.setTack(rs.getFloat(13));
				acqData.setSignalStrength(rs.getInt(14));
				acqData.setCpuTempreture(rs.getInt(15));
				acqData.setLibv(rs.getFloat(16));
				acqData.setPvbv(rs.getFloat(17));
				acqData.setUpdateTime(rs.getTimestamp(18));
				result.add(acqData);
			}
			rs.close();
			ps.close();
			logger.info("查询实时采集表中的所有数据成功");
		} catch (Exception e) {
			logger.error("SQL(查询实时采集表中的所有数据)执行异常: {}", e.toString());
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
	 * 插入采集数据历史表
	 */
	public int insertHistory(AcqData acqData) {
		int result = Constants.SQL_FAIL;
		if (acqData == null) {
			return result;
		}

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;
		try {
			StringBuilder sqlSb = new StringBuilder();
			String deviceNo = acqData.getDeviceNo();
//			Object tableSuffixObj = CacheUtil.get(CacheType.ACQDATA_HISTORY_TBL_INDEX_CACHE, deviceNo);
//			if (tableSuffixObj == null) {
//				return result;
//			}
//			String tableSuffix = CacheUtil.get(CacheType.ACQDATA_HISTORY_TBL_INDEX_CACHE, deviceNo).toString();
            String tableSuffix = "0";
			sqlSb.append(" insert into t_acq_data_" + tableSuffix
					+ "(device_id, device_no, ship_id, acq_time, day_total_mileage, total_mileage, alarm_status, "
					+ " longitude, latitude, signal_type, speed, tack, signal_strength, cpu_tempreture, libv, pvbv) "
					+ " values ");

			sqlSb.append("(");
			sqlSb.append(acqData.getDeviceId() + ",");
			sqlSb.append("'" + acqData.getDeviceNo() + "',");
			sqlSb.append(acqData.getShipId() + ",");
			sqlSb.append("'" + DateUtil.parseDateToString(acqData.getAcqTime()) + "',");
			sqlSb.append(acqData.getDayTotalMileage() + ",");
			sqlSb.append(acqData.getTotalMileage() + ",");
			sqlSb.append(acqData.getAlarmStatus() + ",");
			sqlSb.append(acqData.getLongitude() + ",");
			sqlSb.append(acqData.getLatitude() + ",");
			sqlSb.append(acqData.getSignalType() + ",");
			sqlSb.append(acqData.getSpeed() + ",");
			sqlSb.append(acqData.getTack() + ",");
			sqlSb.append(acqData.getSignalStrength() + ",");
			sqlSb.append(acqData.getCpuTempreture() + ",");
			sqlSb.append(acqData.getLibv() + ",");
			sqlSb.append(acqData.getPvbv() + ")");

			ps = conn.prepareStatement(sqlSb.toString());
			ps.execute();
			ps.close();

			// mongodb插入

			MongoCollection<Document> collection = MongodbUtil.getMongoDBDaoImpl().GetCollection();

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
			Document document = new Document("DEVICE_ID", acqData.getDeviceId())
					.append("DEVICE_NO", acqData.getDeviceNo())
					.append("SHIP_ID", acqData.getShipId())
					.append("IOF_FLAG", acqData.getIofFlag())
					.append("ACQ_TIME", format.format(acqData.getAcqTime()))
					.append("DAY_TOTAL_MILEAGE", acqData.getDayTotalMileage())
					.append("TOTAL_MILEAGE", acqData.getTotalMileage())
					.append("ALARM_STATUS", acqData.getAlarmStatus())
					.append("LONGITUDE", acqData.getLongitude())
					.append("LATITUDE", acqData.getLatitude())
					.append("SIGNAL_TYPE", acqData.getSignalType())
					.append("SPEED", acqData.getSpeed())
					.append("TACK", acqData.getTack())
					.append("SIGNAL_STRENGTH", acqData.getSignalStrength())
					.append("CPU_TEMPRETURE", acqData.getCpuTempreture())
					.append("LIBV", acqData.getLibv())
					.append("PVBV", acqData.getPvbv())
					.append("CREATE_TIME", format.format(acqData.getCreateTime()))
					.append("UPDATE_TIME", format.format(acqData.getUpdateTime()))
					.append("DEL_FLAG",0)
					.append("DATA_TYPE", (acqData.getDataType() != null && acqData.getDataType() == 2)?2:1);
			collection.insertOne(document);

			// logger.info("插入采集数据历史表成功");
		} catch (Exception e) {
			logger.error("SQL(插入采集数据历史表)执行异常: {}", e.toString());
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
	 * 将相同设备历史数据批量插入
	 * 
	 * @param acqDataList
	 * @return
	 */
	public int insertHistory(List<AcqData> acqDataList) {
		int result = Constants.SQL_FAIL;
		if (acqDataList == null || acqDataList.size() == 0) {
			return result;
		}

		Connection conn = JdbcUtil.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e1) {
			logger.error("数据库异常: {}", e1.toString());
		}

		PreparedStatement insPs;

		// 先批量删除上传数据的设备，再批量新增，这样比一个一个更新效率高
		try {
			insPs = conn.prepareStatement("");
			StringBuilder insSqlSb = new StringBuilder();
			String deviceNo = acqDataList.get(0).getDeviceNo();
			Object tableSuffixObj = CacheUtil.get(CacheType.ACQDATA_HISTORY_TBL_INDEX_CACHE, deviceNo);
			if (tableSuffixObj == null) {
				return result;
			}
			String tableSuffix = CacheUtil.get(CacheType.ACQDATA_HISTORY_TBL_INDEX_CACHE, deviceNo).toString();
			insSqlSb.append(" insert into t_acq_data_" + tableSuffix
					+ "(device_id, device_no, ship_id, acq_time, day_total_mileage, total_mileage, alarm_status,"
					+ " longitude, latitude, signal_type, speed, tack, signal_strength, cpu_tempreture, libv, pvbv,"
					+ " data_type) "
					+ " values ");

			MongoCollection<Document> collection = MongodbUtil.getMongoDBDaoImpl().GetCollection();
			List<Document> documents = new ArrayList<>();

			for (int i = 0; i < acqDataList.size(); i++) {
				AcqData acqData = acqDataList.get(i);
				try {

					insSqlSb.append("(");
					insSqlSb.append(acqData.getDeviceId() + ",");
					insSqlSb.append("'" + acqData.getDeviceNo() + "',");
					insSqlSb.append(acqData.getShipId() + ",");
					insSqlSb.append("'" + DateUtil.parseDateToString(acqData.getAcqTime()) + "',");
					insSqlSb.append(acqData.getDayTotalMileage() + ",");
					insSqlSb.append(acqData.getTotalMileage() + ",");
					insSqlSb.append(acqData.getAlarmStatus() + ",");
					insSqlSb.append(acqData.getLongitude() + ",");
					insSqlSb.append(acqData.getLatitude() + ",");
					insSqlSb.append(acqData.getSignalType() + ",");
					insSqlSb.append(acqData.getSpeed() + ",");
					insSqlSb.append(acqData.getTack() + ",");
					insSqlSb.append(acqData.getSignalStrength() + ",");
					insSqlSb.append(acqData.getCpuTempreture() + ",");
					insSqlSb.append(acqData.getLibv() + ",");
					insSqlSb.append(acqData.getPvbv() + ",");
					insSqlSb.append(((acqData.getDataType() != null && acqData.getDataType() == 2) ? 2 : 1) + ")");

					if (i < acqDataList.size() - 1) {
						insSqlSb.append(",");
					}
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					format.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
					Document document = new Document("DEVICE_ID", acqData.getDeviceId())
							.append("DEVICE_NO", acqData.getDeviceNo())
							.append("SHIP_ID", acqData.getShipId())
							.append("IOF_FLAG", acqData.getIofFlag())
							.append("ACQ_TIME", format.format(new Timestamp(acqData.getAcqTime().getTime())))
							.append("DAY_TOTAL_MILEAGE", acqData.getDayTotalMileage())
							.append("TOTAL_MILEAGE", acqData.getTotalMileage())
							.append("ALARM_STATUS", acqData.getAlarmStatus())
							.append("LONGITUDE", acqData.getLongitude())
							.append("LATITUDE", acqData.getLatitude())
							.append("SIGNAL_TYPE", acqData.getSignalType())
							.append("SPEED", acqData.getSpeed())
							.append("TACK", acqData.getTack())
							.append("SIGNAL_STRENGTH", acqData.getSignalStrength())
							.append("CPU_TEMPRETURE", acqData.getCpuTempreture())
							.append("LIBV", acqData.getLibv())
							.append("PVBV", acqData.getPvbv())
							.append("CREATE_TIME", format.format(new Timestamp(acqData.getCreateTime().getTime())))
							.append("UPDATE_TIME", format.format(new Timestamp(acqData.getUpdateTime().getTime())))
							.append("DEL_FLAG", acqData.getDelFlag() != null?acqData.getDelFlag():0)
							.append("DATA_TYPE", (acqData.getDataType() != null && acqData.getDataType() == 2)?2:1);
					documents.add(document);

//					logger.info("更新采集数据addBatch(" + acqData.getDeviceNo() + ")成功");
				} catch (Exception e) {
					logger.info("更新采集数据addBatch(" + acqData.getDeviceNo() + "=====" + tableSuffix + e.getMessage());
				}
			}

			insPs.addBatch(insSqlSb.toString());
			int[] results = insPs.executeBatch();
			conn.commit();
			int tempResult2 = Constants.SQL_FAIL;
			for (int i : results) {
				tempResult2 += i;
			}
			if (tempResult2 > 0) {
				result = Constants.SQL_SUCCESS;
			}
			insPs.close();

			collection.insertMany(documents);

			// logger.info("更新数据库实时数据成功");
		} catch (Exception e) {
			logger.error("SQL(更新数据库实时数据)执行异常: {}", e.toString());

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
					logger.error("conn关闭异常: {}", e.toString());
				}
			}

		}
		return result;
	}

	/**
	 * 创建采集数据历史表
	 */
	public int createAcqDataHistory() {
		int result = Constants.SQL_FAIL;

		Connection conn = JdbcUtil.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		try {
			String dropSqlSuffix = "DROP TABLE T_ACQ_DATA_";
			String createSqlSuffix = "CREATE TABLE T_ACQ_DATA_";
			StringBuilder createSqlSb = new StringBuilder();
			createSqlSb.append("( ACQ_DATA_ID bigint not null auto_increment comment '采集数据标识',");
			createSqlSb.append("DEVICE_ID bigint not null comment '设备标识, (对应设备信息表DEVICE_ID字段)',");
			createSqlSb.append("DEVICE_NO char(8) not null comment '设备编码, 8位数字(冗余字段，对应设备信息表DEVICE_NO字段)',");
			createSqlSb.append("SHIP_ID bigint comment '渔船标识, (对应渔船信息表SHIP_ID字段)',");
			createSqlSb.append("IOF_FLAG int comment '进出港标志, (冗余字段，对应进出港日志表IOF_FLAG字段)',");
			createSqlSb.append("ACQ_TIME datetime not null comment '数据采集时间',");
			createSqlSb.append(
					"DAY_TOTAL_MILEAGE double(12,5) default 0 not null comment '当日累计里程, 单位:海里 1海里=1.852公里(千米)',");
			createSqlSb.append("TOTAL_MILEAGE double(12,5) default 0 not null comment '累计里程, 单位:海里 1海里=1.852公里(千米)',");
			createSqlSb.append("ALARM_STATUS varchar(100) comment '报警状态, (冗余字段，所有报警状态二进制信息)',");
			createSqlSb.append("LONGITUDE double(10,7) not null comment '经度, 单位:度, GPRS格式:ddd.ddddddd, 负号表示西经(W)',");
			createSqlSb.append("LATITUDE double(9,7) not null comment '纬度, 单位:度, 格式:dd.ddddddd, 负号表示北纬(N)',");
			createSqlSb.append("SIGNAL_TYPE int comment '信号类别, 1:GPS北斗 2:LBS (对应字典表CODE:SIGNAL_TYPE)',");
			createSqlSb.append("SPEED float(10,1) comment '航速, 单位:节',");
			createSqlSb.append("TACK float(10,1) comment '航向, 单位:度(北偏东度数)',");
			createSqlSb.append("SIGNAL_STRENGTH int comment 'GPRS信号强度',");
			createSqlSb.append("CPU_TEMPRETURE int comment 'CPU温度, 单位:摄氏度',");
			createSqlSb.append("LIBV float(10,2) not null comment '锂电池电压, 单位:伏特',");
			createSqlSb.append("PVBV float(10,2) comment '光伏电池电压, 单位:伏特',");
			createSqlSb.append("CREATE_TIME datetime default NOW() not null comment '创建时间',");
			createSqlSb.append("UPDATE_TIME datetime default NOW() not null comment '更新时间',");
			createSqlSb.append("DEL_FLAG tinyint(1) default 0 not null comment '删除标志, 0:正常 1:删除',");
			createSqlSb.append("PRIMARY KEY(ACQ_DATA_ID)");
			createSqlSb.append(") engine = InnoDB default charset = utf8 comment = '采集数据表';");
			Statement stmt = conn.createStatement();
			for (int i = 0; i < 5000; i++) {
				String dropSql = dropSqlSuffix + String.valueOf(i) + ";";
				stmt.addBatch(dropSql);
			}
			for (int i = 0; i < 5000; i++) {
				String createSql = createSqlSuffix + String.valueOf(i) + createSqlSb;
				stmt.addBatch(createSql);
			}
			stmt.executeBatch();
			conn.commit();
			stmt.close();
			logger.info("创建采集数据历史表");
		} catch (SQLException e) {
			logger.error("SQL(创建采集数据历史表)执行异常: {}", e.toString());
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
	 * 创建采集数据历史表索引表
	 */
	public int insertIndextblData(int startNo) {
		int result = Constants.SQL_FAIL;

		Connection conn = JdbcUtil.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		try {
			Statement stmt = conn.createStatement();
			for (int i = 0; i < 2000; i++) {
				String sql = "INSERT INTO t_acqtbl_index(index_id,device_no,table_suff) VALUES ('" + (i + 1) + "', '"
						+ (startNo + i) + "', '" + i + "');";
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			conn.commit();
			stmt.close();
			logger.info("创建采集数据历史表索引表");
		} catch (SQLException e) {
			logger.error("SQL(创建采集数据历史表索引表)执行异常: {}", e.toString());
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
