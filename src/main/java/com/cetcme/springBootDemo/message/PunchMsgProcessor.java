package com.cetcme.springBootDemo.message;

import com.cetcme.springBootDemo.dao.DeviceDao;
import com.cetcme.springBootDemo.dao.PunchDao;
import com.cetcme.springBootDemo.dao.ShipDao;
import com.cetcme.springBootDemo.domain.Device;
import com.cetcme.springBootDemo.domain.Punch;
import com.cetcme.springBootDemo.domain.Ship;
import com.cetcme.springBootDemo.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by qiuhong on 09/11/2017.
 */
public class PunchMsgProcessor {


    private static final int MESSAGE_LENGTH = 65;
    private static final int DEVICE_NO_INDEX = 4;
    private static final int DEVICE_NO_LENGTH = 8;
    private static final int SAILOR_ID_NO_INDEX = 13;
    private static final int SAILOR_ID_NO_LENGTH = 9;
    private static final int DATE_TIME_INDEX = 23;
    private static final int DATE_TIME_LENGTH = 6;

    Logger logger = LoggerFactory.getLogger(PunchMsgProcessor.class);

    public void process(String deviceNo) {
        try{
            logger.info("PunchMsgProcessor.process开始");
            Punch punch = new Punch();

            // 设备识别码
            DeviceDao deviceDao = new DeviceDao();
            Device device = deviceDao.getByDeviceNo(deviceNo);
            // 如 果设备编码未设置或者已删除，则直接向设备发送OK信号，丢弃数据
            if (device == null) {
                logger.warn("{} 【{}】设备打卡信息无效，该设备数据库中不存在或已删除！", DateUtil.parseDateToString(new Date()), deviceNo);

                // 向客户端发送成功回执
//                responseTcpOk(message, message.getHeader());

                return;
            }
            Long deviceId = device.getDeviceId();
            punch.setDeviceId(deviceId);
            punch.setDeviceNo(deviceNo);

            // 根据设备标识获取编定的渔船编码
            ShipDao shipDao = new ShipDao();
            Ship ship = shipDao.getByDeviceId(deviceId);
            Long shipId = ship == null ? null : ship.getShipId();
            punch.setShipId(shipId);

            // 身份证信息
            String sailorIdNo = ""; // todo: json
            punch.setSailorIdNo(sailorIdNo);

            // 时间信息
            String utcTime = ""; // todo: json
            Date punchTime = ConvertUtil.utc2LocalDate(utcTime, Constants.DatePattern.UTC_NO_DOT, Constants.DatePattern.LOCAL);

            // 根据打卡时间判断该消息是否已经超时，如果无效，则设为当前时间
            Long discardTime = Long
                    .parseLong(RedissonUtil.redisson.getMap(RedissonUtil.RedisKey.SYS_CONFIG_CACHE.toString()).get(Constants.DISCARD_DELAY).toString());
            if (UdpUtil.isInvalidTime(punchTime, discardTime)) {
                logger.info("打卡时间无效！{}", DateUtil.parseDateToString(punchTime));
                punchTime = new Date();
            }

            punch.setPunchTime(punchTime);

            // 姓名信息
            String sailorName = ""; // todo: json
            punch.setSailorName(sailorName);

            PunchDao punchDao = new PunchDao();

            // 因为存在当设备接收不到"02OK"的消息时，会重复发送同一条数据的情况, 因此保存打卡信息时要过滤重复打卡数据
            punchDao.add(punch);

            // 最后向客户端发送成功回执
//            responseTcpOk(message, message.getHeader());
        }catch(Exception e){
            logger.error("PunchMsgProcessor.process异常:" + e.getMessage());
        }

    }
}
