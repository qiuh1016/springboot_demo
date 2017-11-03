package com.cetcme.springBootDemo.utils;

import com.cetcme.springBootDemo.domain.AcqData;
import com.cetcme.springBootDemo.domain.DeviceExtend;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by qiuhong on 02/11/2017.
 */
public class RedissonUtil {

    public static Logger logger = LoggerFactory.getLogger(RedissonUtil.class);

    RedissonClient redisson;

    public RedissonUtil() {
        Config config = new Config();
        config
            .useSingleServer()
            .setAddress("redis://localhost:6379")
            .setDatabase(0);
        redisson = Redisson.create(config);
    }

    /**
     * 清空数据库
     */
    public void removeAll() {
        redisson.getKeys().flushall();
    }


    /**
     * 设置设备信息列表
     * @param prevAcqData
     * @return
     */
    public String setPrevAcqData(List<AcqData> prevAcqData) {
        List<AcqData> list = redisson.getList(RedisKey.PREV_ACQDATA_CACHE.toString());
        list.clear();
        list.addAll(prevAcqData);
        return "OK";
    }
    public List<AcqData> getPrevAcqData() {
        return redisson.getList(RedisKey.PREV_ACQDATA_CACHE.toString());
    }


    /**
     * 设置设备信息列表
     * @param deviceExtendList
     * @return
     */
    public String setDeviceList(List<DeviceExtend> deviceExtendList) {
        for (DeviceExtend d: deviceExtendList) {
//            logger.info(d.getPicName());

            Map<String, Object> map = redisson.getMap(RedisKey.DEVICE_INFO_CACHE + "_" + d.getDeviceId());
            if (d.getCfsEndDate()       != null) map.put("cfsEndDate"    , d.getCfsEndDate());
            if (d.getCfsStartDate()     != null) map.put("cfsStartDate"  , d.getCfsStartDate());
            if (d.getDeviceId()         != null) map.put("deviceId"      , d.getDeviceId());
            if (d.getDeviceNo()         != null) map.put("deviceNo"      , d.getDeviceNo());
            if (d.getIdcardreaderNo()   != null) map.put("idcardreaderNo", d.getIdcardreaderNo());
            if (d.getOfflineFlag()      != null) map.put("offlineFlag"   , d.getOfflineFlag());
            if (d.getOuttimeFlag()      != null) map.put("outtimeFlag"   , d.getOuttimeFlag());
            if (d.getPairFlag()         != null) map.put("pairFlag"      , d.getPairFlag());
            if (d.getPicName()          != null) map.put("picName"       , d.getPicName());
            if (d.getPicTelNo()         != null) map.put("picTelNo"      , d.getPicTelNo());
            if (d.getShipId()           != null) map.put("shipId"        , d.getShipId());
            if (d.getShipNo()           != null) map.put("shipNo"        , d.getShipNo());
        }

//        List<DeviceExtend> list = redisson.getList(RedisKey.DEVICE_INFO_CACHE.toString());
//        list.clear();
//        list.addAll(deviceExtendList);
        return "OK";
    }
    public Map<String, Object> getDeviceList(String ShipNo) {
        return redisson.getMap(RedisKey.DEVICE_INFO_CACHE + "_" + ShipNo);
    }

    public enum RedisKey {
        DEVICE_INFO_CACHE("DEVICE_INFO_CACHE"),
        PREV_ACQDATA_CACHE("PREV_ACQDATA_CACHE");

        private final String value;

        @Override
        public String toString() {
            return value;
        }

        RedisKey(String value) {
            this.value = value;
        }

        public static RedisKey getEnumByValue(String value) {
            for (RedisKey e : RedisKey.values()) {
                if (e.value.equals(value)) {
                    return e;
                }
            }
            return null;
        }
    }

}

