package com.cetcme.springBootDemo.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final String RESOURCES_PATH = "/src/main/resources/";
	public static final String SERVER_PORT = "server.port";
	public static final String CONFIG_FILE_NAME = "config.properties";
	public static final String DRUID_FILE_NAME = "druid.properties";
	public static final String RULE_FILE_NAME = "rule.properties";
	public static final String LOG4J_FILE_NAME = "log4j.properties";

	public static final String BOOT_FILE_PATH = "E:\\shipdata";

	public static final String BOOTLOADER_FILEPATH_KEY = "bootloader.filePath";
	// bootloader的信号强度阈值
	public static final String BOOTLOADER_SIGNALSTRENGTH_THRESHOLD_KEY = "bootloader.signalStrength.threshold";
	public static final String BOOTLOADER_DATA_RETRY_TIMES_KEY = "bootloader.data.retry.times";
	public static final String BOOTLOADER_DATA_SEND_INTERVAL_KEY = "bootloader.data.send.interval";
	public static final String BOOTLOADER_DATA_REPLY_WAITINGTIME_KEY = "bootloader.data.reply.waitingTime";
	public static final String COMMAND_REPLY_WAITINGTIME_KEY = "command.reply.waitingTime";
	public static final String RESTSERVICE_BASE_URI = "restservice.base.uri";
	public static final String PUSHSERVICE_BASE_URI = "pushservice.base.uri";

	public static final String ORACLE_PASSWORD_KEY = "oracle.password";
	public static final String ORACLE_USER_KEY = "oracle.username";
	public static final String ORACLE_URL_KEY = "oracle.url";
	public static final String ORACLE_DRIVE_KEY = "oracle.drive";

	public static final String MYSQL_PASSWORD_KEY = "mysql.password";
	public static final String MYSQL_USER_KEY = "mysql.username";
	public static final String MYSQL_URL_KEY = "mysql.url";
	public static final String MYSQL_DRIVE_KEY = "mysql.drive";

	public static final String REFRESH_TIME = "refreshTime";
	public static final String LON_OFFSET = "lonOffset";
	public static final String LAT_OFFSET = "latOffset";
	public static final String OFFLINE_DELAY = "offlineDelay";
	public static final String COMM_PORT = "commPort";
	public static final String DISCARD_DELAY = "discardDelay";
	public static final String OFFLINE_ALARM_DELAY = "offlinealarmDelay";
	public static final String LON_MAX = "lonMax";
	public static final String LON_MIN = "lonMin";
	public static final String LAT_MAX = "latMax";
	public static final String LAT_MIN = "latMin";

	public static final String JKXX_USER_NAME = "jkxx";
	public static final int CFS_OUT_FENCE_ALARM = 99;
	public static final int PERSON_RATED_ALARM = 98;
	public static final int FENCEOUT_PUNCH_ALARM = 97;
	public static final int CORDON_IN_ALARM = 96;
	public static final int CORDON_OUT_ALARM = 95;
	public static final int CFS_OUTTIME_ALARM = 12;

	public static final int IOF_FLAG_OUT = 1;
	public static final int IOF_FLAG_IN = 2;
	public static final int IOF_FLAG_CFS_OUT = 3;

	public static final char TRUE_CHAR_FLAG = '1';
	public static final char FALSE_CHAR_FLAG = '0';

	public static final int TRUE_INT_FLAG = 1;
	public static final int FALSE_INT_FLAG = 0;

	public static final int SQL_SUCCESS = 1;
	public static final int SQL_FAIL = 0;

	public static final int HEX = 16;

	public static final String COMMA_DELIMITER = ",";

	public static final String MESSAGE_START_SYMBOL = "$";
	public static final String MESSAGE_END_SYMBOL = "\r\n";
	public static final int MESSAGE_HEADER_LENGTH = 3;
	public static final int COMMON_MESSAGE_CHECKSUM_LENGTH = 2;
	public static final int HISTORY_MESSAGE_CHECKSUM_LENGTH = 1;
	public static final String MESSAGE_CHECKSUM_DELIMITER = "*";
	public static final String COMMON_RESPONSE_MSG_FORMAT = "%sOK\r\n";

	public static final String RESPONSE_OK = "OK";
	public static final String RESPONSE_ERR = "ER";

	public static final long SYSTEM_USER_ID = 0l;

	public static final String NOTIFICATION_ALARM = "1";
	public static final String NOTIFICATION_COMMAND = "2";
	public static final String NOTIFICATION_ALARM_VIEW = "3";//滚动显示的报警
	public static final String NOTIFICATION_ALARM_VIEW_REMOVE = "4";//滚动显示的报警解除

	public static final String CIRCLE_FENCE = "circle";
	public static final String POLYGON_FENCE = "polygon";

	public static final Map<MessageType, CommandType> RESPONSE_MAP = new HashMap<MessageType, CommandType>() {
		private static final long serialVersionUID = 1L;

		{
			put(MessageType.SECONDARY_STATUS_MSG, CommandType.UPDATE_SECONDARY_STATUS_COMMAND);
			put(MessageType.CONFIG_DEVICE_REQUEST, CommandType.REMOTE_CONFIG_COMMAND);
			put(MessageType.BOOTLOADER_CHECKSUM_MSG, CommandType.BOOTLOADER_COMMAND);
			put(MessageType.CONFIG_ID_CARD_READER_REQUEST, CommandType.CONFIG_ID_CARD_READER_COMMAND);
			put(MessageType.CHANGE_DEVICE_IP_REQUEST, CommandType.CHANGE_DEVICE_IP_COMMAND);
			put(MessageType.CHANGE_DEVICE_BD_ADDRESS, CommandType.CHANGE_DEVICE_BD_COMMAND);
		}
	};

	public static enum CacheType {
		SYS_CONFIG_CACHE, IP_ADDRESS_CACHE, DATAGRAM_PACKET_CACHE, DEVICE_INFO_CACHE, DICT_CACHE, ALARM_STATUS_CACHE, 
		WAIT_TO_SEND_COMMAND_CACHE, FENCE_INFO_CACHE, ACQDATA_HISTORY_TBL_INDEX_CACHE, PREV_ACQDATA_CACHE, FIRST_ACQDATA_IOF_IN_CACHE, 
		FIRST_ACQDATA_IOF_OUT_CACHE, ALARM_PERM_CACHE, DEVICE_RULE_USERID, CORDON_GIS_LIST, CORDON_USERID, CORDON_DEVICE_LIST, 
		IP_ADDRESS_BD, CORDON_STATE_IN, CORDON_STATE_OUT;
	}

	/**
	 * 日期单位枚举
	 */
	public static enum DateUnitType {
		// y(年)、M(月)、d(日)、h(时)、m(分)、s(秒)、ms(毫秒)
		YEAR("y"), MONTH("M"), DAY("d"), HOUR("h"), MINUTE("m"), SECOND("s"), MILLISECOND("ms");

		String unit;

		DateUnitType(String unit) {
			this.unit = unit;
		}

		@Override
		public String toString() {
			return unit;
		}

		public static DateUnitType getEnumByValue(String value) {
			for (DateUnitType e : DateUnitType.values()) {
				if (e.unit.equals(value)) {
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * 日期格式枚举
	 */
	public static enum DatePattern {
		YYYYMMDDHHMMSS_("yyyy-MM-dd HH:mm:ss"), YYYYMMDDHHMMSS("yyyy/MM/dd HH:mm:ss"), YYYYMMDD("yyyy/MM/dd"),
		YYYYMMDDHHMMSSSS("yyyyMMddHHmmssSS"), UTC("yyyyMMdd.HHmmss"), UTC_NO_DOT("yyyyMMddHHmmss"),
		LOCAL("yyyyMMdd HHmmss");

		String pattern;

		DatePattern(String pattern) {
			this.pattern = pattern;
		}

		@Override
		public String toString() {
			return pattern;
		}
	}

	/**
	 * 下发指令类型枚举
	 */
	public enum CommandType {
		// 1:状态更新 2:遥控配置 3:终端升级 4:配置身份证读卡器 5:修改IP地址 6:修改BD地址
		UPDATE_SECONDARY_STATUS_COMMAND(1), REMOTE_CONFIG_COMMAND(2), BOOTLOADER_COMMAND(3),
		CONFIG_ID_CARD_READER_COMMAND(4), CHANGE_DEVICE_IP_COMMAND(5), CHANGE_DEVICE_BD_COMMAND(6);

		private final int value;

		public int getValue() {
			return value;
		}

		CommandType(int value) {
			this.value = value;
		}

		public static CommandType getEnumByValue(int value) {
			for (CommandType e : CommandType.values()) {
				if (e.value == value) {
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * 下发指令状态枚举
	 */
	public enum CommandStatus {
		// 0:等待发送 1:正在发送 2:发送成功 8:发送失败 9:取消发送
		WAITING_TO_SEND(0), SENDING(1), SEND_SUCCEED(2), SEND_FAILED(8), CANCEL_SEND(9);

		private final int value;

		public int getValue() {
			return value;
		}

		CommandStatus(int value) {
			this.value = value;
		}

		public static CommandStatus getEnumByValue(int value) {
			for (CommandStatus e : CommandStatus.values()) {
				if (e.value == value) {
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * 消息类别枚举
	 */
	public enum MessageType {
		// 消息1(报设备主要状态)、消息2(报船舶登船人员)、消息3(报设备次要状态)、 消息80(中心配置监管仪)、
		// 消息83(中心读取监管仪次要状态)、消息85(中心升级监管仪软件)、消息86(中心配置身份证读卡器ID)
		PRIMARY_STATUS_MSG("$01"), PUNCH_MSG("$02"), SECONDARY_STATUS_MSG("$03"), CONFIG_DEVICE_REQUEST("$80"),
		SECONDARY_STATUS_MSG_REQUEST("$83"), BOOTLOADER_MSG_REQUEST("$85"), CONFIG_ID_CARD_READER_REQUEST("$86"),
		CHANGE_DEVICE_IP_REQUEST("$87"), CHANGE_DEVICE_BD_ADDRESS("$88"), BOOTLOADER_WRITEFLASH_MSG("$90"),
		BOOTLOADER_CHECKSUM_MSG("$91");

		private final String value;

		@Override
		public String toString() {
			return value;
		}

		MessageType(String value) {
			this.value = value;
		}

		public static MessageType getEnumByValue(String value) {
			for (MessageType e : MessageType.values()) {
				if (e.value.equals(value)) {
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * 经度枚举
	 */
	public enum LongitudeHemisphere {
		// E:东经 、W:西经
		EAST("E"), WEST("W");

		private final String value;

		@Override
		public String toString() {
			return value;
		}

		LongitudeHemisphere(String value) {
			this.value = value;
		}

		public static LongitudeHemisphere getEnumByValue(String value) {
			for (LongitudeHemisphere e : LongitudeHemisphere.values()) {
				if (e.value.equals(value)) {
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * 纬度半球枚举
	 */
	public enum LatitudeHemisphere {
		// N:北纬 、S:南纬
		NORTH("N"), SOUTH("S");

		private final String value;

		@Override
		public String toString() {
			return value;
		}

		LatitudeHemisphere(String value) {
			this.value = value;
		}

		public static LatitudeHemisphere getEnumByValue(String value) {
			for (LatitudeHemisphere e : LatitudeHemisphere.values()) {
				if (e.value.equals(value)) {
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * HEX记录类型
	 */
	public enum HexRecordType {
		// 00(数据记录) 01(文件结束记录) 02(扩展段地址记录) 03(开始段地址记录) 04(扩展线性地址记录)
		// 05(开始线性地址记录)
		DATA("00"), EOF("01"), DAY("02"), HOUR("03"), EXT_LIN("04"), START_LIN("05");

		String type;

		HexRecordType(String type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return type;
		}

		public static HexRecordType getEnumByValue(String value) {
			for (HexRecordType e : HexRecordType.values()) {
				if (e.type.equals(value)) {
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * 港口类型枚举
	 */
	public static enum FenceType {
		// 0(圆形) 1(矩形) 2(多边形)
		CIRCLE(1), RECTANG(2), POLYGON(3);

		int fenceType;

		FenceType(int fenceType) {
			this.fenceType = fenceType;
		}

		public int getValue() {
			return fenceType;
		}

		public static FenceType getEnumByValue(int value) {
			for (FenceType e : FenceType.values()) {
				if (e.fenceType == value) {
					return e;
				}
			}
			return null;
		}
	}
}
