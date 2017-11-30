package com.cetcme.springBootDemo.task;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.cetcme.springBootDemo.utils.Constants;
import com.cetcme.springBootDemo.utils.DateUtil;
import com.cetcme.springBootDemo.utils.MongodbUtil;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
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

            insertHistoryAcqData();
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

    private void insertHistoryAcqData() {
        List<AcqData> srcHistoryAcqDataList = new ArrayList<AcqData>(LocalCacheUtil.getHistoryAcqDataList());
        LocalCacheUtil.clearHistoryAcqDataList();
        if (srcHistoryAcqDataList != null && srcHistoryAcqDataList.size() > 0) {
            long start2 = System.currentTimeMillis();
            MongoCollection<Document> collection = MongodbUtil.getMongoDBDaoImpl().GetCollection();
            List<Document> documents = new ArrayList<>();

            for (int i = 0; i < srcHistoryAcqDataList.size(); i++) {
                AcqData acqData = srcHistoryAcqDataList.get(i);
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
            }

            collection.insertMany(documents);

            long end2 = System.currentTimeMillis();
            logger.info("插入历史AcqData: {}毫秒{}条", end2 - start2, srcHistoryAcqDataList.size());
        }
    }
}
