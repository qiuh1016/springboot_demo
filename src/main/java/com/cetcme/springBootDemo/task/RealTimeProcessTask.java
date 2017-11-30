package com.cetcme.springBootDemo.task;

import com.cetcme.springBootDemo.dao.*;
import com.cetcme.springBootDemo.domain.*;
import com.cetcme.springBootDemo.message.RealTimeMsgProcessor;
import com.cetcme.springBootDemo.utils.*;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by qiuhong on 23/11/2017.
 */
public class RealTimeProcessTask implements Runnable {

    public static Logger logger = LoggerFactory.getLogger(RealTimeMsgProcessor.class);

    public static int readMessageCount = 0;
    public static int processMessageCount = 0;

    private static RedissonClient redisson = RedissonUtil.redisson;;

    private AlarmDao alarmDao = new AlarmDao();

    private static String EMG_ALARM_MSG = "设备编码【%s】发生紧急报警！";

    // 报警信息十六位二进制数字所代表的意义: bit00=拆卸报警、bit01=存储故障 bit02=光伏故障、bit03=低电量报警、
    // bit04=近程通信、bit05=GPRS通信、 bit08=GPS故障、bit09=GPS信号弱、
    // bit10=倾斜报警、bit11=GPRS定位、bit12=锂电池故障、bit13=北斗数据
    public static enum AlarmTypeBit {
        NONE1, NONE2, LIB_BEIDOU_FLAG, LIB_ERROR_FLAG, GPRS_LOCATION_FLAG, TILT_ALARM_FLAG, GPS_SIGNAL_WEAK_FLAG, GPS_ERROR_FLAG, NONE4, EMG_ALARM, GPRS_ERROR_FLAG, NFC_FLAG, LOW_BATTERY_ALARM_FLAG, PVB_ERROR_FLAG, STORAGE_ERROR_FLAG, DISMANTLE_ALARM_FLAG
    };

    public static List<Enum<RealTimeMsgProcessor.AlarmTypeBit>> ERROR_LIST = new ArrayList<Enum<RealTimeMsgProcessor.AlarmTypeBit>>(
            Arrays.asList(RealTimeMsgProcessor.AlarmTypeBit.DISMANTLE_ALARM_FLAG, RealTimeMsgProcessor.AlarmTypeBit.STORAGE_ERROR_FLAG,
                    RealTimeMsgProcessor.AlarmTypeBit.PVB_ERROR_FLAG, RealTimeMsgProcessor.AlarmTypeBit.LOW_BATTERY_ALARM_FLAG, RealTimeMsgProcessor.AlarmTypeBit.NFC_FLAG,
                    RealTimeMsgProcessor.AlarmTypeBit.GPRS_ERROR_FLAG, RealTimeMsgProcessor.AlarmTypeBit.EMG_ALARM, RealTimeMsgProcessor.AlarmTypeBit.GPS_ERROR_FLAG,
                    RealTimeMsgProcessor.AlarmTypeBit.GPS_SIGNAL_WEAK_FLAG, RealTimeMsgProcessor.AlarmTypeBit.TILT_ALARM_FLAG, RealTimeMsgProcessor.AlarmTypeBit.LIB_ERROR_FLAG,
                    RealTimeMsgProcessor.AlarmTypeBit.LIB_BEIDOU_FLAG));




    public RealTimeProcessTask(AcqData curData) {
        this.curData = curData;
    }

    private AcqData curData;

    @Override
    public void run() {
        Boolean showSpendTime = false;
        if (showSpendTime) {
            long start = System.currentTimeMillis();
            this.processSingleFrame(curData);
            long end = System.currentTimeMillis();
            logger.info("RealTimeProcessTask 花了{}毫秒", end - start);
        } else {
            this.processSingleFrame(curData);
        }
    }

    /**
     * 原单帧数据处理
     */
    private void processSingleFrame(AcqData curData){
        String myDeviceNo = "";
        try{
            readMessageCount++;

            myDeviceNo = curData.getDeviceNo(); //(String) deviceJson.get("deviceNo"); // todo: json 获取

            DeviceExtend deviceExtend = (DeviceExtend) RedissonUtil.redisson.getMap(RedissonUtil.RedisKey.DEVICE_INFO_CACHE.toString()).get(myDeviceNo);

            // 如果设备编码未设置或者已删除，则直接向设备发送OK信号，丢弃数据
            if (deviceExtend == null) {
                logger.warn("{} 【{}】设备主要状态信息无效，该设备数据库中不存在或已删除！", DateUtil.parseDateToString(new Date()), myDeviceNo);

                // 向客户端发送成功回执
//                responseTcpOk(message, message.getHeader());
                return;
            }
            Long deviceId = deviceExtend.getDeviceId();
            // 根据设备标识获取编定的渔船编码
            Long shipId = deviceExtend.getShipId();

            // 消息1(报船舶实时消息)不存在BCD码等数据格式，因此可以直接从字符串中截取各字段。
//            AcqData curData = (AcqData) deviceJson.get("AcqData"); // todo: json 获取
            this.analysisFrameMessage(myDeviceNo, deviceExtend, curData);

//            AcqDataDao acqDataDao = new AcqDataDao();
//            acqDataDao.insertHistory(curData);

//            responseTcpOk(message, message.getHeader());

            // 将设备设为在线
            boolean hsaDeviceStatusChange = false;
            // 解除离线超时警报
            if (deviceExtend.getOuttimeFlag()) {
                //			logger.info("设备(" + deviceNo + ")解除离线超时警报开始");
                Alarm alarm = new Alarm();
                alarm.setDeviceId(deviceId);
                alarm.setDeviceNo(myDeviceNo);
                alarm.setShipId(shipId);
                int alarmType = Constants.CFS_OUTTIME_ALARM;
                alarm.setAlarmType(alarmType);
                alarm.setSolveTime(new Date());
                alarm.setSolveFlag(true);
                alarm.setLongitude(curData.getLongitude());
                alarm.setLatitude(curData.getLatitude());
                LocalCacheUtil.addAlarm(alarm);

                deviceExtend.setOuttimeFlag(false);

                hsaDeviceStatusChange = true;
                logger.info("设备(" + myDeviceNo + ")解除离线超时警报结束");
            }

            if (deviceExtend.getOfflineFlag()) {
                deviceExtend.setOfflineFlag(false);
                deviceExtend.setUpdateTime(new Date());

                DeviceDao deviceDao = new DeviceDao();
                deviceDao.setDeviceOnline(myDeviceNo);

                hsaDeviceStatusChange = true;
                logger.info("设备(" + myDeviceNo + ")上线设置结束");
            }

            if (hsaDeviceStatusChange) {
                RedissonUtil.redisson.getMap(RedissonUtil.RedisKey.DEVICE_INFO_CACHE.toString()).put(myDeviceNo, deviceExtend);
            }

            processMessageCount++;
        } catch (Exception e) {
            logger.error(myDeviceNo + "RealtimeMsgProcessor.process异常" + e.getMessage());

        }
    }

    // TODO: qh
    private void analysisFrameMessage(String deviceNo, DeviceExtend deviceExtend, AcqData curData) {
        Long deviceId = deviceExtend.getDeviceId();
        // 根据设备标识获取编定的渔船编码
        Long shipId = deviceExtend.getShipId();
        String shipNo = deviceExtend.getShipNo();

        AcqData prevData = (AcqData) RedissonUtil.redisson.getMap(RedissonUtil.RedisKey.PREV_ACQDATA_CACHE.toString()).get(deviceNo);
        int prevIofFlag = prevData == null ? Constants.IOF_FLAG_OUT : prevData.getIofFlag();
        String iofStr = getCurIofFlag(prevIofFlag, curData, deviceExtend.getCfsStartDate(), deviceExtend.getCfsEndDate());

        int curIofFlag = Integer.parseInt(iofStr.split(",")[0]);
        Long fenceId = Long.parseLong(iofStr.split(",")[1]);
        curData.setIofFlag(curIofFlag);
        curData.setCreateTime(new Date());
        curData.setUpdateTime(new Date());
        Long iofFenceId = fenceId;
        if(iofFenceId == 0){
            iofFenceId = deviceExtend.getFenceId();
        }
        deviceExtend.setFenceId(fenceId);

        // 计算当日里程
        double dayTotalMileage = 0.0;
        double totalMileage = 0.0;
        if (prevData != null) {
            //里程数不在加入LBS数据
            if (curData.getSignalType() == 2) {
                dayTotalMileage = prevData.getDayTotalMileage();
                totalMileage = prevData.getTotalMileage();
            } else {
                dayTotalMileage = getDayTotalMileage(prevData.getDayTotalMileage(), prevData.getAcqTime(),
                        curData.getAcqTime(), prevData.getLongitude(), prevData.getLatitude(), curData.getLongitude(),
                        curData.getLatitude());
                totalMileage = getTotalMileage(prevData.getTotalMileage(), prevData.getAcqTime(), curData.getAcqTime(),
                        prevData.getLongitude(), prevData.getLatitude(), curData.getLongitude(), curData.getLatitude());
            }
        }
        curData.setDayTotalMileage(dayTotalMileage);
        curData.setTotalMileage(totalMileage);

        // 如果进出港标志发生变化，则发短信提醒用户提交进出港人员信息
        if (curIofFlag != prevIofFlag) {
            String picTelNo = deviceExtend.getPicTelNo();
            String shipName = deviceExtend.getShipName();
            if(curIofFlag == Constants.IOF_FLAG_CFS_OUT || curIofFlag == Constants.IOF_FLAG_OUT){
                AcqData curDataPrev = (AcqData) redisson.getMap(RedissonUtil.RedisKey.FIRST_ACQDATA_IOF_OUT_CACHE.toString()).get(deviceNo);
                if(curDataPrev != null){
                    redisson.getMap(RedissonUtil.RedisKey.FIRST_ACQDATA_IOF_OUT_CACHE.toString()).remove(deviceNo);
                    genIofLog(curDataPrev, picTelNo, shipName, curIofFlag, iofFenceId, prevIofFlag);
                }else{
                    genIofLog(curData, picTelNo, shipName, curIofFlag, iofFenceId, prevIofFlag);
                }
            }else if(curIofFlag == Constants.IOF_FLAG_IN){
                AcqData curDataPrev = (AcqData) redisson.getMap(RedissonUtil.RedisKey.FIRST_ACQDATA_IOF_IN_CACHE.toString()).get(deviceNo);
                if(curDataPrev != null){
                    redisson.getMap(RedissonUtil.RedisKey.FIRST_ACQDATA_IOF_IN_CACHE.toString()).remove(deviceNo);
                    genIofLog(curDataPrev, picTelNo, shipName, curIofFlag, iofFenceId, prevIofFlag);
                }else{
                    genIofLog(curData, picTelNo, shipName, curIofFlag, iofFenceId, prevIofFlag);
                }
            }
            // 修改设备表的港口标识
            DeviceDao deviceDao = new DeviceDao();
            deviceDao.updateFenceId(deviceId, fenceId);
            logger.info("设备(" + deviceNo + ")如果进出港标志发生变化，则发短信提醒用户提交进出港人员信息结束");
        }

        //围栏报警处理
        String userIds = (String) redisson.getMap(RedissonUtil.RedisKey.DEVICE_RULE_USERID.toString()).get(deviceNo);
        if(userIds != null){
            String[] userIdArray = userIds.split(",");
            for (int i = 0; i < userIdArray.length; i++) {
                int userId = NumberUtils.toInt(userIdArray[i], 0);
                String cordonIds = (String) redisson.getMap(RedissonUtil.RedisKey.CORDON_USERID.toString()).get(userId+"");
                if(cordonIds != null){
                    String[] cordonIdArray = cordonIds.split(",");
                    for (int j = 0; j < cordonIdArray.length; j++) {
                        String[] arrayFlag = cordonIdArray[j].split("_");
                        if (arrayFlag.length == 4) {
                            int cordonId = NumberUtils.toInt(arrayFlag[0], 0);
                            int cordonFlag = NumberUtils.toInt(arrayFlag[1], -1);
                            int cordonAll = NumberUtils.toInt(arrayFlag[2], -1);
                            String alarmMark = arrayFlag[3];
                            if (cordonAll == 1) {
                                List<GpsPosition> gpsList = (List<GpsPosition>) redisson.getMap(RedissonUtil.RedisKey.CORDON_GIS_LIST.toString()).get(cordonId + "");
                                if (gpsList != null) {
                                    GpsPosition point = new GpsPosition(curData.getLongitude(), curData.getLatitude());
                                    if (GisUtil.isPointInPolygon(point, gpsList)) {
                                        if (cordonFlag == 0) {//只进不出
                                            //这里需要做解除操作
                                            addInOutCordonAlarm(userId, deviceId, deviceNo, shipId, curData.getAcqTime(), curData.getLongitude(), curData.getLatitude(), 0, cordonFlag, cordonId, alarmMark);
                                        } else if (cordonFlag == 1) {//只出不进
                                            //这里需要做报警操作
                                            addInOutCordonAlarm(userId, deviceId, deviceNo, shipId, curData.getAcqTime(), curData.getLongitude(), curData.getLatitude(), 1, cordonFlag, cordonId, alarmMark);
                                        }
                                    } else {
                                        if (cordonFlag == 0) {//只进不出
                                            //这里需要做报警操作
                                            addInOutCordonAlarm(userId, deviceId, deviceNo, shipId, curData.getAcqTime(), curData.getLongitude(), curData.getLatitude(), 1, cordonFlag, cordonId, alarmMark);
                                        } else if (cordonFlag == 1) {//只出不进
                                            //这里需要做解除操作
                                            addInOutCordonAlarm(userId, deviceId, deviceNo, shipId, curData.getAcqTime(), curData.getLongitude(), curData.getLatitude(), 0, cordonFlag, cordonId, alarmMark);
                                        }
                                    }
                                }
                            } else if (cordonAll == 0) {
                                if (cordonId > 0) {
                                    String deviceIds = (String) redisson.getMap(RedissonUtil.RedisKey.CORDON_DEVICE_LIST.toString()).get(cordonId+"");
                                    if (("," + deviceIds + ",").contains("," + deviceId + ",")) {
                                        GpsPosition point = new GpsPosition(curData.getLongitude(), curData.getLatitude());
                                        List<GpsPosition> gpsList = (List<GpsPosition>) redisson.getMap(RedissonUtil.RedisKey.CORDON_GIS_LIST.toString()).get(cordonId+"");
                                        if (GisUtil.isPointInPolygon(point, gpsList)) {
                                            if (cordonFlag == 0) {//只进不出
                                                //这里需要做解除操作
                                                addInOutCordonAlarm(userId, deviceId, deviceNo, shipId, curData.getAcqTime(), curData.getLongitude(), curData.getLatitude(), 0, cordonFlag, cordonId, alarmMark);
                                            } else if (cordonFlag == 1) {//只出不进
                                                //这里需要做报警操作
                                                addInOutCordonAlarm(userId, deviceId, deviceNo, shipId, curData.getAcqTime(), curData.getLongitude(), curData.getLatitude(), 1, cordonFlag, cordonId, alarmMark);
                                            }
                                        } else {
                                            if (cordonFlag == 0) {//只进不出
                                                //这里需要做报警操作
                                                addInOutCordonAlarm(userId, deviceId, deviceNo, shipId, curData.getAcqTime(), curData.getLongitude(), curData.getLatitude(), 1, cordonFlag, cordonId, alarmMark);
                                            } else if (cordonFlag == 1) {//只出不进
                                                //这里需要做解除操作
                                                addInOutCordonAlarm(userId, deviceId, deviceNo, shipId, curData.getAcqTime(), curData.getLongitude(), curData.getLatitude(), 0, cordonFlag, cordonId, alarmMark);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 将报警信息加入报警缓存
        String prevAlarmStatus = prevData == null ? null : prevData.getAlarmStatus();
        addCommonAlarm(deviceId, deviceNo, shipId, shipNo, curData.getAcqTime(), curData.getAlarmStatus(), prevAlarmStatus, curData.getLongitude(), curData.getLatitude());

        LocalCacheUtil.addAcqData(curData);
        LocalCacheUtil.addHistoryAcqData(curData);
        redisson.getMap(RedissonUtil.RedisKey.PREV_ACQDATA_CACHE.toString()).put(deviceNo, curData);
    }

    /**
     * 获取出入港标志(出港：前次记录在港内，本次记录在港外，即生成出港记录；进港:
     * 前次记录在港外，本次记录在港内，并且接下去的30分钟内都在港内，即生成进港记录。)
     */
    private String getCurIofFlag(int prevIofFlag, AcqData curData, Date cfsStartDate, Date cfsEndDate) {
        Date acqTime = curData.getAcqTime();
        // 出入港判断，如果在港则返回所在港标识，否则返回null
        Long fenceId = whichFenceIn(curData);
        int curIofFlag = prevIofFlag;
        long inFlagTime = 1 * 60 * 1000;
        if (fenceId == 0l) {
            // 不在港内，原状态是在港的情况下，则将出入港标志设为出港，如果在伏休期，则设为伏休期出港
            if (prevIofFlag == Constants.IOF_FLAG_IN) {
                String deviceNo = curData.getDeviceNo();
                Object firstTimeObj = redisson.getMap(RedissonUtil.RedisKey.FIRST_ACQDATA_IOF_OUT_CACHE.toString()).get(deviceNo);
                if (firstTimeObj == null) {
                    redisson.getMap(RedissonUtil.RedisKey.FIRST_ACQDATA_IOF_OUT_CACHE.toString()).put(curData.getDeviceNo(), curData);
                } else {
                    AcqData firstAcqData = (AcqData) firstTimeObj;
                    long duration = curData.getAcqTime().getTime() - firstAcqData.getAcqTime().getTime();
                    // 连续进港超过inFlagTime分钟，则将进出港标志设为出港，同时清掉缓存
                    if (duration >= inFlagTime) {
                        curIofFlag = isInCfs(cfsStartDate, cfsEndDate, acqTime) ? Constants.IOF_FLAG_CFS_OUT : Constants.IOF_FLAG_OUT;
                    }
                }
                redisson.getMap(RedissonUtil.RedisKey.FIRST_ACQDATA_IOF_IN_CACHE.toString()).remove(deviceNo);
            }
        } else {
            // 在港內，原状态是在出港的情況下，则查找该设备缓存中是否在在连续进港的首次进港时间,
            // 如果没有，则将当前时间插入缓存，如果有，则判断时间间隔是否超过inFlagTime分钟，如等于或超过则设为进港，同时删除该缓存
            if (prevIofFlag != Constants.IOF_FLAG_IN) {
                // 生成一条进港记录，同时生成进港人员记录
                String deviceNo = curData.getDeviceNo();
                Object firstTimeObj = redisson.getMap(RedissonUtil.RedisKey.FIRST_ACQDATA_IOF_IN_CACHE.toString()).get(deviceNo);
                if (firstTimeObj == null) {
                    redisson.getMap(RedissonUtil.RedisKey.FIRST_ACQDATA_IOF_IN_CACHE.toString()).put(curData.getDeviceNo(), curData);
                } else {
                    AcqData firstAcqData = (AcqData) firstTimeObj;
                    long duration = curData.getAcqTime().getTime() - firstAcqData.getAcqTime().getTime();
                    // 连续进港超过inFlagTime分钟，则将进出港标志设为进港，同时清掉缓存
                    if (duration >= inFlagTime) {
                        curIofFlag = Constants.IOF_FLAG_IN;
                    }
                }
                redisson.getMap(RedissonUtil.RedisKey.FIRST_ACQDATA_IOF_OUT_CACHE.toString()).remove(deviceNo);
            }
        }

        return curIofFlag + "," + fenceId;
    }

    /**
     * 获得所在港口，如在港外，则返回0
     */
    @SuppressWarnings("unchecked")
    private static Long whichFenceIn(AcqData curData) {
        long result = 0l;

//		long start = System.currentTimeMillis();

        List<CircleFence> circleFenceList = (List<CircleFence>) redisson.getMap(RedissonUtil.RedisKey.FENCE_INFO_CACHE.toString()).get(Constants.CIRCLE_FENCE);
        List<PolygonFence> polygonFenceList = (List<PolygonFence>) redisson.getMap(RedissonUtil.RedisKey.FENCE_INFO_CACHE.toString()).get(Constants.POLYGON_FENCE);

        GpsPosition curPosition = new GpsPosition(curData.getLongitude(), curData.getLatitude());

        // 根据系统配置中的计算区域判断是否需要进行遍历所有港口
        double lonMax = Double.parseDouble(redisson.getMap(RedissonUtil.RedisKey.SYS_CONFIG_CACHE.toString()).get(Constants.LON_MAX).toString());
        double lonMin = Double.parseDouble(redisson.getMap(RedissonUtil.RedisKey.SYS_CONFIG_CACHE.toString()).get(Constants.LON_MIN).toString());
        double latMax = Double.parseDouble(redisson.getMap(RedissonUtil.RedisKey.SYS_CONFIG_CACHE.toString()).get(Constants.LAT_MAX).toString());
        double latMin = Double.parseDouble(redisson.getMap(RedissonUtil.RedisKey.SYS_CONFIG_CACHE.toString()).get(Constants.LAT_MIN).toString());

        if (GisUtil.isPointInRect(curPosition, lonMax, lonMin, latMax, latMin)) {
            return result;
        }

        // 遍历所有圆形港口
        if (circleFenceList != null && circleFenceList.size() > 0) {
            for (CircleFence circleFence : circleFenceList) {
                GpsPosition circlePoint = new GpsPosition(circleFence.getLongitude(), circleFence.getLatitude());
                if (GisUtil.isPointInCycle(curPosition, circlePoint, circleFence.getRadius())) {
                    return circleFence.getFenceId();
                }
            }
        }

        // 遍历所有多边形港口
        if (polygonFenceList != null && polygonFenceList.size() > 0) {
            for (PolygonFence polygonFence : polygonFenceList) {
                if (GisUtil.isPointInPolygon(curPosition, polygonFence.getVertices())) {
                    return polygonFence.getFenceId();
                }
            }
        }

//		long end = System.currentTimeMillis();
//		System.out.println(end - start);
        return result;
    }

    /**
     * 判断是否伏休期
     */
    private static boolean isInCfs(Date cfsStartDate, Date cfsEndDate, Date acqTime) {
        return cfsStartDate != null && cfsEndDate != null && acqTime.after(cfsStartDate) && acqTime.before(cfsEndDate);
    }

    private void genIofLog(AcqData curData, String picTelNo, String shipName, int curIofFlag, Long fenceId, int prevIofFlag) {
        ShipDao shipDao = new ShipDao();
        IofLog iofLog = new IofLog();
        iofLog.setDeviceId(curData.getDeviceId());
        iofLog.setDeviceNo(curData.getDeviceNo());
        iofLog.setIofTime(curData.getAcqTime());
        iofLog.setShipId(curData.getShipId());
        iofLog.setLongitude(curData.getLongitude());
        iofLog.setLatitude(curData.getLatitude());
        iofLog.setIofFlag(curIofFlag);
        iofLog.setFenceId(fenceId);
        if (curIofFlag == Constants.IOF_FLAG_CFS_OUT) {
            iofLog.setIofStatus(1);
        } else {
            iofLog.setIofStatus(0);
        }
        IofLogDao iofLogDao = new IofLogDao();
        int iofLogId = iofLogDao.insertIofLog(iofLog);

        if (curIofFlag != prevIofFlag && curIofFlag == Constants.IOF_FLAG_CFS_OUT) {
            addCfsOutFenceAlarm(curData.getDeviceId(), curData.getDeviceNo(), curData.getShipId(), curData.getAcqTime(),
                    curData.getLongitude(), curData.getLatitude(), curIofFlag);
        } else if (prevIofFlag == Constants.IOF_FLAG_CFS_OUT && curIofFlag == Constants.IOF_FLAG_IN) {
            addCfsOutFenceAlarm(curData.getDeviceId(), curData.getDeviceNo(), curData.getShipId(), curData.getAcqTime(),
                    curData.getLongitude(), curData.getLatitude(), curIofFlag);
        }

        IofSailorDao iofSailorDao = new IofSailorDao();
        List<IofSailor> submitIofSailorList = iofSailorDao.getSubmitIofSailorList(iofLog);

        String alarmMes = "";
        Ship ship = shipDao.getByShipId(iofLog.getShipId());
        if (submitIofSailorList.size() > 0) {
            int submitIofSailorCnt = submitIofSailorList.size();
            iofSailorDao.update(iofLog);
            if (curIofFlag != Constants.IOF_FLAG_IN) {
                String nameList = "";
                for(IofSailor iofSailor : submitIofSailorList){
                    if("".equals(nameList)) {
                        nameList = iofSailor.getSailorName();
                    } else {
                        nameList += ","+iofSailor.getSailorName();
                    }
                }
                if(ship != null && ship.getPersonRated() != null){
                    if(submitIofSailorCnt > ship.getPersonRated()){
                        addPersonRatedAlarm(iofLog.getDeviceId(), iofLog.getDeviceNo(), iofLog.getShipId(), iofLog.getIofTime(), iofLog.getLongitude(), iofLog.getLatitude(), curIofFlag);
                    }
                }

                try {
                    FenceDao fenceDao = new FenceDao();
                    Fence fence = fenceDao.getByFenceId(fenceId);
                    String fenceName = "";
                    String fencePhone = "";
                    String fenceMes = "";
                    if(fence != null){
                        fenceName = fence.getFenceName();
                        fencePhone = fence.getFencePhone();
                        fenceMes = ",如有疑问请联系"+fenceName;
                        if(fencePhone != null && !"".equals(fencePhone)){
                            fenceMes += ",电话:"+fencePhone;
                        }
                    }
                    if(!"".equals(alarmMes)) {
                        //YxtUtil.SendShipSMS(picTelNo, ship.getShipName()+"已出港,人员名单:"+nameList, alarmMes, fenceMes);
                    } else {
                        //YxtUtil.SendCgMsg(picTelNo, ship.getShipName()+"已出港,人员名单:"+nameList+fenceMes);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            } else {
                relieveAlarm(iofLog.getDeviceId(), iofLog.getDeviceNo(), iofLog.getShipId(), iofLog.getIofTime(), iofLog.getLongitude(), iofLog.getLatitude(), Constants.PERSON_RATED_ALARM);
                relieveAlarm(iofLog.getDeviceId(), iofLog.getDeviceNo(), iofLog.getShipId(), iofLog.getIofTime(), iofLog.getLongitude(), iofLog.getLatitude(), Constants.FENCEOUT_PUNCH_ALARM);
            }
            return;
        }

        // 查看打卡表中是否有未使用的打卡信息
        PunchDao punchDao = new PunchDao();
        String nameList = "";
        if (curIofFlag != Constants.IOF_FLAG_IN) {
            List<Punch> unusedPunchList = punchDao.getUnUserdPunchRecordList(curData.getDeviceId(), curData.getShipId());
            for(Punch punch : unusedPunchList){
                if("".equals(nameList)) {
                    nameList = punch.getSailorName();
                } else {
                    nameList += ","+punch.getSailorName();
                }
            }
            if (unusedPunchList.size() > 0) {
                // 将打卡人员插入出入港人员表
                iofSailorDao.addIofSailor(iofLog);

                // 将打卡信息状态设为已用
                punchDao.updateStatus(curData.getDeviceId(), curData.getShipId());

                int unusedPunchCnt = unusedPunchList.size();
                if(ship != null && ship.getPersonRated() != null){
                    if(unusedPunchCnt > ship.getPersonRated()){
                        addPersonRatedAlarm(iofLog.getDeviceId(), iofLog.getDeviceNo(), iofLog.getShipId(), iofLog.getIofTime(), iofLog.getLongitude(), iofLog.getLatitude(), curIofFlag);
                    }
                }
            } else {
                alarmMes = addFenceOutPunchAlarm(iofLog.getDeviceId(), iofLog.getDeviceNo(), iofLog.getShipId(), iofLog.getIofTime(), iofLog.getLongitude(), iofLog.getLatitude(), curIofFlag);
            }
        } else {
            //最后一条出入港记录
            int iofLogIdMax = iofLogDao.maxIofLog(iofLog);
            if(iofLogIdMax > 0){
                //最后一次出入港人员名单
                List<IofSailor> iofSailorList = iofSailorDao.prevFenceOutList(iofLogIdMax);
                if(iofSailorList != null && iofSailorList.size() > 0){
                    iofSailorDao.addIofSailor(iofSailorList, iofLogId, 2);
                }
            }
        }

        // 如果负责人电话不为空,且未提交进出港，则发送短信提醒
        boolean telFlag = true;
        if(ship != null){
            if (StringUtils.isBlank(ship.getPicTelNo())) {
                telFlag = false;
            } else {
                picTelNo = ship.getPicTelNo();
            }
        } else {
            telFlag = false;
        }

        // 根据出入港类型向渔船负责人的手机号码发送短信
        FenceDao fenceDao = new FenceDao();
        Fence fence = fenceDao.getByFenceId(fenceId);
        String fenceName = "";
        String fencePhone = "";
        String fenceMes = "";
        if(fence != null){
            fenceName = fence.getFenceName();
            fencePhone = fence.getFencePhone();
            fenceMes = ",如有疑问请联系"+fenceName;
            if(fencePhone != null && !"".equals(fencePhone)){
                fenceMes += ",电话:"+fencePhone;
            }
        }
        if (curIofFlag == Constants.IOF_FLAG_IN) {
            //进港解除"出港人员未打卡记录"和"额定人数不一致记录"
            relieveAlarm(iofLog.getDeviceId(), iofLog.getDeviceNo(), iofLog.getShipId(), iofLog.getIofTime(), iofLog.getLongitude(), iofLog.getLatitude(), Constants.PERSON_RATED_ALARM);
            relieveAlarm(iofLog.getDeviceId(), iofLog.getDeviceNo(), iofLog.getShipId(), iofLog.getIofTime(), iofLog.getLongitude(), iofLog.getLatitude(), Constants.FENCEOUT_PUNCH_ALARM);
            // 发送回港提醒短信
            if(telFlag){
                try {
                    String message = "船名:"+ship.getShipName()+"请于下次出港时进行人员身份证签证"+fenceMes;
                    //YxtUtil.SendPhoneMsg(picTelNo, message);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        } else {
            // 发送出港提醒短信
            if(telFlag){
                try {
                    if(!"".equals(alarmMes)) {
                        //YxtUtil.SendShipSMS(picTelNo, ship.getShipName()+"已出港,人员名单:"+nameList, alarmMes, fenceMes);
                    } else {
                        String message = "船名:"+ship.getShipName()+"已出港,人员名单:"+nameList+fenceMes;
                        //YxtUtil.SendPhoneMsg(picTelNo, message);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    private void addCfsOutFenceAlarm(Long deviceId, String deviceNo, Long shipId, Date acqTime, double longitude, double latitude, int iofFlag) {
        Alarm alarm = new Alarm();
        alarm.setDeviceId(deviceId);
        alarm.setDeviceNo(deviceNo);
        alarm.setShipId(shipId);
        int alarmType = Constants.CFS_OUT_FENCE_ALARM;
        alarm.setAlarmType(alarmType);

        if (iofFlag == Constants.IOF_FLAG_CFS_OUT) {
            String alarmNo = UUID.randomUUID().toString().replaceAll("-", "");
            alarm.setAlarmNo(alarmNo);
            // 伏休期出港，则生成伏休期出港警报
            alarm.setReportTime(acqTime);
            alarm.setSolveFlag(false);
        } else {
            String alarmNo = alarmDao.getAlarmNoPageList(deviceId, shipId, alarmType, 0);
            if(alarmNo != null && !"".equals(alarmNo)){
                alarm.setAlarmNo(alarmNo);
                // 进港，则解除伏休期出港警报
                alarm.setSolveTime(acqTime);
                alarm.setSolveFlag(true);
            }else{
                alarmNo = UUID.randomUUID().toString().replaceAll("-", "");
                alarm.setAlarmNo(alarmNo);
                // 伏休期出港，则生成伏休期出港警报
                alarm.setReportTime(acqTime);
                alarm.setSolveFlag(false);
            }
        }

        alarm.setLongitude(longitude);
        alarm.setLatitude(latitude);
        LocalCacheUtil.addAlarm(alarm);

        int sendMessage = 0;
        if(redisson.getMap(RedissonUtil.RedisKey.SYS_CONFIG_CACHE.toString()).get("sendMessage") != null){
            sendMessage = (int) redisson.getMap(RedissonUtil.RedisKey.SYS_CONFIG_CACHE.toString()).get("sendMessage");
        }
        if(sendMessage == 1){
            @SuppressWarnings("unchecked")
            List<Integer> alarmTypeList = (List<Integer>) redisson.getMap(RedissonUtil.RedisKey.ALARM_PERM_CACHE.toString()).get(Constants.JKXX_USER_NAME);
            if (alarmTypeList != null && !alarmTypeList.isEmpty() && alarmTypeList.contains(alarmType)) {
                DeviceExtend deviceExtend = (DeviceExtend) redisson.getMap(RedissonUtil.RedisKey.DEVICE_INFO_CACHE.toString()).get(deviceNo);
                pushMsgToBpm(alarm, deviceExtend.getShipNo(), acqTime);
            }
        }
    }

    private void pushMsgToBpm(Alarm alarm, String shipNo, Date acqTime) {
        if(alarm.getSolveFlag() == null || shipNo == null || "".equals(shipNo)){
            return;
        }
        PushLog pushLog = new PushLog();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String key = String.format("ALARM_TYPE,%s", alarm.getAlarmType());
        Dict dict = (Dict) redisson.getMap(RedissonUtil.RedisKey.DICT_CACHE.toString()).get(key);
        pushLog.setBoatCode(shipNo);
        params.add(new BasicNameValuePair("boatCode", shipNo));
        pushLog.setAlarmCode(alarm.getAlarmNo());
        params.add(new BasicNameValuePair("alarmCode", alarm.getAlarmNo()));
        pushLog.setAlarmName(dict.getDictValue());
        params.add(new BasicNameValuePair("alarmName", dict.getDictValue()));
        pushLog.setAlarmFlag(alarm.getSolveFlag()?1:0);
        params.add(new BasicNameValuePair("alarmFlag", alarm.getSolveFlag()?"1":"0"));
        pushLog.setAlarmTime(acqTime);
        params.add(
                new BasicNameValuePair("alarmTime", DateUtil.parseDateToString(acqTime, Constants.DatePattern.YYYYMMDDHHMMSS_)));

        // 向嘉科推送
		/*Properties proper = PropertiesUtil.getResources(Constants.CONFIG_FILE_NAME);
		String baseUri = proper.getProperty(Constants.PUSHSERVICE_BASE_URI);
		logger.info("shipNo:{}---alarmCode:{}---alarmName:{}---alarmFlag:{}---alarmTime:{}"
				, shipNo, alarm.getAlarmNo(), dict.getDictValue(), alarm.getSolveFlag(), DateUtil.parseDateToString(acqTime, DatePattern.YYYYMMDDHHMMSS_));
		if(shipNo != null){
			String backJson = RestServiceUtil.post(baseUri + ALARM_URI, params);
			if(backJson != null){
				JSONObject json = JSONObject.fromObject(backJson);
				PushLogDao pushLogDao = new PushLogDao();
				if("true".equals(json.getString("success"))){
					pushLog.setPushFlag(1);
				}else if("false".equals(json.getString("success"))){
					pushLog.setPushFlag(0);
				}
				pushLogDao.insertPushLog(pushLog);
			}
		}*/
    }

    private String addPersonRatedAlarm(Long deviceId, String deviceNo, Long shipId, Date acqTime, double longitude, double latitude, int iofFlag) {
        String alarmMes = "";
        Alarm alarm = new Alarm();
        alarm.setDeviceId(deviceId);
        alarm.setDeviceNo(deviceNo);
        alarm.setShipId(shipId);
        String alarmNo = UUID.randomUUID().toString().replaceAll("-", "");
        alarm.setAlarmNo(alarmNo);
        int alarmType = Constants.PERSON_RATED_ALARM;
        alarm.setAlarmType(alarmType);

        if (iofFlag == Constants.IOF_FLAG_OUT || iofFlag == Constants.IOF_FLAG_CFS_OUT) {
            // 出港人员额定人数不一致
            alarm.setReportTime(acqTime);
            alarm.setSolveFlag(false);
            alarmMes = "出港人数超员";
        }

        alarm.setLongitude(longitude);
        alarm.setLatitude(latitude);
        LocalCacheUtil.addAlarm(alarm);

        int sendMessage = 0;
        if(redisson.getMap(RedissonUtil.RedisKey.SYS_CONFIG_CACHE.toString()).get("sendMessage") != null){
            sendMessage = (int) redisson.getMap(RedissonUtil.RedisKey.SYS_CONFIG_CACHE.toString()).get("sendMessage");
        }
        if(sendMessage == 1){
            @SuppressWarnings("unchecked")
            List<Integer> alarmTypeList = (List<Integer>) redisson.getMap(RedissonUtil.RedisKey.ALARM_PERM_CACHE.toString()).get(Constants.JKXX_USER_NAME);
            if (alarmTypeList != null && !alarmTypeList.isEmpty() && alarmTypeList.contains(alarmType)) {
                DeviceExtend deviceExtend = (DeviceExtend) redisson.getMap(RedissonUtil.RedisKey.DEVICE_INFO_CACHE.toString()).get(deviceNo);
                pushMsgToBpm(alarm, deviceExtend.getShipNo(), acqTime);
            }
        }

        return alarmMes;
    }

    private void relieveAlarm(Long deviceId, String deviceNo, Long shipId, Date acqTime, double longitude, double latitude, int alarmType) {
        String alarmNo = alarmDao.getAlarmNoPageList(deviceId, shipId, alarmType, 0);
        if(alarmNo != null && !"".equals(alarmNo)){
            Alarm alarm = new Alarm();
            alarm.setDeviceId(deviceId);
            alarm.setDeviceNo(deviceNo);
            alarm.setAlarmNo(alarmNo);
            alarm.setShipId(shipId);
            alarm.setAlarmType(alarmType);

            // 进港，则解除逻辑报警
            alarm.setSolveTime(acqTime);
            alarm.setSolveFlag(true);

            alarm.setLongitude(longitude);
            alarm.setLatitude(latitude);
            LocalCacheUtil.addAlarm(alarm);

            int sendMessage = 0;
            if(redisson.getMap(RedissonUtil.RedisKey.SYS_CONFIG_CACHE.toString()).get("sendMessage") != null){
                sendMessage = (int) redisson.getMap(RedissonUtil.RedisKey.SYS_CONFIG_CACHE.toString()).get("sendMessage");
            }
            if(sendMessage == 1){
                @SuppressWarnings("unchecked")
                List<Integer> alarmTypeList = (List<Integer>) redisson.getMap(RedissonUtil.RedisKey.ALARM_PERM_CACHE.toString()).get(Constants.JKXX_USER_NAME);
                if (alarmTypeList != null && !alarmTypeList.isEmpty() && alarmTypeList.contains(alarmType)) {
                    DeviceExtend deviceExtend = (DeviceExtend) redisson.getMap(RedissonUtil.RedisKey.DEVICE_INFO_CACHE.toString()).get(deviceNo);
                    pushMsgToBpm(alarm, deviceExtend.getShipNo(), acqTime);
                }
            }
        }
    }

    private String addFenceOutPunchAlarm(Long deviceId, String deviceNo, Long shipId, Date acqTime, double longitude, double latitude, int iofFlag) {
        String alarmMes = "";
        Alarm alarm = new Alarm();
        alarm.setDeviceId(deviceId);
        alarm.setDeviceNo(deviceNo);
        alarm.setShipId(shipId);
        String alarmNo = UUID.randomUUID().toString().replaceAll("-", "");
        alarm.setAlarmNo(alarmNo);
        int alarmType = Constants.FENCEOUT_PUNCH_ALARM;
        alarm.setAlarmType(alarmType);

        if (iofFlag == Constants.IOF_FLAG_OUT || iofFlag == Constants.IOF_FLAG_CFS_OUT) {
            // 出港人员信息未确认，则生成出港人员信息未确认警报
            alarm.setReportTime(acqTime);
            alarm.setSolveFlag(false);
            alarmMes = "未打卡出港";
        }

        alarm.setLongitude(longitude);
        alarm.setLatitude(latitude);
        LocalCacheUtil.addAlarm(alarm);

        int sendMessage = 0;
        if(redisson.getMap(RedissonUtil.RedisKey.SYS_CONFIG_CACHE.toString()).get("sendMessage") != null){
            sendMessage = (int) redisson.getMap(RedissonUtil.RedisKey.SYS_CONFIG_CACHE.toString()).get("sendMessage");
        }
        if(sendMessage == 1){
            @SuppressWarnings("unchecked")
            List<Integer> alarmTypeList = (List<Integer>) redisson.getMap(RedissonUtil.RedisKey.ALARM_PERM_CACHE.toString()).get(Constants.JKXX_USER_NAME);
            if (alarmTypeList != null && !alarmTypeList.isEmpty() && alarmTypeList.contains(alarmType)) {
                DeviceExtend deviceExtend = (DeviceExtend) redisson.getMap(RedissonUtil.RedisKey.DEVICE_INFO_CACHE.toString()).get(deviceNo);
                pushMsgToBpm(alarm, deviceExtend.getShipNo(), acqTime);
            }
        }

        return alarmMes;
    }

    private void addInOutCordonAlarm(int userId, Long deviceId, String deviceNo, Long shipId, Date acqTime, double longitude, double latitude, int alarmFlag, int cordonFlag, int cordonId, String alarmMark) {
        boolean actionFlag = false;
        int alarmType = (cordonFlag == 1) ? Constants.CORDON_OUT_ALARM : Constants.CORDON_IN_ALARM;
        Alarm alarm = new Alarm();
        if(alarmFlag == 0){
            String alarmNo = deviceId+"-"+cordonId+"-"+cordonFlag+"-"+alarmFlag;
            alarm.setDeviceId(deviceId);
            alarm.setAlarmNo(alarmNo);
            alarm.setSolveFlag(true);
            alarm.setSolveTime(acqTime);
            alarm.setAlarmMark(alarmMark);
            alarm.setAlarmType(alarmType);
            alarm.setDeviceNo(deviceNo);

            if(cordonFlag == 0) {
                redisson.getMap(RedissonUtil.RedisKey.CORDON_STATE_IN.toString()).remove(alarmNo);
            }else if(cordonFlag == 1) {
                redisson.getMap(RedissonUtil.RedisKey.CORDON_STATE_OUT.toString()).remove(alarmNo);
            }
            LocalCacheUtil.addAlarm(alarm);

            actionFlag = true;
        }else if(alarmFlag == 1){
            String alarmNo = deviceId+"-"+cordonId+"-"+cordonFlag+"-"+alarmFlag;
            AlarmDao alarmDao = new AlarmDao();
            int getAlarm = alarmDao.getAlarm(alarmNo);
            if(getAlarm == 0){
                boolean addAlarm = true;
                if(cordonFlag == 0) {
                    if(redisson.getMap(RedissonUtil.RedisKey.CORDON_STATE_IN.toString()).get(alarmNo) == null){
                        redisson.getMap(RedissonUtil.RedisKey.CORDON_STATE_IN.toString()).put(alarmNo, 1);
                    }else{
                        addAlarm = false;
                    }
                }else if(cordonFlag == 1) {
                    if(redisson.getMap(RedissonUtil.RedisKey.CORDON_STATE_OUT.toString()).get(alarmNo) == null){
                        redisson.getMap(RedissonUtil.RedisKey.CORDON_STATE_OUT.toString()).put(alarmNo, "1");
                    }else{
                        addAlarm = false;
                    }
                }

                if(addAlarm) {
                    alarm.setDeviceId(deviceId);
                    alarm.setDeviceNo(deviceNo);
                    alarm.setShipId(shipId);
                    alarm.setAlarmNo(alarmNo);
                    alarm.setAlarmType(alarmType);
                    alarm.setLongitude(longitude);
                    alarm.setLatitude(latitude);
                    alarm.setReportTime(acqTime);
                    alarm.setSolveFlag(false);
                    alarm.setAlarmMark(alarmMark);
                    alarm.setAlarmUserId(userId);
                    LocalCacheUtil.addAlarm(alarm);

                    actionFlag = true;
                }
            }
        }

        if(actionFlag){
            StringBuilder contentSb = new StringBuilder();
            String content = "";
            String notiType = "0";
            contentSb.append("{");
            contentSb.append("deviceNo:'" + deviceNo + "'");
            contentSb.append(",alarmType:'" + alarmType + "'");
            contentSb.append(",userId:'" + userId + "'");
            if(alarm.getSolveFlag()){
                notiType = Constants.NOTIFICATION_ALARM_VIEW_REMOVE;
            }else{
                contentSb.append(",reportTimeDisp:'" + acqTime.toLocaleString() + "'");

                String key = String.format("%s,%s", "ALARM_TYPE", alarmType);
                Dict dict = (Dict) redisson.getMap(RedissonUtil.RedisKey.DICT_CACHE.toString()).get(key);
                notiType = Constants.NOTIFICATION_ALARM_VIEW;
                contentSb.append(",alarmTypeName:'" + alarmMark + "围栏(" + dict.getDictValue() + ")'");
            }
            contentSb.append("}");
            content = contentSb.toString();
            NotificationUtil.send(deviceNo, "滚动报警", content, notiType);
        }
    }

    private void addCommonAlarm(Long deviceId, String deviceNo, Long shipId, String shipNo, Date acqTime, String currentAlarmStatus, String prevAlarmStatus, double longitude, double latitude) {

        for (int j = 0; j < ERROR_LIST.size(); j++) {
            Enum<RealTimeMsgProcessor.AlarmTypeBit> alarmTypeBit = ERROR_LIST.get(j);
            Alarm alarm = new Alarm();
            alarm.setDeviceId(deviceId);
            alarm.setDeviceNo(deviceNo);
            alarm.setShipId(shipId);
            int alarmType = j + 1;
            //GPRS通信故障和GPS信号弱报警不处理
            //第12位不参与报警
            if(alarmType == 6 ||alarmType == 9 || alarmType == 12){
                continue;
            }
            alarm.setAlarmType(alarmType);
            Byte prevStatusFlag = prevAlarmStatus == null ? (byte)48 : prevAlarmStatus.getBytes()[alarmTypeBit.ordinal()];
            Byte curStatusFlag = currentAlarmStatus.getBytes()[alarmTypeBit.ordinal()];

            // 如果报警状态没有变化，则不处理
            if (curStatusFlag.equals(prevStatusFlag)) {
                continue;
            }

            boolean solveFlag = !CommonUtil.isTrue(curStatusFlag);
            if (solveFlag) {
                String alarmNo = alarmDao.getAlarmNoPageList(deviceId, shipId, alarmType, 0);
                if(alarmNo != null && !"".equals(alarmNo)){
                    alarm.setAlarmNo(alarmNo);
                    // 存在未解除警报，且该警报为解除状态，则将原警报状态设为已解除
                    alarm.setSolveTime(acqTime);
                }else{
                    alarmNo = UUID.randomUUID().toString().replaceAll("-", "");
                    alarm.setAlarmNo(alarmNo);
                    // 如果是紧急报警，则直接向前台推送消息
                    alarm.setReportTime(acqTime);
                }
            } else {
                String alarmNo = UUID.randomUUID().toString().replaceAll("-", "");
                alarm.setAlarmNo(alarmNo);
                // 如果是紧急报警，则直接向前台推送消息
                alarm.setReportTime(acqTime);
            }
            if(!solveFlag || alarmTypeBit != RealTimeMsgProcessor.AlarmTypeBit.EMG_ALARM){
                String title = "滚动报警";
                String content = "";
                String notiType = "0";
//				private static String EMG_ALARM_VIEW_MSG = "设备【%s】在【%s】发生【%s】！";
                if (alarmTypeBit == RealTimeMsgProcessor.AlarmTypeBit.EMG_ALARM) {
                    notiType = Constants.NOTIFICATION_ALARM;
                    content = String.format(EMG_ALARM_MSG, deviceNo);
                    title = "紧急报警";
                }else{
                    StringBuilder contentSb = new StringBuilder();
                    contentSb.append("{");
                    contentSb.append("deviceNo:'" + deviceNo + "'");
                    contentSb.append(",alarmType:'" + alarmType + "'");
                    if(solveFlag){
                        notiType = Constants.NOTIFICATION_ALARM_VIEW_REMOVE;
                    }else{
                        contentSb.append(",reportTimeDisp:'" + acqTime.toLocaleString() + "'");

                        String key = String.format("%s,%s", "ALARM_TYPE", alarmType);
                        Dict dict = (Dict) redisson.getMap(RedissonUtil.RedisKey.DICT_CACHE.toString()).get(key);
                        notiType = Constants.NOTIFICATION_ALARM_VIEW;
                        contentSb.append(",alarmTypeName:'" + dict.getDictValue() + "'");
                    }
                    contentSb.append("}");
                    content = contentSb.toString();
                }
                NotificationUtil.send(deviceNo, title, content, notiType);
            }

            alarm.setSolveFlag(solveFlag);
            alarm.setLongitude(longitude);
            alarm.setLatitude(latitude);
            LocalCacheUtil.addAlarm(alarm);

            int sendMessage = 0;
            if(redisson.getMap(RedissonUtil.RedisKey.SYS_CONFIG_CACHE.toString()).get("sendMessage") != null){
                sendMessage = (int) redisson.getMap(RedissonUtil.RedisKey.SYS_CONFIG_CACHE.toString()).get("sendMessage");
            }
            if(sendMessage == 1){
                @SuppressWarnings("unchecked")
                List<Integer> alarmTypeList = (List<Integer>) redisson.getMap(RedissonUtil.RedisKey.ALARM_PERM_CACHE.toString()).get(Constants.JKXX_USER_NAME);
                if (alarmTypeList != null && !alarmTypeList.isEmpty() && alarmTypeList.contains(alarmType)) {
                    pushMsgToBpm(alarm, shipNo, acqTime);
                }
            }
        }
    }

    /**
     * 获取当日行程(单位:海里)
     */
    private double getDayTotalMileage(Double prevDayTotalMileage, Date prevAcqTime, Date curAcqTime, Double prevLon, Double prevLat, Double curLon, Double curLat) {

        // 判断上一条数据与当前数据是否是同一天，如果不是，则返回0，否则继续下一步计算
        if (prevDayTotalMileage == null || !isSameDay(prevAcqTime, curAcqTime)) {
            return 0.0;
        }

        // 将距离与上一次的里程数相加
        if (prevLon == null || prevLat == null || curLon == null || curLat == null
                || (prevLon.equals(curLon) && prevLat.equals(curLat))) {
            return prevDayTotalMileage;
        }

        // 根据经纬度，计算出当前坐标与上一条坐标的距离
        return prevDayTotalMileage + GisUtil.getDistance(prevLon, prevLat, curLon, curLat);
    }

    /**
     * 判断是不是同一天
     */
    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * 获取总行程(单位:海里)
     */
    private double getTotalMileage(Double prevTotalMileage, Date prevAcqTime, Date curAcqTime, Double prevLon, Double prevLat, Double curLon, Double curLat) {

        if (prevTotalMileage == null) {
            return 0.0;
        }

        // 将距离与上一次的里程数相加
        if (prevLon == null || prevLat == null || curLon == null || curLat == null
                || (prevLon.equals(curLon) && prevLat.equals(curLat))) {
            return prevTotalMileage;
        }

        // 根据经纬度，计算出当前坐标与上一条坐标的距离
        return prevTotalMileage + GisUtil.getDistance(prevLon, prevLat, curLon, curLat);
    }
}
