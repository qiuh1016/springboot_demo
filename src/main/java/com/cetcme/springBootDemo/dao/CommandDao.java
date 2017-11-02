package com.cetcme.springBootDemo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cetcme.springBootDemo.domain.Command;
import com.cetcme.springBootDemo.domain.CommandExample;
import com.cetcme.springBootDemo.domain.CommandExample.Criteria;
import com.cetcme.springBootDemo.domain.Device;
import com.cetcme.springBootDemo.domain.DeviceExample;
import com.cetcme.springBootDemo.mapper.CommandMapper;
import com.cetcme.springBootDemo.mapper.DeviceMapper;
import com.cetcme.springBootDemo.utils.Constants;
import com.cetcme.springBootDemo.utils.Constants.CommandStatus;
import com.cetcme.springBootDemo.utils.Constants.CommandType;
import com.cetcme.springBootDemo.utils.JdbcUtil;
import com.cetcme.springBootDemo.utils.MybatisUtil;

public class CommandDao {

	Logger logger = LoggerFactory.getLogger(CommandDao.class);

	public int getSendingCommandCount(Long deviceId, CommandType commandType) {
		int result = 0;
		if (deviceId == null) {
			return result;
		}
		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			CommandMapper mapper = session.getMapper(CommandMapper.class);
			CommandExample example = new CommandExample();
			Criteria criteria = example.createCriteria();
			criteria.andDeviceIdEqualTo(deviceId);
			criteria.andCommandTypeEqualTo(commandType.getValue());
			criteria.andCommandStatusEqualTo(CommandStatus.SENDING.getValue());
			criteria.andDelFlagEqualTo(false);
			result = mapper.countByExample(example);
			session.commit(true);
			logger.info("getSendingCommandCount(" + deviceId + "," + commandType + ")成功");
		} catch (Exception e) {
			logger.error("SQL(getSendingCommandCount)执行异常(" + deviceId + "," + commandType + "): {}", e.toString());
			session.rollback(true);
		} finally {
			session.close();
		}
		return result;
	}

	public List<Command> getWaitToSendCommand(Long deviceId, String deviceNo) {
		List<Command> result = null;
		if (deviceNo == null || deviceId == null) {
			return result;
		}
		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			CommandMapper mapper = session.getMapper(CommandMapper.class);
			CommandExample example = new CommandExample();
			Criteria criteria = example.createCriteria();
			criteria.andDeviceIdEqualTo(deviceId);
			criteria.andDeviceNoEqualTo(deviceNo);
			criteria.andCommandStatusEqualTo(CommandStatus.WAITING_TO_SEND.getValue());
			criteria.andDelFlagEqualTo(false);
			result = mapper.selectByExample(example);
			session.commit(true);
			logger.info("getWaitToSendCommand(" + deviceNo + ")成功");
		} catch (Exception e) {
			logger.error("SQL(getWaitToSendCommand)执行异常(" + deviceNo + "): {}", e.toString());
			session.rollback(true);
		} finally {
			session.close();
		}
		return result;
	}

	public List<Command> getAllWaitToSendCommands() {
		List<Command> result = new ArrayList<Command>();

		Connection conn = JdbcUtil.getConnection();
		PreparedStatement ps;

		try {
			// 从业务角度，同一个设备同一种报警只有一条未发送指令，但为了避免脏数据的影响，还是按时间排序，取最后的指令下发
			String sql = "select command_id,device_id, device_no, command_content,command_status,command_type from "
					+ " (select * from t_command where del_flag = 0 and command_status = 0 order by device_no, command_type, create_time desc) t1 "
					+ " group by device_no, command_type";

			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Command command = new Command();
				command.setCommandId(rs.getLong(1));
				command.setDeviceId(rs.getLong(2));
				command.setDeviceNo(rs.getString(3));
				command.setCommandContent(rs.getString(4));
				command.setCommandStatus(rs.getInt(5));
				command.setCommandType(rs.getInt(6));
				result.add(command);
			}
			rs.close();
			ps.close();
			logger.info("getAllWaitToSendCommands成功");
		} catch (SQLException e) {
			logger.error("SQL(getAllWaitToSendCommands)执行异常: {}", e.toString());
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
	 * 获取未发送或者发送中的指令数量
	 */
	public int cancelNotFinished(Long deviceId, CommandType commandType, Long userId) {
		int result = 0;
		if (deviceId == null) {
			return result;
		}

		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			CommandMapper mapper = session.getMapper(CommandMapper.class);
			CommandExample example = new CommandExample();
			Criteria criteria = example.createCriteria();
			criteria.andDeviceIdEqualTo(deviceId);
			criteria.andCommandTypeEqualTo(commandType.getValue());
			List<Integer> commandStatusList = new ArrayList<Integer>(
					Arrays.asList(CommandStatus.WAITING_TO_SEND.getValue(), CommandStatus.SENDING.getValue()));
			criteria.andCommandStatusIn(commandStatusList);
			criteria.andDelFlagEqualTo(false);
			Command command = new Command();
			command.setCommandStatus(CommandStatus.CANCEL_SEND.getValue());
			command.setUpdateUserId(userId);
			command.setUpdateTime(new Date());
			result = mapper.updateByExampleSelective(command, example);
			session.commit(true);
			logger.info("获取未发送或者发送中的指令数量(" + deviceId + "," + commandType + ")成功");
		} catch (Exception e) {
			logger.error("SQL(获取未发送或者发送中的指令数量)执行异常(" + deviceId + "," + commandType + "): {}", e.toString());
			session.rollback(true);
		} finally {
			session.close();
		}
		return result;
	}

	/**
	 * 下发指令成功
	 */
	public int setSucceedStatus(Long deviceId, CommandType commandType) {
		return update(deviceId, commandType, CommandStatus.SEND_SUCCEED);
	}

	/**
	 * 下发指令失败
	 */
	public int setFailedStatus(Long deviceId, CommandType commandType) {
		return update(deviceId, commandType, CommandStatus.SEND_FAILED);
	}

	public int setSendingStatus(Long deviceId, String deviceNo) {
		int result = Constants.SQL_FAIL;
		if (deviceNo == null || deviceId == null) {
			return result;
		}

		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			CommandMapper mapper = session.getMapper(CommandMapper.class);
			CommandExample example = new CommandExample();
			Criteria criteria = example.createCriteria();
			criteria.andDeviceIdEqualTo(deviceId);
			criteria.andDeviceNoEqualTo(deviceNo);
			criteria.andCommandStatusEqualTo(CommandStatus.WAITING_TO_SEND.getValue());
			criteria.andDelFlagEqualTo(false);
			Command command = new Command();
			command.setCommandStatus(CommandStatus.SENDING.getValue());
			command.setUpdateTime(new Date());
			command.setUpdateUserId(Constants.SYSTEM_USER_ID);
			result = mapper.updateByExampleSelective(command, example);
			session.commit(true);
		} catch (Exception e) {
			session.rollback(true);
			logger.error("SQL执行异常: {}", e.toString());
		} finally {
			session.close();
		}
		return result;
	}

	/**
	 * 更新下发指令状态
	 */
	public synchronized int update(Long deviceId, CommandType commandType, CommandStatus commandStatus) {
		int result = Constants.SQL_FAIL;
		if (deviceId == null) {
			return result;
		}

		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			Date dateNow = new Date();
			CommandMapper mapper = session.getMapper(CommandMapper.class);
			CommandExample example = new CommandExample();
			Criteria criteria = example.createCriteria();
			criteria.andDeviceIdEqualTo(deviceId);
			criteria.andCommandTypeEqualTo(commandType.getValue());
			criteria.andCommandStatusEqualTo(CommandStatus.SENDING.getValue());
			Command command = new Command();
			command.setCommandStatus(commandStatus.getValue());
			command.setUpdateTime(dateNow);
			command.setUpdateUserId(Constants.SYSTEM_USER_ID);
			result = mapper.updateByExampleSelective(command, example);
			
			if(commandType.getValue() == 3 && commandStatus.getValue() == 2){
				DeviceMapper mapperDevice = session.getMapper(DeviceMapper.class);
				DeviceExample exampleDevice = new DeviceExample();
				Criteria criteriaDevice = example.createCriteria();
				criteriaDevice.andDeviceIdEqualTo(deviceId);
				Device device = new Device();
				device.setBootUpdateTime(dateNow);
				mapperDevice.updateByExampleSelective(device, exampleDevice);
			}
			
			session.commit(true);
		} catch (Exception e) {
			session.rollback(true);
			logger.error("SQL执行异常: {}", e.toString());
		} finally {
			session.close();
		}
		return result;
	}

	public Long add(Command command) {
		Long result = null;
		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			CommandMapper mapper = session.getMapper(CommandMapper.class);
			int ret = mapper.insertSelective(command);
			if (ret == Constants.SQL_SUCCESS) {
				result = command.getCommandId();
			}
			session.commit(true);
		} catch (Exception e) {
			session.rollback(true);
			logger.error("SQL执行异常: {}", e.toString());
		} finally {
			session.close();
		}
		return result;
	}

	/**
	 * 批量新增下发指令数据
	 */
	public int addBatch(List<Command> commandList) {
		int result = Constants.SQL_FAIL;
		if (commandList == null || commandList.size() == 0) {
			return result;
		}

		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			CommandMapper mapper = session.getMapper(CommandMapper.class);
			result = mapper.insertBatch(commandList);
			session.commit(true);
		} catch (Exception e) {
			session.rollback(true);
			logger.error("SQL执行异常: {}", e);
		} finally {
			session.close();
		}
		return result;
	}

}
