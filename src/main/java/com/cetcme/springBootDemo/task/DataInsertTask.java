package com.cetcme.springBootDemo.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cetcme.springBootDemo.dao.AcqDataDao;
import com.cetcme.springBootDemo.dao.AlarmDao;
import com.cetcme.springBootDemo.domain.AcqData;
import com.cetcme.springBootDemo.domain.Alarm;
import com.cetcme.springBootDemo.utils.LocalCacheUtil;

public class DataInsertTask extends TimerTask {

	private AlarmDao alarmDao = new AlarmDao();
	private AcqDataDao acqDataDao = new AcqDataDao();

	public static Logger logger = LoggerFactory.getLogger(DataInsertTask.class);

	public DataInsertTask() {
	}

	@Override
	public void run() {
		try {
//			logger.info("DataInsertTask开始");
			insertAcqData();

			insertAlarm();
		} catch (Exception e) {
			logger.error("异常:" + e.toString());
		}
	}

	private void insertAlarm() {
		List<Alarm> srcAlarmList = new ArrayList<Alarm>(LocalCacheUtil.getAlarmList());
		LocalCacheUtil.clearAlarmList();
		if (srcAlarmList != null && srcAlarmList.size() > 0) {
			long start2 = System.currentTimeMillis();
			List<Alarm> alarmList = new ArrayList<Alarm>();
			Collections.addAll(alarmList, new Alarm[srcAlarmList.size()]);
			Collections.copy(alarmList, srcAlarmList);
			alarmDao.addBatch(alarmList);
			alarmList.clear();
			long end2 = System.currentTimeMillis();
			logger.info("插入报警数据{}毫秒{}条", end2 - start2, srcAlarmList.size());
		}
	}

	private void insertAcqData() {
		ConcurrentHashMap<String, AcqData> srcAcqDataMap = new ConcurrentHashMap<String, AcqData>(LocalCacheUtil.getAcqDataMap());
		LocalCacheUtil.clearAcqDataMap();
		// List深拷贝
		if (srcAcqDataMap != null && srcAcqDataMap.size() > 0) {
			long start1 = System.currentTimeMillis();
			List<AcqData> acqDataList = new ArrayList<AcqData>();
			Iterator<String> it = srcAcqDataMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next().toString();
				acqDataList.add(srcAcqDataMap.get(key));
			}

			acqDataDao.updateBatch(acqDataList);
			acqDataList.clear();
			long end1 = System.currentTimeMillis();
			logger.info("插入实时数据{}毫秒{}条", end1 - start1, srcAcqDataMap.size());
		}

	}
}
