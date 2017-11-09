package com.cetcme.springBootDemo.message;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.Map.Entry;

import com.cetcme.springBootDemo.utils.LocalCacheUtil;
import com.cetcme.springBootDemo.utils.RedissonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cetcme.springBootDemo.dao.DeviceDao;
import com.cetcme.springBootDemo.domain.AcqData;
import com.cetcme.springBootDemo.domain.Alarm;
import com.cetcme.springBootDemo.domain.DeviceExtend;
//import com.cetcme.springBootDemo.utils.CacheUtil;
//import com.cetcme.springBootDemo.utils.LocalCacheUtil;
import com.cetcme.springBootDemo.utils.RedissonUtil.RedisKey;

import io.netty.channel.ChannelHandlerContext;


public class DeviceStatusProcessor {

	Timer timer;

	public DeviceStatusProcessor(int interval) {

		int period = interval;
		if (interval == 0) {
			period = 1;
		}

		this.timer = new Timer();

		timer.schedule(new CheckTask(), 0, period * 1000);
	}

	Logger logger = LoggerFactory.getLogger(DeviceStatusProcessor.class);

	class CheckTask extends TimerTask {

		@Override
		public void run() {
			try {
//				long start = System.currentTimeMillis();

				Map<String, Object> deviceMap = RedissonUtil.redisson.getMap(RedisKey.DEVICE_INFO_CACHE.toString());
				Long offlineTime = (Long) RedissonUtil.redisson.getMap(RedisKey.SYS_CONFIG_CACHE.toString()).get("offlineDelay");
				Long offlinealarmDelay = (Long)  RedissonUtil.redisson.getMap(RedisKey.SYS_CONFIG_CACHE.toString()).get("offlinealarmDelay");
				DeviceDao deviceDao = new DeviceDao();

				if (deviceMap.size() > 0) {
					for (Entry<String, Object> e : deviceMap.entrySet()) {
						String deviceNo = e.getKey();
						DeviceExtend device = (DeviceExtend) e.getValue();

						AcqData acqData = (AcqData) RedissonUtil.redisson.getMap(RedisKey.PREV_ACQDATA_CACHE.toString()).get(deviceNo);
						boolean hasDeviceStatusChange = false;
						if (acqData == null) {
							// 离线判断
							if (!device.getOfflineFlag()) {
								device.setOfflineFlag(true);
								deviceDao.setDeviceOffline(deviceNo);
//									hasDeviceStatusChange = true;
								logger.info("设备({})acqData为空下线", deviceNo);
							}

//								if (hasDeviceStatusChange) {
//									CacheUtil.put(CacheType.DEVICE_INFO_CACHE, deviceNo, device);
//								}
							continue;

						} else {
							Date nowDate = new Date();
							if (acqData.getUpdateTime() == null) {
								continue;
							}
							Long delayTime = nowDate.getTime() - acqData.getUpdateTime().getTime();

							// 离线判断
							/*if (!device.getOfflineFlag()) {
								if (delayTime > offlineTime) {
									device.setOfflineFlag(true);
									deviceDao.setDeviceOffline(deviceNo);

									try{
										ChannelHandlerContext ctf = (ChannelHandlerContext) RedissonUtil.redisson.getMap(CacheType.DATAGRAM_PACKET_CACHE, deviceNo);
										if(ctf != null){
											String host = ((InetSocketAddress) ctf.channel().remoteAddress()).getAddress().getHostAddress();
											if(acqData.getDataType() != null && acqData.getDataType() == 1){
												ctf.close();
												CacheUtil.remove(CacheType.IP_ADDRESS_CACHE, host);
												CacheUtil.remove(CacheType.DATAGRAM_PACKET_CACHE, deviceNo);
											}else if(acqData.getDataType() == null){
												ctf.close();
												CacheUtil.remove(CacheType.IP_ADDRESS_CACHE, host);
												CacheUtil.remove(CacheType.DATAGRAM_PACKET_CACHE, deviceNo);
											}
//												logger.error("设备({})通道关闭成功", deviceNo);
										}else{
//											logger.error("设备({})通道未找到", deviceNo);
										}
									}catch(Exception e1){
										logger.error("设备({})通道关闭异常:{}", deviceNo, e1.toString());
									}
									hasDeviceStatusChange = true;
									logger.info("设备({})超时下线[delayTime({}) - offlineTime({})](updateTime:{})", deviceNo, delayTime, offlineTime, acqData.getUpdateTime().getTime());
								}
							}*/

							// 超时报警判断
							if (!device.getOuttimeFlag()) {
								if (delayTime > offlinealarmDelay && device.getFenceId() != null && device.getFenceId() > 0) {
									device.setOuttimeFlag(true);
									deviceDao.setDeviceOuttime(deviceNo);

									Alarm alarm = new Alarm();
									String alarmNo = UUID.randomUUID().toString().replaceAll("-", "");
									alarm.setAlarmNo(alarmNo);
									alarm.setDeviceId(device.getDeviceId());
									alarm.setDeviceNo(device.getDeviceNo());
									alarm.setShipId(device.getShipId());
									alarm.setAlarmType(12);
									alarm.setSolveFlag(false);
									alarm.setReportTime(new Date());
									LocalCacheUtil.addAlarm(alarm);
									hasDeviceStatusChange = true;
								}
							}

							if (hasDeviceStatusChange) {
								RedissonUtil.redisson.getMap(RedisKey.DEVICE_INFO_CACHE.toString()).put(deviceNo, device);
							}
						}
					}
				}

//				long end = System.currentTimeMillis();
//				logger.info("设备在线及超时报警检测花了{}毫秒", end - start);
			} catch (Exception e) {
				logger.error("{}", e.toString());
			}
		}
	}
}
