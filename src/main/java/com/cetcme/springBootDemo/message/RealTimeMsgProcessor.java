package com.cetcme.springBootDemo.message;

import com.cetcme.springBootDemo.dao.AcqDataDao;
import com.cetcme.springBootDemo.dao.DeviceDao;
import com.cetcme.springBootDemo.domain.AcqData;
import com.cetcme.springBootDemo.domain.Alarm;
import com.cetcme.springBootDemo.domain.DeviceExtend;
import com.cetcme.springBootDemo.netty.TcpClient;
import com.cetcme.springBootDemo.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import com.cetcme.springBootDemo.utils.RedissonUtil.RedisKey;

/**
 * Created by qiuhong on 02/11/2017.
 */
public class RealTimeMsgProcessor {

    public static Logger logger = LoggerFactory.getLogger(RealTimeMsgProcessor.class);

    public static int readMessageCount = 0;
    public static int processMessageCount = 0;

    public void process(JSONArray deviceArr) {

        for (Object d : deviceArr) {
            this.processSingleFrame((JSONObject) d);
        }
    }

    /**
     * 原单帧数据处理
     */
    private void processSingleFrame(JSONObject deviceJson){
        String myDeviceNo = "";
        try{
            readMessageCount++;

            myDeviceNo = (String) deviceJson.get("deviceNo"); // todo: json 获取

            DeviceExtend deviceExtend = (DeviceExtend) RedissonUtil.redisson.getMap(RedisKey.DEVICE_INFO_CACHE.toString()).get(myDeviceNo);

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
            AcqData curData = (AcqData) deviceJson.get("AcqData"); // todo: json 获取

            AcqDataDao acqDataDao = new AcqDataDao();
            acqDataDao.insertHistory(curData);

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
                RedissonUtil.redisson.getMap(RedisKey.DEVICE_INFO_CACHE.toString()).put(myDeviceNo, deviceExtend);
            }

            processMessageCount++;
        } catch (Exception e) {
            logger.error(myDeviceNo + "RealtimeMsgProcessor.process异常" + e.getMessage());

        }
    }

}
