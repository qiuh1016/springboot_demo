package com.cetcme.springBootDemo.utils;

import com.cetcme.springBootDemo.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by qiuhong on 02/11/2017.
 */
public class RedissonUtil {

    public static Logger logger = LoggerFactory.getLogger(RedissonUtil.class);

    public static RedissonClient redisson;

    public RedissonUtil() {
        Config config = new Config();
        config
            .useSingleServer()
            .setAddress("redis://127.0.0.1:6379")
//            .setAddress("redis://61.164.208.174:3350")
//            .setPassword("foobared")
//            .setConnectionMinimumIdleSize(1)
//            .setConnectionPoolSize(8)
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
     * 从数据库读入实时采集数据(对新上传的数据而言，就是上一条数据)
     * @param prevAcqData
     * @return
     */
    public void loadPrevAcqData(List<AcqData> prevAcqData) {
//        Map<String, Object> map = redisson.getMap(RedisKey.PREV_ACQDATA_CACHE.toString());
//        for (AcqData d: prevAcqData) {
//            map.put(d.getDeviceNo(), d);
//        }

        Map<String, Object> map = new HashMap<String, Object>();
        for (AcqData d: prevAcqData) {
            map.put(d.getDeviceNo(), d);
        }

        redisson.getMap(RedisKey.PREV_ACQDATA_CACHE.toString()).putAll(map);
    }
    public List<AcqData> getPrevAcqData() {
        return redisson.getList(RedisKey.PREV_ACQDATA_CACHE.toString());
    }

    /**
     * 设置设备信息列表
     * @param deviceExtendList
     * @return
     */
    public void loadDeviceInfo(List<DeviceExtend> deviceExtendList) {
//        Map<String, Object> map = redisson.getMap(RedisKey.DEVICE_INFO_CACHE.toString());

        Map<String, Object> map = new HashMap<String, Object>();
        for (DeviceExtend d: deviceExtendList) {
            map.put(d.getDeviceNo(), d);
        }

        redisson.getMap(RedisKey.DEVICE_INFO_CACHE.toString()).putAll(map);
        // todo: 需要换成RBucket
    }

    public DeviceExtend getDeviceInfo(String deviceNo) {
        Map<String, Object> map = redisson.getMap(RedisKey.DEVICE_INFO_CACHE.toString());
        DeviceExtend deviceExtend = (DeviceExtend) map.get(deviceNo);
        return deviceExtend;
    }

    public void loadSysConfig(List<SysConfig> sysConfigs) {
        Map<String, Object> map = redisson.getMap(RedisKey.SYS_CONFIG_CACHE.toString());
        for (SysConfig sysConfig : sysConfigs) {
            // 时间单位全部转为毫秒
            String key = sysConfig.getParamCode();

            String originValue = sysConfig.getParamValue();

            String unit = sysConfig.getUnit();
            Constants.DateUnitType unitType = Constants.DateUnitType.getEnumByValue(unit);
            // 如果是时间单位，统一转化为毫秒 毫秒都是整数 所以强制转为整数
            if (unitType != null) {
                Double doubleValue = DateUtil.convertToMiliSecond(unitType, originValue);
                map.put(key, doubleValue.longValue());
                continue;
            }

            if (CommonUtil.isInteger(originValue)) {
                Integer intValue = Integer.valueOf(originValue);
                map.put(key, intValue);
                continue;
            }

            if (CommonUtil.isDouble(originValue)) {
                Double doubleValue = Double.valueOf(originValue);
                map.put(key, doubleValue);
                continue;
            }
            map.put(key, originValue);
        }
    }

    public void loadDict(List<Dict> dictList) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Dict dict : dictList) {
            String key = String.format("%s,%s", dict.getDictType(), dict.getDictCode());
            map.put(key, dict);
        }

        redisson.getMap(RedisKey.DICT_CACHE.toString()).putAll(map);
    }

    public void loadWaitToSendCommands(List<Command> waitToSendCommandList) {
        Map<String, Object> map = new HashMap<String, Object>();
        String prevKey = "";
        List<Command> commandList = null;

        for (Command command : waitToSendCommandList) {
            String curKey = command.getDeviceNo();
            String value = command.getCommandContent();
            if (StringUtils.isBlank(value)) {
                continue;
            }

            if (commandList == null) {
                commandList = new ArrayList<Command>();
                commandList.add(command);
                prevKey = curKey;
                continue;
            }

            if (StringUtils.equals(prevKey, curKey)) {
                commandList.add(command);
                continue;
            }

            map.put(prevKey, commandList);
            commandList = new ArrayList<Command>();
            commandList.add(command);
            prevKey = curKey;
        }
        if (commandList != null) {
            map.put(prevKey, commandList);
        }

        redisson.getMap(RedisKey.WAIT_TO_SEND_COMMAND_CACHE.toString()).putAll(map);
    }

    /**
     * 从数据库中读入港口信息
     */
    public void loadFenceInfo(List<FenceExtend> fenceList) {
        Map<String, Object> map = new HashMap<String, Object>();

        List<CircleFence> circleFenceList = new ArrayList<CircleFence>();
        List<PolygonFence> polygonFenceList = new ArrayList<PolygonFence>();

        for (int i = 0; i < fenceList.size(); i++) {
            FenceExtend fenceExtend = fenceList.get(i);
            switch (Constants.FenceType.getEnumByValue(fenceExtend.getFenceType())) {
                case CIRCLE:
                    // 如果是圆形围栏，则只取一条，且取半径
                    if (fenceExtend.getLongitude() == null || fenceExtend.getLatitude() == null
                            || fenceExtend.getRadius() == null || fenceExtend.getRadius() == 0) {
                        continue;
                    }
                    CircleFence circleFence = new CircleFence();
                    circleFence.setLongitude(fenceExtend.getLongitude());
                    circleFence.setLatitude(fenceExtend.getLatitude());
                    circleFence.setRadius(fenceExtend.getRadius());
                    circleFence.setFenceId(fenceExtend.getFenceId());
                    circleFenceList.add(circleFence);
                    break;
                case RECTANG:
                case POLYGON:
                    // 如果是多方形围栏(包括正方形围栏)，则把该围栏所有的点保存到一条记录中
                    Long curFenceId = fenceExtend.getFenceId();
                    List<GpsPosition> vertices = new ArrayList<GpsPosition>();
                    int j = i;
                    for (; j < fenceList.size(); j++) {
                        FenceExtend tempFenceExtend = fenceList.get(j);
                        // 如果不相等，则退出
                        if (!curFenceId.equals(tempFenceExtend.getFenceId())) {
                            break;
                        }
                        Double lon = tempFenceExtend.getLongitude();
                        Double lat = tempFenceExtend.getLatitude();
                        if (lon == null || lat == null) {
                            continue;
                        }
                        vertices.add(new GpsPosition(lon, lat));
                    }
                    if (vertices.size() > 0) {
                        PolygonFence polygonFence = new PolygonFence();
                        polygonFence.setFenceId(curFenceId);
                        polygonFence.setVertices(vertices);
                        polygonFenceList.add(polygonFence);
                    }

                    i = j - 1;
                    break;
                default:
                    break;
            }
        }

        map.put(Constants.CIRCLE_FENCE, circleFenceList);
        map.put(Constants.POLYGON_FENCE, polygonFenceList);

        redisson.getMap(RedisKey.FENCE_INFO_CACHE.toString()).putAll(map);
    }

    /**
     * 从数据库中读入历史数据表索引信息
     */
    public void loadAcqDataHistoryTblIndex(List<AcqTblIndex> acqTblIndexList) {
        Map<String, Object> map = new HashMap<>();

        for (AcqTblIndex acqTblIndex : acqTblIndexList) {
            String key = acqTblIndex.getDeviceNo();
            String value = acqTblIndex.getTableSuff();
            if (value != null) {
                map.put(key, value);
            }
        }

        redisson.getMap(RedisKey.ACQDATA_HISTORY_TBL_INDEX_CACHE.toString()).putAll(map);

    }

    /**
     * 从数据库中读入嘉科信息报警权限信息
     */
    public void loadJkxxAlarmPerm(List<Integer> alarmPermList) {
        Map<String, Object> map = redisson.getMap(RedisKey.ALARM_PERM_CACHE.toString());
        map.put(Constants.JKXX_USER_NAME, alarmPermList);
    }

    /**
     * 获取所有设备权限用户Id
     */
    public void loadDeviceRuleUserId(Map<String, String> ruleMap) {
        Map<String, Object> map = redisson.getMap(RedisKey.DEVICE_RULE_USERID.toString());
        for(String key : ruleMap.keySet()){
            map.put(key, ruleMap.get(key)+"");
        }
    }

    /**
     * 获取所有围栏坐标
     */
    public void loadCordonList(Map<String, List<GpsPosition>> cordonMap) {
        Map<String, Object> map = redisson.getMap(RedisKey.CORDON_GIS_LIST.toString());
        for(String key : cordonMap.keySet()){
            map.put(key, cordonMap.get(key));
        }
    }

    /**
     * 获取围栏与用户_围栏报警类型的关系
     */
    public void loadCordonUser(Map<String, String> cordonMap) {
        Map<String, Object> map = redisson.getMap(RedisKey.CORDON_USERID.toString());
        for(String key : cordonMap.keySet()){
            map.put(key, cordonMap.get(key)+"");
        }
    }

    /**
     * 获取围栏与设备的关系
     */
    public void loadCordonDevice(Map<String, String> cordonMap) {
        Map<String, Object> map = redisson.getMap(RedisKey.CORDON_USERID.toString());
        for(String key : cordonMap.keySet()){
            map.put(key, cordonMap.get(key) + "");
        }
    }

    /**
     * 获取进出围栏报警未解除部分
     */
    public void loadAlarmInCordon(Map<String, String> cordonInMap) {
        Map<String, Object> map = redisson.getMap(RedisKey.CORDON_STATE_IN.toString());
        for(String key : cordonInMap.keySet()){
            map.put(key, cordonInMap.get(key) + "");
        }
    }

    /**
     * 获取进出围栏报警未解除部分
     */
    public void loadAlarmOutCordon(Map<String, String> cordonOutMap) {
        Map<String, Object> map = redisson.getMap(RedisKey.CORDON_STATE_IN.toString());
        for(String key : cordonOutMap.keySet()){
            map.put(key, cordonOutMap.get(key) + "");
        }
    }

    public enum RedisKey {
        DEVICE_INFO_CACHE("DEVICE_INFO_CACHE"),
        SYS_CONFIG_CACHE("SYS_CONFIG_CACHE"),
        DICT_CACHE("DICT_CACHE"),
        PREV_ACQDATA_CACHE("PREV_ACQDATA_CACHE"),
        WAIT_TO_SEND_COMMAND_CACHE("WAIT_TO_SEND_COMMAND_CACHE"),
        FENCE_INFO_CACHE("FENCE_INFO_CACHE"),
        ACQDATA_HISTORY_TBL_INDEX_CACHE("ACQDATA_HISTORY_TBL_INDEX_CACHE"),
        ALARM_PERM_CACHE("ALARM_PERM_CACHE"),
        DEVICE_RULE_USERID("DEVICE_RULE_USERID"),
        CORDON_GIS_LIST("CORDON_GIS_LIST"),
        CORDON_USERID("CORDON_USERID"),
        CORDON_DEVICE_LIST("CORDON_DEVICE_LIST"),
        CORDON_STATE_IN("CORDON_STATE_IN"),
        CORDON_STATE_OUT("CORDON_STATE_OUT"),
        FIRST_ACQDATA_IOF_OUT_CACHE("FIRST_ACQDATA_IOF_OUT_CACHE"),
        FIRST_ACQDATA_IOF_IN_CACHE("FIRST_ACQDATA_IOF_IN_CACHE");
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

