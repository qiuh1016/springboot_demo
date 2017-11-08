package com.cetcme.springBootDemo.service;

import com.cetcme.springBootDemo.dao.*;
import com.cetcme.springBootDemo.domain.*;
import com.cetcme.springBootDemo.utils.CommonUtil;
import com.cetcme.springBootDemo.utils.Constants;
import com.cetcme.springBootDemo.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cetcme.springBootDemo.App.redissonUtil;

/**
 * Created by qiuhong on 03/11/2017.
 */
public class CacheService {

    public static Logger logger = LoggerFactory.getLogger(CacheService.class);
    public static int loadCount = 0;

    public void loadCache() {
//        redissonUtil.removeAll();

        loadPrevAcqData();
        loadSysConfig();
        loadDict();
        loadDeviceInfo();
        loadWaitToSendCommands();
        loadFenceInfo();
        loadAcqDataHistoryTblIndex();
        loadJkxxAlarmPerm();
        loadDeviceRuleUserId();
        loadCordonUser();
        loadCordonList();
        loadCordonDevice();
        loadAlarmCordon();
        loadCount++;
    }

    public void reloadCache() {
        //半小时更新一次
        if(loadCount % 15 == 0){
            loadDeviceRuleUserId();
            loadSysConfig();
        }
        //一小时更新一次
        if(loadCount % 30 == 0){
            loadDict();
        }
        loadDeviceInfo();
        loadWaitToSendCommands();
        //20分钟更新一次
        if(loadCount % 10 == 0){
            loadCordonUser();
            loadCordonList();
            loadCordonDevice();
            loadFenceInfo();
        }
        loadAcqDataHistoryTblIndex();
        //一小时更新一次
        if(loadCount % 30 == 0){
            loadJkxxAlarmPerm();
        }
        if(loadCount == 30){
            loadCount = 0;
        }
        loadCount++;
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

        redissonUtil.loadPrevAcqData(prevAcqDatas);

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

        redissonUtil.loadDeviceInfo(deviceList);

        long end = System.currentTimeMillis();
        logger.info("导入设备信息缓存花了{}毫秒", end - start);
    }

    public void loadSysConfig() {
		long start = System.currentTimeMillis();
        SysConfigDao dao = new SysConfigDao();
        List<SysConfig> sysConfigs = dao.getAll();
        if (sysConfigs == null || sysConfigs.size() == 0) {
            return;
        }

        redissonUtil.loadSysConfig(sysConfigs);

		long end = System.currentTimeMillis();
		logger.info("导入系统配置信息缓存花了{}毫秒", end - start);
    }

    /**
     * 从数据库读入字典信息
     */
    public void loadDict() {
		long start = System.currentTimeMillis();
        DictDao dao = new DictDao();
        List<Dict> dictList = dao.getAll();
        if (dictList == null || dictList.size() == 0) {
            return;
        }
        redissonUtil.loadDict(dictList);

		long end = System.currentTimeMillis();
		logger.info("导入字典信息到缓存花了{}毫秒", end - start);
    }

    /**
     * 从数据库读入待下发的指令
     */
    public void loadWaitToSendCommands() {
        long start = System.currentTimeMillis();
        CommandDao dao = new CommandDao();
        List<Command> waitToSendCommandList = dao.getAllWaitToSendCommands();
        if (waitToSendCommandList == null || waitToSendCommandList.size() == 0) {
            return;
        }

        redissonUtil.loadWaitToSendCommands(waitToSendCommandList);

        long end = System.currentTimeMillis();
		logger.info("导入系统待下发指令缓存花了{}毫秒", end - start);
    }

    /**
     * 从数据库中读入港口信息
     */
    public void loadFenceInfo() {
		long start = System.currentTimeMillis();
        FenceDao dao = new FenceDao();
        List<FenceExtend> fenceList = dao.getAll();
        if (fenceList == null || fenceList.size() == 0) {
            return;
        }

        redissonUtil.loadFenceInfo(fenceList);

		long end = System.currentTimeMillis();
		logger.info("导入港口数据缓存花了{}毫秒", end - start);
    }

    /**
     * 从数据库中读入历史数据表索引 信息
     */
    public void loadAcqDataHistoryTblIndex() {
		long start = System.currentTimeMillis();
        AcqTblIndexDao dao = new AcqTblIndexDao();
        List<AcqTblIndex> acqTblIndexList = dao.getAll();
        if (acqTblIndexList == null || acqTblIndexList.size() == 0) {
            return;
        }

        redissonUtil.loadAcqDataHistoryTblIndex(acqTblIndexList);

		long end = System.currentTimeMillis();
		logger.info("导入历史数据表索引缓存花了{}毫秒", end - start);
    }

    /**
     * 从数据库中读入嘉科信息报警权限信息
     */
    public void loadJkxxAlarmPerm() {
		long start = System.currentTimeMillis();
        AlarmDao dao = new AlarmDao();
        List<Integer> alarmPermList = dao.getJkxxAlarmType();
        if (alarmPermList == null || alarmPermList.size() == 0) {
            return;
        }

        redissonUtil.loadJkxxAlarmPerm(alarmPermList);

		long end = System.currentTimeMillis();
		logger.info("导入历史数据表索引缓存花了{}毫秒", end - start);
    }

    /**
     * 获取所有设备权限用户Id
     */
    public void loadDeviceRuleUserId() {
		long start = System.currentTimeMillis();
        DeviceDao dao = new DeviceDao();
        Map<String, String> ruleMap = dao.deviceRuleUserId();
        if (ruleMap.size() == 0) {
            return;
        }

        redissonUtil.loadDeviceRuleUserId(ruleMap);

		long end = System.currentTimeMillis();
		logger.info("导入历史数据表索引缓存花了{}毫秒", end - start);
    }

    /**
     * 获取所有围栏坐标
     */
    public void loadCordonList() {
		long start = System.currentTimeMillis();
        CordonDao dao = new CordonDao();
        Map<String, List<GpsPosition>> cordonMap = dao.getCordonList();
        if (cordonMap.size() == 0) {
            return;
        }

        redissonUtil.loadCordonList(cordonMap);

		long end = System.currentTimeMillis();
		logger.info("导入历史数据表索引缓存花了{}毫秒", end - start);
    }

    /**
     * 获取围栏与用户_围栏报警类型的关系
     */
    public void loadCordonUser() {
		long start = System.currentTimeMillis();
        CordonDao dao = new CordonDao();
        Map<String, String> cordonMap = dao.getCordonUserList();
        if (cordonMap.size() == 0) {
            return;
        }

        redissonUtil.loadCordonUser(cordonMap);

		long end = System.currentTimeMillis();
		logger.info("导入历史数据表索引缓存花了{}毫秒", end - start);
    }

    /**
     * 获取围栏与设备的关系
     */
    public void loadCordonDevice() {
		long start = System.currentTimeMillis();
        CordonDao dao = new CordonDao();
        Map<String, String> cordonMap = dao.getCordonDeviceList();
        if (cordonMap.size() == 0) {
            return;
        }

        redissonUtil.loadCordonDevice(cordonMap);

		long end = System.currentTimeMillis();
		logger.info("导入历史数据表索引缓存花了{}毫秒", end - start);
    }

    /**
     * 获取进出围栏报警未解除部分
     */
    public void loadAlarmCordon() {
        long start = System.currentTimeMillis();

        CordonDao dao = new CordonDao();
        Map<String, String> cordonInMap = dao.getCordonIN();
        if (cordonInMap.size() == 0) {
            return;
        }

        redissonUtil.loadAlarmInCordon(cordonInMap);

        Map<String, String> cordonOutMap = dao.getCordonOUT();
        if (cordonOutMap.size() == 0) {
            return;
        }

        redissonUtil.loadAlarmOutCordon(cordonOutMap);

        long end = System.currentTimeMillis();
        logger.info("导入设备信息缓存花了{}毫秒", end - start);
    }
}
