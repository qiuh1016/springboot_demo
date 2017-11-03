package com.cetcme.springBootDemo.service;

import com.cetcme.springBootDemo.dao.AcqDataDao;
import com.cetcme.springBootDemo.dao.DeviceDao;
import com.cetcme.springBootDemo.domain.AcqData;
import com.cetcme.springBootDemo.domain.DeviceExtend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.cetcme.springBootDemo.App.redissonUtil;

/**
 * Created by qiuhong on 03/11/2017.
 */
public class CacheService {

    public static Logger logger = LoggerFactory.getLogger(CacheService.class);

    public void loadCache() {
        redissonUtil.removeAll();
        loadPrevAcqData();
        loadDeviceInfo();
    }

    /**
     * 从数据库读入实时采集数据(对新上传的数据而言，就是上一条数据)
     */
    public void loadPrevAcqData() {
		long start = System.currentTimeMillis();
        AcqDataDao dao = new AcqDataDao();
        List<AcqData> prevAcqDatas = dao.getAll();
        if (prevAcqDatas == null || prevAcqDatas.size() == 0) {
            return;
        }

        redissonUtil.setPrevAcqData(prevAcqDatas);

		long end = System.currentTimeMillis();
		logger.info("导入前次实时数据缓存花了{}毫秒", end - start);
    }

    public void loadDeviceInfo() {
        long start = System.currentTimeMillis();
        DeviceDao dao = new DeviceDao();
        List<DeviceExtend> deviceList = dao.getAll();
        if (deviceList == null || deviceList.size() == 0) {
            return;
        }

        redissonUtil.setDeviceList(deviceList);

        long end = System.currentTimeMillis();
        logger.info("导入设备信息缓存花了{}毫秒", end - start);
    }
}
