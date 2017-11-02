package com.cetcme.springBootDemo.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cetcme.springBootDemo.domain.Ship;
import com.cetcme.springBootDemo.domain.ShipExample;
import com.cetcme.springBootDemo.domain.ShipExample.Criteria;
import com.cetcme.springBootDemo.mapper.ShipMapper;
import com.cetcme.springBootDemo.utils.MybatisUtil;

public class ShipDao {

	Logger logger = LoggerFactory.getLogger(ShipDao.class);
	
	/**
	 * 根据渔船编码获取对应的渔船信息
	 */
	public Ship getByShipId(Long shipId) {
		Ship result = null;

		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			ShipMapper mapper = session.getMapper(ShipMapper.class);
			result = mapper.selectByPrimaryKey(shipId);
			session.commit(true);
			logger.info("根据渔船编码获取对应的渔船信息(" + shipId + ")成功");
		} catch (Exception e) {
			logger.error("SQL(根据渔船编码获取对应的渔船信息(" + shipId + "): {}", e.toString());
		} finally {
			session.close();
		}
		return result;
	}

	/**
	 * 根据设备编码获取对应的渔船信息
	 */
	public Ship getByDeviceId(Long deviceId) {
		Ship result = null;

		SqlSession session = MybatisUtil.getFactory().openSession();
		try {
			ShipMapper mapper = session.getMapper(ShipMapper.class);
			ShipExample example = new ShipExample();
			Criteria criteria = example.createCriteria();
			criteria.andDeviceIdEqualTo(deviceId);
			criteria.andDelFlagEqualTo(false);
			List<Ship> shipList = mapper.selectByExample(example);
			if (shipList != null && shipList.size() > 0) {
				result = shipList.get(0);
			}
			session.commit(true);
			logger.info("根据设备编码获取对应的渔船信息(" + deviceId + ")成功");
		} catch (Exception e) {
			logger.error("SQL(根据设备编码获取对应的渔船信息(" + deviceId + "): {}", e.toString());
		} finally {
			session.close();
		}
		return result;
	}
}
