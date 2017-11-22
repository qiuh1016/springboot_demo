package com.cetcme.springBootDemo.message;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;


import com.cetcme.springBootDemo.dao.DeviceDao;
import com.cetcme.springBootDemo.domain.Device;
import com.cetcme.springBootDemo.utils.Constants;

import com.cetcme.springBootDemo.utils.ConvertUtil;
import com.cetcme.springBootDemo.utils.DateUtil;

public class DeviceMsgProcessor {

    Logger logger = LoggerFactory.getLogger(DeviceMsgProcessor.class);

    public void process(JSONObject jsonObject) {
        try{
            logger.info("PunchMsgProcessor.process开始");
            Device newDevice = new Device();

            // 设备识别码
            String deviceNo = ""; // todo: json
            newDevice.setDeviceNo(deviceNo);

            DeviceDao deviceDao = new DeviceDao();
            Device device = deviceDao.getByDeviceNo(deviceNo);
            // 如果设备编码未设置或者已删除，则直接向设备发送OK信号，丢弃数据
            if (device == null) {
                logger.warn("{} 【{}】设备次要状态信息无效，该设备数据库中不存在或已删除！", DateUtil.parseDateToString(new Date()), deviceNo);

                // 向客户端发送成功回执
//                responseTcpOk(message, message.getHeader());

                return;
            }

            Long deviceId = device.getDeviceId();

            // 海拔高度
            Double altitude = jsonObject.getDouble("altitude"); // todo: json ConvertUtil.string2Double(datas[1])
            newDevice.setAltitude(altitude);

            // 水平精度
            Double hdop = jsonObject.getDouble("hdop"); // todo: json ConvertUtil.string2Double(datas[2]) * HDOP_RATIO;
            newDevice.setHdop(hdop);

            // 卫星数量
            Integer satelliteCnt = jsonObject.getInt("satelliteCnt"); // todo: json ConvertUtil.string2Int(datas[3]);
            newDevice.setSatelliteCnt(satelliteCnt);

            // 设备倾斜状态
            String skewStr = jsonObject.getString("skewStr"); // todo: json datas[4];
            // X轴倾斜
            String skewXStr = skewStr.substring(0, 2);
            int skewX = ConvertUtil.hexStr2Int(skewXStr);
            // 最高位为符号位
            if (skewX >= 128) {
                skewX = 128 - skewX;
            }
            // 最高位为符号位
            newDevice.setSkewX(skewX);
            // Y轴倾斜
            String skewYStr = skewStr.substring(2, 4);

            int skewY = ConvertUtil.hexStr2Int(skewYStr);
            // 最高位为符号位
            if (skewY >= 128) {
                skewY = 128 - skewY;
            }
            newDevice.setSkewY(skewY);
            // Z轴倾斜
            String skewZStr = skewStr.substring(4, 6);
            int skewZ = ConvertUtil.hexStr2Int(skewZStr);
            // 最高位为符号位
            if (skewZ >= 128) {
                skewZ = 128 - skewZ;
            }
            newDevice.setSkewZ(skewZ);

            // 发射频次
            Integer transmitFreq = jsonObject.getInt("transmitFreq"); // todo: json ConvertUtil.hexStr2Int(datas[5]);
            newDevice.setTransmitFreq(transmitFreq);

            // 发射移动距离
            Integer transmitDist = jsonObject.getInt("transmitDist"); // todo: json ConvertUtil.hexStr2Int(datas[6]);
            newDevice.setTransmitDist(transmitDist);

            // 记录频次
            Integer recordFreq = jsonObject.getInt("recordFreq"); // todo: json ConvertUtil.hexStr2Int(datas[7]);
            newDevice.setRecordFreq(recordFreq);

            // 记录移动距离
            Integer recordDist = jsonObject.getInt("recordDist"); // todo: json ConvertUtil.hexStr2Int(datas[8]);
            newDevice.setRecordDist(recordDist);

            newDevice.setUpdateTime(new Date());

            int result = deviceDao.update(newDevice);
            if (result == Constants.SQL_FAIL) {
                logger.warn("{} 【{}】设备次要状态更新失败，该设备在数据库中不存在或已删除！", DateUtil.parseDateToString(new Date()), deviceNo);
            }

            // 最后向客户端发送成功回执
//            responseTcpOk(message, message.getHeader());
        }catch(Exception e){
            logger.error("DeviceMsgProcessor.process异常:" + e.getMessage());
        }

    }

}
